package com.example.qrlibrary.qrcode.activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qrlibrary.R;

/**
 * Created by ShuWen on 2017/2/16.
 */

public abstract class DefaultQRScanActivity extends BaseQRScanActivity{

    protected ImageView capture_scan_back_iv;
    protected ImageView capture_flashlight;
    protected  ImageView capture_scan_photo;
    protected boolean isFlashlightOpen = false;
    protected TextView capture_title_content_tv;
    protected RelativeLayout default_title_rl;
    protected ImageView scanLine;


    @Override
    protected int getTitleCustomLayout() {
        return R.layout.base_qrscan_title;
    }

    @Override
    protected void initTitleViewEvents() {
        capture_scan_back_iv = (ImageView) findViewById(R.id.capture_scan_back_iv);
        capture_flashlight = (ImageView) findViewById(R.id.capture_flashlight);
        capture_scan_photo = (ImageView) findViewById(R.id.capture_scan_photo);
        capture_title_content_tv = (TextView) findViewById(R.id.capture_title_content_tv);
        default_title_rl = (RelativeLayout) findViewById(R.id.default_title_rl);

        capture_scan_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        capture_flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlashlightOpen) {
                    cameraManager.setTorch(false); // 关闭闪光灯
                    isFlashlightOpen = false;
                } else {
                    cameraManager.setTorch(true); // 打开闪光灯
                    isFlashlightOpen = true;
                }
            }
        });

        capture_scan_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
                innerIntent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
                startActivityForResult(wrapperIntent, REQUEST_CODE);
            }
        });

    }

    protected ImageView getCaptureScanBackIv(){
        return capture_scan_back_iv;
    }

    protected RelativeLayout getTitleRelativeLayout(){
        return default_title_rl;
    }

    protected ImageView getCaptureFlashLight(){
        return capture_flashlight;
    }

    protected ImageView getCaptureScanPhoto(){
        return capture_scan_photo;
    }

    protected TextView getTitleTextView(){
        return capture_title_content_tv;
    }

    protected RelativeLayout getContainerView(){
        return scanContainer;
    }



    @Override
    protected int getContainerLayout() {
        return R.layout.base_qrscan_container;
    }

    @Override
    protected void initContainerEvents() {

    }

    @Override
    protected int getScanLayoutId() {
        return R.layout.base_qrscan_scanview;
    }

    @Override
    protected void initScanViewEvents() {
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        //扫描线条，动画设置
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        initCustomViewAndEvents();
    }

    /** 若需要自定义默认控件和事件  可以在此方法中添加  比如修改返回图标、修改标题栏背景、隐藏闪光灯等等 */
    protected abstract void initCustomViewAndEvents();
}
