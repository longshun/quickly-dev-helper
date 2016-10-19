
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.Toast;

import com.beyondsoft.giinii.dbhelper.dao.JournalDao;
import com.beyondsoft.giinii.dbhelper.dao.PhotoDao;
import com.beyondsoft.giinii.dbhelper.dao.Photo_ContactDao;
import com.beyondsoft.giinii.dbhelper.dao.Photo_FaceImageDao;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
	
public class ImportToDatebase {
	
	private static ExifInterface exif ;
	
	private static  int width  ;
	private static  int hight   ;
	private static  long photo_size   ;
	private static  String thumbnail   ;
	private static  String createDate   ;
	private static  String timeImported ;
	private static  Double coordX   ;
	private static  Double coordY   ;
	private static  String cityName  ;
	
	private static  String [] cityNames = new String[]{"beijing","wuhan","Athens","London"} ;
	private static  int cityIndex = 1 ;
	private static  int contactId = 1 ;
	private static  int faceImageId = 1 ;
	private static  int photo_Num = 1 ;
	private static  int unKownNum = 12 ;
	
	/**
	 * 北京：116.407413E   39.904214N    116  24  26        39  54    15
	 * 武汉     114.305357      30.593087    114  18  20       30  35    35


	 * 
雅典： 23.716647   37.97918               23  42  59         37  58   45

伦敦：-0.1262362       51.5001524            0  7   34            51    30  5



116/1,10/1,58443/1250

	 */
	
	private static  String [] coordXs = new String[]{ "116/1,24/1,88/3" , "114/1,18/1,45/2" , "23/1,43/1" , "0/1,7/1,58443/1250" } ; 
	private static  String [] coordYs = new String[]{ "39/1,54/1,40/3" , " 30/1,35/1,71/2" , "37/1,58/1,92/2" , "51/1,30/1,6443/1250"  } ;
	private static  String [] coordXsing = new String[]{ "E" , "E" , "E" , "W" } ;
	private static  String [] coordYsing = new String[] { "N" , "N" , "N" , "N" } ;
	private static  int coordNum = 0 ;
	
	
	
	private static String author ;
	private static String right ;
	private static String caption ;
	
	private static String TAG = "project" ;
	
	public static  void serchPhotoes( File pictureDir) {
		
		SQLiteDatabase writeDB = Static_Variable.writableDatabase ;
		
		writeDB.execSQL(" insert into contact(name ) values(? ) ", new String[]{"zhangsan"}) ;
		writeDB.execSQL(" insert into contact(name ) values(? ) ", new String[]{"wangwu"}) ;
		writeDB.execSQL(" insert into contact(name ) values(? ) ", new String[]{"lisi"}) ;
		writeDB.execSQL(" insert into faceImage( imagePath , contactId ) values( ? , ? ) ", new String[]{ "/mnt/sdcard/face/face1.jpg" , "1" }) ;
		writeDB.execSQL(" insert into faceImage( imagePath , contactId ) values( ? , ? ) ", new String[]{ "/mnt/sdcard/face/face2.jpg" , "2" }) ;
		
		timeImported = Date_Util.importStr2timeStr( Static_Variable.distDir_Name ) ;
		
		writeDB.beginTransaction() ;
		try {
			photoInfoToDB( pictureDir , writeDB ) ;
			writeDB.setTransactionSuccessful() ;
		}finally{
			writeDB.endTransaction() ;
		}
		
		jourToDB( timeImported ) ;
		cityToDB() ;
	}
	
	public static void serchMedias(  ){
		File mediaDir = new File("/mnt/sdcard/media") ;
		serchMedia(  new File(mediaDir, "music"), Static_Variable.media_Music  ) ;
		serchMedia(  new File(mediaDir, "video"), Static_Variable.media_Video  ) ;
	}
	
	private static void serchMedia(File distDir , int category) {
		File[] files = distDir.listFiles() ;
		SQLiteDatabase writeDB = Static_Variable.writableDatabase ;
		String sql = " insert into media(filePath,category) values(?,?) " ;
		writeDB.beginTransaction() ;
		try {
			for( File file : files ){
				writeDB.execSQL(sql, new String[]{file.getAbsolutePath() , String.valueOf(category)} ) ;
			}
			writeDB.setTransactionSuccessful() ;
		} catch (Exception e) {
			Toast.makeText(Static_Variable.context, " import mediaInfo failed ", 3).show() ;
		}finally{
			writeDB.endTransaction() ;
		}
	}
	
