package com.fletamuto.common.control;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

/*
 * ȭ�� �ϴܿ��� �ö���� �����̵� Activity
 * 
 * �� class �� ��� ���� class ���� layout ���� �� �� 
 * setSlidingView �Լ��� sliding �� View �� �����ϰ�  appearAnimation() �Լ��� ȣ�� �ؾ� sliding ���� �Ѵ�.
 * �ּ��� �� �� �۾��� ��� �޴� class �� ��� ������
 * ����� ������ MonthlyCalendar.java, monthly_calendar.xml ���� ����
 * 
 * Animation �Լ��� �ٲٸ� ��ܿ��� �������� �ϰų� ��,�쿡�� ��Ÿ���� �� �� �� ���� (���� �̱���)
 */
public class BaseSlidingActivity extends Activity {
	
	//�����̵��� �� View
	private View slidingView = null;
	
	//�����̵� �� �ð�
	private int slidingDuration = 500;
	
	//Background ��� £�� ����, 1.0f �� ���� £�� �� 
	private float dimmingAmount = 0.75f;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //ȭ���� dimming �ϴ� �κ�
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = dimmingAmount;
        getWindow().setAttributes(lpWindow);
        
        //Ÿ��Ʋ ���߱�
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    
    public View getSlidingView() {
    	return slidingView;
    }
    public void setSlidingView(View view) {
    	slidingView = view;
    }
    
    public int getSlidingDuration() {
    	return slidingDuration;
    }
    public void setSlidingDuration(int duration) {
    	slidingDuration = duration;
    }
    
    public float getDimmigAmount() {
    	return dimmingAmount;
    }
    public void setDimmingAmount(float value) {
    	dimmingAmount = value;
    }
    
    /***************************************************/
    /** �ִϸ��̼� ���� **/
    /***************************************************/
    //���� �ö� ����
    public void appearAnimation() {
     Animation appear = new TranslateAnimation(
    		 Animation.RELATIVE_TO_PARENT, 0.0f, 
             Animation.RELATIVE_TO_PARENT, 0.0f,
             Animation.RELATIVE_TO_PARENT, 1.0f,
             Animation.RELATIVE_TO_PARENT, 0.0f
             );
     appear.setDuration(slidingDuration);
     appear.setInterpolator(new AccelerateInterpolator());
     appear.setFillAfter(true);
     
     slidingView.startAnimation(appear);
     
    }
    
    //�Ʒ��� ��������
    public void disAppearAnimation() {
     Animation disappear = new TranslateAnimation(
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 1.0f
     );
     
     //������� animation ���� �� Activity ���̱�
     disappear.setAnimationListener(new AnimationListener() {
    	public void onAnimationEnd(Animation animaiotn) {
    		finish();
    	}
    	
    	public void onAnimationStart(Animation animaiotn) {;}
    	public void onAnimationRepeat(Animation animaiotn) {;}
     });
     
     disappear.setDuration(slidingDuration);
     disappear.setInterpolator(new DecelerateInterpolator());
     disappear.setFillAfter(true);
     
     slidingView.startAnimation(disappear);
     
    }
    
    //Back Ű ó��
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		disAppearAnimation();
    		setResult(RESULT_CANCELED);
    	}
      
    	return true;
    }
}
