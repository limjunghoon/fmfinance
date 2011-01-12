package com.fletamuto.sptb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * finance에서 기본이 되는 뷰 엑티비티
 * @author yongbban
 * @version 1.0.0.1
 */
public abstract class FmBaseActivity extends Activity {
	private FmTitleLayout titleLayout;
	
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
    		titleLayout = new FmTitleLayout(this, layoutResID);
    		super.setContentView(titleLayout);
    	}
    	else {
    		super.setContentView(layoutResID);
    	}
    	
    	initialize();
    	setTitleBtn();
    	setTitleButtonListener();
    }
    
    public void setContentView(int layoutResID) {
    	super.setContentView(layoutResID);
    	
    	initialize();
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
    	if (titleLayout == null) return;
    	
    	Button btnBack = titleLayout.getButton(FmTitleLayout.BTN_LEFT_01);
    	btnBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
    	
    	
    }
    
    protected void setTitleButtonListener(int btnIndex, View.OnClickListener listener) {
    	if (titleLayout == null) return;
    	Button button = null;
    	
    	if (btnIndex == FmTitleLayout.BTN_LEFT_01) {
    		button = titleLayout.getButton(FmTitleLayout.BTN_LEFT_01);
    	}
    	else if (btnIndex == FmTitleLayout.BTN_RIGTH_01) {
    		button = titleLayout.getButton(FmTitleLayout.BTN_RIGTH_01);
    	}
    	else {
    		return;
    	}
    	
    	button.setOnClickListener(listener);
    }
    
    /**
	 * 뷰의 제목을 설정
	 * @param title 설정할 뷰 제목
	 */
    public void setTitle(CharSequence title) {
    	if (titleLayout == null) return;
    	titleLayout.setTitle(title);
    };
    
    public void setTitleBtnVisibility(int btnIndex, int visibility) {
    	if (titleLayout == null) return;
    	titleLayout.setVisibility(btnIndex, visibility);
    }
    
    public void setTitleBtnText(int btnIndex, CharSequence name) {
    	if (titleLayout == null) return;
    	titleLayout.setButtonText(btnIndex, name);
    }
    
    public void setTitleBtnEnabled(int layoutResID, boolean enabled) {
    	if (titleLayout == null) return;
    	titleLayout.setEnabledButton(layoutResID, enabled);
    }
}