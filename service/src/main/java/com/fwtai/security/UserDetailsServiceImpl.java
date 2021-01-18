package com.fwtai.security;

import com.fwtai.bean.JwtUser;
import com.fwtai.bean.SysUser;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.LocalUrl;
import com.fwtai.config.Permissions;
import com.fwtai.service.AsyncService;
import com.fwtai.service.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Resource
    private AsyncService asyncService;
	
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final SysUser user = userService.getUserByUserName(username);
        if(user == null) throw new UsernameNotFoundException("账号或密码有误");//它会走认证失败的回调,即方法 unsuccessfulAuthentication()
        final Integer enabled = user.getEnabled();
        if(enabled == 0){
            asyncService.updateLogin(username);
        }
        final ArrayList<HashMap<String,Object>> areaData = new ArrayList<>(1);
        final HashMap<String,Object> data = new HashMap<>(6);
        if(!username.equals(ConfigFile.KEY_SUPER))
        data.put("kid",user.getAreaId());//区域的id
        data.put("province_id",user.getProvinceId());//省级id
        data.put("county_id",user.getCountyId());//县（区）[sys_area的kid]
        data.put("city_id",user.getCityId());//市级id主键
        data.put("name",user.getAreaName());//区域名称
        data.put("area_level",user.getAreaLevel());//区域级别1-5,省市县镇村
        areaData.add(data);
        return new JwtUser(user.getKid(),user.getUserName(),user.getUserPassword(),enabled,user.getAudit(),areaData);
    }

    public JwtUser getUserById(final String userId,String url){
        final SysUser user = userService.getUserById(userId);
        if(user != null){
            final List<String> roles = userService.getRolePermissions(userId,url);
            final List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
            for(final String role : roles){
                authorities.add(new SimpleGrantedAuthority(role));
            }
            if(url.contains("/listData")){
                if(url.startsWith("/")){
                    url = url.substring(1);
                }
                LocalUrl.set(url);
                if(!roles.isEmpty())
                    Permissions.set(roles);
            }
            return new JwtUser(user.getUserName(),authorities);
        }
        throw new RuntimeException("账号信息不存在");
    }
}