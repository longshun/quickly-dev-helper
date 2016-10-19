

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.beyondsoft.photoriver.res;

public class Utils implements res {

	/*
	 * public static Bitmap[] getBitmap(Context c) { // TODO Auto-generated
	 * method stub Bitmap bmp; Bitmap[] bmps = new Bitmap[resurcesId.length];
	 * for(int i=0;i<resurcesId.length;i++) bmps[i] =
	 * BitmapFactory.decodeResource(c.getResources(), resurcesId[i]);
	 * 
	 * return bmps; }
	 */

	public static float makeRodom(int i) {
		float rodom = (float) (Math.random() * i);
		return rodom;
	}

	public static int[] loadBitmap(GL10 gl, Bitmap[] bitmaps) {
		int[] texs = new int[bitmaps.length];
		int[] mCropWorkspace = new int[4];
		if (gl != null) {
			for (int i = 0; i < bitmaps.length; i++) {
				Bitmap bitmap = bitmaps[i];
				gl.glGenTextures(1, texs, i);

				gl.glBindTexture(GL10.GL_TEXTURE_2D, texs[i]);

				gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
						GL10.GL_CLAMP_TO_EDGE);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
						GL10.GL_CLAMP_TO_EDGE);

				gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
						GL10.GL_REPLACE);

				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
				int error = gl.glGetError();
				if (error != GL10.GL_NO_ERROR) {
					Log.e("SpriteMethodTest", "texImage2D: " + error);
				}
				mCropWorkspace[0] = 0;
				mCropWorkspace[1] = bitmap.getHeight();
				mCropWorkspace[2] = bitmap.getWidth();
				mCropWorkspace[3] = -bitmap.getHeight();
				Log.i("marktexture:", "  bitmap.getHeight()"
						+ bitmap.getHeight() + ",bitmap.getWidth():"
						+ bitmap.getWidth());
				bitmap.recycle();

				((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
						GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);

				error = gl.glGetError();
				if (error != GL10.GL_NO_ERROR) {
					Log.e("SpriteMethodTest", "Texture Load GLError: " + error);
				}
			}
		}

		return texs;
	}

	private static List<Point> points;

	public static void initMap() {
		rep = new ArrayList<Integer>();
		points = new ArrayList<Point>();
		points.add(new Point(1.5f, 0.7f, 700, 80));
		points.add(new Point(1.5f, 0.0f, 700, 240));
		points.add(new Point(1.5f, -0.7f, 700, 400));

		points.add(new Point(0.8f, 0.7f, 560, 80));
		points.add(new Point(0.8f, 0.0f, 560, 240));
		points.add(new Point(0.8f, -0.7f, 560, 400));

		points.add(new Point(0.1f, 0.7f, 420, 80));
		points.add(new Point(0.1f, 0.0f, 420, 240));
		points.add(new Point(0.1f, -0.7f, 420, 400));

		points.add(new Point(-0.7f, 0.7f, 280, 80));
		points.add(new Point(-0.7f, 0.0f, 280, 240));
		points.add(new Point(-0.7f, -0.7f, 280, 400));
	}

	public static void makePhoto(Bitmap[] bitmaps) {
		// TODO Auto-generated method stub
		// Photo.photos = new Photo[bitmaps.length];
		for (int i = 0; i < bitmaps.length; i++) {
			float y = makeRodom(1) * 0.9f;
			if (makeRodom(1) > 0.5) {
				y = -y;
			}

			// Photo photo = new Photo(-2-makeRodom(2),y,z);
			// Photo.photos[i] = photo;
		}
	}

	private static List<Integer> rep;

	public static Point getPoint() {
		int x = (int) (makeRodom(12));
		Log.i("iva", "x=" + x);
		x = findRepeat(x);
		Point p = null;
		rep.add(x);
		p = points.get(x);
		return p;

	}

	private static int findRepeat(int x) {
		// TODO Auto-generated method stub
		boolean is = true;
		for (Integer i : rep) {
			if (i == x) {

				is = false;
			}
		}

		if (is) {
			return x;
		} else {
			x = (int) (makeRodom(110) % 10);
			return findRepeat(x);
		}

	}

	public static int[] waterresurcesId = new int[] {
	/*
	 * R.drawable.shui, R.drawable.a, R.drawable.b
	 */
	};

	/*
	 * public static int[]resurcesId = new int[]{ R.drawable.gui_lss,
	 * R.drawable.a, R.drawable.aa, R.drawable.b, R.drawable.bb,
	 * R.drawable.yuan_yfg, R.drawable.lang_mxj, R.drawable.lv_ymj,
	 * R.drawable.xian_hdd, R.drawable.zeus };
	 */

	public static void initWindows(Activity activity) {
		activity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
