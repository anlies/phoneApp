package com.example.zhantuoer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhantuoer.QianDaoActivity;
import com.example.zhantuoer.R;
import com.example.zhantuoer.mubiao_xiangqing;

import java.util.List;


public class JiLuAdapter extends RecyclerView.Adapter<JiLuAdapter.ViewHolder> {
    private Context mContext;
    public static List<JiLu_structor> jilus;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View jiluView;
        TextView jilu_tittle;
        TextView jindu;
        //今日签到未完成
        Button qiandao;
        //今日已完成
        Button qiandao_done;
        //任务已完成
        Button doneAll;
        //任务失败
        Button qiandao_fail;
        //这个View 就是每个List的最外层布局
        public ViewHolder(View view){
            super(view);
            jiluView = view;
            jilu_tittle = (TextView) view.findViewById(R.id.jilu_tittle);
            jindu = (TextView) view.findViewById(R.id.jindu);
            qiandao = (Button)view.findViewById(R.id.qiandao);
            qiandao_done = (Button)view.findViewById(R.id.qiandao_done);
            doneAll = (Button)view.findViewById(R.id.doneAll);
            qiandao_fail = (Button)view.findViewById(R.id.qiandao_fail);
        }
    }

    public JiLuAdapter(Context mContext,List<JiLu_structor> jilus){
        this.jilus = jilus;
        this.mContext = mContext;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mubiao_content,parent,false);

        //列表点击事件 内部类访问成员变量用final修饰
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.jiluView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                JiLu_structor jiLu_structor = jilus.get(position);
                Intent intent = new Intent(v.getContext(),mubiao_xiangqing.class);
                intent.putExtra("tID",jiLu_structor.gettID());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.qiandao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                JiLu_structor jiLu_structor = jilus.get(position);
                Intent intent = new Intent(v.getContext(),QianDaoActivity.class);
                intent.putExtra("tID",jiLu_structor.gettID());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.doneAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"恭喜你已完成了本任务",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.qiandao_fail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"没关系，再接再厉！",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.qiandao_done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"你已完成今日任务，请勿重复签到",Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.qiandao.setVisibility(View.GONE);
        holder.qiandao_done.setVisibility(View.GONE);
        holder.doneAll.setVisibility(View.GONE);
        holder.qiandao_fail.setVisibility(View.GONE);
        JiLu_structor jiLu_structor = jilus.get(position);
        holder.jilu_tittle.setText(jiLu_structor.getTittle());
        holder.jindu.setText(jiLu_structor.getJindu());
        switch(jiLu_structor.getQiandao()){
            case 0:
                holder.qiandao.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.qiandao_done.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.qiandao_fail.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.doneAll.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }


    @Override
    public int getItemCount() {
        return jilus.size();
    }
}
