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
 * 화면 하단에서 올라오는 슬라이딩 Activity
 * 
 * 이 class 를 상속 받은 class 에서 layout 전개 한 후 
 * setSlidingView 함수로 sliding 될 View 를 설정하고  appearAnimation() 함수를 호출 해야 sliding 동작 한다.
 * 최소한 위 두 작업은 상속 받는 class 에 들어 가야함
 * 적용법 참조는 MonthlyCalendar.java, monthly_calendar.xml 파일 참조
 * 
 * Animation 함수만 바꾸면 상단에서 내려오게 하거나 좌,우에서 나타나게 할 수 도 있음 (현재 미구현)
 */
public class BaseSlidingActivity extends Activity {
	
	//슬라이딩이 될 View
	private View slidingView = null;
	
	//슬라이딩 될 시간
	private int slidingDuration = 500;
	
	//Background 디밍 짙기 설정, 1.0f 가 가장 짙은 값 
	private float dimmingAmount = 0.75f;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //화면을 dimming 하는 부분
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = dimmingAmount;
        getWindow().setAttributes(lpWindow);
        
        //타이틀 감추기
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
    /** 애니메이션 설정 **/
    /***************************************************/
    //위로 올라 오는
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
    
    //아래로 내려가는
    public void disAppearAnimation() {
     Animation disappear = new TranslateAnimation(
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 1.0f
     );
     
     //사라지는 animation 끝난 후 Activity 죽이기
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
    
    //Back 키 처리
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		disAppearAnimation();
    		setResult(RESULT_CANCELED);
    	}
      
    	return true;
    }
}
