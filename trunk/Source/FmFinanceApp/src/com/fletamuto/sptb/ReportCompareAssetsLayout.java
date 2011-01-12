package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.db.DBMgr;

public class ReportCompareAssetsLayout extends ReportBaseCompare {
	public static final int ACT_ADD_ASSETS = MsgDef.ActRequest.ACT_ADD_ASSETS;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_compare, true);
    	
    	setButtonClickListener();
    	getData();
    	updateChildView();
    }

	private void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ReportCompareAssetsLayout.this, SelectCategoryAssetsLayout.class);
				startActivityForResult(intent, ACT_ADD_ASSETS);
			}
		});
	}
    
    @Override
    protected void initialize() {
    	setItemType(AssetsItem.TYPE);
    	super.initialize();
    }
    
    protected void getData() {
		mFinanceItems = DBMgr.getAllItems(mType);
		mTotalAmout = DBMgr.getTotalAmount(mType);
		updateMapCategory();
	}
    
	@Override
	protected void setTitleBtn() {
		setTitle("자산");
		setAddButtonListener();
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, getResources().getString(R.string.btn_add));
		
		super.setTitleBtn();
	}
	
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
		Intent intent = new Intent(ReportCompareAssetsLayout.this, ReportAssetsExpandLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, categoryAmount.getName());
		startActivity(intent);
	}
	
	private void setButtonClickListener() {
	
	}

	protected void updateChildView() {
		TextView tvTotalAmount = (TextView)findViewById(R.id.TVTotalAmount);
		tvTotalAmount.setText(String.format("금액 : %,d", mTotalAmout));
		
		updateBarGraph();
		addButtonInLayout();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ASSETS) {
			if (resultCode == RESULT_OK) {
				getData();
		    	updateChildView();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}