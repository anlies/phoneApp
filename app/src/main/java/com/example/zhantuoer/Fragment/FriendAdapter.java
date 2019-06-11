package com.example.zhantuoer.Fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhantuoer.R;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context mContext;
    public static List<FriendBase> friendBases;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View friendView;
        TextView friendName;
        RelativeLayout beijing;
        Button zhuli;
        ProgressBar progressBar;
        Button jiandu;

        //这个View 就是每个List的最外层布局
        public ViewHolder(View view){
            super(view);
            friendView = view;
            friendName = (TextView) view.findViewById(R.id.friend_name);
            beijing = (RelativeLayout)view.findViewById(R.id.beijing);
            zhuli = (Button)view.findViewById(R.id.zhuli);
            progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
            jiandu = (Button)view.findViewById(R.id.jiandu);
        }
    }

    public FriendAdapter(Context mContext,List<FriendBase> friendBases){
        this.friendBases = friendBases;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_base,parent,false);

        //列表点击事件 内部类访问成员变量用final修饰
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.zhuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = viewHolder.progressBar.getProgress();
                progress = progress + 10;
                viewHolder.progressBar.setProgress(progress);
            }
        });
        viewHolder.jiandu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"申请监督成功",Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FriendBase friendBase = friendBases.get(position);
        holder.friendName.setText(friendBase.getUserName());
        holder.beijing.setBackgroundColor(mContext.getResources().getColor(friendBase.getBeijing()));
    }


    @Override
    public int getItemCount() {
        return friendBases.size();
    }
}
