package com.mokylin.cater_server.web;

import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.service.WxLoginService;
import com.mokylin.cater_server.web.result.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GetUserController {
    @Autowired
    private WxLoginService wxLoginService;

    @RequestMapping("/weapp/user")
    public ResultEntity user(HttpServletRequest request) {
        SessionInfo sessionUser = wxLoginService.validation(request);
        ResultEntity result = new ResultEntity();
        if (sessionUser == null) {
            result.setCode(-1);
            result.setMsg("failed");
        } else {
            result.setCode(0);
            result.setData(sessionUser.getUserInfo());
            result.setMsg("success");
        }
        return result;
    }
}
