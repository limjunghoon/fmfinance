package com.fletamuto.sptb.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.R;

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