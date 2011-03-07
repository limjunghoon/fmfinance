package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.fletamuto.sptb.ReportMonthOfYearCategoryLayout.MonthAmountItem;
import com.fletamuto.sptb.ReportMonthOfYearCategoryLayout.ReportMonthlyItemAdapter;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.view.FmBaseLayout;

public class ReportCompareAssetsLayout extends ReportBaseCompare {
	public static final int ACT_ADD_ASSETS = MsgDef.ActRequest.ACT_ADD_ASSETS;
	
	/** 메인 분류 아이디 -1이면 전체분류*/
	public Category mMainCategory = null;
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_compare, true);
    	
    	setButtonClickListener();
    	getData();
    	updateChildView();
    }

//	private void setAddButtonListener() {
//		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(ReportCompareAssetsLayout.this, SelectCategoryAssetsLayout.class);
//				startActivityForResult(intent, ACT_ADD_ASSETS);
//			}
//		});
//	}
    
    @Override
    protected void initialize() {
    	setItemType(AssetsItem.TYPE);
    	
    	int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
    	if (categoryID != -1) {
    		mMainCategory = DBMgr.getCategoryFromID(mType, categoryID);
    	}
    	
    	super.initialize();
    }
    
    protected void getData() {
    	if (mMainCategory == null) {
    		mFinanceItems = DBMgr.getAllItems(mType);
    		mTotalAmout = DBMgr.getTotalAmount(mType);
    	}
    	else {
    		mFinanceItems = DBMgr.getItemsFromCategoryID(mType, mMainCategory.getID());
    		mTotalAmout = DBMgr.getTotalMainCategoryAmount(mType, mMainCategory.getID());
    	}
		
		updateMapCategory();
	}
    
	@Override
	protected void setTitleBtn() {
		if (mMainCategory == null) {
			setTitle("자산 분류");
			
			setTitleBtnText(FmBaseLayout.BTN_LEFT_01, "부채");
			setTitleBtnVisibility(FmBaseLayout.BTN_LEFT_01, View.VISIBLE);
		}
		else {
			setTitle(mMainCategory.getName() + " 분류");
		}
		
//		setAddButtonListener();
//		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
//		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, getResources().getString(R.string.btn_add));
		
		super.setTitleBtn();
	}
	
	@Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(this, ReportCompareLiabilityLayout.class);
		startActivity(intent);
		
    	finish();
    }
	
	
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
//		Intent intent = new Intent(ReportCompareAssetsLayout.this, ReportMonthOfYearCategoryLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
//		intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, IncomeItem.TYPE);
//		startActivity(intent);
		
		if (mMainCategory == null) {
			Intent intent = new Intent(ReportCompareAssetsLayout.this, ReportCompareAssetsLayout.class);
			intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
			startActivity(intent);
		}
		
	}
	
	private void setButtonClickListener() {
		findViewById(R.id.BtnTotalAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ReportCompareAssetsLayout.this, ReportChangeAssets.class);
				startActivity(intent);
			}
		});
	}

	protected void updateChildView() {
		Button tvTotalAmount = (Button)findViewById(R.id.BtnTotalAmount);
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
	
	@Override
	public void updateMapCategory() {
		
		if (mMainCategory == null) {
			super.updateMapCategory();
		}
		else {
			mCategoryAmount.clear();
			
			int itemSize = mFinanceItems.size();
			
			for (int index = 0; index < itemSize; index++) {
				FinanceItem item = mFinanceItems.get(index);
				CategoryAmount categoryAmount = new CategoryAmount(item.getType());
				categoryAmount.set(item.getID(), item.getTitle(), item.getAmount());
				mCategoryAmount.put(item.getID(), categoryAmount);
			}
		}
		
	}
	
	
}