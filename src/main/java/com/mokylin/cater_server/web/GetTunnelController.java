package com.mokylin.cater_server.web;

import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.service.WxLoginService;
import com.mokylin.cater_server.web.result.ResultEntity;
import com.mokylin.cater_server.web.result.ResultTunnel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GetTunnelController {
    @Autowired
    private WxLoginService wxLoginService;
    @Value("${wss.url}")
    private String wssUrl;

    @RequestMapping("/weapp/tunnel")
    public ResultEntity tunnel(HttpServletRequest request) {
        SessionInfo sessionUser = wxLoginService.validation(request);
        ResultEntity result = new ResultEntity();
        if (sessionUser == null) {
            result.setCode(-1);
            result.setData("user not login");
        } else {
            ResultTunnel tunnel = new ResultTunnel();
            tunnel.setTunnelId(sessionUser.getSkey());
            tunnel.setConnectUrl(wssUrl);
            result.setData(tunnel);
        }
        return result;
    }
}
