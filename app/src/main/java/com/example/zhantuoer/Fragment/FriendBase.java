package com.example.zhantuoer.Fragment;

public class FriendBase {
    //头像
    private String photo;
    //用户名
    private String userName;
    //进度
    private int jindu;
    //背景
    private int beijing;

    public FriendBase(String photo, String userName, int jindu, int beijing) {
        this.photo = photo;
        this.userName = userName;
        this.jindu = jindu;
        this.beijing = beijing;
    }

    public String getPhoto() {
        return photo;
    }

    public String getUserName() {
        return userName;
    }

    public int getJindu() {
        return jindu;
    }

    public int getBeijing() {
        return beijing;
    }
}
