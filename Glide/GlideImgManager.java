package com.onetoo.www.onetoo.utils.glideutils;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.onetoo.www.onetoo.R;

import java.io.File;
import java.net.URL;

/**
 * @author 慕泽
 * @time 2016/9/24 0024  9:42
 * @desc 操作工具类
 */
public class GlideImgManager {

    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void load(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
    }
    /*加载原圆形图片*/
    public static void loadCircle(Context context,URL url,ImageView view){
        Glide.with(context).load(url).placeholder(R.drawable.icon_load_fail).error(R.drawable.icon_load_fail).transform(new GlideCircleTransform(context)).into(view);
    }
    public static void loadCircle(Context context,Uri uri,ImageView view){
        Glide.with(context).load(uri).placeholder(R.drawable.icon_load_fail).error(R.drawable.icon_load_fail).transform(new GlideCircleTransform(context)).into(view);
    }
    public static void loadCircle(Context context,int resourceId,ImageView view){
        Glide.with(context).load(resourceId).placeholder(R.drawable.icon_load_fail).error(R.drawable.icon_load_fail).transform(new GlideCircleTransform(context)).into(view);
    }
    public static void loadCircle(Context context,File file,ImageView view){
        Glide.with(context).load(file).placeholder(R.drawable.icon_load_fail).error(R.drawable.icon_load_fail).transform(new GlideCircleTransform(context)).into(view);
    }

    public static void loadCircle(Context context, URL url, int emptyImg, int place, ImageView view) {
        Glide.with(context).load(url).placeholder(place).error(emptyImg).transform(new GlideCircleTransform(context)).into(view);
    }
    public static void loadCircle(Context context, Uri uri, int emptyImg, int place, ImageView view) {
        Glide.with(context).load(uri).placeholder(place).error(emptyImg).transform(new GlideCircleTransform(context)).into(view);
    }

    public static void loadCircle(Context context, int resource, int error, int place, ImageView view) {
        Glide.with(context).load(resource).placeholder(place).error(error).transform(new GlideCircleTransform(context)).into(view);
    }

    public static void loadCircle(Context context, File file, int error, int place, ImageView view) {
        Glide.with(context).load(file).placeholder(place).error(error).transform(new GlideCircleTransform(context)).into(view);
    }

    /*加载圆角图片*/
    public static void loadRound(Context context,File file,int emptyImg,int erroImg,ImageView view) {
        Glide.with(context).load(file).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(view);
    }
    public static void loadRound(Context context,URL url,int emptyImg,int erroImg,ImageView view) {
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(view);
    }
    public static void loadRound(Context context,Uri uri,int emptyImg,int erroImg,ImageView view) {
        Glide.with(context).load(uri).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(view);
    }
    public static void loadRound(Context context,int resourceId,int emptyImg,int erroImg,ImageView view) {
        Glide.with(context).load(resourceId).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(view);
    }


    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag
     */
    public static void load(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(iv);
        }
    }

    public static void load(Context context, int resource, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(resource).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(resource).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(iv);
        }
    }

    public static void load(Context context, File file, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(file).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(file).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(iv);
        }
    }
}
