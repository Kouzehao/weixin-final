package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.IntentFilter;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Fragment4 extends Fragment {
    private ImageButton playPauseButton, nextButton, previousButton;
    private ImageView imageView;
    private ObjectAnimator rotationAnimator;
    private int rand=0;
    private TextView textView;
    int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    String[] texts={"告白气球","小情歌","天黑黑"};

    // 初始化旋转动画的方法
    private void initializeRotationAnimation() {
        rotationAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotationAnimator.setDuration(10000); // 一次旋转的持续时间
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimator.setInterpolator(new LinearInterpolator());
    }

    // BroadcastReceiver，用于接收 MyService 发送的音乐信息
    private BroadcastReceiver musicInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.musicplayer.MUSIC_INFO".equals(intent.getAction())) {
                int musicIndex = intent.getIntExtra("musicIndex", 0);
                updateImageView(musicIndex);
            }
        }
    };

    // 更新 ImageView 并开始旋转动画的方法
    private void updateImageView(int musicIndex) {
        // 根据 musicIndex 更新 imageView
        imageView.setImageResource(images[rand%3]);
        textView.setText("当前播放的歌曲是--"+texts[rand%3]);
        rand++;
        if (rotationAnimator.isStarted()) {
            rotationAnimator.end();
        }
        rotationAnimator.start();
    }

    public void onStart() {
        super.onStart();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.example.musicplayer.MUSIC_INFO");
        getActivity().registerReceiver(musicInfoReceiver, filter);
        // 初始化 ImageView 和旋转动画
        textView=getView().findViewById(R.id.current_song_name);
        imageView = getView().findViewById(R.id.album_art);
                initializeRotationAnimation();
    }

    public void onStop() {
        super.onStop();
        // 取消注册广播接收器
        getActivity().unregisterReceiver(musicInfoReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab4, container, false);

        previousButton =view.findViewById(R.id.btn_previous);
        playPauseButton = view.findViewById(R.id.btn_play_pause);
        nextButton = view.findViewById(R.id.btn_next);

        // 启动音乐播放服务
        Intent serviceIntent = new Intent(getContext(), MyService.class);
        getContext().startService(serviceIntent);

        // 为按钮设置点击事件监听器
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置 Intent 的动作
                serviceIntent.setAction(MyService.ACTION_PLAY);
                getContext().startService(serviceIntent);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置 Intent 的动作
                serviceIntent.setAction(MyService.ACTION_PAUSE);
                getContext().startService(serviceIntent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置 Intent 的动作
                serviceIntent.setAction(MyService.ACTION_NEXT);
                getContext().startService(serviceIntent);
            }
        });

        return view;
    }

    private void controlMusic(String action) {
        Intent serviceIntent = new Intent(getContext(), MyService.class);
        serviceIntent.setAction(action);
        getContext().startService(serviceIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 停止音乐播放服务
        Intent serviceIntent = new Intent(getContext(), MyService.class);
        getContext().stopService(serviceIntent);
    }

}