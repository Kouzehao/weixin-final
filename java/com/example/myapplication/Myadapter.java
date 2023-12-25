package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


// 自定义适配器类，继承自RecyclerView.Adapter
public class Myadapter extends RecyclerView.Adapter<Myadapter.Myholder> {

    Context context1; // 上下文，用于访问应用的资源和布局
    List<String> list1; // 用于存储显示在RecyclerView中的数据
    OnItemClickListener listener; // 点击事件监听器接口

    // 定义一个接口，用于处理项的点击事件
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener mListener; // 声明一个点击事件监听器
    // 设置点击事件的监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    // 适配器的构造函数
    public Myadapter(Context context, List list) {
        context1 = context;
        list1 = list;
        mListener = listener;
    }

    // 创建ViewHolder
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 从XML布局文件中加载布局
        View view = LayoutInflater.from(context1).inflate(R.layout.item, parent, false);
        // 创建ViewHolder实例
        Myholder holder = new Myholder(view);
        return holder;
    }

    // 绑定数据到ViewHolder
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, @SuppressLint("RecyclerView") int position) {
        // 设置数据到TextView
        holder.textView.setText(list1.get(position));
        // 设置点击事件监听器
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    // 返回列表中的项数
    @Override
    public int getItemCount() {
        return list1.size();
    }

    // 内部类，定义ViewHolder
    public class Myholder extends RecyclerView.ViewHolder {
        TextView textView; // ViewHolder中的TextView

        public Myholder(@NonNull View itemView) {
            super(itemView);
            // 获取布局中的TextView
            textView = itemView.findViewById(R.id.textView6);
        }
    }

    // 更新适配器中的数据，并通知数据改变
    public void updateData(List<String> newData) {
        list1 = newData;
        notifyDataSetChanged();
    }
}