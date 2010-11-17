package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fletamuto.sptb.ReportBaseCompare.CategoryAmount;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportCompareLiabilityLayout extends ReportBaseCompare {

	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_compare, true);
    	
    	getData();
    	setButtonClickListener();
    	
    	updateChildView();
    }

	private void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ReportCompareLiabilityLayout.this, InputLiabilityLayout.class);
				startActivity(intent);
			}
		});
	}
    
    @Override
    protected void initialize() {
    	setItemType(LiabilityItem.TYPE);
    	super.initialize();
    }
    
    protected void getData() {
		mFinanceItems = DBMgr.getAllItems(mType);
		mTotalAmout = DBMgr.getTotalAmount(mType);
		updateMapCategory();
	}
    
	@Override
	protected void setTitleBtn() {
		setTitle("∫Œ√º");
		setAddButtonListener();
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, getResources().getString(R.string.btn_add));
		
		super.setTitleBtn();
	}


	
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
		Intent intent = new Intent(ReportCompareLiabilityLayout.this, ReportLiabilityLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
		startActivity(intent);
	}
	
	private void setButtonClickListener() {
	
	}

	private void updateChildView() {
		updateBarGraph();
		addButtonInLayout();
		
	}
	


}