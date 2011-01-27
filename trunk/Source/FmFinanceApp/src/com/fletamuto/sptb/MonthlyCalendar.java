package com.fletamuto.sptb;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MonthlyCalendar extends Activity {
	
	private MonthlyCalendarAdapter mCalendarAdapter;
	private static final String INTENT_KEY_YYYYMM = "YYYYMM";
	LinearLayout layout = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //화면을 dimming 하는 부분
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.75f;
        getWindow().setAttributes(lpWindow);
        
        setContentView(R.layout.monthly_calendar);

        layout = (LinearLayout) findViewById(R.id.CalendarLL);
        
        //Animation 으로 달력 보이기
        appearAnimation();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();        
        Calendar cal = null;
        if (bundle != null)
        	cal = (Calendar)bundle.getSerializable(INTENT_KEY_YYYYMM);
        if (cal == null)
            cal = Calendar.getInstance();
                
        cal.set(Calendar.DATE, 1);        
		setCalendar(cal);

		setDateMoveButtonHandler(R.id.prevYear, Calendar.YEAR, -1);
		setDateMoveButtonHandler(R.id.prevMonth, Calendar.MONTH, -1);
		setDateMoveButtonHandler(R.id.nextYear, Calendar.YEAR, 1);
		setDateMoveButtonHandler(R.id.nextMonth, Calendar.MONTH, 1);

    }
    
    /***************************************************/
    /** 애니메이션 설정 **/
    /***************************************************/
    //위로 올라 오는
    void appearAnimation() {
     Animation appear = new TranslateAnimation(
    		 Animation.RELATIVE_TO_PARENT, 0.0f, 
             Animation.RELATIVE_TO_PARENT, 0.0f,
             Animation.RELATIVE_TO_PARENT, 1.0f,
             Animation.RELATIVE_TO_PARENT, 0.0f
             );
     appear.setDuration(700);
     appear.setInterpolator(new AccelerateInterpolator());
     appear.setFillAfter(true);
     
     layout.startAnimation(appear);
     
    }
    
    //아래로 내려가는
    void disAppearAnimation() {
     Animation disappear = new TranslateAnimation(
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 0.0f,
    		 Animation.RELATIVE_TO_PARENT, 1.0f
     );
     
     //달력 사라진 후 Activity 죽이기
     disappear.setAnimationListener(new AnimationListener() {
    	public void onAnimationEnd(Animation animaiotn) {
    		finish();
    	}
    	
    	public void onAnimationStart(Animation animaiotn) {;}
    	public void onAnimationRepeat(Animation animaiotn) {;}
     });
     
     disappear.setDuration(500);
     disappear.setInterpolator(new DecelerateInterpolator());
     disappear.setFillAfter(true);
     
     layout.startAnimation(disappear);
     
    }
    
    private void setCalendar(Calendar cal) {
    	
    	if (mCalendarAdapter == null)
    		mCalendarAdapter = new MonthlyCalendarAdapter(this, cal);
    	else
    		mCalendarAdapter.setBaseDate(cal);
    		
        Button btn = (Button) findViewById(R.id.prevYear);
        btn.setTag(cal);
        btn = (Button) findViewById(R.id.prevMonth);
        btn.setTag(cal);
        btn = (Button) findViewById(R.id.nextYear);
        btn.setTag(cal);
        btn = (Button) findViewById(R.id.nextMonth);
        btn.setTag(cal);

        TextView tv = (TextView) findViewById(R.id.textview);
        String yyyyMM = String.format("%s 년 %s 월"
				, Integer.toString(cal.get(Calendar.YEAR))
				, Integer.toString(cal.get(Calendar.MONTH)+1));
        tv.setText(yyyyMM);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(mCalendarAdapter);
    }
    
    private void setDateMoveButtonHandler(int id, final int YearOrMonth, final int direction) {
        Button btn = (Button) findViewById(id);
		btn.setOnClickListener(new View.OnClickListener() {			

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar cal = (Calendar)v.getTag();
				cal.add(YearOrMonth, direction);
				setCalendar(cal);
			}
		});
    	
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
