package com.fletamuto.sptb;


import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.view.FmBaseLayout;

public abstract class DetailBaseLayout extends FmBaseActivity {
	
	
	public abstract void onEditBtnClick();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
  //      setContentView(R.layout.report_history, true);
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
		
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "ÆíÁý");
		setEidtButtonListener();
	    setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
	}
}