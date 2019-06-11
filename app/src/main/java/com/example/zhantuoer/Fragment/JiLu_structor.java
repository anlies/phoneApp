package com.example.zhantuoer.Fragment;

public class JiLu_structor {
    //签到ID
    private String tID;
    //记录标题
    private String tittle;
    //进度
    private String jindu;
    //按钮状态
    private int qiandao;
    //统计任务个数，用于显示在统计页
    //今日未签到的个数
    public static int qiandao_wei =0;
    //今日已完成的个数
    public static int qiandao_done=0;
    //任务已完成的个数
    public static int doneAll=0;
    //任务失败
    public static int qiandao_fail = 0;
    //任务总数
    public static int work_count = 0;
    //任务天数最长的天数
    public static int long_day =0;
    //任务天数最长的标题
    public static  String long_tittle ="";


    public JiLu_structor(String tID,String tittle, String jindu, int qiandao) {
        this.tID  = tID;
        this.tittle = tittle;
        this.jindu = jindu;
        this.qiandao = qiandao;
    }

    public String gettID(){
        return tID;
    }

    public String getTittle() {
        return tittle;
    }

    public String getJindu() {
        return jindu;
    }

    public int getQiandao() {
        return qiandao;
    }
}
