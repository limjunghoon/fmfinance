package com.fletamuto.sptb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FmTitleLayout extends LinearLayout {
	private ViewGroup bodyLayout;
	private LinearLayout TitleLayout;
	private Button btnLeft01;
	private Button btnRigth01;
	private TextView tvTitle;
	
	public static final int BTN_LEFT_01 = 0;
	public static final int BTN_RIGTH_01 = 1;
	
	public FmTitleLayout(Context context, int layoutResID) {
		super(context);
		
		initialize(context, layoutResID);
		addContent();
	}

	private void addContent() {
		addView(TitleLayout);
		LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		addView(bodyLayout, params);
	}
	
	private void initialize(Context context, int layoutResID) {
		setOrientation(LinearLayout.VERTICAL);
		bodyLayout = (ViewGroup)View.inflate(context, layoutResID, null);
		TitleLayout = (LinearLayout)View.inflate(context, R.layout.titlebar, null);
		btnLeft01 = (Button)TitleLayout.findViewById(R.id.BtnTitleLeft01);
		btnRigth01 = (Button)TitleLayout.findViewById(R.id.BtnTitleRigth01);
		btnRigth01.setVisibility(View.INVISIBLE);
		tvTitle = (TextView)TitleLayout.findViewById(R.id.TVTitle);
	}
	
	public void setVisibility(int btnIndex, int visibility) {
		if (TitleLayout == null) return;
		
		if (btnIndex == BTN_LEFT_01) {
			btnLeft01.setVisibility(visibility);
		}
		
		if (btnIndex == BTN_RIGTH_01) {
			btnRigth01.setVisibility(visibility);
		}
	}
	
	public void setEnabledButton(int btnIndex, boolean enabled) {
		if (TitleLayout == null) return;
		
		if (btnIndex == BTN_LEFT_01) {
			btnLeft01.setEnabled(enabled);
		}
		
		if (btnIndex == BTN_RIGTH_01) {
			btnRigth01.setEnabled(enabled);
		}
	}
	
	public Button getButton(int btnIndex) {
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
	
	public void setButtonText(int btnIndex, CharSequence name) {
		if (btnIndex == BTN_LEFT_01) {
			btnLeft01.setText(name);
		}
		else if (btnIndex == BTN_RIGTH_01) {
			btnRigth01.setText(name);
		}
	}
	
	
	
	public void setTitle(CharSequence title) {
		if (TitleLayout == null) return;
		tvTitle.setText(title);
	}
}