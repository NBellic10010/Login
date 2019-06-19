package com.me.serviceimpl;

import com.me.dao.User_1Mapper;
import com.me.models.User_1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.me.service.UserService_1;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserService_1_Impl implements UserService_1 {

    @Autowired
    private User_1Mapper userDao;

    public User_1 getUserById(int userId) {
        return this.userDao.selectByPrimaryKey(userId);
        //User user = new User();
        //return user;
    }

    public User_1 getUserByNameAndPw(String username, String pw) {
        return this.userDao.selectByNameandPw(username,pw);
        //User user = new User();
        //return user;
    }

    public int createUser(String name,String password){
        User_1 newuser = new User_1();
        newuser.setUserName(name);
        newuser.setStudentPassword(password);
        return this.userDao.insertSelective(newuser);
    }

    @Transactional
    public void deluserByBatch(List<Integer> list){
        for(Integer id:list){
            this.userDao.deleteByPrimaryKey(id);
        }
    }

    @Transactional
    public void insertuserByBatch(User_1 userdata){
        this.userDao.insert(userdata);
    }

}