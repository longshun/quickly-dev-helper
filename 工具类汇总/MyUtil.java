

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import com.beyondsoft.giinii.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MyUtil {

	MyUtil() {

	}

	public static FloatBuffer makeFloatBuffer(float[] arr) {

		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

	static int i = 0;
	public static Bitmap mBitmap;
	public static Bitmap mBitmap1;
	public static Bitmap mBitmap2;
	public static ArrayList<String> paths = new ArrayList<String>();

	public static void putImagePaths() {
		paths.add("/mnt/sdcard/img.png");
		paths.add("/mnt/sdcard/zs.jpg");
	}

	public static void loadPicture(Context c) {

		mBitmap1 = BitmapFactory.decodeResource(c.getResources(),
				R.drawable.btnall);
		mBitmap2 = BitmapFactory.decodeResource(c.getResources(),
				R.drawable.btnshoesbox);

	}

	public static Bitmap getPicture(boolean next) {

		if (next) {
			mBitmap = BitmapFactory.decodeFile(paths.get(i));
			if (++i == paths.size())
				i = 0;
			Log.i("INFO", "picture = " + paths.get(i));
		} else {
			mBitmap = BitmapFactory.decodeFile(paths.get(i));
			if (--i < 0)
				i = paths.size();
		}

		return mBitmap;
	}

}
