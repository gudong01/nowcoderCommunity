package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

//实现现场隔离
//容器 持有用户信息 代替session
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user) {
        //ThreadLocal类的方法
        users.set(user);
    }

    public User getUsers() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
