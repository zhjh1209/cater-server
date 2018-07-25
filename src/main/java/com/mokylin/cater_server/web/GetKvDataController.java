package com.mokylin.cater_server.web;

import com.alibaba.fastjson.JSON;
import com.mokylin.cater_server.dao.UserDataRepository;
import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.entity.UserData;
import com.mokylin.cater_server.game.User;
import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.service.WxLoginService;
import com.mokylin.cater_server.web.result.ResultEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GetKvDataController {
    @Autowired
    private WorldService worldService;
    @Autowired
    private WxLoginService wxLoginService;
    @Autowired
    private UserDataRepository userDataRepository;

    @RequestMapping(value = "/weapp/get_kvdata", method = RequestMethod.GET)
    public ResultEntity getKvData(HttpServletRequest request) {
        SessionInfo sessionUser = wxLoginService.validation(request);
        ResultEntity result = new ResultEntity();
        if (sessionUser == null) {
            result.setCode(-1);
            result.setData("user not login");
        } else {
            User user = worldService.getUser(sessionUser.getOpenId());
            String kvdata = null;
            if (user != null) {
                kvdata = user.getUserData().getKvData();
            } else {
                Optional<UserData> userDataOpt =
                        userDataRepository.findById(sessionUser.getOpenId());
                if (userDataOpt.isPresent()) {
                    kvdata = userDataOpt.get().getKvData();
                }
            }
            result.setCode(0);
            result.setData(JSON.parseArray(kvdata));
            result.setMsg("success");
        }
        return result;
    }
}
