package com.fletamuto.sptb.view;

import android.content.Context;
import android.widget.LinearLayout;

public abstract class BaseSliderLayout extends LinearLayout {
	private int mSliderListenerBtnId;
	
	public BaseSliderLayout(Context context) {
		super(context);
		
	}

	public void setSliderListenerBtnId(int mSliderListenerBtnId) {
		this.mSliderListenerBtnId = mSliderListenerBtnId;
	}

	public int getSliderListenerBtnId() {
		return mSliderListenerBtnId;
	}
    
}