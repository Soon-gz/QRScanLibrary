# QRScanLibrary
这样的例子虽然已经很多了，不过我在网上浏览了一圈，也没找到几个图库二维码图片识别例子，好的算法识别率才高。这里有一个好点的算法，算法不是我写的，只是作为整理记录，给众多安卓开发者一个方便。demo的UI有点low，不过功能却是实实在在，有需要的朋友可以自定义一些UI界面。我写demo得时候就不费那劲了。
看看截图功能。
![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/a.jpg)

点击生成二维码，传入一个字符串，生成相应的二维码。

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/b.jpg)

生成二维码返回的是一个bitmap，很好处理，简单实用。点击开始扫描

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/c.jpg)

进入扫描界面，右上角有两个按钮，一个是打开手机闪光灯，一个是手机相册选取图片解析。
只要扫描成功，就会有一个回调接口，在回调接口做相应处理。下面的图片是扫描成功和图片解析成功回调的界面。

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/d.jpg)

功能介绍的差不多了，看看代码。首先导入Zxing 的gradle
//二维码扫描、识别、生成
    compile 'com.google.zxing:core:3.2.1'
然后在git上下载源码demo，取其中的QRlibrary的文件夹，导入你的项目。

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/e.png)

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/f.png)

点击确定，然后等待编译就ok了。之后再建立你的项目与QRlibrary的依赖。

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/g.png)

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/h.png)

选择了Module依赖之后，选择QRlibrary，确定之后等待编译。在你的项目中创建一个类QRScanActivity（这里是创建一个类，而不是Activity，当然也要在application中注册，只是不需要创建xml文件）继承QRCode2ScanActivity,重写以下方法即可。handleDecode是二维码扫描成功回调接口，扫描成功之后在这里写之后的逻辑。

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/i.png)

图片解析成功回调的方法，也就是onActivityResult，当然还必须调用解析的方法。

![image](https://github.com/SingleShu/QRScanLibrary/raw/master/imagecache/j.png)

图片解析逻辑单独贴出来
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
                                startActivity(new Intent(QRScanAtivity.this,ResultActivity.class).putExtra("result", recode));
                                finish();
                            }
                        }
                    }).start();
这段代码需要在onActivityResult中调用。
我在我的项目中导入library之后，创建了一个QRScanActivity，粘出所有逻辑
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
二维码生成，扫描还有图片解析，运用就是这些了。具体源码逻辑就不细谈了。我把源码放在git上，有兴趣想要了解是如何实现的朋友，那就自己去看源码吧。
如果觉得有用请帮给个star,谢谢