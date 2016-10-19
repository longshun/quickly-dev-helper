
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.beyondsoft.giinii.album.turnpage.BookLayout;
import com.beyondsoft.giinii.album.turnpage.PageContent;
import com.beyondsoft.giinii.album.turnpage.SinglePage;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.provider.MediaStore;
import android.util.Log;

public class BitmapUtil {
	public static final String THUMBNAIL_DIR = ".thumbnails";

	public static Bitmap getBitmap(Context context, int id, int thumbType) {
		ContentResolver contentResolver = context.getContentResolver();
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
				contentResolver, id, thumbType, null);
		return bitmap;
	}

	public static Bitmap createBitmap(Bitmap src, Bitmap watermark,
			float degrees) {
		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap newb = Bitmap.createBitmap(w + 20, h + 20, Config.ARGB_4444);
		Canvas cv = new Canvas(newb);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		cv.rotate(degrees);
		cv.drawBitmap(src, 20, 0, paint);
		cv.rotate(-degrees);
		cv.drawBitmap(watermark, 16, 5, paint);

		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		cv = null;
		return newb;
	}

	public static Bitmap drawLine(Bitmap src) {

		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_4444);
		Canvas cv = new Canvas(newb);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(5);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		cv.drawBitmap(src, 0, 0, null);
		cv.drawLine(0, 0, w - 1, 0, paint);
		cv.drawLine(0, 0, 0, h - 1, paint);
		cv.drawLine(w - 1, 0, w - 1, h - 1, paint);
		cv.drawLine(0, h - 1, w - 1, h - 1, paint);

		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		cv = null;
		return newb;
	}

	public static boolean judegBitmap(Bitmap bitmap1, Bitmap bitmap2) {
		int[] pixels1 = new int[bitmap1.getWidth() * bitmap1.getHeight()];
		int[] pixels2 = new int[bitmap2.getWidth() * bitmap2.getHeight()];
		bitmap1.getPixels(pixels1, 0, bitmap1.getWidth(), 0, 0, bitmap1
				.getWidth(), bitmap1.getHeight());
		bitmap2.getPixels(pixels2, 0, bitmap2.getWidth(), 0, 0, bitmap2
				.getWidth(), bitmap2.getHeight());
		int length1 = pixels1.length;
		int length2 = pixels2.length;
		if (length2 != length1) {
			return false;
		} else {
			for (int i = 0; i < length1; i++) {
				int color1 = pixels1[i];
				int color2 = pixels2[i];
				if (color1 != color2) {
					return false;
				}
			}
		}
		return true;
	}

	public static int[] getImageWH(File file) {
		int[] wh = { -1, -1,-1 };
		if (file != null && file.exists() && !file.isDirectory()) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				InputStream is = new FileInputStream(file);
				BitmapFactory.decodeStream(is, null, options);
				wh[0] = options.outWidth;
				wh[1] = options.outHeight;
				wh[2] = is.available();
			} catch (Exception e) {
				// Log.w(tag, e.getMessage(), e);
			}
		}

		return wh;
	}

	public static Bitmap getBitmap(File file, int width, int height) {
		Bitmap bitmap;
		int[] wh = getImageWH(file);
		int oriWidth = wh[0];
		int oriHeight = wh[1];
		int size = wh[2];
		int scale = 1;
		int dstWidth = wh[0], dstHeight = wh[1];
		Log.i("ivan","size = "+size);
		while (dstWidth > width || dstHeight > height) {
			scale *= 2;
			dstWidth = oriWidth / scale;
			dstHeight = oriHeight / scale;
		}
		if (scale > 1) {
			scale /= 2;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = scale;
		Log.i("ivan","w= "+dstWidth+" h ="+dstHeight);
		bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		if (oriWidth > 1000 || oriHeight > 1000) {
			Bitmap b = Bitmap.createBitmap(oriWidth/scale, oriHeight/scale, Bitmap.Config.ARGB_4444);
			Canvas c = new Canvas(b);
			
			c.drawBitmap(bitmap, new Matrix(), new Paint());
			bitmap.recycle();
			Log.i("ivan","w= "+b.getWidth()+" h ="+b.getHeight());
			return b;
		}else return bitmap;
	}

	public static Bitmap fitSizePic(String path) {
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = false;
		int scalePer = 1;
		try {
			File file = new File(path);
			long size = file.length();
			while(true){
				if(size > (1.2 * 1024 * 1024)){
					size /= 2;
					scalePer += 1;
					Log.e("fileSize.................", "" + size);
					continue;
				}else{
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		bfo.inPreferredConfig = Bitmap.Config.RGB_565;  
		bfo.inSampleSize = scalePer;
		Bitmap bmp = BitmapFactory.decodeFile(path, bfo);
		return bmp;
//		File file = new File(path);
//		Bitmap resizeBmp = null;
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//
//		// 数字越大读出的图片占用的heap越小 不然总是溢出
//		if (file.length() < 819200) {
//			opts.inSampleSize = 1;
//		} else if (file.length() < 1048576) { 
//			opts.inSampleSize = 2;
//		} else if (file.length() < 2048576) { 
//			opts.inSampleSize = 3;
//		} else if (file.length() < 3048576) {
//			opts.inSampleSize = 4;
//		} else {
//			opts.inSampleSize = 5;
//		}
//		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
//		return resizeBmp;
	}

	public static Bitmap catchOOM(String filePath, Options o) {
		Bitmap bitmap = null;
		try {
			o.inSampleSize += 1;
			bitmap = BitmapFactory.decodeFile(filePath, o);
		} catch (OutOfMemoryError e) {
			return catchOOM(filePath, o);
		}
		return bitmap;

	}
	
	  public static float[] scalePic(String path, int widthToScale, int heightToScale){
			
			if(path == null){
				return null;
			}
			
			int height = heightToScale;
			int width = widthToScale;
			Bitmap bmp = fitSizePic(path);
			int photoWidth = bmp.getWidth();
			int photoHeight = bmp.getHeight();
			bmp.recycle();
			float scaleW = 0f;
			float scaleH = 0f;
			if((photoWidth > width) || (photoHeight > height)){
				scaleW = ((float)width) / photoWidth;
				scaleH = ((float)height) / photoHeight;
				if(scaleW < scaleH){
					scaleW = scaleH;
				}else{
					scaleH = scaleW;
				}
			}else if((photoWidth < width) && (photoHeight < height)){
				scaleW = ((float)photoWidth) / width;
				scaleH = ((float)photoHeight) / height;
				if(scaleW > scaleH){
					scaleW = scaleH;
				}else{
					scaleH = scaleW;
				}
			}
			float[] scale = new float[2];
			scale[0] = scaleW;
			scale[1] = scaleH;
			return scale;
		}
	  
	  //执行前后翻页
	  public static void filpPage(BookLayout bookLayout, Context context,List<Bitmap> bitmaps, boolean flag) {
			// TODO Auto-generated method stub
			List<SinglePage> singlePageList = new ArrayList<SinglePage>();
			for(Bitmap bitmap : bitmaps){
				PageContent content = new PageContent(context, bitmap);
				SinglePage page = new SinglePage(context, bookLayout, content);
				singlePageList.add(page);
			}
			bookLayout.setBookLayout(987, 712);
			bookLayout.setPageList(singlePageList);
			if(flag){
				singlePageList.get(0).autoFlip(flag);
			}else{
				singlePageList.get(1).autoFlip(flag);
			}
		}
}
