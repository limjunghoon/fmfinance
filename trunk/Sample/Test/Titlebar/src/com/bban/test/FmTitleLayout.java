package com.bban.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FmTitleLayout extends LinearLayout {
	private ViewGroup bodyLayout;
	private LinearLayout TitleLayout;
	private Button btnLeft01;
	private Button btnRigth01;
	
	public static final int BTN_LEFT_01 = 0;
	public static final int BTN_RIGTH_01 = 1;
	
	public FmTitleLayout(Context context, int layoutResID) {
		super(context);
		
		initialize(context, layoutResID);
		addContent();
	}

	private void addContent() {
		addView(TitleLayout);
		addView(bodyLayout);
	}
	
	private void initialize(Context context, int layoutResID) {
		setOrientation(LinearLayout.VERTICAL);
		bodyLayout = (ViewGroup)View.inflate(context, layoutResID, null);
		TitleLayout = (LinearLayout)View.inflate(context, R.layout.titlebar, null);
		btnLeft01 = (Button)TitleLayout.findViewById(R.id.BtnTitleLeft01);
		btnRigth01 = (Button)TitleLayout.findViewById(R.id.BtnTitleRigth01);
		btnRigth01.setVisibility(View.INVISIBLE);
	}
	
	public void setVisibility(int btnIndex, int visibility) {
		
		if (btnIndex == BTN_LEFT_01) {
			TitleLayout.findViewById(R.id.BtnTitleLeft01).setVisibility(visibility);
		}
		
		if (btnIndex == BTN_RIGTH_01) {
			TitleLayout.findViewById(R.id.BtnTitleRigth01).setVisibility(visibility);
		}
	}
	
	public Button getBtn(int btnIndex) {
		if (btnIndex == BTN_LEFT_01) {
			return btnLeft01;
		}
		else if (btnIndex == BTN_RIGTH_01) {
			return btnRigth01;
		}
		else {
			return null;
		}
	}
}