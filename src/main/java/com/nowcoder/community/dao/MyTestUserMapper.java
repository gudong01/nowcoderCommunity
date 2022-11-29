package com.nowcoder.community.dao;
import com.nowcoder.community.entity.MyTestUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyTestUserMapper {
    MyTestUser selectByName(String myname);
    void insertData(MyTestUser testUser);
}
