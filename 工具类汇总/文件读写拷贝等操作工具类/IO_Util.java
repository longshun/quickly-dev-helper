

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;

import android.os.Environment;
import android.widget.Toast;

public class IO_Util {
	
	

	/**
	 * 读取文件内容
	 * @param path  目标文件绝对路径
	 * @param bufSize  缓冲区大小
	 * @param charSet   字符编码    ："UTF-8"、"GBK" 等
	 * @return  String  文件内容
	 */
	public static String readTextFile(String path , int buffSize , String charSet ) {
		
		File file = new File( path ) ;
		FileInputStream fis = null ;
        InputStreamReader isr = null ;
        BufferedReader bis = null ;
        
        CharBuffer target = CharBuffer.allocate(buffSize) ;
        StringBuilder content = new StringBuilder() ;
        int num = 0 ;
        try {
        	fis = new FileInputStream(file) ; 
        	isr = new InputStreamReader( fis , charSet ) ;
        	bis = new BufferedReader( isr ) ;
        	while( (num = bis.read(target)) != -1 ){
				target.rewind() ;
				content.append(target.subSequence(0, num)) ;	
			}
        	return content.toString()  ;
		} catch (Exception e) {
			return null ;
		}finally{
			if( bis != null ){
				try {
					bis.close() ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	
	/**
	 * 将内容写入目标文件，将覆盖源文件内容
	 * @param targetPath   目标文件绝对路径
	 * @param content       待写入内容
	 * @param buffSize      缓冲区大小
	 * @param charSet       字符编码  ："UTF-8"、"GBK" 等
	 * @return boolean      写入成功: true ,  失败:false
	 */
	public static boolean writeTextFile( String targetPath , String content , int buffSize , String charSet ){
		
		File writeFile = new File( targetPath ) ;
		FileOutputStream fos = null ;
		OutputStreamWriter osw = null ;
		BufferedWriter bos = null ;
		
		try {
			fos = new FileOutputStream(writeFile) ;
			osw = new OutputStreamWriter(fos, charSet ) ;
			bos = new BufferedWriter( osw , buffSize ) ;
			bos.write(content) ;
			bos.flush() ;
			return true ;
		} catch (IOException e) {
			e.printStackTrace();
			return false ;
		}finally{
			if( bos != null ) {
				try {
					bos.close() ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	

	/**
	 * 创建日志文件
	 * @param  timeStr   该日志所属日期字符串
	 * @return  String   成功:文件绝对路径    失败:null      
	 */
	public static String createJournalFile (  String  dateStr ) {
		
		String jouStr = "journal"  ;
		String yearStr = dateStr.substring( 0 , 4 ) ;
		
		File sdCard = Environment.getExternalStorageDirectory() ;
		String dateJouFileStr = null ;
		if( !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
			Toast.makeText(Static_Variable.context, " sdcard is not exist ", 3 ) ;
			return null ;
		}
			
		File jouFile = new File( sdCard , jouStr ) ;
		if ( !jouFile.exists() ){
			jouFile.mkdir() ;
		}
		
		File yearFile = new File( jouFile , yearStr ) ;
		if ( !yearFile.exists() ){
			yearFile.mkdir() ;
		}
		
		File dateJouFile = new File( yearFile , dateStr+".txt" ) ;
		dateJouFileStr = dateJouFile.getAbsolutePath() ;
		try {
			dateJouFile.createNewFile() ;
			return dateJouFileStr ;
		} catch (IOException e) { 
			Toast.makeText(Static_Variable.context, " create jou file failed  ", 3 ).show() ;
			return null ;
		}
	}
	
	
	/**
	 * 创建指定目录，注意不是创建文件
	 * @param  dirStr   该日志所属日期字符串  :  /mnt/sdcard/journal/2005/hello/
	 * @return  file    返回文件目录      
	 */
	public static File  create_Dir_Str (  String dirStr , String separator) {
		
		
		String[] arraStr = dirStr.split(separator) ;
		int i = 0 ;
		if( arraStr[0].trim().equals("") ){
			i = 1 ;
		}
		File file = new File(arraStr[1]) ;
		for(  ; i< (arraStr.length-1) ; i++ ){
			String dir = arraStr[i+1] ;
			file = new File( file ,  dir) ;
			if( !file.exists() ){
				file.mkdir() ;
			}
		}
		return file ;
	}
	
	

	public static void copyFile( File srcFile , File distFile ){
		FileInputStream fis = null ;
		FileOutputStream fos = null ;
		try {
			fis = new FileInputStream(srcFile) ;
			fos = new FileOutputStream(distFile) ;
			byte[] buffer = new byte[1024*10] ;
			int len = -1 ;
			while( (len=fis.read(buffer)) != -1 ){
				fos.write(buffer, 0, len) ;
				fos.flush() ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if( fis != null ){
				try {
					fis.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if( fos != null ){
						try {
							fos.close() ;
						} catch (IOException e) {
						 	// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}
	
	
	
}
