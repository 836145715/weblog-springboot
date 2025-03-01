package com.zmx.weblog.jwt.service;

import com.zmx.weblog.common.domain.dos.UserDO;
import com.zmx.weblog.common.domain.dos.UserRoleDO;
import com.zmx.weblog.common.domain.mapper.UserMapper;
import com.zmx.weblog.common.domain.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查询
        UserDO userDO = userMapper.findByUsername(username);
        if(Objects.isNull(userDO)){
            throw new UsernameNotFoundException("用户不存在");
        }

        //用户角色
        List<UserRoleDO> roleDOS = userRoleMapper.selectByUsername(username);
        String[] roleArr = null;
        if(Objects.nonNull(roleDOS) && roleDOS.size() > 0){
            roleArr = roleDOS.stream().map(UserRoleDO::getRole).toArray(String[]::new);
        }

        return User.withUsername(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities(roleArr)
                .build();
    }
}
