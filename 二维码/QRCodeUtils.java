package com.onetoo.www.onetoo.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by longShun on 2016/10/9.
 * 二维码相关
 */
public final class QRCodeUtils {

    private static final int QRCODE_SIZE = 500;
    private static final String TAG = "QRCodeUtils";

    /**
     * @param content 生成二维码的文本
     * @param imageView 要设置二维码的控件
     * @param avatar 中间小头像
     */
    public static void createQrCodeWithAvatarToView(String content,ImageView imageView, Bitmap avatar){
        Bitmap qrCode = create2DCode(content);
        Log.d(TAG, "createQrCodeWithAvatarToView: "+qrCode.toString());
        //韦控件设置填充的二维码
        if (imageView != null) {
            imageView.setImageBitmap(qrCode);
        }
        drawQRCodeBitmapWithPortrait(imageView,qrCode,avatar);
    }

    /**
     * @param ivQrCode 装有二维码的控件
     */
    public static void saveQRCode(ImageView ivQrCode,String dirPath) {
        Drawable drawable = ivQrCode.getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap1 = bitmapDrawable.getBitmap();
        File file = new File(dirPath,"qrCode.png");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.PNG,100, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在二维码上绘制头像
     */
    public static void drawQRCodeBitmapWithPortrait(ImageView imageView,Bitmap qrCode, Bitmap avatar) {
        if (imageView == null || qrCode ==null || avatar == null) {
            throw new RuntimeException("args cant be null!");
        }
        //avatar被重复使用造成异常

        //生成中间小图片,一般不超过二维码尺寸1/5
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(
                avatar, QRCODE_SIZE / 6, QRCODE_SIZE / 6, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        // 头像图片的大小
        int thumbnail_W = thumbnail.getWidth();
        int thumbnail_H = thumbnail.getHeight();

        // 设置头像要显示的位置，即居中显示
        int left = (QRCODE_SIZE - thumbnail_W) / 2;
        int top = (QRCODE_SIZE - thumbnail_H) / 2;
        int right = left + thumbnail_W;
        int bottom = top + thumbnail_H;
        Rect rect1 = new Rect(left, top, right, bottom);

        // 取得qr二维码图片上的画笔，即要在二维码图片上绘制我们的头像
        Canvas canvas = new Canvas(qrCode);

        // 设置我们要绘制的范围大小，也就是头像的大小范围
        Rect rect2 = new Rect(0, 0, thumbnail_W,thumbnail_H);
        // 开始绘制
        canvas.drawBitmap(thumbnail, rect2, rect1, null);
    }

    /**
     *
     * @param string string
     * @return bitmap
     */
    public static Bitmap create2DCode(String string) {
        return Create2DCode(string, 0, 0);
    }

    /**
     * @param str 用字符串生成二维码
     */
    public static Bitmap Create2DCode(String str, int codeWidth, int codeHeight) {
        // 用于设置QR二维码参数
        Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别——这里选择最高H级别
        qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //判断用户指定的二维码大小
        if (codeWidth < 200 || codeHeight < 200 || codeWidth > 600 || codeHeight > 600) {
            codeWidth = QRCODE_SIZE;
            codeHeight = QRCODE_SIZE;
        }
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix;
        try {
            matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, codeWidth, codeHeight,qrParam);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }else{//这个else要加上去，否者保存的二维码全黑
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
