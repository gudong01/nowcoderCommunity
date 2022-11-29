package com.nowcoder.community.service;

import com.nowcoder.community.dao.MyTestUserMapper;
import com.nowcoder.community.entity.MyTestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyTestUserService {
    @Autowired
    private MyTestUserMapper mapper;

    public MyTestUser findByName(String myname){
        return mapper.selectByName(myname);
    }
    public void AddTestUser(MyTestUser myTestUser){
        //myTestUser.set
        mapper.insertData(myTestUser);
    }
}
