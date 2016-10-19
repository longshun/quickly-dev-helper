

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class ToolsImgEditMethod {
	
	
	//��ԭͼ�����򣬷�����ͼ
	public Bitmap creatBitmapByModel(Bitmap src, Bitmap model,int alpha) {
		int srcW = src.getWidth();
		int srcH = src.getHeight();
		
	    Bitmap	new_map = Bitmap.createBitmap(srcW, srcH , Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(new_map);
		canvas.drawBitmap(src, 0, 0, null);
		Paint paint = new Paint();
		paint.setAlpha(alpha);
		canvas.drawBitmap(model, 0,0, paint);
		canvas.save();
		return new_map;
	}
	
	/**
	 * ���������ԭͼ���һ����ϵ���ͼƬ
	 * FIXME 
	 * @param src
	 * @param size
	 * @param color
	 * @param text
	 * @param x
	 * @param y
	 * @return
	 */
	public Bitmap creatBitmapByFont(Bitmap src, int size, int color, String text, int x, int y) {
		int srcW = src.getWidth();
		int srcH = src.getHeight();
	    Bitmap	new_map = Bitmap.createBitmap(srcW, srcH , Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(new_map);
	    canvas.drawBitmap(src, 0, 0, null);
	    Paint paint = new Paint();
	    paint.setColor(color);
	    paint.setTextSize(size);
	    canvas.drawText(text, x, y, paint);
	    canvas.save();
	    return new_map;
	}
	
	/**
	 * ͼƬ��ת
	 * FIXME 
	 * @param mBitmap
	 * @param angle
	 * @return
	 */
	public Bitmap imgRotate(Bitmap mBitmap, float angle) {
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap bp = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix,false);
		return bp;
	}
	
	/**
	 * ��ȡ���к� de ͼƬ
	 * @return
	 */
	public Bitmap getCutBitmapByLeupRido(Bitmap src, Rect rect){
		try {
			int width = src.getWidth();
			int height = src.getHeight();
			if(rect.left > width || rect.top > height || (rect.left + rect.right) < 0
					|| (rect.top + rect.bottom) < 0) {
				return null;
			} else {
				if(rect.left < 0) {
					rect.right += rect.left;
					rect.left = 0;
				}
				if(rect.top < 0) {
					rect.bottom += rect.top;
					rect.top = 0;
				}
				if(rect.left + rect.right > width) {
					rect.right = width - rect.left;
				}
				if(rect.top + rect.bottom > height) {
					rect.bottom = height - rect.top;
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(src, rect.left, rect.top, rect.right, rect.bottom);
			return bitmap;
		}catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * ��������
	 * ���������򷵻�һ���ƽ����ͼƬ
	 * @param bitmap ԭʼͼƬ
	 * @param rect ����������ԭʼͼƬ��λ��
	 * @return
	 */
	public Bitmap getBalanceBitmap(Bitmap bitmap, Rect rect) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		int[] pixels = new int[width * height];
		int[] new_pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		int r = 0, g = 0, b = 0;
		int new_r = 0, new_g = 0, new_b = 0;
		int color = getAVG(bitmap, rect);
		
		float[] scale = new float[3];
//		scale = getScale(color);
		

		for (int i = 0; i < pixels.length; i++) {
			r = Color.red(pixels[i]);
			g = Color.green(pixels[i]);
			b = Color.blue(pixels[i]);
			int gray_r = Color.red(color);
			int gray_g = Color.green(color);
			int gray_b = Color.blue(color);
			
//			int gray = Math.max(Math.max(gray_r, gray_g), gray_b); //max
//			int gray = Math.min(Math.min(gray_r, gray_g), gray_b); //min
			int gray = (gray_r + gray_g + gray_b) / 3; //avg
			
			new_r = (int)r * gray / Color.red(color);
			if (new_r > 255) {
				new_r = 255;
			}
			new_g = (int)g * gray / Color.green(color);
			if(new_g > 255) {
				new_g = 255;
			}
			new_b = (int)b * gray / Color.blue(color);
			if(new_b > 255) {
				new_b = 255;
			}
			new_pixels[i] = Color.rgb(new_r, new_g, new_b);
		}
		newBitmap.setPixels(new_pixels, 0, width, 0, 0, width, height);
		return newBitmap;

	}
    
//	/**
//	 * ��ȡ��ɫ�����j
//	 * @param color
//	 * @return
//	 */
//	private float[] getScale(int color) {
//		float[] scale = new float[3];
//		int r = Color.red(color);
//		int g = Color.green(color);
//		int b = Color.blue(color);
//		scale[0] = 1f * r / g;
//		scale[1] = g;
//		scale[2] = 1f * b / g;
//		return scale;
//	}

	/**
	 * ��ȡ��ɫƽ��ֵ
	 * @param bitmap ԭʼͼƬ
	 * @param rect ѡ���������
	 * @return
	 */
	private int getAVG(Bitmap bitmap, Rect rect) {
		int color = 0;
		int avg_r, r_count = 0;
		int avg_g, g_count = 0;
		int avg_b, b_count = 0;
		int[] pixels = new int[(rect.right - rect.left) * (rect.bottom - rect.top)];
		//1.(modif)Parameters 4 and 5 as the starting coordinates
		//2.���ʲ��ܳ���ͼƬ��Χ
		bitmap.getPixels(pixels, 0, rect.right - rect.left, rect.left, rect.top, rect.right - rect.left,
				rect.bottom - rect.top);
		int length = pixels.length;
														
		for (int i = 0; i < length; i++) {
			r_count += Color.red(pixels[i]);
			g_count += Color.green(pixels[i]);
			b_count += Color.blue(pixels[i]);
		}
		avg_r = r_count / length;
		avg_g = g_count / length;
		avg_b = b_count / length;
		color = Color.rgb(avg_r, avg_g, avg_b);
		return color;
	}
	
	/**
	 * ������ת�ķ���
	 * @param bitmap
	 * @return
	 */
	public Bitmap mirrorLeft(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = bitmap.getPixel(x, y);
				newBitmap.setPixel((width - 1) - x, y, color);
			}
		}
		return newBitmap;
	}

	/**
	 * ������ת�ķ���
	 * @param bitmap
	 * @return
	 */
	public Bitmap mirrorUp(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = bitmap.getPixel(x, y);
					newBitmap.setPixel(x, (height - 1) - y, color);
			}
		}
		return newBitmap;
	}
	/***
	 * ��ݶԱȶȣ��vȵ�����
	 * @param mBitmap
	 * @param contrastValue
	 * @param brightValue
	 * @return
	 */
	
	public Bitmap setConttrast(Bitmap mBitmap,float contrastValue,float brightValue){
		
		float[] brightArray={1, 0, 0, 0, brightValue, 
	               0, 1, 0, 0, brightValue,
	               0, 0,1, 0, brightValue,
	               0, 0, 0, 1, 0 };
		float[] contrastArray= {  contrastValue,0,0, 0,128*(1-contrastValue),
                0,contrastValue,0,0, 128*(1-contrastValue), 
                0,0,contrastValue,0, 128*(1-contrastValue), 
                0,0,0,1,  0};
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		Bitmap grayImg = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(grayImg);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.set(brightArray);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		
		Paint paint1=new Paint();
		ColorMatrix colorMatrix1 = new ColorMatrix();
		colorMatrix1.set(contrastArray);
		ColorMatrixColorFilter colorMatrixFilter1 = new ColorMatrixColorFilter(colorMatrix1);
	
		paint1.setColorFilter(colorMatrixFilter1);
		canvas.drawBitmap(mBitmap, 0, 0, paint);
		canvas.save();
		canvas.drawBitmap(grayImg, 0, 0, paint1);
		return grayImg;
	}
	
	/**�Զ�ȥ���� **/
	public Bitmap imgRemoveRedeye(Bitmap mBitmap) {
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		int[] pixels = new int[width * height];
		int[] newPixels = new int[width * height];
		mBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		int r = 0, g = 0, b = 0;
		for(int i = 0; i < pixels.length; i++) {
			r = Color.red(pixels[i]);
			g = Color.green(pixels[i]);
			b = Color.blue(pixels[i]);
			r  = r + (r - Math.max(g, b)) / 112 * ((g * 59 + b * 11) / 70 - r);
			newPixels[i] = Color.rgb(r, g, b);
		}
		bitmap.setPixels(newPixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	  public Bitmap imageXiuFu(Bitmap bitmap) {
	    	int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
	    	bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
	    	int length = pixels.length;
	    	int avg_r = 0, avg_g = 0, avg_b = 0;
	    	for(int i = 0; i < length; i++) {
	    		avg_r += Color.red(pixels[i]);
	    		avg_g += Color.green(pixels[i]);
	    		avg_b += Color.blue(pixels[i]);
	    	}
	    	avg_r /= length;
	    	avg_g /= length;
	    	avg_b /= length;
	    	int[] newPixels = new int[bitmap.getWidth() * bitmap.getHeight()];
	    	for(int i = 0; i < length; i++) {
	    		int r = Math.min(Color.red(pixels[i]) * 128 / avg_r, 255);
				int g = Math.min(Color.green(pixels[i]) * 128 / avg_g, 255);
				int b = Math.min(Color.blue(pixels[i]) * 128 / avg_b, 255);
				newPixels[i] = Color.rgb(r, g, b);
	    	}
	    	Bitmap img = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			img.setPixels(newPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
			return img;
	    }
	  
	  public Bitmap imageBaoGuang(Bitmap bitmap) {
		    int width = bitmap.getWidth();
		    int height = bitmap.getHeight();
		    int[] pixels = new int[width * height];
			int[] newPixels = new int[width * height];
			bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
			float[] hsv = new float[3];
			float avg = 0.0f;
			for(int i = 0; i < pixels.length; i++) {
				int color = pixels[i];
				Color.colorToHSV(color, hsv);
				avg += hsv[2];
			}
			avg = avg / pixels.length;  //ƽ��ֵ������
			float offset = 0.65f - avg;
			//�������� �����趨һ����Ϊ���ع�ķ�Χֵ 
			for(int i = 0; i < pixels.length; i++) {
				int color = pixels[i];
				Color.colorToHSV(color, hsv);
				hsv[2] += offset;
				int newColor = Color.HSVToColor(hsv);
				int r = Math.min(Color.red(newColor), 255);
				int g = Math.min(Color.green(newColor), 255);
				int b = Math.min(Color.blue(newColor), 255);
				newPixels[i] = Color.rgb(r, g, b);
			}
			Bitmap img = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			img.setPixels(newPixels, 0, width, 0, 0, width, height);
			return img;
	    }
	  
	  public Bitmap imageTiaose(Bitmap bitmap) {
		    int width = bitmap.getWidth();
		    int height = bitmap.getHeight();
	    	int[] pixels = new int[width * height];
	    	bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
	    	float[] hsv = new float[3];
	    	Bitmap img = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    	int[] new_pixels = new int[width * height];
	    	int length = pixels.length;
	    	for(int i = 0; i < length; i++) {
	    		int color = pixels[i];
	    		Color.colorToHSV(color, hsv);
	    		hsv[1] += 0.05;
	    		int newColor = Color.HSVToColor(hsv);
	    		int r = Math.min(Color.red(newColor), 255);
				int g = Math.min(Color.green(newColor), 255);
				int b = Math.min(Color.blue(newColor), 255);
				new_pixels[i] = Color.rgb(r, g, b);
	    	}
	    	img.setPixels(new_pixels, 0, width, 0, 0, width, height);
	    	return img;
	    }
	 
	  
}
