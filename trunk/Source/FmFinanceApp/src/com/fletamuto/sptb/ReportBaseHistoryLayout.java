package com.fletamuto.sptb;


import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.view.FmBaseLayout;

public abstract class ReportBaseHistoryLayout extends FmBaseActivity {
	private Calendar mCalendar = Calendar.getInstance();
	
	public abstract void onEditBtnClick();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_history, true);
    }
    
    @Override
    public void initialize() {
    	super.initialize();
    }

    public void setEidtButtonListener() {
		setTitleButtonListener(FmBaseLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onEditBtnClick();
				
			}
		});
	}
    
    


	@Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	TextView tvCurrentMonth = (TextView)findViewById(R.id.TVCurrentMonth);
    	tvCurrentMonth.setText(String.format("%d년 %d월", mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1));
    	
    	
    	setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "편집");
    	setEidtButtonListener();
        setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
    	
    	
    }
    
    protected void setVisibleTitle() {
		findViewById(R.id.LLTitle).setVisibility(View.VISIBLE);
	}

	public void setCalendar(Calendar calendar) {
		this.mCalendar = calendar;
	}

	public Calendar getCalendar() {
		return mCalendar;
	}
 
}