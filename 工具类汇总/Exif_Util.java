
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.media.ExifInterface;

import com.drew.metadata.Directory;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;


public class Exif_Util {

	
	public static  void setDateTime (String dateStr , ExifInterface exif){
		
		exif.setAttribute(ExifInterface.TAG_DATETIME, dateStr ) ;

	}
	
	// 若exif中没有日期信息且格式不符合要求，则返回NULL
	public static String getDateStr_Exif (Object exif){
		String newDateTime  = null ;
		if( exif instanceof ExifInterface){
			newDateTime = getDateTextStr_ExifInterface( (ExifInterface)exif) ;
		}
		if( exif instanceof Directory ){
			newDateTime = getDateTextStr_Metadata( (Directory) exif) ;
		}
		
		
		
		
		if( newDateTime == null ){
			return null ;
		}else{
			String regex = "^[1-9]\\d{3}:(0[1-9]|1[012]):(0[1-9]|[12]\\d|3[01])\\s([01]\\d|2[0123]):([0-4]\\d|5[0-9]):([0-4]\\d|5[0-9])$" ;
			Pattern p = Pattern.compile(regex) ;
			Matcher m = p.matcher(newDateTime) ;
			boolean valid = m.find() ;
			if ( !valid ){
				return null ;
			}
		}
		
		String[] dateTimeArra = newDateTime.split(" ");
		String date = dateTimeArra[0] ;
		date = date.replace(':', '-') ;
		String result = date + " " + dateTimeArra[1] ;
		
		return result ;
		
	}
	
	public static String getDateStr_displ ( Object exif ){
		String time = getDateStr_Exif(exif) ;
		
		if( exif instanceof ExifInterface){
			time = getDateTextStr_ExifInterface( (ExifInterface)exif) ;
		}
		if( exif instanceof Directory ){
			time = getDateTextStr_Metadata( (Directory) exif) ;
		}
		
		if( time == null) return null  ;
		
		Calendar calendar = Calendar.getInstance() ;
		
//		calendar.set(String.valueOf(date[0]), String.valueOf(date[1]), date[2], timearr[0], timearr[1], timearr[2]) ;
//		
//		
//		Calendar calendar = Date_Util.str2calendar( time ) ;
		StringBuilder sb_time = new StringBuilder() ;
		sb_time.append( calendar.get(Calendar.YEAR) ).append('.').append( calendar.get(Calendar.MONTH)).append('.')
		 	   .append( calendar.get(Calendar.DAY_OF_MONTH)).append("   ")
		 	   .append(calendar.get(Calendar.HOUR_OF_DAY)).append(':').append(calendar.get(Calendar.MINUTE)) ;
		
		
		return sb_time.toString() ;
	}
	
	
	private static String getDateTextStr_Metadata(Directory exif) {
		String dateTime = null ;
		try {
			dateTime = exif.getDescription(ExifDirectory.TAG_DATETIME_DIGITIZED) ;
		} catch (MetadataException e) {
			return null  ;
		}
		
		return dateTime ;
	}

	public static String getDateTextStr_ExifInterface (ExifInterface exif){
		String dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME) ;
		if(dateTime == null ){
			return null ;
		}
		
	
		
		return dateTime ;
		
	}
	
	
	
	/***
	 * 
	 * @param tag : ExifInterface.TAG_GPS_LONGITUDE 经度          ExifInterface.GPSLatitude 纬度   
	 * @param value : 25:89:41   25度89分41秒
	 * @param exif
	 */
	public static void setGps_Str(String  tag , String value ,ExifInterface exif ){
		
		String[] arra = value.split(":") ;
		StringBuilder sb = new StringBuilder() ;
		sb.append(arra[0]).append("/1,").append(arra[1]).append("/1,").append(arra[2]).append("/1");
		exif.setAttribute(tag , sb.toString()) ;
	}
	
	
	/**
	 * 
	 * @param tag  tag : ExifInterface.TAG_GPS_LONGITUDE 经度          ExifInterface.GPSLatitude 纬度   
	 * @param degree  度
	 * @param minute  分
	 * @param second  秒
	 * @param exif   
	 */
	public static void setGps_Int(String tag , int degree , int minute , int second , ExifInterface exif ){
		
		StringBuilder sb = new StringBuilder() ;
		sb.append(degree).append("/1,").append(minute).append("/1,").append(second).append("/1");
		exif.setAttribute( tag , sb.toString()) ;
	}
	
	/**
	 * 
	 * @param tag : ExifInterface.TAG_GPS_LONGITUDE 经度          ExifInterface.GPSLatitude 纬度   
	 * @param exif
	 * @return  Double  
	 */
	public static Double getGps_Double(String tag , ExifInterface exif){
		
		String value = exif.getAttribute(tag) ;
		if( value == null || value.trim().equals("") ){
			return  null ;
		}
		
		String signStr = "" ;
		if( tag.equals(ExifInterface.TAG_GPS_LONGITUDE) ){
			signStr  = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) ;
		}else {
			signStr = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) ;
		}
		
		double val = GPSStr2Double(value, signStr);
		
		return val ;
	}

	
	/**
	 * 将GPS字符串格式转换为浮点数
	 * @param value  
	 * @param signStr     其参考符号
	 * @return   Double  浮点数
	 */
	public static double GPSStr2Double(String value, String signStr) {
		String[] values = value.split(",") ;
		double val = 0;
		int[] intArra = new int[]{1,60,3600} ;
		if( signStr != null ){
			char signStr_0 = signStr.charAt(0) ;
			if( signStr_0 == 'S' || signStr_0 == 'W' ||  signStr_0 == 's' || signStr_0 == 'w'  ){
				for(int i = 0 ; i < values.length ; i ++ ){
					String[] valArra = values[i].split("/") ;
					val = val - ( Double.parseDouble(valArra[0]) / Double.parseDouble(valArra[1]) ) /intArra[i] ;
				}
			}else{
				for(int i = 0 ; i < values.length ; i ++ ){
					String[] valArra = values[i].split("/") ;
					val = val + ( Double.parseDouble(valArra[0]) / Double.parseDouble(valArra[1]) ) /intArra[i] ;
				}
			}
		}else{
			for(int i = 0 ; i < values.length ; i ++ ){
				String[] valArra = values[i].split("/") ;
				val = val + ( Double.parseDouble(valArra[0]) / Double.parseDouble(valArra[1]) ) /intArra[i] ;
			}
		}
		return val;
	}
	
	
}
