package com.nowcoder.community.entity;

public class MyTestUser {
    private String myname;
    private String myage;

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getMyage() {
        return myage;
    }

    public void setMyage(String myage) {
        this.myage = myage;
    }

    @Override
    public String toString() {
        return "MyTestUser{" +
                "myname='" + myname + '\'' +
                ", myage='" + myage + '\'' +
                '}';
    }
}