	private static void cityToDB() {
		SQLiteDatabase writeDB = Static_Variable.writableDatabase ;
		
		String sql = " insert into city( name , coordX , coordY  ) values( ? , ? , ? ) " ;
		for( int i = 0 ; i<4 ; i++ ){
			writeDB.execSQL(sql, new String[]{ cityNames[i] , String.valueOf( Exif_Util.GPSStr2Double(coordXs[i], coordXsing[i]) ) , String.valueOf( Exif_Util.GPSStr2Double(coordYs[i], coordYsing[i]) ) } ) ;
		}
		
//		String sql = " select cityName from photo group by cityName order by cityName " ;
//		Cursor cursor = writeDB.rawQuery(sql, null) ;
//		writeDB.beginTransaction() ;
//		try {
//			while (cursor.moveToNext()) {
//				String name = cursor.getString(0) ;
//				if(name == null){
//					continue ;
//				}
//				 double[] GPSPoint  =  getCityGPS(name) ; 
//				 double GPSLong = GPSPoint[0] ;
//				 double GPSLat = GPSPoint[1] ;
////				 String
//				 CityDao.insert(name, GPSLong, GPSLat) ;
//			}
//			writeDB.setTransactionSuccessful() ;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}finally{
//			writeDB.endTransaction() ;
//		}
//		cursor.close() ;
	}



	private static void jourToDB( String timeImportedStr ) {
		
		SQLiteDatabase writeDB = Static_Variable.writableDatabase ;
		
		String q_sql = "  SELECT SUBSTR( p.createDate , 1 , 10 ) AS jourDate FROM photo AS p " +
					"where p.dateImported = '"+timeImportedStr+"' GROUP BY  SUBSTR( p.createDate , 1 , 10 ) " ;
		Cursor cursor = writeDB.rawQuery( q_sql , null ) ;
		
		writeDB.beginTransaction() ;
		try {
			while( cursor.moveToNext() ){
				String timeStr = cursor.getString( 0 ) ;
				JournalDao.insert( writeDB , timeStr ) ;
			}
			writeDB.setTransactionSuccessful() ;
		} catch (Exception e) {
			Toast.makeText(Static_Variable.context, " sorry ,import journal to DB failed ", 3 ).show() ;
		}finally{
			cursor.close() ;
			writeDB.endTransaction() ;
		}
		
	}



	public static void photoInfoToDB( File pictureDir , SQLiteDatabase writableDatabase){
		
		File[] files = pictureDir.listFiles() ;
		if ( files == null  ) {
			return ;
		}
		for( File file : files ){
			if( !file.isDirectory() && file.getName().toLowerCase().endsWith(".jpg")){
					process( file , writableDatabase ) ;
			}else{
				photoInfoToDB( file , writableDatabase ) ; 
			}
		}
	}
		
	
	
