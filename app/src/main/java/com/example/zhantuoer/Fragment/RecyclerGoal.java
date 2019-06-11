package com.example.zhantuoer.Fragment;


public class RecyclerGoal  {
    //帖子ID
    private String tid;
    //头像
    private String mPhoto;
    //用户名
    private String name;
    //标题
    private String tittle;
    //内容
    private String content;
    //点赞数
    private String good_number;
    //时间
    private String time;
    //图片资源
    private static final String NO_IMAGE = "";
    private String mFirstImageId = NO_IMAGE;
    private String mSecondImageId = NO_IMAGE;
    private String mThirdImageId = NO_IMAGE;
    //发帖人用户名，用于关注功能
    private String guanzhuUser;

    public String getmFirstImageId() {
        return mFirstImageId;
    }

    public String getmSecondImageId() {
        return mSecondImageId;
    }

    public String getmThirdImageId() {
        return mThirdImageId;
    }

    public String getGood_number(){return good_number;}

    public String getGuanzhuUser(){return guanzhuUser;
    }

    public RecyclerGoal(String tid,String mPhoto, String name, String tittle,String content,String mFirstImageId,String mSecondImageId,String mThirdImageId,String good_number,String time,String guanzhu) {
        this.tid = tid;
        this.mPhoto = mPhoto;
        this.name = name;
        this.tittle = tittle;
        this.content = content;
        this.mFirstImageId = mFirstImageId;
        this.mSecondImageId = mSecondImageId;
        this.mThirdImageId = mThirdImageId;
        this.good_number = good_number;
        this.time = time;
        this.guanzhuUser = guanzhu;
    }


    public String getPhoto() {
        return mPhoto;
    }

    public String getName() {
        return name;
    }

    public String getTittle() {
        return tittle;
    }

    public String getContent(){return content;}

    public String getTime(){
        return time;
    }

    public String gettID(){
        return tid;
    }
}
