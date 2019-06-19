package com.me.dao;

import com.me.models.User_1;

public interface User_1Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User_1 record);

    int insertSelective(User_1 record);

    User_1 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User_1 record);

    int updateByPrimaryKey(User_1 record);
}