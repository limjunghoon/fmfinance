package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

public class ReportCompareLiabilityLayout extends ReportBaseCompare {
	public static final int ACT_ADD_LIABLITY = MsgDef.ActRequest.ACT_ADD_LIABLITY;
	
	/** 메인 분류 아이디 -1이면 전체분류*/
	public Category mMainCategory = null;
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_compare, true);
    	
    	getData();
    	setButtonClickListener();
    	
    	updateChildView();
    }

//	private void setAddButtonListener() {
//		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(ReportCompareLiabilityLayout.this, SelectCategoryLiabilityLayout.class);
//				startActivityForResult(intent, ACT_ADD_LIABLITY);
//			}
//		});
//	}
    
    @Override
    protected void initialize() {
    	setItemType(LiabilityItem.TYPE);
    	
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
			setTitle("부체 분류");
			
			setTitleBtnText(FmBaseLayout.BTN_LEFT_01, "자산");
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
    	Intent intent = new Intent(this, ReportCompareAssetsLayout.class);
		startActivity(intent);
		
    	finish();
    }
	
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
//		Intent intent = new Intent(ReportCompareAssetsLayout.this, ReportMonthOfYearCategoryLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
//		intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, IncomeItem.TYPE);
//		startActivity(intent);
		
		if (mMainCategory == null) {
			Intent intent = new Intent(ReportCompareLiabilityLayout.this, ReportCompareLiabilityLayout.class);
			intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
			intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, LiabilityItem.TYPE);
			startActivity(intent);
		}
		else {
			Intent intent = new Intent(ReportCompareLiabilityLayout.this, ReportChangeAssets.class);
			intent.putExtra(MsgDef.ExtraNames.ITEM_ID, categoryAmount.getCategoryID());
			intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, LiabilityItem.TYPE);
			startActivity(intent);
		}
		
	}
	
	private void setButtonClickListener() {
		findViewById(R.id.BtnTotalAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ReportCompareLiabilityLayout.this, ReportChangeAssets.class);
				intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, LiabilityItem.TYPE);
				if (mMainCategory != null) {
					intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
				}
				
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
		if (requestCode == ACT_ADD_LIABLITY) {
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