package com.mokylin.cater_server.web;

import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.service.WxLoginService;
import com.mokylin.cater_server.web.result.ResultEntity;
import com.mokylin.cater_server.web.result.ResultUserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
    @Autowired
    private WxLoginService wxLoginService;

    @RequestMapping("/weapp/login")
    public ResultEntity login(HttpServletRequest request) {
        SessionInfo sessionInfo = wxLoginService.authorization(request);
        ResultEntity result = new ResultEntity();
        if (sessionInfo == null) {
            result.setCode(-1);
            result.setMsg("login failed");
        } else {
            ResultUserInfo resultUserInfo = new ResultUserInfo();
            resultUserInfo.setTime((int) (System.currentTimeMillis() / 1000));
            resultUserInfo.setSkey(sessionInfo.getSkey());
            resultUserInfo.setUserinfo(sessionInfo.getUserInfo());
            result.setCode(0);
            result.setData(resultUserInfo);
            result.setMsg("login success");
        }
        return result;
    }
}
