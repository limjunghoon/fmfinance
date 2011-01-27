package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.util.Log;

public class MonthlyCalendarAdapter extends BaseAdapter {
	
	private MonthlyCalendar mCalendar;
	private Context mContext;
	private Calendar mBaseDate;
	private int mStartPos;
	private int mEndPos;
	private int mDaysInMonth;
	private static final int CELL_WIDTH = 20;
	private static final int CELL_HEIGH = 20;
	
	private static final String[] mWeekTitleIds = { 
		   "일"
		   ,"월"
		   ,"화"
		   ,"수"
		   ,"목"
		   ,"금"
		   ,"토"
	};
	private static final String[] mFullWeekTitleIds = { 
	    "일요일"
	   ,"월요일"
	   ,"화요일"
	   ,"수요일"
	   ,"목요일"
	   ,"금요일"
	   ,"토요일"
};
	private static final int[] mWeekColorIds = { 
		   	Color.RED
		   ,Color.BLACK
		   ,Color.BLACK
		   ,Color.BLACK
		   ,Color.BLACK
		   ,Color.BLACK
		   ,Color.BLUE
	};
	

	public MonthlyCalendarAdapter(/*Context*/MonthlyCalendar c, Calendar cal) {
        mContext = c;
        mCalendar = c;
        setBaseDate(cal);
    }

    public void setBaseDate(Calendar cal) {
        mBaseDate = (Calendar)cal.clone();
        Calendar lastDayInMonth = (Calendar)cal.clone();
        lastDayInMonth.add(Calendar.MONTH, 1);
        lastDayInMonth.add(Calendar.DATE, -1);
        mDaysInMonth = lastDayInMonth.get(Calendar.DATE);
        mStartPos = 7 
                  + mBaseDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY // 일요일을 0으로 해서 요일의 순서 
                  ;
        mEndPos = mStartPos 
                + mDaysInMonth;    	
    }
    
	public int getCount() {
    	return mEndPos; 
	}


	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View oldView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v;

		if (position < 7) { // 제목 행
			
			if (oldView == null) {
				v = new TextView(mContext);
				
				((TextView)v).setGravity(Gravity.CENTER);
				((TextView)v).setText(mWeekTitleIds[position]);
				((TextView)v).setTextColor(mWeekColorIds[position]);
			}
			else {
				v = oldView;
			}
		}
		
		else if (position >= mStartPos && position <= mEndPos) { // 유효한 날짜 영역
			if (oldView == null) {
			v = new TextView(mContext);
			((TextView)v).setGravity(Gravity.CENTER);
			int nDay = getDayFromPosition(position);
			Calendar c = (Calendar)mBaseDate.clone();
			c.set(Calendar.DATE, nDay);
			v.setTag(c);
			((TextView)v).setText(Integer.toString(nDay));
			((TextView)v).setTextColor(mWeekColorIds[c.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY]);
			//날짜가 선택 되었을 때
			v.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

					Calendar c = (Calendar)v.getTag();
					if (c == null) return;
					int [] values = {c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)};

					Intent intent = new Intent();
	            	intent.putExtra("SELECTED_DATE", values);
	            	mCalendar.setResult(mCalendar.RESULT_OK,intent);
	            	mCalendar.disAppearAnimation();

	            }
	          	});
			
			}
			else {
				v = oldView;
			}
		} else { // 빈 영역
			v = new TextView(mContext);
		}

		if (oldView == null) {
			v.setLayoutParams(new GridView.LayoutParams(CELL_WIDTH, CELL_HEIGH));
		}

		return v;
	}

	private int getDayFromPosition(int position) {
		return position - mStartPos +1;		
	}
	
}
