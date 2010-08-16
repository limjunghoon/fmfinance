package com.bban.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BaseApp extends Activity {
	private FmTitleLayout titleLayout;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
    }
    
    public void setContentView(int layoutResID, boolean title) {
    	if (title == true) {
    		titleLayout = new FmTitleLayout(this, layoutResID);
    		super.setContentView(titleLayout);
    	}
    	else {
    		super.setContentView(layoutResID);
    	}
    }
    
    protected void setTitleButtonListener() {
    	if (titleLayout == null) return;
    	
    	Button btnBack = titleLayout.getBtn(FmTitleLayout.BTN_LEFT_01);
    	btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    	
    }
}