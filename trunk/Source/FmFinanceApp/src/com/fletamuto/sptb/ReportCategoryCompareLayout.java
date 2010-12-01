package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.LiabilityItem;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class ReportCategoryCompareLayout extends FmBaseActivity { 
	private int mItemType = -1;
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_categoyr_compare, true);
       
        setBtnClickListener();
    }

	private void setBtnClickListener() {
		findViewById(R.id.BtnCompareIncome).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mItemType = IncomeItem.TYPE;
				startSelectCategoryActivety(SelectCategoryIncomeLayout.class);
			}
		});
		
		findViewById(R.id.BtnCompearExpense).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mItemType = ExpenseItem.TYPE;
				startSelectCategoryActivety(SelectCategoryExpenseLayout.class);
			}
		});

		findViewById(R.id.BtnCompareAssets).setOnClickListener(new View.OnClickListener() {
	
		public void onClick(View v) {
				mItemType = AssetsItem.TYPE;
				startSelectCategoryActivety(SelectCategoryAssetsLayout.class);
			}
		});
		
		findViewById(R.id.BtnCompareLiability).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mItemType = LiabilityItem.TYPE;
				startSelectCategoryActivety(SelectCategoryLiabilityLayout.class);
			}
		});
	}
	
	protected void startSelectCategoryActivety(Class<?> calss) {
		Intent intent = new Intent(ReportCategoryCompareLayout.this, calss);
		intent.putExtra(MsgDef.ExtraNames.SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY, false);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_CATEGORY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_CATEGORY) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(ReportCategoryCompareLayout.this, ReportCategoryCompareBaseLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, mItemType);
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, data.getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1));
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, data.getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME));
				
				startActivity(intent);
    			return;
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}