package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.fletamuto.common.control.BaseSlidingActivity;

public class MonthlyCalendar extends BaseSlidingActivity {
	
	private MonthlyCalendarAdapter mCalendarAdapter;
	private static final String INTENT_KEY_YYYYMM = "YYYYMM";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.monthly_calendar);
        
        //sliding 을 위해 View 설정 하고 animation 시작 함수 호출 하는 부분 start
        setSlidingView(findViewById(R.id.CalendarLL));
        appearAnimation();
        //end

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
}
