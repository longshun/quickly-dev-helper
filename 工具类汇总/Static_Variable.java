

import java.io.File;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.beyondsoft.giinii.settings.dbhelper.DBHelper;

public class Static_Variable {

	public static Context context;
	public static DBHelper dbHelper;
	public static ContentResolver contentResolver;
	public static SQLiteDatabase readableDatabase;
	public static SQLiteDatabase writableDatabase;

	public static File sdCard = Environment.getExternalStorageDirectory();
	// public static final String dbName = "giini.db" ;
	public static final String dbName = sdCard.getAbsolutePath()
			+ "/GiiNiDatabases/GiiNi_UI.db";// 合并一个数据库到SDcard上的时候，使用。

	// Personalization
	public static int personal = 2;
}
