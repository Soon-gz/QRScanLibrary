package com.example.administrator.qrscanlibrary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.qrlibrary.qrcode.QRCode2ScanActivity;
import com.example.qrlibrary.qrcode.decode.Utils;
import com.google.zxing.Result;

/**
 * Created by Administrator on 2016/10/18.
 */

public class QRScanAtivity extends QRCode2ScanActivity {

    private static final int REQUEST_CODE = 234;
    private String photo_path;
    @Override
    public void handleDecode(Result rawResult, Bundle bundle) {
        super.handleDecode(rawResult, bundle);
        //扫描成功，回调的方法
        inactivityTimer.onActivity();

        bundle.putString("result", rawResult.getText());

        startActivity(new Intent(QRScanAtivity.this, ResultActivity.class).putExtras(bundle));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE:

                    String[] proj = { MediaStore.Images.Media.DATA };
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(),
                            proj, null, null, null);

                    if (cursor.moveToFirst()) {

                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(column_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(),
                                    data.getData());
                            Log.i("123path  Utils", photo_path);
                        }
                        Log.i("123path", photo_path);
                    }

                    cursor.close();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Result result = scanningImage(photo_path);
                            // String result = decode(photo_path);
                            if (result == null) {
                                Log.i("123", "   -----------");
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "图片格式有误",Toast.LENGTH_SHORT)
                                        .show();
                                Looper.loop();
                            } else {
                                Log.i("tag00", result.toString());
                                // Log.i("123result", result.getText());
                                // 数据返回
                                String recode = recode(result.toString());
                                //解析成功之后，recode是解析成都的字符串，这里对其做数据处理
                                startActivity(new Intent(QRScanAtivity.this,ResultActivity.class).putExtra("result", recode));
                                finish();
                            }
                        }
                    }).start();
                    break;

            }

        }
    }
}
