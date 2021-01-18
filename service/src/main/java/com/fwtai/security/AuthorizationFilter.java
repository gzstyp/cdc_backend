package com.fwtai.security;

import com.fwtai.bean.JwtUser;
import com.fwtai.config.AreaLevel;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.FlagToken;
import com.fwtai.config.LocalUserId;
import com.fwtai.config.RenewalToken;
import com.fwtai.tool.ToolBean;
import com.fwtai.tool.ToolJWT;
import com.fwtai.tool.ToolString;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter{

    public AuthorizationFilter(final AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain) throws IOException, ServletException{
        final String uri = request.getRequestURI();
        final String refresh_token = ToolString.wipeString(request.getHeader(ConfigFile.REFRESH_TOKEN));
        final String access_token = ToolString.wipeString(request.getHeader(ConfigFile.ACCESS_TOKEN));
        final String url_refresh_token = ToolString.wipeString(request.getParameter(ConfigFile.REFRESH_TOKEN));
        final String url_access_token = ToolString.wipeString(request.getParameter(ConfigFile.ACCESS_TOKEN));
        final String refresh = refresh_token == null ? url_refresh_token : refresh_token;
        final String access = access_token == null ? url_access_token : access_token;
        if(access != null){
            try {
                ToolJWT.parser(refresh);
            } catch (final Exception e) {
                if(e instanceof ExpiredJwtException){
                    RenewalToken.set(access);
                }
            }
            try {
                final String userId = ToolJWT.extractUserId(access);
                final Integer level = (Integer)ToolJWT.getLevel(access_token,"area_level");
                LocalUserId.set(userId);
                AreaLevel.set(level);
                final JwtUser jwtUser = ToolBean.getBean(request,UserDetailsServiceImpl.class).getUserById(userId,uri.startsWith("/") ? uri.substring(1) : uri);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtUser.getUsername(),null,jwtUser.getAuthorities()));
                super.doFilterInternal(request,response,chain);
            } catch (final Exception exception){
                RenewalToken.remove();
                FlagToken.set(2);
                chain.doFilter(request,response);
            }
        }else{
            chain.doFilter(request,response);
        }
    }
}