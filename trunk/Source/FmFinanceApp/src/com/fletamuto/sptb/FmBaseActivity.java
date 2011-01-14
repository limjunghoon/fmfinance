package com.fletamuto.sptb;

import com.fletamuto.sptb.util.LogTag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * finance에서 기본이 되는 뷰 엑티비티
 * @author yongbban
 * @version 1.0.0.1
 */
public abstract class FmBaseActivity extends Activity {
//	private FmTitleLayout mTitleLayout;
	private FmMainMenuLayout  mMenuLayout;
	
	private boolean mMenuVisible = true;
	private static int mCurrentMenu = R.id.BtnMenuIncomeExpense;
	
	private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			onMenuClick(v.getId());
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        
    }
    
    /**
     * 지정된 레이아웃 뷰에 타이틀바를 표시한다.
     * @param layoutResID 표시할 레이아웃 아이디
     * @param title 타이틀뷰 표시여부
     */
    public void setContentView(int layoutResID, boolean title) {
    	if (title == true) {
    		
    		
 //   		if (mMenuVisible == true) {
    			mMenuLayout = new FmMainMenuLayout(this, layoutResID, true);
    			super.setContentView(mMenuLayout);
//    		}
//    		else {
//    			mTitleLayout = new FmTitleLayout(this, layoutResID);
//    			super.setContentView(mTitleLayout);
//    		}
    		
    	}
    	else {
    		if (mMenuVisible == true) {
    			mMenuLayout = new FmMainMenuLayout(this, layoutResID, false);
    			super.setContentView(mMenuLayout);
    		}
    		else {
    			super.setContentView(layoutResID);
    		}
    	}
    	
    	initialize();
    	setTitleBtn();
    	setTitleButtonListener();
    	
    	mMenuLayout.setMenuBtnClickListener(mMenuClickListener);
    }
    
    public void setContentView(int layoutResID) {
    	
    	if (mMenuVisible == true) {
			mMenuLayout = new FmMainMenuLayout(this, layoutResID, false);
			super.setContentView(mMenuLayout);
		}
		else {
			super.setContentView(layoutResID);
		}
    	
    	initialize();
    	mMenuLayout.setMenuBtnClickListener(mMenuClickListener);
    }
    
    /**
     * 타이틀 버튼 설정을 한다.
     */
    protected  void setTitleBtn() {
		
	}
    
    protected void initialize() {
    	
    }
    
    
    
    /** 제목창 버튼 클릭시 리스너 설정 */
    protected void setTitleButtonListener() {
    	if (mMenuLayout == null) return;
    	
    	Button btnBack = mMenuLayout.getButton(FmTitleLayout.BTN_LEFT_01);
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
    	if (mMenuLayout == null) return;
    	Button button = null;
    	
    	if (btnIndex == FmTitleLayout.BTN_LEFT_01) {
    		button = mMenuLayout.getButton(FmMainMenuLayout.BTN_LEFT_01);
    	}
    	else if (btnIndex == FmTitleLayout.BTN_RIGTH_01) {
    		button = mMenuLayout.getButton(FmMainMenuLayout.BTN_RIGTH_01);
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
    	if (mMenuLayout == null) return;
    	mMenuLayout.setTitle(title);
    };
    
    public void setTitleBtnVisibility(int btnIndex, int visibility) {
    	if (mMenuLayout == null) return;
    	mMenuLayout.setVisibility(btnIndex, visibility);
    }
    
    public void setTitleBtnText(int btnIndex, CharSequence name) {
    	if (mMenuLayout == null) return;
    	mMenuLayout.setButtonText(btnIndex, name);
    }
    
    public void setTitleBtnEnabled(int layoutResID, boolean enabled) {
    	if (mMenuLayout == null) return;
    	mMenuLayout.setEnabledButton(layoutResID, enabled);
    }
    
	protected void onMenuClick(int id) {
		if (id == mCurrentMenu) return;
		
		Class<?> changeClass = null;
		
		if (id == R.id.BtnMenuIncomeExpense)	changeClass = MainIncomeAndExpenseLayout.class;
		else if (id == R.id.BtnMenuAssets) changeClass = MainAssetsLayout.class;
		else if (id == R.id.BtnMenuReport) changeClass = MainReportLayout.class;
		else if (id == R.id.BtnMenuBudget) 	changeClass = BudgetLayout.class;
		else if (id == R.id.BtnMenuSetting) 	changeClass = MainSettingLayout.class;
		else {
			Log.e(LogTag.LAYOUT, "== unregistered event hander ");
			return;
		}
		
		mCurrentMenu = id;
		Intent intent = new Intent(this, changeClass);
		startActivity(intent);
	}
}