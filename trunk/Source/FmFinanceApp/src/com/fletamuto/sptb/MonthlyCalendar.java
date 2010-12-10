package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.content.Context;

public class MonthlyCalendar {
	
	private MonthlyCalendarAdapter mCalendarAdapter;
	private static final String INTENT_KEY_YYYYMM = "YYYYMM";
	private Context mContext;
	private View popupview;
	private Intent intent;
	private PopupWindow popup;
	private View mainView;
	private Calendar selectCalendar;
	
	public MonthlyCalendar(Context context, Intent it, View popup, View main) {
		mContext = context;
		popupview = popup;
		intent = it;   
		mainView = main;		
	}
    
	public PopupWindow getPopupWindow() {
		return popup;
	}
	
	public View getSelectDate() {
		return mCalendarAdapter.getSelectDate();
	}
	
	public void setSelectCalendar (Calendar cal) {
		selectCalendar = cal;
	}
	
	public Calendar getSelectCalendar() {
		return selectCalendar;
	}
	
	public void showMonthlyCalendarPopup() {
		
		popup = new PopupWindow(popupview, 320, 280, true);
		popup.showAtLocation(mainView, Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
		
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
   		mCalendarAdapter = new MonthlyCalendarAdapter(mContext, cal, this);
    		
        Button btn = (Button) popupview.findViewById(R.id.prevYear);
        btn.setTag(cal);
        btn = (Button) popupview.findViewById(R.id.prevMonth);
        btn.setTag(cal);
        btn = (Button) popupview.findViewById(R.id.nextYear);
        btn.setTag(cal);
        btn = (Button) popupview.findViewById(R.id.nextMonth);
        btn.setTag(cal);

        TextView tv = (TextView) popupview.findViewById(R.id.textview);
        String yyyyMM = String.format("%s ³â %s ¿ù"
        							, Integer.toString(cal.get(Calendar.YEAR))
        							, Integer.toString(cal.get(Calendar.MONTH)+1));
        tv.setText(yyyyMM);

        GridView gridview = (GridView) popupview.findViewById(R.id.gridview);
        gridview.setAdapter(mCalendarAdapter);
        
        Button btnClose = (Button) popupview.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				setSelectCalendar(null);
				popup.dismiss();
				
			}
		});

    }
    
    private void setDateMoveButtonHandler(int id, final int YearOrMonth, final int direction) {
        Button btn = (Button) popupview.findViewById(id);
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
