package com.onetoo.www.onetoo.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by longShun on 2016/10/12.
 * OkHttp网络访问工具类
 */
public class OkHttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final String TAG = "OkHttpUtil";

    static {
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * 该不会开启异步线程。
     *
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 异步get访问网络
     *
     * @param url      网络地址
     * @param callback 回调接口
     */
    public static void doGet(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        enqueue(request, callback);
    }

    public static void doPost(String url, String xml, Callback callback) {
        MediaType MEDIA_TYPE_XML = MediaType.parse("text/xml; charset=utf-8");
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_XML, xml)).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public static void doPost(String url, FormEncodingBuilder builder, Callback callback){
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 为get请求添加一个参数，返回带参数的地址
     */
    public static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }

    /**
     * 为get请求添加多个参数，返回带参数的地址
     */
    public static String attachHttpGetParams(String url, HashMap<String, String> hashMap) {
        String ret = null;
        if (url != null && hashMap != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(url).append("?");
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.append(key).append("=").append(value).append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
            ret = builder.toString();
        }
        return ret;
    }

    /**
     * 上传多张图片
     */
    public static void uploadImages(@NonNull String url, @NonNull List<String> imagePaths, @NonNull Callback callback) {
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (int i = 0; i < imagePaths.size(); i++) {
            File f = new File(imagePaths.get(i));
            builder.addFormDataPart("UploadFileForm[imageFile]", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /*上传一张图片*/
    public static void uploadImage(String url, String imgPath, Callback callback) {
        List<String> imageList = new ArrayList<>();
        imageList.add(imgPath);
        uploadImages(url, imageList, callback);
    }
    //断点下载
    public static void download(Callback callback){
        Request request = new Request.Builder().url("http://www.onetoo.me/app-onetoo.apk").build();
        enqueue(request,callback);
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//
//            @SuppressWarnings("resource")
//            @Override
//            public void onResponse(Response response) throws IOException {
//                InputStream is = null;
//                byte[] buf = new byte[2048];
//                int len = 0;
//                FileOutputStream fos = null;
//                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//                try {
//                    is = response.body().byteStream();
//                    long total = response.body().contentLength();
//                    File file = new File(SDPath, "onetoo.apk");
//                    fos = new FileOutputStream(file);
//                    long sum = 0;
//                    fos.flush();
//                    Log.i("tiancao", "文件下载成功");
//                } catch (Exception e) {
//                    Log.i("tiancao", "文件下载失败");
//                } finally {
//                    try {
//                        if (is != null)
//                            is.close();
//                    } catch (IOException e) {
//                    }
//                    try {
//                        if (fos != null)
//                            fos.close();
//                    } catch (IOException e) {
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Request arg0, IOException arg1) {
//                Log.d("h_bl", "onFailure");
//            }
//        });
    }
}
