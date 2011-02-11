package com.fletamuto.sptb;


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
		
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "ÆíÁý");
		setEidtButtonListener();
	    setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
	}
	
	
}