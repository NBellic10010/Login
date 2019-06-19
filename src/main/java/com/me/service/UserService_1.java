package com.me.service;

import com.me.models.User_1;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;

import java.util.List;

@Component
public interface UserService_1 {
    public User_1 getUserById(int userId);
    public int createUser(String name,String password);
    public User_1 getUserByNameAndPw(String username,String pw);
    public void deluserByBatch(List<Integer> list);
    public void insertuserByBatch(User_1 userdata);
    //public BBSItem getNewestPost();
    //public int createPost(String content,String );
}