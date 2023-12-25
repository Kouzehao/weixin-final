package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.ImageView;

public class MyService extends Service {
    MediaPlayer player;
    int[] musics = {R.raw.music1, R.raw.music2, R.raw.music3};
    // 图片资源数组
    int currentMusicIndex = 0; // 当前播放的歌曲索引

    // 定义动作字符串常量
    public static final String ACTION_PLAY = "com.example.myapplication.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.myapplication.action.PAUSE";
    public static final String ACTION_NEXT = "com.example.myapplication.action.NEXT";


    // 发送音乐信息广播的方法
    private void sendMusicInfoBroadcast() {
        // 创建一个意图（Intent）用于发送广播
        Intent intent = new Intent("com.example.musicplayer.MUSIC_INFO");
        // 向广播中添加额外信息，这里是当前音乐的索引
        intent.putExtra("musicIndex", currentMusicIndex);
        // 发送广播
        sendBroadcast(intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化音乐播放器
        initializePlayer(currentMusicIndex);
    }

    private void initializePlayer(int musicIndex) {
        // 如果播放器已存在，则先释放资源
        if (player != null) {
            player.release();
        }
        // 创建新的MediaPlayer实例，并加载指定索引的音乐
        player = MediaPlayer.create(this, musics[musicIndex]);
        // 设置音乐播放完毕的监听器
        player.setOnCompletionListener(mp -> {
            // 当一首歌曲播放完成时，自动播放下一首
            currentMusicIndex = (currentMusicIndex + 1) % musics.length;
            initializePlayer(currentMusicIndex);
            player.start();
            sendMusicInfoBroadcast(); // 发送广播，更新音乐信息
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_PLAY:
                        // 如果音乐未在播放，则开始播放
                        if (!player.isPlaying()) {
                            player.start();
                            sendMusicInfoBroadcast(); // 发送广播，更新音乐信息
                        }
                        break;
                    case ACTION_PAUSE:
                        // 如果音乐正在播放，则暂停
                        if (player.isPlaying()) {
                            player.pause();
                        }
                        break;
                    case ACTION_NEXT:
                        // 播放下一首音乐
                        currentMusicIndex = (currentMusicIndex + 1) % musics.length;
                        initializePlayer(currentMusicIndex);
                        player.start();
                        sendMusicInfoBroadcast(); // 发送广播，更新音乐信息
                        break;
                }
            }
        }
        // 返回START_STICKY，如果服务被系统终止，系统将尝试重新创建服务
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // 服务销毁时，停止并释放播放器
        if (player != null) {
            player.stop();
            player.release();
        }
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null; // 不提供绑定功能
    }


}