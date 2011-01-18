package com.fletamuto.sptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fletamuto.sptb.util.LogTag;

/**
 * finance���� �⺻�� �Ǵ� �� ��Ƽ��Ƽ
 * @author yongbban
 * @version 1.0.0.1
 */
public abstract class FmBaseActivity extends Activity {
//	private FmTitleLayout mTitleLayout;
	private FmMainMenu  mMenuLayout;
	
	private boolean mRootView = false;
	private boolean mMenuVisible = true;
	//private static int mCurrentMenu;
	
	private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			int id = v.getId();
			int menuIndex = FmMainMenu.MENU_INCOME_EXPENSE;
			
			if (id == R.id.BtnMenuIncomeExpense)	menuIndex = FmMainMenu.MENU_INCOME_EXPENSE;
			else if (id == R.id.BtnMenuAssets) menuIndex = FmMainMenu.MENU_ASSETS;
			else if (id == R.id.BtnMenuReport) menuIndex = FmMainMenu.MENU_REPORT;
			else if (id == R.id.BtnMenuBudget) 	menuIndex = FmMainMenu.MENU_BUDGET;
			else if (id == R.id.BtnMenuSetting) menuIndex = FmMainMenu.MENU_SETTING;
					
			onMenuClick(menuIndex);
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
    }
    
    @Override
    protected void onResume() {
    	if (FmMainMenu.isChanging()) {
    		if (isRootView()) {
    			changeMenu();
    		}
    		finish();
    	}
    	
    	super.onResume();
    }
    
    /**
     * ������ ���̾ƿ� �信 Ÿ��Ʋ�ٸ� ǥ���Ѵ�.
     * @param layoutResID ǥ���� ���̾ƿ� ���̵�
     * @param title Ÿ��Ʋ�� ǥ�ÿ���
     */
    public void setContentView(int layoutResID, boolean title) {
    	if (title == true) {
    		
    		
 //   		if (mMenuVisible == true) {
    			mMenuLayout = new FmMainMenu(this, layoutResID, true);
    			super.setContentView(mMenuLayout);
//    		}
//    		else {
//    			mTitleLayout = new FmTitleLayout(this, layoutResID);
//    			super.setContentView(mTitleLayout);
//    		}
    		
    	}
    	else {
    		if (mMenuVisible == true) {
    			mMenuLayout = new FmMainMenu(this, layoutResID, false);
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
			mMenuLayout = new FmMainMenu(this, layoutResID, false);
			super.setContentView(mMenuLayout);
		}
		else {
			super.setContentView(layoutResID);
		}
    	
    	initialize();
    	mMenuLayout.setMenuBtnClickListener(mMenuClickListener);
    }
    
    /**
     * Ÿ��Ʋ ��ư ������ �Ѵ�.
     */
    protected  void setTitleBtn() {
		
	}
    
    protected void initialize() {
    	
    }
    
    
    
    /** ����â ��ư Ŭ���� ������ ���� */
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
    		button = mMenuLayout.getButton(FmMainMenu.BTN_LEFT_01);
    	}
    	else if (btnIndex == FmTitleLayout.BTN_RIGTH_01) {
    		button = mMenuLayout.getButton(FmMainMenu.BTN_RIGTH_01);
    	}
    	else {
    		return;
    	}
    	if (button == null) return;
    	button.setOnClickListener(listener);
    }
    
    /**
	 * ���� ������ ����
	 * @param title ������ �� ����
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
    
	protected void onMenuClick(int menuIndex) {
		if (menuIndex == FmMainMenu.getCurrentMenu()) return;
		
		FmMainMenu.setChanging(true);
		FmMainMenu.setCurrentMenu(menuIndex);
		
		if (isRootView()) {
			changeMenu();
		} 
		
		finish();
	}
	
	protected void changeMenu() {
		int menuIndex = FmMainMenu.getCurrentMenu();
		Class<?> changeClass = null;
		
		if (menuIndex == FmMainMenu.MENU_INCOME_EXPENSE)	changeClass = MainIncomeAndExpenseLayout.class;
		else if (menuIndex == FmMainMenu.MENU_ASSETS) changeClass = MainAssetsLayout.class;
		else if (menuIndex == FmMainMenu.MENU_REPORT) changeClass = MainReportLayout.class;
		else if (menuIndex == FmMainMenu.MENU_BUDGET) 	changeClass = BudgetLayout.class;
		else if (menuIndex == FmMainMenu.MENU_SETTING) 	changeClass = MainSettingLayout.class;
		else {
			Log.e(LogTag.LAYOUT, "== unregistered event hander ");
			menuIndex = FmMainMenu.MENU_INCOME_EXPENSE;
			changeClass = MainIncomeAndExpenseLayout.class;
		}
		
		FmMainMenu.setChanging(false);
		Intent intent = new Intent(this, changeClass);
		startActivity(intent);   
	}

	public void setRootView(boolean mRootView) {
		this.mRootView = mRootView;
	}

	public boolean isRootView() {
		return mRootView;
	}

}