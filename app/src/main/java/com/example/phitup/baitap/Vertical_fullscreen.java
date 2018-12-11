package com.example.phitup.baitap;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class Vertical_fullscreen extends AppCompatActivity {

    VideoView mVideoView1;
    SeekBar mSeekBar;
    Handler mHandler = new Handler();
    Utilities utils;
    TextView txtTotal, txtCurrent;
    int currentPosition= 0;
    int progress = 0;
    long totalDuration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vertical_fullscreen);

        mSeekBar = findViewById(R.id.ProgressBar);
        mVideoView1 = findViewById(R.id.my_video_view1);
        mVideoView1.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.demo);


        txtCurrent = findViewById(R.id.textViewCurrent);
        txtTotal = findViewById(R.id.textViewTotal);
        mSeekBar.setProgress(0);
        mSeekBar.setMax(100);

        utils = new Utilities();

        if (savedInstanceState != null){
            Log.d("BBB", "Saved");
            int progress = savedInstanceState.getInt("videoProgress");
            mVideoView1.seekTo(progress);
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTimeTask);
                totalDuration = mVideoView1.getDuration();
                currentPosition = utils.progressToTimer(mSeekBar.getProgress(), (int)totalDuration);
                mVideoView1.seekTo(currentPosition);
                updateProgressBar();
            }
        });
        Log.d("BBB", "not Save");
        Log.d("BBB", "Giá trị : " + savedInstanceState);
        mVideoView1.start();
//        mHandler.removeCallbacks(updateTimeTask);
//        totalDuration = mVideoView1.getDuration();
//        if(savedInstanceState == null){
//            currentPosition = utils.progressToTimer(mSeekBar.getProgress(),
//                    totalDuration);
//            mVideoView1.seekTo(currentPosition);
//            Log.d("BBB", "Bundle == null");
//        }
        updateProgressBar();
    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            long totalDuration = mVideoView1.getDuration();
            long currentDuration = mVideoView1.getCurrentPosition();
            progress = (utils.getProgressPercentage(currentDuration,
                    totalDuration));
            String Current = utils.milliSecondsToTimer(currentDuration);
            String Total = utils.milliSecondsToTimer(totalDuration);
            txtCurrent.setText(Current);
            txtTotal.setText(Total);
            mSeekBar.setProgress(progress);
            currentPosition = utils.progressToTimer(mSeekBar.getProgress(), (int)totalDuration);
//            Log.d("BBB","Progress : " +  progress);
//            Log.d("BBB","current position : " + currentPosition);
            mHandler.postDelayed(this, 100);

        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("BBB", "Send");
        outState.putInt("videoProgress", mSeekBar.getProgress());
    }

}
