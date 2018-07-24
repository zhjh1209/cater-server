package com.mokylin.cater_server.dao;


import com.mokylin.cater_server.entity.UserData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDataRepository extends MongoRepository<UserData, String> {
    UserData save(UserData userData);

    Optional<UserData> findById(String openId);

    Page<UserData> findAll(Pageable page);
}
