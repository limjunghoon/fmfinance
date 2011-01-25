package com.fletamuto.sptb.view;

import com.fletamuto.sptb.R;
import com.fletamuto.sptb.R.anim;
import com.fletamuto.sptb.R.id;
import com.fletamuto.sptb.R.layout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FmBaseLayout extends FrameLayout {
	public static final int MENU_INCOME_EXPENSE  = 0;
	public static final int MENU_ASSETS  = 1;
	public static final int MENU_REPORT  = 2;
	public static final int MENU_BUDGET  = 3;
	public static final int MENU_SETTING  = 4;
	public static final int MAX_MENU_COUNT  = 5;
	
	private static boolean mChanging = false; 
	private static int mCurrentMenu;
//	private static int mMenuIndex = MENU_INCOME_EXPENSE;
	
	private ViewGroup mBodyLayout;
	private LinearLayout mMenuLayout;	
	private Button mBtnMenu[] = new Button[MAX_MENU_COUNT];
	private boolean mTitle = false;
	private boolean mSlide = true;
	private LinearLayout mTitleLayout = null;
	private LinearLayout mMainLayout;
	private LinearLayout mSlideLayout;
	private LinearLayout mSlideBottomLayout;
	private LinearLayout mSlideBottomBodyLayout;
	private LinearLayout mSlideBottomTitleLayout;
	private boolean mActiveSliding = false;
	
	private Button btnLeft01;
	private Button btnRigth01;
	private TextView tvTitle;
	
	public static final int BTN_LEFT_01 = 0;
	public static final int BTN_RIGTH_01 = 1;

	
	public FmBaseLayout(Context context, int layoutResID, boolean title) {
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
		mMainLayout = new LinearLayout(getContext());
		
		if (mTitleLayout != null) {
			mMainLayout.addView(mTitleLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		}
		
		setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		mMainLayout.addView(mBodyLayout, new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1));
		LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
		mMainLayout.addView(mMenuLayout, params);
		mMainLayout.setOrientation(LinearLayout.VERTICAL);
		addView(mMainLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		
		if (mSlideLayout != null) {
			addView(mSlideLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		}
	}
	
	public void setSlideVisibility(int visibility) {
		if (mSlideBottomLayout == null) return;
		mSlideBottomLayout.setVisibility(visibility);
		mActiveSliding = (visibility == View.VISIBLE); 
	}
	
	public void showOpenAnimatioin() {
		if (mSlideBottomLayout == null) return;
		mSlideBottomLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_show_effect));
	}
	
	public void showCloseAnimatioin() {
		if (mSlideBottomLayout == null) return;
		mSlideBottomLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_hide_effect));
	}
	
	private void initialize(Context context, int layoutResID) {
//		setOrientation(LinearLayout.VERTICAL);
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
		
		if (mSlide) {
			mSlideLayout = (LinearLayout) View.inflate(context, R.layout.main_slide, null);
			mSlideBottomLayout = (LinearLayout) mSlideLayout.findViewById(R.id.LLBottomSlide);
			mSlideBottomBodyLayout = (LinearLayout) mSlideLayout.findViewById(R.id.LLBottomSlideBody);
			mSlideBottomTitleLayout = (LinearLayout) mSlideLayout.findViewById(R.id.LLBottomSlideTitle);
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

	public static void setChanging(boolean mChanging) {
		FmBaseLayout.mChanging = mChanging;
	}

	public static boolean isChanging() {
		return mChanging;
	}
//
//	public static void setCurrentMenuIndex(int mMenuIndex) {
//		FmMainMenuLayout.mMenuIndex = mMenuIndex;
//	}
//
//	public static int getCurrentMenuIndex() {
//		return mMenuIndex;
//	}

	public static void setCurrentMenu(int currentMenu) {
		FmBaseLayout.mCurrentMenu = currentMenu;
	}

	public static int getCurrentMenu() {
		return mCurrentMenu;
	}

	public void setActiveSliding(boolean activeSliding) {
		this.mActiveSliding = activeSliding;
	}

	public boolean isActiveSliding() {
		return mActiveSliding;
	}

//	public void setSlideView(int layoutResource) {
//		if (mSlideBottomBodyLayout == null) return;
//		mSlideBottomBodyLayout.removeAllViews();
//		View layout = View.inflate(getContext(), layoutResource, null);
//		mSlideBottomBodyLayout.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
//	}

	public void setSlideView(View layout) {
		if (mSlideBottomBodyLayout == null) return;
		mSlideBottomBodyLayout.removeAllViews();
		mSlideBottomBodyLayout.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		
		if (mSlideBottomTitleLayout != null) {
			mSlideBottomTitleLayout.findViewById(R.id.BtnSlideOK).setTag(layout);
		}
	}
	
	public void setBottomSlideComplateListener(View.OnClickListener listener) {
		if (mSlideBottomTitleLayout == null) return;
		mSlideBottomTitleLayout.findViewById(R.id.BtnSlideOK).setOnClickListener(listener);
	}
	
	public void setBottomSlideCancelListener(View.OnClickListener listener) {
		if (mSlideBottomTitleLayout == null) return;
		mSlideBottomTitleLayout.findViewById(R.id.BtnSlideCancel).setOnClickListener(listener);
	}
	
	public void setMenuVisible(int visibility) {
		if (mMenuLayout == null) {
			return;
		}
		
		mMenuLayout.setVisibility(visibility);
	}
	 
}