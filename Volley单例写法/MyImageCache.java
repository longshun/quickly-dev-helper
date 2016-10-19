package com.example.volleytest51;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 自己定义的内存缓存类
 * @author Administrator
 *
 */
public class MyImageCache implements ImageCache {
	private LruCache<String, Bitmap> lruCache;

	/**
	 * 取图片
	 */
	public MyImageCache() {
		int maxSize = 4 * 1024 * 1024;//4m空间-->对应单位是byte
		lruCache = new LruCache<String, Bitmap>(maxSize) {
			/**
			 * 统一计算单位,和初始化的时候maxSize的单位进行统一
			 */
			@Override
			protected int sizeOf(String key, Bitmap value) {//curMaxSize+value.getByteCount();
				// TODO
				return value.getByteCount();
			}
		};
	}

	/**
	 * 内存缓存核心操作一:取的操作
	 */
	@Override
	public Bitmap getBitmap(String url) {
		// TODO
		return lruCache.get(url);
	}

	/**
	 * 内存缓存核心操作一:存的操作
	 */
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO
		lruCache.put(url, bitmap);
	}

}
