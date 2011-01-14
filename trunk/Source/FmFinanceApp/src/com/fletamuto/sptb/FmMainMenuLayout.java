package com.fletamuto.sptb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FmMainMenuLayout extends LinearLayout {
	public static final int MENU_INCOME_EXPENSE  = 0;
	public static final int MENU_ASSETS  = 1;
	public static final int MENU_REPORT  = 2;
	public static final int MENU_BUDGET  = 3;
	public static final int MENU_SETTING  = 4;
	public static final int MAX_MENU_COUNT  = 5;
	
	private ViewGroup mBodyLayout;
	private LinearLayout mMenuLayout;	
	private Button mBtnMenu[] = new Button[MAX_MENU_COUNT];
	private boolean mTitle = false;
	private LinearLayout mTitleLayout = null;
	
	private Button btnLeft01;
	private Button btnRigth01;
	private TextView tvTitle;
	
	public static final int BTN_LEFT_01 = 0;
	public static final int BTN_RIGTH_01 = 1;

	
	public FmMainMenuLayout(Context context, int layoutResID, boolean title) {
		super(context);
		
		mTitle = title;
		initialize(context, layoutResID);
		addContent();
	}

	public void setMenuBtnClickListener(View.OnClickListener menuClickListener) {
		for (int index = 0; index < MAX_MENU_COUNT; index++) {
			mBtnMenu[index].setOnClickListener(menuClickListener);
		}
	}

	private void addContent() {
		if (mTitleLayout != null) {
			addView(mTitleLayout);
		}
		
		setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		addView(mBodyLayout, new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1));
		LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
		addView(mMenuLayout, params);
	}
	
	private void initialize(Context context, int layoutResID) {
		setOrientation(LinearLayout.VERTICAL);
		mBodyLayout = (ViewGroup)View.inflate(context, layoutResID, null);
		mMenuLayout = (LinearLayout)View.inflate(context, R.layout.menu, null);
		mBtnMenu[MENU_INCOME_EXPENSE] = (Button)mMenuLayout.findViewById(R.id.BtnMenuIncomeExpense);
		mBtnMenu[MENU_ASSETS] = (Button)mMenuLayout.findViewById(R.id.BtnMenuAssets);
		mBtnMenu[MENU_REPORT] = (Button)mMenuLayout.findViewById(R.id.BtnMenuReport);
		mBtnMenu[MENU_BUDGET] = (Button)mMenuLayout.findViewById(R.id.BtnMenuBudget);
		mBtnMenu[MENU_SETTING] = (Button)mMenuLayout.findViewById(R.id.BtnMenuSetting);
		
		if (mTitle) {
			mTitleLayout = (LinearLayout) View.inflate(context, R.layout.titlebar, null);
			btnLeft01 = (Button)mTitleLayout.findViewById(R.id.BtnTitleLeft01);
			btnLeft01.setVisibility(View.INVISIBLE);
			btnRigth01 = (Button)mTitleLayout.findViewById(R.id.BtnTitleRigth01);
			btnRigth01.setVisibility(View.INVISIBLE);
			tvTitle = (TextView)mTitleLayout.findViewById(R.id.TVTitle);
		}
	}
	
	public void setVisibility(int btnIndex, int visibility) {
		if (mTitleLayout == null) return;
		
		if (btnIndex == BTN_LEFT_01) {
			btnLeft01.setVisibility(visibility);
		}
		
		if (btnIndex == BTN_RIGTH_01) {
			btnRigth01.setVisibility(visibility);
		}
	}
	
	public void setEnabledButton(int btnIndex, boolean enabled) {
		if (mTitleLayout == null) return;
		
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
			if (btnLeft01 == null) return; 
			btnLeft01.setText(name);
		}
		else if (btnIndex == BTN_RIGTH_01) {
			if (btnRigth01 == null) return;
			btnRigth01.setText(name);
		}
	}
	
	/**
	 * 뷰의 제목을 설정
	 * @param title 설정할 뷰 제목
	 */
	public void setTitle(CharSequence title) {
		if (mTitleLayout == null) return;
		tvTitle.setText(title);
	}
}