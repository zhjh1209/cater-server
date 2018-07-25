package com.mokylin.cater_server.web;

import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.service.WxLoginService;
import com.mokylin.cater_server.web.result.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GetWorldRankController {
    @Autowired
    private WorldService worldService;
    @Autowired
    private WxLoginService wxLoginService;

    @RequestMapping(value = "/weapp/get_rank", method = RequestMethod.GET)
    public ResultEntity getWorldRank(HttpServletRequest request) {
        SessionInfo sessionUser = wxLoginService.validation(request);
        ResultEntity result = new ResultEntity();
        if (sessionUser == null) {
            result.setCode(-1);
            result.setData("user not login");
        } else {
            result.setCode(0);
            result.setData(worldService.getWorldRank());
            result.setMsg("success");
        }
        return result;
    }
}
