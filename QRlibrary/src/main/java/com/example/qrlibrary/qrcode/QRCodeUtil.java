package com.example.qrlibrary.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

/**
 * 二维码生成工具类
 * Created by Shuwen on 2016/9/22.
 */
public class QRCodeUtil {
    /**
     * 将指定的内容生成成二维码
     * <p/>
     * 将要生成二维码的内容
     *
     * @return 返回生成好的二维码事件
     */
    public static Bitmap CreateTwoDCode(String str) {
        try {

            String content = str;
            // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //编码
            hints.put(EncodeHintType.MARGIN, 2);
            BitMatrix matrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, 800, 800,hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}