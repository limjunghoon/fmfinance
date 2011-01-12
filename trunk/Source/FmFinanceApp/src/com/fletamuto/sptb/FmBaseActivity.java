package com.fletamuto.sptb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * finance���� �⺻�� �Ǵ� �� ��Ƽ��Ƽ
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
     * ������ ���̾ƿ� �信 Ÿ��Ʋ�ٸ� ǥ���Ѵ�.
     * @param layoutResID ǥ���� ���̾ƿ� ���̵�
     * @param title Ÿ��Ʋ�� ǥ�ÿ���
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
     * Ÿ��Ʋ ��ư ������ �Ѵ�.
     */
    protected  void setTitleBtn() {
		
	}
    
    protected void initialize() {
    	
    }
    
    
    
    /** ����â ��ư Ŭ���� ������ ���� */
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
	 * ���� ������ ����
	 * @param title ������ �� ����
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