
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import android.database.sqlite.SQLiteDatabase;
import android.media.ExifInterface;
import android.util.Log;

import com.beyondsoft.giinii.dbhelper.DBHelper;
import com.beyondsoft.giinii.dbhelper.dao.PhotoDao;
	
public class SerchPhotoesUitl {
	
	private static ExifInterface exif ;
	
	private static  int width  ;
	private static  int hight   ;
	private static  long photo_size   ;
	private static  String thumbnail   ;
	private static  String createDate   ;
	private static  String filePath  ;
	private static  double coordX   ;
	private static  double coordY   ;
	private static  String cityName  ;
	
	public static PhotoDao photoDao  = null ;
	
	
	private static String TAG = "project" ;
	
	public static  void serchPhotoes( File dir) {
		
		String [] cityNames = new String[]{"beijing","shenzhen","shanghai","gongan","shangdi"} ;
		int cityIndex = 1 ;
		
		
		
		SQLiteDatabase database = Static_Variable.dbHelper.getWritableDatabase() ;
		database.execSQL(" insert into contact(name ) values(? ) ", new String[]{"zhangsan"}) ;
		database.execSQL(" insert into contact(name ) values(? ) ", new String[]{"wangwu"}) ;
		database.execSQL(" insert into contact(name ) values(? ) ", new String[]{"lisi"}) ;
		
		
		
		File[] files = dir.listFiles() ;
		int contactId = 1 ;
        for(File file : files){
        	String path = file.toString() ;
        	if( path.endsWith("JPG") || path.endsWith("jpg")){
        		
        		Log.i(TAG, path) ;
        		try {
        			exif  = new ExifInterface(file.toString()) ;
        			
        			width = Integer.valueOf( exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) ) ;
        			hight = Integer.valueOf( exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) ) ;
        			photo_size = file.length() ;
        			thumbnail = path ;
        			createDate = Exif_Util.getDateTextStr_ExifInterface(exif) ; 
        			createDate = (String) null2Str(createDate) ;
        			filePath = path ;
        			coordX = Exif_Util.getGps_Double(ExifInterface.TAG_GPS_LONGITUDE, exif) ;
        			coordY = Exif_Util.getGps_Double(ExifInterface.TAG_GPS_LATITUDE, exif) ;
        			
        			cityName = cityNames[cityIndex] ;
        			cityIndex ++ ;
        			if(cityIndex == 5){
        				cityIndex = 0 ;
        			}
        			
        			
//            		photoDao.Insert(width, hight, photo_size, thumbnail, createDate, filePath, coordX, coordY, cityName , contactId) ;
            		contactId ++ ;
            		if(contactId == 4){
            			contactId = 1 ;
            		}
            		
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
	}
	private static Object null2Str(Object object) { 
		// TODO Auto-generated method stub
		if(object == null){
			return "" ;
		}
		return object ;
	}
}
