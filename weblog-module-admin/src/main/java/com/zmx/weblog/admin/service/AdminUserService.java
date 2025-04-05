package com.zmx.weblog.admin.service;

import com.zmx.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.zmx.weblog.common.utils.Response;

public interface AdminUserService {

    Response updatePassword(UpdateAdminUserPasswordReqVO reqVO);

    Response findUserInfo();
}
