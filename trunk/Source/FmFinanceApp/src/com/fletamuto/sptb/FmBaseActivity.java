package com.fletamuto.sptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;

import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.view.BaseSliderLayout;
import com.fletamuto.sptb.view.FmBaseLayout;

/**
 * finance에서 기본이 되는 뷰 엑티비티
 * @author yongbban
 * @version 1.0.0.1
 */
public abstract class FmBaseActivity extends Activity {
//	private FmTitleLayout mTitleLayout;
	private FmBaseLayout  mBaseLayout;
	
	private boolean mRootView = false;
	private boolean mMenuVisible = true;
	//private static int mCurrentMenu;
	
	private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			int id = v.getId();
			int menuIndex = FmBaseLayout.MENU_INCOME_EXPENSE;
			
			if (id == R.id.BtnMenuIncomeExpense)	menuIndex = FmBaseLayout.MENU_INCOME_EXPENSE;
			else if (id == R.id.BtnMenuAssets) menuIndex = FmBaseLayout.MENU_ASSETS;
			else if (id == R.id.BtnMenuReport) menuIndex = FmBaseLayout.MENU_REPORT;
			else if (id == R.id.BtnMenuBudget) 	menuIndex = FmBaseLayout.MENU_BUDGET;
			else if (id == R.id.BtnMenuSetting) menuIndex = FmBaseLayout.MENU_SETTING;
					
			onMenuClick(menuIndex);
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    /**
     * 타이틀 버튼 설정을 한다.
     */
    protected  void setTitleBtn() {
		
	}
    
    protected void initialize() {
    	setBottomSlideComplateListener();
    	setBottomSlideCancelListener();
    }
    
    
    @Override
    protected void onResume() {
    	if (FmBaseLayout.isChanging()) {
    		if (isRootView()) {
    			changeMenu();
    		}
    		finish();
    	}
    	
    	super.onResume();
    }
    
    /**
     * 지정된 레이아웃 뷰에 타이틀바를 표시한다.
     * @param layoutResID 표시할 레이아웃 아이디
     * @param title 타이틀뷰 표시여부
     */
    public void setContentView(int layoutResID, boolean title) {
    	if (title == true) {
    		
    		
 //   		if (mMenuVisible == true) {
    			mBaseLayout = new FmBaseLayout(this, layoutResID, true);
    			super.setContentView(mBaseLayout);
//    		}
//    		else {
//    			mTitleLayout = new FmTitleLayout(this, layoutResID);
//    			super.setContentView(mTitleLayout);
//    		}
    		
    	}
    	else {
    		if (mMenuVisible == true) {
    			mBaseLayout = new FmBaseLayout(this, layoutResID, false);
    			super.setContentView(mBaseLayout);
    		}
    		else {
    			super.setContentView(layoutResID);
    		}
    	}
    	
    	initialize();
    	setTitleBtn();
    	setTitleButtonListener();
    	
    	mBaseLayout.setMenuBtnClickListener(mMenuClickListener);
    }
    
