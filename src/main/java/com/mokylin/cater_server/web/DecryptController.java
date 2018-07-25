package com.mokylin.cater_server.web;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.service.WxLoginService;
import com.mokylin.cater_server.web.entity.DecryptEntity;
import com.mokylin.cater_server.web.result.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DecryptController {
    @Autowired
    private WxLoginService wxLoginService;

    @RequestMapping(value = "/weapp/decrypt", method = RequestMethod.POST)
    public ResultEntity decrypt(HttpServletRequest request, @RequestBody DecryptEntity body) {
        SessionInfo sessionUser = wxLoginService.validation(request);
        ResultEntity result = new ResultEntity();
        if (sessionUser == null) {
            result.setCode(-1);
            result.setMsg("login invalidation");
        } else {
            String iv = body.getIv();
            String encryptData = body.getEd();
            String string = WxLoginService.decrypt(sessionUser.getSessionKey(), iv, encryptData);
            if (string != null) {
                JSONObject data = JSONObject.parseObject(string);
                result.setCode(0);
                result.setData(data);
                result.setMsg("success");
            } else {
                result.setCode(-1);
                result.setMsg("decrypt error");
            }
        }
        return result;
    }
}
