

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.beyondsoft.giinii.dbhelper.dao.PhotoDao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageUtils {
	//初始化集合
	 public static List<Bitmap> getBitmapList(Context context,List<String> filePaths){
		 List<Bitmap> list = new ArrayList<Bitmap>();
		 if(filePaths.size()<5){
			 for(int i=0;i<filePaths.size();i++){
				 list.add(ImageUtils.getReflectedImage(getBitmap(filePaths.get(i))));
			 }
		 }else{
		 for(int i=0;i<5;i++){
			// list.add(ImageUtils.getReflectedImage(BitmapFactory.decodeResource(context.getResources(),getImageId()[i])));
             list.add(ImageUtils.getReflectedImage(getBitmap(filePaths.get(i))));
           }
		 }
		 return list;
	 }
	 //处理缓存集合
	 public static void dealwithBitmapList(String filePath,List<Bitmap> list,Context context,boolean isRightOrLeft){
		Log.i("test","testdeal");
		 if(isRightOrLeft){
		 list.get(0).recycle();
		 list.remove(0);
		 list.add(ImageUtils.getReflectedImage(getBitmap(filePath)));
	     Log.i("ok",list.size()+"ge");
		 }else{
	     list.get(4).recycle();
		 list.remove(4);
		 list.add(0,ImageUtils.getReflectedImage(getBitmap(filePath)));
		 Log.i("ok",list.size()+"gezuo");
		 }
	 }
     //删除时候的缓存处理
	 public static void dealDeletewithBitmapList(String filePath,List<Bitmap> list,Context context,int state){
		 Log.i("test","testdeal2");
		 if(state==6){
			 list.get(0).recycle();
			 list.remove(0);
			 
			 list.add(0,ImageUtils.getReflectedImage(getBitmap(filePath)));
		 }
		 if(state==5){
			 if(filePath!=null){
			 list.get(1).recycle();
			 list.remove(1);
			 list.add(0,ImageUtils.getReflectedImage(getBitmap(filePath)));
			 }else{
				 list.get(1).recycle();
				 list.remove(1);
			 }
		 }
		 if(state==4){
			 if(filePath!=null){
			 list.get(2).recycle();
			 list.remove(2);
			 list.add(0,ImageUtils.getReflectedImage(getBitmap(filePath)));
			 }else{
				 list.get(2).recycle();
				 list.remove(2);
			 }
		 }
		 /**
		  * 当在末尾的时候
		  */
		 if(state==2){
			 if(filePath!=null){
			 list.get(4).recycle();
			 list.remove(4);
			 list.add(0,ImageUtils.getReflectedImage(getBitmap(filePath)));
			 }else{
				 list.get(4).recycle();
				 list.remove(4);
			 }
		 }
		 /**
		  * 当在第二个的时候
		  */
		 if(state==3){
			 if(filePath!=null){
			 list.get(3).recycle();
			 list.remove(3);
			 list.add(0,ImageUtils.getReflectedImage(getBitmap(filePath)));
			 }else{
				 list.get(3).recycle();
				 list.remove(3);
			 }
		 }
		 if(state==0){
			 list.get(0).recycle();
			 list.remove(0);
		 }
		 if(state==1){
			 Log.i("width=",list.get(1).getWidth()+"chuli");
			 list.get(1).recycle();
			 list.remove(1);
			 list.add(ImageUtils.getReflectedImage(getBitmap(filePath)));
		 }
		 
		
	 }
     /**
      * 缩放图片
      */
     public static Bitmap setBitmapScale(Bitmap bm,float scale){
    	 
    		    // ���ͼƬ�Ŀ��
    		    int width = bm.getWidth();
    		    int height = bm.getHeight();
    		   
    		    
    		    // ȡ����Ҫ���ŵ�matrix����
    		    Matrix matrix = new Matrix();
    		    matrix.postScale(scale,scale);
    		    // �õ��µ�ͼƬ
    		    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
    		      true);
    	 return newbm;
     }
     //获取图片倒影
     public static Bitmap getReflectedImage(Bitmap originalImage) {   
         final int reflectionGap = 0;   
   
         int width = originalImage.getWidth();   
         int height = originalImage.getHeight();   
   
         Matrix matrix = new Matrix();   
         matrix.preScale(1, -1);   
   
         Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,   
                 height / 2, width, height / 2, matrix, false);   
   
         Bitmap bitmapWithReflection = Bitmap.createBitmap(width,   
                 (height + height / 5), Config.ARGB_8888);   
   
         Canvas canvas = new Canvas(bitmapWithReflection);   
         canvas.drawBitmap(originalImage, 0, 0, null);   
         Paint defaultPaint = new Paint();   
         canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);   
   
         canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);   
   
         Paint paint = new Paint();   
         LinearGradient shader = new LinearGradient(0,   
                 originalImage.getHeight(), 0, bitmapWithReflection.getHeight()   
                         + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);   
         paint.setShader(shader);   
         paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));   
         canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()   
                 + reflectionGap, paint);   
   
         defaultPaint.setColor(Color.LTGRAY);   
         width--;   
   
         try {   
             if (!originalImage.isRecycled())   
                 originalImage.recycle();   
         } catch (Exception e) {   
         	Log.e("error",e.getMessage());
         }   

         try {   
        	 
             if (!reflectionImage.isRecycled()) 
            	 Log.i("tt","FFF");
            	 reflectionImage.recycle();   
         } catch (Exception e) {   
         	Log.e("error",e.getMessage());
         }   
   
         return bitmapWithReflection;   
     }  
     //处理图片的大小（防止溢出）
     public  static Bitmap getBitmapByPath(String path) {
 		File f = new File(path);
 		Bitmap resizeBmp = null;
 		BitmapFactory.Options opts = new BitmapFactory.Options();
 		 if(f.length()<50000){
 			 opts.inSampleSize = 1;
 		 }else if(f.length()<100000){
 			 opts.inSampleSize = 2;
 		 }else if(f.length()<200000){
 			 opts.inSampleSize = 3;
 		 }else if (f.length() < 442500) {
 		 opts.inSampleSize = 4;
 		 } else if (f.length() < 885000) {
 		 opts.inSampleSize = 6;
 		 } else if (f.length() < 1770000) {
 		 opts.inSampleSize = 10;
 		 } else if (f.length() < 3540000) {
 		 opts.inSampleSize = 12;
 		 } else {
 			 System.out.println("8  " + f.length() + " , "+f.length() / (1024f *
 					 1024f) * 1000f);
 		 opts.inSampleSize = 19;
 		 }
 		resizeBmp = BitmapFactory.decodeFile(f.getPath(), opts);
 		
 		return resizeBmp;

 	}
     /**
      * 建一个文件
      */
     public static File getDir(){
    	 File dir  = Environment.getExternalStorageDirectory();
    	 File file = new File(dir,"beyond");
    	 if(!file.exists()){
    		 file.mkdir(); 
    	 }
    	 return file;
     }
     /**
      * 从sdcard获取图片
      */
     public static Bitmap getBitmap(String filePath){
    	 return getBitmapByPath(filePath);
     }
     /**
      * 从sdcard获取文件
      */
     public static List<String> getFilePaths(ArrayList<Integer> photo_id_list){
     	 //String filePath[] = new String[photo_id_list.size()];
     	List<String> filePath_list = new ArrayList<String>();
     	 for(int i=0;i<photo_id_list.size();i++){
     		 filePath_list.add(PhotoDao.query_Filepath_Id(photo_id_list.get(i)));
     	 }
     	 return filePath_list ;
     	 
      }
	public static void deleteImage(String filePath) {
		 File file = new File(filePath);
		 file.delete();
		
	}
	public static void refreshFilePaths(int index,List<String> filePaths) {
		filePaths.remove(index);
	}
	 /**
     * 删除图片
     * @param filePath
     */
    public static void deleteImages(ArrayList<String> deleteImageList){
   	 PhotoDao.deletePhotos2(deleteImageList);
    }
    
    /**
     * 画图的方法
     */
    
    public static Bitmap createBitmap(Bitmap src, Bitmap photo) {
		if (src == null) {
			return null;
		}

		int w = src.getWidth();
		int h = src.getHeight();
		Log.d("abc", w + " = w" + h + " = h ");
		int ww = photo.getWidth();
		int wh = photo.getHeight();
		Log.d("abc", ww + " = ww" + wh + " = wh ");
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(photo, 0, 0, null);
		cv.drawBitmap(src, 0, 0, null);// 
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}
    
    
    
   /* 
    * 如果使用线程加旋转效果，则使用imgRotate2 传值计算 angle为1、-1；
	 * 公式：angle * 10
    */
	public static Bitmap imgRotate(Bitmap srcBitmap, float angle) {
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();
		
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		
		//after rotate bitmap
		Bitmap destBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix,
				false);
		return destBitmap;
	}
	
	/**
	 * 普通修复
	 * @param srcBitmap - 原图片
	 * @return
	 */
	public static Bitmap imageXiuFu(Bitmap srcBitmap) {
		int[] pixels = new int[srcBitmap.getWidth() * srcBitmap.getHeight()];
		srcBitmap.getPixels(pixels, 0, srcBitmap.getWidth(), 0, 0, srcBitmap.getWidth(),
				srcBitmap.getHeight());
		int length = pixels.length;
		int avg_r = 0, avg_g = 0, avg_b = 0;
		for (int i = 0; i < length; i++) {
			avg_r += Color.red(pixels[i]);
			avg_g += Color.green(pixels[i]);
			avg_b += Color.blue(pixels[i]);
		}
		avg_r /= length;
		avg_g /= length;
		avg_b /= length;
		
		int[] newPixels = new int[srcBitmap.getWidth() * srcBitmap.getHeight()];
		for (int i = 0; i < length; i++) {
			int r = Math.min(Color.red(pixels[i]) * 128 / avg_r, 255);
			int g = Math.min(Color.green(pixels[i]) * 128 / avg_g, 255);
			int b = Math.min(Color.blue(pixels[i]) * 128 / avg_b, 255);
			newPixels[i] = Color.rgb(r, g, b);
		}
		
		Bitmap destImg = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(),
				Config.ARGB_8888);
		destImg.setPixels(newPixels, 0, srcBitmap.getWidth(), 0, 0, srcBitmap.getWidth(),
				srcBitmap.getHeight());
		return destImg;
	}
	
	/**
	 * save
	 * @param newImgPath 保存图片路径
	 * @param editBitmap 要保存的图片
	 * @return 
	 */
	public static boolean saveLocalImg(String newImgPath,Bitmap editBitmap){
		FileOutputStream fos = null;
		try {
			File file = new File(newImgPath) ;
			fos = new FileOutputStream(file);
             Bitmap b = editBitmap; 
             b.compress(CompressFormat.JPEG, 100, fos) ; 
             fos.flush();
             return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                	return false;
                }
            }
		}
	}

}

