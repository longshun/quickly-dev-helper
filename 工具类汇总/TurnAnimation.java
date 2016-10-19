

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class TurnAnimation extends Animation {
	
    Camera camera = new Camera();
	
	private int centerX, centerY;
	private int code;
	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		setDuration(2500);
		setFillAfter(true);
		setInterpolator(new LinearInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
	    Matrix matrix = t.getMatrix();
		camera.save();
		camera.translate(0f, 0f, 0f);
		if(code == 2) {
			camera.rotateY(180 * interpolatedTime);
			camera.getMatrix(matrix);
			matrix.preTranslate(-centerX , -centerY );
			matrix.postTranslate(centerX , centerY );
		}else if(code == 1) {
			camera.rotateY(-180 * interpolatedTime);
			camera.getMatrix(matrix);
			matrix.preTranslate(-centerX , -centerY );
			matrix.postTranslate(centerX , centerY );
		}else if(code == 4) {
			camera.rotateX(180 * interpolatedTime);
			camera.getMatrix(matrix);
			matrix.preTranslate(-centerX , -centerY );
			matrix.postTranslate(centerX , centerY );
 		}else if(code == 3) {
 			camera.rotateX(-180 * interpolatedTime);
 			camera.getMatrix(matrix);
 			matrix.preTranslate(-centerX , -centerY );
 			matrix.postTranslate(centerX , centerY );
 		}
		camera.restore();
	}
	
	public void setX(int x) {
		this.centerX = x;
	}

	public void setY(int y) {
		this.centerY = y;
	}
	
	public void setCode(int code) {
		this.code = code;
	}

}
