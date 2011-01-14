package com.fletamuto.sptb;

import com.fletamuto.sptb.util.LogTag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * finance���� �⺻�� �Ǵ� �� ��Ƽ��Ƽ
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
     * ������ ���̾ƿ� �信 Ÿ��Ʋ�ٸ� ǥ���Ѵ�.
     * @param layoutResID ǥ���� ���̾ƿ� ���̵�
     * @param title Ÿ��Ʋ�� ǥ�ÿ���
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