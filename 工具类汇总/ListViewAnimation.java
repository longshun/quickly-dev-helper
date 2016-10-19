
import com.beyondsoft.giinii.domain.ItemDataUtil;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

public class ListViewAnimation {
	 private Animation [] animations = null; 
	 private int toX[];
	 private int toY[];
	 //初始化动画
     public void init(int[]toY){
    	 animations = new Animation[toY.length];
    	 this.toY = toY;
     }
     public void small(){
    	 int animationsId = 0;
    	 for(int i=0;i<toY.length;i++){
    			 animations[animationsId]=new TranslateAnimation(0,0,0,toY[i]);
    			 animations[animationsId].setDuration(1000);
    			 animations[animationsId].setFillAfter(true);
    			 animationsId++;
    	 }
     }
     public void big(){
    	 int animationsId = 0;
    	 for(int i=0;i<toY.length;i++){
    			 animations[animationsId]=new TranslateAnimation(0,0,toY[i],0);
    			 //animations[animationsId]= new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
    			 animations[animationsId].setDuration(500);
    			 animations[animationsId].setFillAfter(true);
    			 animationsId++;
    	 }
    	 animations[0].setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				ItemDataUtil.isStartWhenListViewAnimation=false;
			}
		});
     }
     //根据gridview的不用控件来开启不同动画效果
     public  void startGridView(int position,View view){
    	/* for(int i=0;i<view.getCount();i++){
    		 View view1 =(View) view.getItemAtPosition(i);
    	 }*/
    	 view.startAnimation(animations[position]);
     }
}
