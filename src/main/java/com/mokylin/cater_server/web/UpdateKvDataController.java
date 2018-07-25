package com.mokylin.cater_server.web;

import com.mokylin.cater_server.dao.UserDataRepository;
import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.entity.UserData;
import com.mokylin.cater_server.game.User;
import com.mokylin.cater_server.service.WorldService;
import com.mokylin.cater_server.service.WxLoginService;
import com.mokylin.cater_server.web.entity.KvEntity;
import com.mokylin.cater_server.web.result.ResultEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UpdateKvDataController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WxLoginService wxLoginService;
    @Autowired
    private WorldService worldService;
    @Autowired
    private UserDataRepository userDataRepository;

    @RequestMapping(value = "/weapp/update_kvdata", method = RequestMethod.POST)
    public ResultEntity update(HttpServletRequest request, @RequestBody KvEntity kvEntity) {
        SessionInfo sessionUser = wxLoginService.validation(request);
        ResultEntity result = new ResultEntity();
        if (sessionUser == null) {
            result.setCode(-1);
            result.setMsg("login invalidation");
        } else {
            try {
                User user = worldService.getUser(sessionUser.getOpenId());
                if (user != null) {
                    Map<String, Object> kvData = user.getUserData().getKvData();
                    if (kvEntity.getKey() != null) {
                        kvData.put(kvEntity.getKey(), kvEntity.getData());
                    }
                } else {
                    Optional<UserData> userDataOpt =
                            userDataRepository.findById(sessionUser.getOpenId());
                    if (userDataOpt.isPresent()) {
                        UserData userData = userDataOpt.get();
                        Map<String, Object> kvData = userData.getKvData();
                        if (kvEntity.getKey() != null) {
                            kvData.put(kvEntity.getKey(), kvEntity.getData());
                            userDataRepository.save(userData);

                        }
                    }
                }
                result.setCode(0);
                result.setMsg("success");
            } catch (Exception e) {
                result.setCode(-1);
                result.setMsg("failed");
                logger.error("更新分数失败：", e);
            }
        }
        return result;
    }
}
