package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket (user_id, ticket, status, expired) "+
                    "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true ,keyProperty = "id")   //主键生成 注入到id
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({"select id,user_id,ticket,status,expired from login_ticket where ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({
            //动态sql
            "<script>", //脚本
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ", // (\")是为了避免语意冲突
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket,int status);
}
