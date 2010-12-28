package com.fletamuto.sptb;

import android.os.Bundle;
import android.view.View;

public abstract class StateDefaultLayout extends StateBaseLayout {  	
	
	protected abstract void onClickHistoryBtn();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setHistoryButtonListener();
    }

	protected void setHistoryButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onClickHistoryBtn();
			}
		});
	}
	
	@Override
	protected Class<?> getActivityClass() {
		return InputAssetsLayout.class;
	}
}