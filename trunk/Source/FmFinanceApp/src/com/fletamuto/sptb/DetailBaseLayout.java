package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.view.FmBaseLayout;

public abstract class DetailBaseLayout extends FmBaseActivity {
	protected static final int LAST_DAY_OF_MONTH = ItemDef.LAST_DAY_OF_MONTH;
	public static final int STATE_CREATE = 0;
	public static final int STATE_EDIT = 1;
	public static final int STATE_EXPENSE = 2;
	public static final int STATE_INCOME = 3;
	public static final int STATE_TRANSFOR_WITHDRAWAL = 4;
	public static final int STATE_TRANSFOR_DEPOSIT = 5;
	public static final int STATE_SETTLEMENT = 6;
	
	public abstract void onEditBtnClick();
	public abstract void updateChildView();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
  //      setContentView(R.layout.report_history, true);
    }
    
	public void setEidtButtonListener() {
		setTitleButtonListener(FmBaseLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onEditBtnClick();
				
			}
		});
	}
  
	@Override
	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "����");
		setEidtButtonListener();
	    setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_EDIT_ITEM) {
			if (resultCode == RESULT_OK) {
				updateChildView();
			}
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}