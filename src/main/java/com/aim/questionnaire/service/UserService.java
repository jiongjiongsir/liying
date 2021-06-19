package com.aim.questionnaire.service;

import com.aim.questionnaire.common.utils.DateUtil;
import com.aim.questionnaire.common.utils.UUIDUtil;
import com.aim.questionnaire.dao.ProjectEntityMapper;
import com.aim.questionnaire.dao.UserEntityMapper;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserEntityMapper userEntityMapper;

    public int insert(UserEntity userEntity) {
        String id = UUIDUtil.getOneUUID();
        userEntity.setId(id);
        //获取用户信息
        userEntity.setUsername(userEntity.getUsername());
        // 获取当前时间
        Date date = DateUtil.getCreateTime();
        userEntity.setCreationDate(date);
        userEntity.setLastUpdateDate(date);
//        System.out.println("user信息:"+userEntity);

        int result = userEntityMapper.insert(userEntity);
        return result;
    }

    public UserEntity selectAllByName(String userName) {
        UserEntity userEntity = userEntityMapper.selectAllByName(userName);
        return userEntity;
    }

    public List<UserEntity> queryUserList(Map map) {
        return userEntityMapper.selectAll(map);
    }

    public int resetPassword(String userName) {
        return userEntityMapper.updatePassword(userName);
    }
    public String getCount()
    {
        return userEntityMapper.getCount();
    }
    public List<UserEntity> queryUserListIndistinct(Map map) {
        return userEntityMapper.selectIndistinct(map);
    }
    public List<Map<String,String>> getAiAnswer(){ return userEntityMapper.getAiAnswer();}
}
