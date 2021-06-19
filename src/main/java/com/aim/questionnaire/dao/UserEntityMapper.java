package com.aim.questionnaire.dao;

import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface UserEntityMapper {
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    UserEntity selectAllByName(String username);
    int insert(UserEntity userEntity);
    List<UserEntity>  selectAll(Map map);
    int updatePassword(String userName);
    String getCount();
    List<UserEntity>  selectIndistinct(Map map);
    List<Map<String,String>> getAiAnswer();

}