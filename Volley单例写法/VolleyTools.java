package com.example.volleytest51;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

public class VolleyTools {
	private RequestQueue queue;
	private ImageLoader imageLoader;
	private ImageCache imageCache;
	private static VolleyTools instance;//定义变量

	private VolleyTools(Context context) {//私有构造函数
		queue = Volley.newRequestQueue(context);
		imageCache = new MyImageCache();
		imageLoader = new ImageLoader(queue, imageCache);
	}

	public static VolleyTools getInstance(Context context) {//暴露公开方法
		if (instance == null) {
			return instance = new VolleyTools(context);
		}
		return instance;
	}
	/**使用getter方法暴露常用对象**/

	public RequestQueue getQueue() {
		return queue;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public ImageCache getImageCache() {
		return imageCache;
	}
	

}
