package com.example.zhantuoer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhantuoer.R;
import com.example.zhantuoer.TieContentActivity;

import java.util.List;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private Context mContext;
    public static List<RecyclerGoal> recyclerGoals;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View goalView;
        ImageView userImage;
        TextView userName;
        TextView recycler_tittle;
        TextView recycler_content;
        ImageView firstImage;
        ImageView secondImage;
        ImageView thirdImage;
        LinearLayout chushi;
        TextView good_number;
        TextView fatie_time;

        //这个View 就是每个List的最外层布局
        public ViewHolder(View view){
            super(view);
            goalView = view;
            userImage = (ImageView)view.findViewById(R.id.userImage);
            userName = (TextView) view.findViewById(R.id.userName);
            recycler_tittle = (TextView)view.findViewById(R.id.recycler_tittle);
            recycler_content =(TextView)view.findViewById(R.id.recycler_content);
            firstImage = (ImageView)view.findViewById(R.id.firstImage);
            secondImage = (ImageView)view.findViewById(R.id.secondImage);
            thirdImage = (ImageView)view.findViewById(R.id.thirdImage);
            chushi = (LinearLayout)view.findViewById(R.id.chushi);
            chushi.setVisibility(View.GONE);
            good_number = (TextView)view.findViewById(R.id.good_number);
            fatie_time =(TextView)view.findViewById(R.id.fatie_time);
        }
    }

    public GoalAdapter(Context mContext,List<RecyclerGoal> recyclerGoals){
        this.recyclerGoals = recyclerGoals;
        this.mContext = mContext;
    }

    //利用position寻找每个子项，然后对每个子项赋值
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecyclerGoal recyclerGoal = recyclerGoals.get(position);
        glideSetImage(holder,position,"mPhoto");
        holder.userName.setText(recyclerGoal.getName());
        holder.recycler_tittle.setText(recyclerGoal.getTittle());
        holder.recycler_content.setText(recyclerGoal.getContent());
        StringBuilder s = new StringBuilder("点赞数："+recyclerGoal.getGood_number());
        holder.fatie_time.setText(recyclerGoal.getTime());
        holder.good_number.setText(s);
        if (!recyclerGoal.getmThirdImageId().equals("") &&
                !recyclerGoal.getmSecondImageId().equals("") &&
                !recyclerGoal.getmFirstImageId().equals("")) {
            glideSetImage(holder,position,"thiImage");
            glideSetImage(holder,position,"secImage");
            glideSetImage(holder,position,"firstImage");
            showImage(holder,1,1,1);
            holder.chushi.setVisibility(View.VISIBLE);
        }else if (!recyclerGoal.getmSecondImageId().equals("") &&
                !recyclerGoal.getmFirstImageId().equals("")) {
            glideSetImage(holder,position,"secImage");
            glideSetImage(holder,position,"firstImage");
            showImage(holder,1,1,0);
            holder.chushi.setVisibility(View.VISIBLE);
        } else if(!recyclerGoal.getmFirstImageId().equals("")){
            glideSetImage(holder,position,"firstImage");
            showImage(holder,1,0,0);
            holder.chushi.setVisibility(View.VISIBLE);
        }else {
            holder.chushi.setVisibility(View.GONE);
        }
    }

    private void showImage(ViewHolder holder,int firstImage,int secondImage,int lastImage){
        holder.firstImage.setVisibility(View.INVISIBLE);
        holder.secondImage.setVisibility(View.INVISIBLE);
        holder.thirdImage.setVisibility(View.INVISIBLE);
        if(firstImage==1){
            holder.firstImage.setVisibility(View.VISIBLE);
        }
        if(secondImage == 1){
            holder.secondImage.setVisibility(View.VISIBLE);
        }
        if(lastImage == 1){
            holder.thirdImage.setVisibility(View.VISIBLE);
        }
    }

    private void glideSetImage(ViewHolder holder, int position,String imageID){
        switch (imageID){
            case "mPhoto":
                Glide.with(mContext)
                    .load(recyclerGoals.get(position).getPhoto()) //加载地址
                    .placeholder(R.drawable.nav_icon)
                    .error(R.drawable.nav_icon)
                    .into(holder.userImage);//显示的位置
            break;
            case "firstImage":
                Glide.with(mContext)
                        .load(recyclerGoals.get(position).getmFirstImageId()) //加载地址
                        .into(holder.firstImage);//显示的位置
                break;
            case "secImage":
                Glide.with(mContext)
                        .load(recyclerGoals.get(position).getmSecondImageId()) //加载地址
                        .into(holder.secondImage);//显示的位置
                break;
            case "thiImage":
                Glide.with(mContext)
                        .load(recyclerGoals.get(position).getmThirdImageId()) //加载地址
                        .into(holder.thirdImage);//显示的位置
                break;
            default:
        }
    }

    //找到父项并返回ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list,parent,false);

        //列表点击事件 内部类访问成员变量用final修饰
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.goalView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                RecyclerGoal recyclerGoal = recyclerGoals.get(position);
                Intent intent = new Intent(v.getContext(),TieContentActivity.class);
                intent.putExtra("user_name",recyclerGoal.getName());
                intent.putExtra("tID",recyclerGoal.gettID());
                intent.putExtra("photo",recyclerGoal.getPhoto());
                intent.putExtra("guanzhu",recyclerGoal.getGuanzhuUser());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.userImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Toast.makeText(v.getContext(),"click了头像",Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    //返回子项长度
    @Override
    public int getItemCount() {
        return recyclerGoals.size();
    }
}
