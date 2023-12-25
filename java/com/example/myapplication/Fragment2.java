package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class Fragment2 extends Fragment {
    private RecyclerView recyclerView;
    private List<String> list = new ArrayList<>();
    private Context context;
    private Myadapter myadapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    // 该方法用于创建此fragment的视图
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 为这个fragment填充布局
        View view = inflater.inflate(R.layout.tab2, container, false);
        context = view.getContext();

        // 设置RecyclerView
        recyclerView = view.findViewById(R.id.recycleview);

        // 设置SwipeRefreshLayout，用于刷新
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        setupSwipeRefreshLayout();

        // 数据库操作，获取联系人名称
        MyDAO myDAO = new MyDAO(context);
        myDAO.connectDB();
        List<String> names = myDAO.getAllContactNames();

        // 设置RecyclerView适配器
        myadapter = new Myadapter(context, names);
        myadapter.setOnItemClickListener(new Myadapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 处理条目点击事件
                String text = names.get(position);
                // 进行跳转
                Intent intent = new Intent(getContext(), MainActivity2.class);
                // 传输数据
                intent.putExtra("data", text);
                startActivity(intent);// 启动
            }
        });
        // 设置RecyclerView的适配器
        recyclerView.setAdapter(myadapter);
        // 创建一个LinearLayoutManager用于管理RecyclerView的布局
        LinearLayoutManager manager = new LinearLayoutManager(context);
        // 设置布局管理器的方向为垂直，这意味着列表项将垂直排列
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        // 将布局管理器应用到RecyclerView
        recyclerView.setLayoutManager(manager);
        return view;
    }

    // 设置SwipeRefreshLayout的方法
    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 在这里添加刷新数据的逻辑
                refreshData(); // 调用刷新方法
            }
        });
    }

    // 刷新数据的方法
    private void refreshData() {
        MyDAO myDAO = new MyDAO(context);
        myDAO.connectDB();
        List<String> names = myDAO.getAllContactNames(); // 重新从数据库获取联系人姓名
        myadapter.updateData(names); // 更新适配器数据
        swipeRefreshLayout.setRefreshing(false); // 停止刷新动画
    }
}