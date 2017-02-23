package com.example.administrator.qrscanlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.qrlibrary.qrcode.activity.DefaultQRScanActivity;

/**
 * Created by Administrator on 2016/10/18.
 */

public class QRScanAtivity extends DefaultQRScanActivity {


    @Override
    protected void handleDecodeResult(String rawResult, Bundle bundle) {
        bundle.putString("result",rawResult);
        startActivity(new Intent(QRScanAtivity.this, ResultActivity.class).putExtras(bundle));
        finish();
    }

    @Override
    protected void onAlbumResult(int requestCode, int resultCode, String resultData) {
        startActivity(new Intent(QRScanAtivity.this,ResultActivity.class).putExtra("result", resultData));
        finish();
    }

    @Override
    protected void initCustomViewAndEvents() {

    }
}
