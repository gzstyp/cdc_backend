package com.fwtai.security;

import com.fwtai.bean.JwtUser;
import com.fwtai.config.ConfigFile;
import com.fwtai.entity.User;
import com.fwtai.service.core.MenuService;
import com.fwtai.service.core.UserService;
import com.fwtai.tool.ToolAttack;
import com.fwtai.tool.ToolBean;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJWT;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginAuthentication extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;

    public LoginAuthentication(final AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl(ConfigFile.URL_PROCESSING);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,final HttpServletResponse response) throws AuthenticationException{
        final HashMap<String,String> params = ToolClient.getFormParams(request);
        final String p_username = "username";
        final String p_password = "password";
        final String validate = ToolClient.validateField(params,p_username,p_password);
        if(validate != null){
            ToolClient.responseJson(validate,response);
            return null;
        }
        final String username = params.get(p_username);
        final String password = params.get(p_password);
        final String ip = ToolClient.getIp(request);
        final ToolAttack toolAttack = new ToolAttack();
        final boolean blocked = toolAttack.isBlocked(ip);
        if(blocked){
            final String msg = "帐号或密码错误次数过多,IP<br/>"+ip+"<br/>已被系统屏蔽,请30分钟后重试!";
            ToolClient.responseJson(ToolClient.createJson(ConfigFile.code198,msg),response);
            return null;
        }
        final UserService userService = ToolBean.getBean(request,UserService.class);
        if(userService.checkLogin(username,password)){
            toolAttack.loginSucceed(ip);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }else{
            toolAttack.loginFailed(ip);
            /*final boolean bl = toolAttack.getCount(ip) > 4;
            if(blocked || bl){
                final String msg = "帐号或密码错误次数过多,IP<br/>"+ip+"<br/>已被系统屏蔽,请30分钟后重试!";
                ToolClient.responseJson(ToolClient.createJson(ConfigFile.code198,msg),response);
                return null;
            }*/
            //在此处理锁定功能!!!
            final User user = userService.queryUser(username);
            if(user != null){
                final int errorCount = user.getErrorCount() + 1;
                if(errorCount < 4){
                    userService.updateErrors(username);
                }
                final Long error = user.getError();
                if(error < 0){
                    final String msg = "当前帐号或密码连续错误3次!<br/>已被系统临时锁定……<br/>请在"+user.getErrorTime()+"后再重试";
                    ToolClient.responseJson(ToolClient.createJson(ConfigFile.code198,msg),response);
                    return null;
                }
                if (errorCount >= 3){
                    userService.updateLoginTime(username);//当错误3次时更新错误的时刻就锁定
                    final String msg = "当前帐号或密码连续错误3次<br/>已被系统临时锁定,请30分钟后重试";
                    ToolClient.responseJson(ToolClient.createJson(ConfigFile.code198,msg),response);
                    return null;
                }
            }
            ToolClient.responseJson(ToolClient.invalidUserInfo(),response);
            return null;
        }
    }

    // 登录认证成功后调用
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain,final Authentication authResult) throws IOException, ServletException{
        final String type = request.getParameter("type");//除了PC端之外都要这个参数,登录类型1 为android ; 2 ios;3 小程序
        //取得账号信息
        final JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        final String userId = jwtUser.getUserId();
        //加载前端菜单
        final Map<String,Object> map = new HashMap<>(6);
        if(type == null || type.isEmpty()){
            map.put(ConfigFile.REFRESH_TOKEN,ToolJWT.expireRefreshToken(userId));
            map.put(ConfigFile.ACCESS_TOKEN,ToolJWT.expireAccessToken(userId));
            map.put("menuData",ToolBean.getBean(request,MenuService.class).getMenuData(userId));
            map.put("userName",jwtUser.getUsername());
            map.put("areaData",jwtUser.getAreaData());
        }else{
            map.put(ConfigFile.REFRESH_TOKEN,ToolJWT.buildRefreshToken(userId));
            map.put(ConfigFile.ACCESS_TOKEN,ToolJWT.buildAccessToken(userId));
            map.put("areaData",jwtUser.getAreaData());
            map.put("audit",jwtUser.getAudit());
            map.put("userId",userId);
            map.put("userName",jwtUser.getUsername());
        }
        ToolClient.responseJson(ToolClient.queryJson(map),response);
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException e) throws IOException, ServletException{
        String msg = ToolClient.invalidUserInfo();
        if(e instanceof LockedException){
            msg = "账号已被系统锁定";
        }else if(e instanceof BadCredentialsException){
            msg = "账号或密码错误";
        }else if(e instanceof AccountExpiredException){
            msg = "账号已过期失效";
        }else if(e instanceof DisabledException){
            msg = "账号已被禁用";
        }
        ToolClient.responseJson(ToolClient.createJsonFail(msg),response);
    }
}