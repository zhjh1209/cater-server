package com.mokylin.cater_server.service;

import com.alibaba.fastjson.JSONObject;
import com.mokylin.cater_server.dao.SessionInfoRepository;
import com.mokylin.cater_server.dao.UserDataRepository;
import com.mokylin.cater_server.entity.SessionInfo;
import com.mokylin.cater_server.entity.UserData;
import com.mokylin.cater_server.entity.UserInfo;
import com.mokylin.cater_server.util.AES;
import com.mokylin.cater_server.web.result.ResultUserInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class WxLoginService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${app.appid}")
    private String appId;
    @Value("${app.secret}")
    private String secret;

    @Autowired
    private SessionInfoRepository sessionInfoRepository;
    @Autowired
    private UserDataRepository userDataRepository;

    public SessionInfo authorization(HttpServletRequest request) {
        String code = request.getHeader("x-wx-code");
        String encryptedData = request.getHeader("x-wx-encrypted-data");
        String iv = request.getHeader("x-wx-iv");
        // 检查 headers
        if (StringUtils.isEmpty(code) && StringUtils.isEmpty(encryptedData) &&
                StringUtils.isEmpty(iv)) {
            logger.error("请求头没有包含微信HEADER信息");
            return null;
        }
        JSONObject sessionObj = getSessionKey(code);
        if (sessionObj == null) {
            return null;
        }
        String openid = sessionObj.getString("openid");
        String sessionKey = sessionObj.getString("session_key");
        String skey = DigestUtils.sha1Hex(sessionKey);
        Optional<SessionInfo> sessionInfoOpt = sessionInfoRepository.findByOpenId(openid);
        // 如果只有 code 视为仅使用 code 登录
        if (!StringUtils.isEmpty(code) && StringUtils.isEmpty(encryptedData) &&
                StringUtils.isEmpty(iv)) {
            if (sessionInfoOpt.isPresent()) {
                SessionInfo sessionInfo = sessionInfoOpt.get();
                sessionInfo.setLastVisitTime(new Date());
                sessionInfo.setSkey(skey);
                sessionInfo.setSessionKey(sessionKey);
                sessionInfoRepository.save(sessionInfo);
                return sessionInfo;
            } else {
                SessionInfo sessionInfo = new SessionInfo();
                Date now = new Date();
                sessionInfo.setLastVisitTime(now);
                sessionInfo.setSkey(skey);
                sessionInfo.setSessionKey(sessionKey);
                sessionInfo.setOpenId(openid);
                sessionInfo.setCreateTime(now);
                sessionInfo.setUuid(UUID.randomUUID().toString());
                sessionInfoRepository.save(sessionInfo);
                UserData userData = new UserData();
                userData.setOpenId(openid);
                UserInfo userInfo = new UserInfo();
                userInfo.setOpenId(openid);
                userInfo.setNickName(openid);
                userInfo.setLanguage("");
                userInfo.setCity("");
                userInfo.setProvince("");
                userInfo.setCountry("");
                userData.setUserInfo(userInfo);
                userData.setKvData(new HashMap<>());
                userDataRepository.save(userData);
                return sessionInfo;
            }
        }
        try {
            String data = decrypt(sessionKey, iv, encryptedData);
            if (data == null) {
                return null;
            }
            JSONObject json = JSONObject.parseObject(data);
            UserInfo userInfo = json.toJavaObject(UserInfo.class);
            if (sessionInfoOpt.isPresent()) {
                SessionInfo sessionInfo = sessionInfoOpt.get();
                sessionInfo.setLastVisitTime(new Date());
                sessionInfo.setSkey(skey);
                sessionInfo.setSessionKey(sessionKey);
                sessionInfo.setUserInfo(userInfo);
                sessionInfoRepository.save(sessionInfo);
                return sessionInfo;
            } else {
                SessionInfo sessionInfo = new SessionInfo();
                Date now = new Date();
                sessionInfo.setLastVisitTime(now);
                sessionInfo.setSkey(skey);
                sessionInfo.setSessionKey(sessionKey);
                sessionInfo.setUserInfo(userInfo);
                sessionInfo.setOpenId(openid);
                sessionInfo.setCreateTime(now);
                sessionInfo.setUuid(UUID.randomUUID().toString());
                sessionInfoRepository.save(sessionInfo);
                UserData userData = new UserData();
                userData.setOpenId(openid);
                userData.setUserInfo(userInfo);
                userData.setKvData(new HashMap<>());
                userDataRepository.save(userData);
                return sessionInfo;
            }
        } catch (Exception e) {
            logger.error("解密sessionKey失败：", e);
        }
        return null;
    }

    public SessionInfo validation(HttpServletRequest request) {
        String skey = request.getHeader("x-wx-skey");
        if (StringUtils.isEmpty(skey)) {
            logger.error("微信HEADER中未包含skey信息");
            return null;
        }
        Optional<SessionInfo> sessionInfoOpt = sessionInfoRepository.findBySkey(skey);
        if (sessionInfoOpt.isPresent()) {
            SessionInfo sessionInfo = sessionInfoOpt.get();
            Date lastVisit = sessionInfo.getLastVisitTime();
            long expires = 7200 * 1000;
            if (lastVisit == null || lastVisit.getTime() + expires < System.currentTimeMillis()) {
                return null;
            } else {
                return sessionInfo;
            }
        }
        return null;
    }

    private JSONObject getSessionKey(String code) {
        StringBuilder urlBuilder =
                new StringBuilder("https://api.weixin.qq.com/sns/jscode2session");
        urlBuilder.append("?appid=").append(appId).append("&secret=").append(secret)
                .append("&js_code=").append(code).append("&grant_type=authorization_code");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(urlBuilder.toString()).get().build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseText = response.body().string();
            JSONObject json = JSONObject.parseObject(responseText);
            if (json.getIntValue("errcode") > 0 || StringUtils.isEmpty(json.getString("openid")) ||
                    StringUtils.isEmpty(json.getString("session_key"))) {
                logger.error("微信授权失败：result={}", json);
                return null;
            }
            return json;
        } catch (Exception e) {
            logger.error("获取session_key报错：", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     */
    public static String decrypt(String key, String iv, String encryted) {
        try {
            byte[] keyBytes = Base64.decodeBase64(key);
            byte[] ivBytes = Base64.decodeBase64(iv);
            byte[] encryptedBytes = Base64.decodeBase64(encryted);
            byte[] data = AES.decrypt(keyBytes, ivBytes, encryptedBytes);
            return new String(data, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