    public void setContentView(int layoutResID) {
    	
    	if (mMenuVisible == true) {
			mBaseLayout = new FmBaseLayout(this, layoutResID, false);
			super.setContentView(mBaseLayout);
		}
		else {
			super.setContentView(layoutResID);
		}
    	
    	initialize();
    	mBaseLayout.setMenuBtnClickListener(mMenuClickListener);
    }
    

    
    
    
    /** 제목창 버튼 클릭시 리스너 설정 */
    protected void setTitleButtonListener() {
    	if (mBaseLayout == null) return;
    	
    	Button btnBack = mBaseLayout.getButton(FmTitleLayout.BTN_LEFT_01);
    	if (btnBack == null) return;
    	btnBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				onClickLeft1TitleBtn();
				
			}
		});
    	
    	
    }
    
    protected void onClickLeft1TitleBtn() {
    	finish();
	}

	protected void setTitleButtonListener(int btnIndex, View.OnClickListener listener) {
    	if (mBaseLayout == null) return;
    	Button button = null;
    	
    	if (btnIndex == FmTitleLayout.BTN_LEFT_01) {
    		button = mBaseLayout.getButton(FmBaseLayout.BTN_LEFT_01);
    	}
    	else if (btnIndex == FmTitleLayout.BTN_RIGTH_01) {
    		button = mBaseLayout.getButton(FmBaseLayout.BTN_RIGTH_01);
    	}
    	else {
    		return;
    	}
    	if (button == null) return;
    	button.setOnClickListener(listener);
    }
    
    /**
	 * 뷰의 제목을 설정
	 * @param title 설정할 뷰 제목
	 */
    public void setTitle(CharSequence title) {
    	if (mBaseLayout == null) return;
    	mBaseLayout.setTitle(title);
    };
    
    public void setTitleBtnVisibility(int btnIndex, int visibility) {
    	if (mBaseLayout == null) return;
    	mBaseLayout.setVisibility(btnIndex, visibility);
    }
    
    public void setTitleBtnText(int btnIndex, CharSequence name) {
    	if (mBaseLayout == null) return;
    	mBaseLayout.setButtonText(btnIndex, name);
    }
    
    public void setTitleBtnEnabled(int layoutResID, boolean enabled) {
    	if (mBaseLayout == null) return;
    	mBaseLayout.setEnabledButton(layoutResID, enabled);
    }
    
	protected void onMenuClick(int menuIndex) {
		if (menuIndex == FmBaseLayout.getCurrentMenu()) return;
		
		FmBaseLayout.setChanging(true);
		FmBaseLayout.setCurrentMenu(menuIndex);
		
		if (isRootView()) {
			changeMenu();
		} 
		
		finish();
	}
	
	protected void changeMenu() {
		int menuIndex = FmBaseLayout.getCurrentMenu();
		Class<?> changeClass = null;
		
		if (menuIndex == FmBaseLayout.MENU_INCOME_EXPENSE)	changeClass = MainIncomeAndExpenseLayout.class;
		else if (menuIndex == FmBaseLayout.MENU_ASSETS) changeClass = MainAssetsLayout.class;
		else if (menuIndex == FmBaseLayout.MENU_REPORT) changeClass = MainReportLayout.class;
		else if (menuIndex == FmBaseLayout.MENU_BUDGET) 	changeClass = BudgetLayout.class;
		else if (menuIndex == FmBaseLayout.MENU_SETTING) 	changeClass = MainSettingLayout.class;
		else {
			Log.e(LogTag.LAYOUT, "== unregistered event hander ");
			menuIndex = FmBaseLayout.MENU_INCOME_EXPENSE;
			changeClass = MainIncomeAndExpenseLayout.class;
		}
		
		FmBaseLayout.setChanging(false);
		Intent intent = new Intent(this, changeClass);
		startActivity(intent);   
	}

	public void setRootView(boolean mRootView) {
		this.mRootView = mRootView;
	}

	public boolean isRootView() {
		return mRootView;
	}
	
	public void showSlideView() {
		mBaseLayout.setSlideVisibility(View.VISIBLE);
		mBaseLayout.showOpenAnimatioin();
	}
	
	public void hideSlideView() {
		mBaseLayout.showCloseAnimatioin();
		mBaseLayout.setSlideVisibility(View.INVISIBLE);
	}
	
	public boolean isActiveSliding() {
		return mBaseLayout.isActiveSliding();
	}
	
//    protected void setSlideView(int layoutResource) {
//    	mBaseLayout.setSlideView(layoutResource);
//    }
    
    protected void setSlideView(View layout) {
    	mBaseLayout.setSlideView(layout);
    }
    
    public void setBottomSlideComplateListener() {
    	mBaseLayout.setBottomSlideComplateListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onClickBottomSlideComplate(v);
			}
		});
	}
    
    /**
     * 슬라인더뷰에서 완료버튼 클릭시
     */
    protected void onClickBottomSlideComplate(View v) {
    	hideSlideView();
	}
    
    public void setBottomSlideCancelListener() {
    	mBaseLayout.setBottomSlideCancelListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onClickBottomSlideCancel(v);
			}
		});
	}
    
    /**
     * 슬라인더뷰에서 취소버튼 클릭시
     */
    protected void onClickBottomSlideCancel(View v) {
    	hideSlideView();
	}
    
    public void setMenuVisible(int visibility) {
    	if (mBaseLayout == null) return;
    	
    	mBaseLayout.setMenuVisible(visibility);
    }
}