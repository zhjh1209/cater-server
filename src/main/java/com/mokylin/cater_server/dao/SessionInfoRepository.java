package com.mokylin.cater_server.dao;

import com.mokylin.cater_server.entity.SessionInfo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SessionInfoRepository extends MongoRepository<SessionInfo, String> {
    /**
     * 根据skey查询
     * @param skey
     * @return
     */
    Optional<SessionInfo> findBySkey(String skey);

    /**
     * 根据openId查询
     * @param openId
     * @return
     */
    Optional<SessionInfo> findByOpenId(String openId);

    /**
     * 保存sessionInfo
     * @param sessionInfo
     * @return
     */
    SessionInfo save(SessionInfo sessionInfo);
}