	private static void process(File file , SQLiteDatabase writableDatabase ) {
		
		
        	
		try {
			
			cityName = cityNames[cityIndex] ;
			
			insert_PhotoInfo( file , writableDatabase );
			
    		
    		contactId ++ ;
    		faceImageId ++ ;
    		cityName = cityNames[cityIndex++] ;
    		photo_Num ++ ;
			unKownNum ++ ;
			coordNum ++ ;
    		if(contactId == 4){
    			contactId = 1 ;
    		}
    		if( faceImageId == 3 ){
    			faceImageId = 1 ;
    		}
    		
    		if(cityIndex == 4){
    			cityIndex = 0 ;
    		}
    		if(unKownNum == 15){
    			unKownNum = 12 ;
    		}
    		if( coordNum == 4 ){
    			coordNum = 0 ;
    		}
    		
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	
	public static void insert_PhotoInfo( File file , SQLiteDatabase writableDatabase )
			throws IOException {
		
		 String filePath = file.toString() ;
		
		exif  = new ExifInterface(file.toString()) ;
		
		width = Integer.valueOf( exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) ) ;
		hight = Integer.valueOf( exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) ) ;
		photo_size = file.length() ;
		thumbnail = filePath ;
		createDate = Exif_Util.getDateStr_Exif(exif) ;
		if(createDate == null){
			long time  = file.lastModified() ;
			Calendar calendar = Calendar.getInstance() ;
			calendar.setTimeInMillis(time) ;
			createDate = Date_Util.calendar2TimeStr(calendar) ;
		}
		
		coordX = Exif_Util.GPSStr2Double(coordXs[coordNum], coordXsing[coordNum])   ;
		coordY = Exif_Util.GPSStr2Double(coordYs[coordNum], coordYsing[coordNum])  ;
		cityName = cityNames[coordNum] ;
		
		

		Metadata metadata = null ;
		try {
			metadata = JpegMetadataReader.readMetadata(file); 
		} catch (Exception e1) {
			Log.i(TAG, "exif读写异常") ;
		}
		Directory exif_Metadata = metadata.getDirectory(ExifDirectory.class ); //这里要稍微注意下 
		
		if( exif_Metadata != null ){
			author = exif_Metadata.getString(ExifDirectory.TAG_ARTIST) ;
			if( author == null ){
				author = "" ;
			}
			
			right = exif_Metadata.getString(ExifDirectory.TAG_COPYRIGHT) ;
			if( right == null ){
				right = "" ;
			}
			
			caption  = exif_Metadata.getString(ExifDirectory.TAG_IMAGE_DESCRIPTION) ;
			if( caption == null ){
				caption = "" ;
			}
		}
		
		
		if( photo_Num < 45 ){
			PhotoDao.Insert( writableDatabase , width, hight, photo_size, thumbnail, createDate, timeImported , filePath, coordX, coordY, cityName  , author , right , caption , Static_Variable.belong_unACK  ) ;
			
			Photo_ContactDao.insert(writableDatabase , contactId , contactId ) ;
			Photo_FaceImageDao.insert(writableDatabase, faceImageId ) ;
		}else if( photo_Num < 58 ){
			PhotoDao.Insert( writableDatabase , width, hight, photo_size, thumbnail, createDate, timeImported , filePath, coordX, coordY, cityName  , author , right , caption , Static_Variable.belong_ACK  ) ;
			
			Photo_ContactDao.insert(writableDatabase , contactId , contactId ) ;
			Photo_FaceImageDao.insert(writableDatabase, faceImageId ) ;
		}else{
			PhotoDao.Insert( writableDatabase , width, hight, photo_size, thumbnail, createDate, timeImported , filePath, coordX, coordY, cityName  , author , right , caption , Static_Variable.belong_unKnown  ) ;
			
			
			Photo_ContactDao.insert(writableDatabase , 0 , unKownNum ) ;
			Photo_FaceImageDao.insert(writableDatabase, faceImageId ) ;
		}
		
	}
	
	
	

    
    /**
     * 
     * @param lng经度
     * @param lat纬度
     * @return cityName城市名称 
     */
    private static String  getCityName(double lng ,double lat ,Locale locale) {
    	String name=null;
    	Geocoder gc = new Geocoder(Static_Variable.context, locale);
    	List<Address> addresses = null;
    	try {
    		addresses = gc.getFromLocation(lat, lng, 10);
    		if(addresses!=null&&addresses.size()>0){
    			Address a = addresses.get(0);
    			name=a.getLocality();
    			return name;
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return name;
    }


    /***
     * 同过名字查找经纬度
     * @param name
     * @return[0]=lon经度 [1]=lat纬度
     */
    private static double[] getCityGPS(String name){
    	Geocoder gc=new Geocoder(Static_Variable.context);
    	List<Address> l=null;
    	double[] geoPoint=null;
    	try {
			l=gc.getFromLocationName(name, 3);
			if(l!=null&&l.size()>0){
	    		Address a=l.get(0);
	    		geoPoint=new double[2];
	    		geoPoint[0]=a.getLongitude();
	    		geoPoint[1]=a.getLatitude();
	    		return geoPoint;
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return geoPoint;
    }

}
