package com.me.Controller;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.me.AESEncryption;
import com.me.models.User_1;

import com.me.service.UserService_1;
import com.me.serviceimpl.UserService_1_Impl;
import com.sun.javafx.collections.MappingChange;
import org.apache.ibatis.mapping.ResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.me.HttpRequest;
import sun.rmi.runtime.Log;
import sun.security.krb5.internal.crypto.Aes128;


@Controller
@RequestMapping("/user")
// /user/**
@SessionAttributes("user")
public class UserController {
    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService_1 userservice;

    // /user/test?id=1
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(User_1 user, Model model) {
        ModelAndView mav = new ModelAndView("index");
        int userId = user.getId();
        System.out.println("userId:" + userId);
        try {
            User_1 userfd = this.userservice.getUserById(userId);
            Gson gson = new Gson();
            String userobject = gson.toJson(userfd);
            //System.out.println(userobject);
            //mav.setViewName("forward:../WEB-INF/jsp/user/test.jsp");
            log.debug(userfd.toString());
            model.addAttribute("userfd", userfd);
            //mav.addObject("log", "hello?");
            System.out.println("bugger");

        } catch (Exception e) {
            StackTraceElement[] Trace = e.getStackTrace();
            for (StackTraceElement st : Trace) {
                System.out.println(st);
            }
        }
        return "test";
    }



    @RequestMapping(value="/dologin",method = RequestMethod.POST)
    @ResponseBody
    public String dologin(HttpServletRequest request,Model model){
        User_1 user = null;

        String username = request.getParameter("username");
        String password = request.getParameter("pw");

        if(username == null || password == null){
            return "Cannot be blank.";
        }

        //System.out.println(username+'-'+password);

        Gson gson = new Gson();
        user = this.userservice.getUserByNameAndPw(username,password);

        if(user == null){
            String ErrorInfo = "User required does not exist.";
            return ErrorInfo;
        }
        else{
            System.out.println(user.getUserName());
            String userobject = gson.toJson(user);
            HttpSession session = request.getSession();
            model.addAttribute(userobject);
            session.setAttribute("SessionUser",user);
            return userobject;
        }
    }

    @RequestMapping(value = "/sendmsg",method = RequestMethod.GET)
    @ResponseBody
    public String sendmsg(HttpServletRequest request,Model model){
        String tel = request.getParameter("tel");
        String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=1400218881&random=xxxx";

        JsonObject jsonContent = new JsonObject();
        jsonContent.addProperty("ext","");
        jsonContent.addProperty("extend","");
        //jsonContent.addProperty("params",);
        jsonContent.addProperty("sig","");
        HttpRequest req = HttpRequest.post(url);

        return null;
    }



    @RequestMapping(value = "/asknumber",method = RequestMethod.POST)
    @ResponseBody
    public String Auth_User(HttpServletRequest request, @RequestBody JSONObject json) throws IOException {
        ServletInputStream stream = request.getInputStream();
        String username = json.get("username").toString();
        String password = json.get("password").toString();
        System.out.println(username+'-'+password);

        String urlstr = "http://authserver.cidp.edu.cn/authserver/login?service=http%3A%2F%2Fehall.cidp.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.cidp.edu.cn%2Fnew%2Findex.html";
        AESEncryption aesEncryption = new AESEncryption();
        String pwdDefaultEncryptSalt = "3o1TzdXvFet6BmRT";
        //pwdDefaultEncryptSalt=pwdDefaultEncryptSalt.replace("/(^\s+)|(\s+$)/g", "")
        byte[] key = pwdDefaultEncryptSalt.getBytes("utf-8");
        byte[] passbyte = (aesEncryption.randomString(64)+password).getBytes("utf-8");
        byte[] iv = aesEncryption.randomString(16).getBytes("utf-8");
        byte[] pw = aesEncryption.encrypt(passbyte,key,iv);


        String pwasB64 = Base64.getEncoder().encodeToString(pw);
        pwasB64 = aesEncryption.encodeURIComponent(pwasB64);
        System.out.println(pwasB64);
        System.out.println(pwasB64.length());
        /**
         *  Map<String,String> body = new HashMap<String, String>();
        body.put("username",username);
        body.put("password",pw.toString());
        body.put("it","LT-301431-DAa4amJOcJvwsjSXSo4H9byxkmA0p91559976423431-0fzg-cas");
        body.put("dilt","userNamePasswordLogin");
        body.put("execution","e2s1");
        body.put("_eventId","submit");
        body.put("rmShown","1");

        HttpRequest req =  HttpRequest.post(url).form(body);
        req.header("Host","authserver.cidp.edu.cn");
        req.header("Content-Type","application/x-www-form-urlencoded");
        req.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        **/
        URL url = new URL(urlstr);
        //得到connection对象。
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //设置请求方式
        connection.setRequestMethod("POST");//设置请求方式为POST
        connection.setRequestProperty("Host","authserver.cidp.edu.cn");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        connection.setRequestProperty("Referer","http://authserver.cidp.edu.cn/authserver/login?service=http%3A%2F%2Fehall.cidp.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.cidp.edu.cn%2Fnew%2Findex.html");
        connection.setDoOutput(true);//允许写出
        connection.setDoInput(true);//允许读入
        connection.setUseCaches(false);//不使用缓存
        //连接
        connection.connect();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
        String body = "username="+username+"&"+"password="+pwasB64+"&lt=LT-341648-IJDIy2nQx0yi2bzfA5Ga1DLVpvc9aW1560258597546-WzGX-cas&dllt=userNamePasswordLogin&execution=e1s1&_eventId=submit&rmShown=1";
        writer.write(body);
        writer.close();

        //得到响应码
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            //得到响应流
            InputStream inputStream = connection.getInputStream();
            //将响应流转换成字符串
            String result = inputStream.toString();//将流转换为字符串。
        }
        System.out.println(responseCode);
        return "success";
    }

    @RequestMapping(value="/doreg",method = RequestMethod.POST)
    @ResponseBody
    public String doregister(HttpServletRequest request,Model model){
        //HttpServletResponse response =

        String name = request.getParameter("username");
        String pas = request.getParameter("pw");
        this.userservice.createUser(name,pas);
        return "success";

    }

    @RequestMapping(value="/dodel",method = RequestMethod.DELETE)
    @ResponseBody
    public String dodel(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        List<Integer> list = (List<Integer>) session.getAttribute("list");
        this.userservice.deluserByBatch(list);
        return "success";
    }

    @RequestMapping(value="/doins",method = RequestMethod.POST)
    @ResponseBody
    public String doinsert(HttpServletRequest request,Model model){
        User_1 userdata =(User_1)JSON.parse(request.getParameter("data"));
        this.userservice.insertuserByBatch(userdata);
        return "success";
    }


}

