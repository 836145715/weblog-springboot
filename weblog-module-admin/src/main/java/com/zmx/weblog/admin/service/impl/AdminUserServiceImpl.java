package com.zmx.weblog.admin.service.impl;

import com.zmx.weblog.admin.model.vo.UpdateAdminUserPasswordReqVO;
import com.zmx.weblog.admin.service.AdminUserService;
import com.zmx.weblog.common.domain.mapper.UserMapper;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Response updatePassword(UpdateAdminUserPasswordReqVO reqVO) {
        String username = reqVO.getUsername();
        String password = reqVO.getPassword();
        //加密密码
        String encodePassword = passwordEncoder.encode(password);
        //更新到数据库
        int count = userMapper.updatePasswordByUsername(username, encodePassword);
        return count ==1 ? Response.success() : Response.fail(ResponseCodeEnum.USER_NOT_FOUND);
    }
}
