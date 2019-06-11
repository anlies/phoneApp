package com.example.zhantuoer;

//为了个人资料与网络对接，增加的属性构造类

public class GeRenData {
    //昵称
    private String nicheng;
    //头像
    private String headPhoto;
    //性别
    private String sex;
    //年龄
    private String age;
    //电话号码
    private String phonenumber;
    //个性签名
    private String qianming;
    //学历
    private String edu;

    public GeRenData(String nicheng,String headPhoto, String sex, String age, String phonenumber, String qianming, String edu, String job, String city) {
        this.nicheng = nicheng;
        this.headPhoto = headPhoto;
        this.sex = sex;
        this.age = age;
        this.phonenumber = phonenumber;
        this.qianming = qianming;
        this.edu = edu;
        this.job = job;
        this.city = city;
    }

    //职业
    private String job;
    //城市
    private String city;

    public String getNicheng() {
        return nicheng;
    }

    public String getHeadPhoto(){return headPhoto;}

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getQianming() {
        return qianming;
    }

    public String getEdu() {
        return edu;
    }

    public String getJob() {
        return job;
    }

    public String getCity() {
        return city;
    }
}
