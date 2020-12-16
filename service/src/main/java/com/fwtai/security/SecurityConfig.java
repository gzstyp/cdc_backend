package com.fwtai.security;

import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception{
        web.ignoring().antMatchers(ConfigFile.IGNORE_URLS);//忽略url
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginPage(ConfigFile.URL_LOGIN_PAGE)
            .permitAll()
            .and()//此时又回到上面第1行的 'authorizeRequests()'
            .logout()
            .logoutUrl(ConfigFile.URL_LOGOUT)
            .permitAll()
            //.logoutRequestMatcher(new AntPathRequestMatcher("/exit","POST"))//此方式是POST
            .logoutSuccessHandler((request,response,authentication)->{//实现接口 org.springframework.security.web.authentication.logout.LogoutSuccessHandler 内部类
                SecurityContextHolder.clearContext();
                ToolClient.responseJson(ToolClient.createJsonSuccess("退出注销成功"),response);
            })
            .permitAll()
            .and()
            .cors().and().csrf().disable()
            .addFilter(new LoginAuthentication(authenticationManager()))
            .addFilter(new AuthorizationFilter(authenticationManager()))
            // 不需要session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()//未登录认证处理或无权限的处理
            .authenticationEntryPoint(new AuthFailureEntryPoint());
    }
}