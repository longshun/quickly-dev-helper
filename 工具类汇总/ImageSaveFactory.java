

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beyondsoft.giinii.editimg.activity.PhotoEditActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageSaveFactory {

	public static ImageSaveFactory imgSaveFactory = null;
	private int maxLength = 5; // 图片容器大小
	private int index = 1; // 图片存储序号
	public static List<String> imgPaths = new ArrayList<String>();
	public static final String UNDO = "undo";
	public static final String REDO = "redo";
	public static int undoIndex = 0;// 记录返回 上 一步操作的计数
	public static int redoIndex = 0;// 记录返回 下 一步操作的计数

	private ImageSaveFactory() {
	}

	public static ImageSaveFactory getImgSaveFactory() {
		if (imgSaveFactory == null) {
			imgSaveFactory = new ImageSaveFactory();
		}
		return imgSaveFactory;
	}

	/**
	 * 外部调用的图片保存方法 FIXME
	 * 
	 * @param bitmap
	 * @param context
	 */
	public void saveImg(Bitmap bitmap, Context context) {
		File fileDir = context.getFilesDir();
		String path = fileDir.getParent() + java.io.File.separator
				+ fileDir.getName() + "/";
		if (imgPaths.size() < maxLength) {
			String name = "img" + index;
			setIndex2();// 在保存之前操作
			imgPaths.add(name);
			save(path + name + ".png", bitmap);// �������
			// index = imgPaths.size();
		} else {
			String name = "img" + index;

			if(redoIndex > 0){//先删
				setIndex2();//这里需要 针对 超过指定数量时的处理20110123
			}else{
				imgPaths.remove(0);//redoIndex > 0时，则不需要删除第一个~
				setIndex3();
			}
			
			imgPaths.add(name);//再加
			save(path + name + ".png", bitmap);
			// index = 10;
		}
	}

	/**
	 * �ڲ����õı��淽�� FIXME
	 * 
	 * @param path
	 * @param bitmap
	 */
	private void save(String path, Bitmap bitmap) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(path));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			index++; // ���м�1
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 外部获取图片的方法  FIXME
	 * 
	 * @param context
	 * @return
	 */
	public Bitmap getImg(Context context, String opraType) {
		int unredoMapSize = imgPaths.size();
		// System.out.println("imgPaths.size()  "+unredoMapSize);
		if (unredoMapSize < 1) {
			return null;
		}
		File fileDir = context.getFilesDir();
		String path = fileDir.getParent() + java.io.File.separator
				+ fileDir.getName() + "/";
		FileInputStream fis = null;
		try {
			String name = "";
			int urRes = getClickButton(opraType);
			if (urRes == 1) {
				name = imgPaths.get(undoIndex - 1);
			} else if (urRes == 2) {
				name = imgPaths.get(imgPaths.size() - redoIndex - 1);
			} else {
				System.out.println("   no_undo & no_redo");
			}
			// imgPaths.remove(imgPaths.size() - 1);
			fis = new FileInputStream(new File(path + name + ".png"));
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	private void setIndex2() {
//		int differIndex = imgPaths.size() - redoIndex - 1;// list�����뵱ǰ������֮��������
		int differIndex = redoIndex;// list个数与当前返回数之间相差个数
		if (redoIndex > 0) {
			int imgSize = imgPaths.size() ;
			for (int i = 0; i < differIndex; i++) {
				//imgSize - redoIndex + i;���ﲻ��Ҫ�ټ�i,��undo 2���������error
				if (imgPaths.get(imgSize - redoIndex) != null) {
					imgPaths.remove(imgSize - redoIndex);
					// ���¸�Ϊindex = imgPaths.size();
					// if((index -= 1) >=
					// 1){//������Ҫ��index��undoIndex,redoIndex����ֵ�Ĵ���
					// index -= 1;
					// }else{
					// index = 1;
					// }
				}
			}
			// ɾ��ǰ����ĺ󼸲���Ȼ���ٸı�undoIndex��redoIndex��ֵ
//			redoIndex -= 1;
//			undoIndex += 1;
			int img2Size = imgPaths.size();//和上一个imgSize值不相等
			undoIndex = img2Size + 1;//加1是之后的save保存，在这里加了
			redoIndex = 0;
		} else {
			undoIndex += 1;
			redoIndex = 0;// ��ʶ
		}
	}

	private void setIndex3() {
		undoIndex = maxLength;
		redoIndex = 0;
	}

	/**
	 * 返回操作标记
	 * 
	 * @param opraType
	 * 
	 * @return 0 表示无操作 1 标识返回上一步 undo 2 返回下一步 redo
	 */
	private int getClickButton(String opraType) {
		try {
			if (opraType.equals(UNDO)) {
				undoIndex -= 1;
				redoIndex += 1;
				if (undoIndex <= 1) {
					PhotoEditActivity.b_redoBtnClick = true;
					PhotoEditActivity.b_undoBtnClick = false;
				}
				return 1;
			} else if (opraType.equals(REDO)) {
				redoIndex -= 1;
				undoIndex += 1;
				if (redoIndex <= 0) {
					PhotoEditActivity.b_redoBtnClick = false;
					PhotoEditActivity.b_undoBtnClick = true;
				}
				return 2;
			} else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * ��ȡͼƬ���� FIXME
	 * 
	 * @return
	 */
	public static int getImgCount() {
		return imgPaths.size();
	}

}


