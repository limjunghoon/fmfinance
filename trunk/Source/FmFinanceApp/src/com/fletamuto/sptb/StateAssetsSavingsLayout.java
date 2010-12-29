package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class StateAssetsSavingsLayout extends StateDefaultLayout {  	
	private AssetsSavingsItem mSavings;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
        updateDeleteBtnText();
        setPorgress();
    }


	@Override
	protected void initialize() {
		super.initialize();
		
		setGraphHeigth(250);
		getData();
	}
	
	@Override
	protected void updateChildView() {
		TextView tvAmount = (TextView)findViewById(R.id.TVStateTitle);
		tvAmount.setText(String.format("�ݾ� : %,d��   ���� : %d%%", mSavings.getAmount(), mSavings.getRate()));
		
		TextView tvYear = (TextView) findViewById(R.id.TVCurrentYear);
		tvYear.setText(String.format("%d��", mYear));
		
		updateLineView();
	}

	@Override
	protected void onClickHistoryBtn() {
		Intent intent = new Intent(StateAssetsSavingsLayout.this, ReportAssetsHistoryLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, mItem);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_STATE_HISTORY);
	}

	@Override
	protected void startChangeStateActivtiy() {
		Intent intent = new Intent(this, getActivityClass());
//		intent.putExtra(MsgDef.ExtraNames.INPUT_CHANGE_MODE, true);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mItem.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}

	@Override
	protected void getData() {
		mAmountMonthInYear = DBMgr.getTotalAssetAmountMonthInYear(mItem.getID(), mYear);
		mSavings = (AssetsSavingsItem)getItem();
	}
	
	private void setPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
		int monthPeriodTerm = mSavings.getMonthPeriodTerm();
		int monthProgressCount = mSavings.getMonthProcessCount() ;
		
		progress.setMax(monthPeriodTerm);
		progress.setProgress(monthProgressCount);
		
		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
		tvProgrss.setText(String.format("����(%d/%d)", mSavings.getMonthProcessCount(), mSavings.getMonthPeriodTerm()));
		tvProgrss.invalidate();
	}

	@Override
	protected Class<?> getActivityClass() {
		return InputAssetsDepositLayout.class;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_EDIT_ITEM) {
    		if (resultCode == RESULT_OK) {
    			int id = data.getIntExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, -1);
    			if (id != -1) {
    				mSavings = (AssetsSavingsItem) DBMgr.getItem(AssetsItem.TYPE, id);
    				updateChildView();
    				setPorgress();
    			}
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
			if (resultCode == RESULT_OK) {
				completionAssets(data.getIntExtra(MsgDef.ExtraNames.ADD_ITEM_ID, -1));
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void completionAssets(int incomeID) {
		mSavings.setState(FinanceItem.STATE_COMPLEATE);
		DBMgr.updateFinanceItem(mSavings);
		DBMgr.addIncomeFromAssets(incomeID, mSavings.getID());

		finish();
	}

	@Override
	protected void onDeleteBtnClick() {
		Intent intent = new Intent(this, InputIncomeFromAssetsLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, createIncomeItem());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ITEM);
	}
	
	protected IncomeItem createIncomeItem() {
		IncomeItem income = new IncomeItem();
		
		ArrayList<Category> categories = DBMgr.getCategory(IncomeItem.TYPE, ItemDef.ExtendAssets.NONE);
		int categorySize = categories.size();
		
		for (int index = 0; index < categorySize; index++) {
			Category category = categories.get(index); 
			if (category.getName().compareTo(getItem().getCategory().getName()) == 0) {
				category.setExtndType(ItemDef.ExtendAssets.DEPOSIT);
				income.setCategory(category);
				break;
			}
		}
		
		income.setAmount(getItem().getTotalAmount());
//		income.setCount(getItem().getCount());
//		income.setCreateDate(getItem().getCreateDate());
		
		return income;
	}
	
	protected void updateDeleteBtnText() {
		if (mSavings.isOverExpirationDate()) {
			((Button)findViewById(R.id.BtnStateDelete)).setText("�ؾ�");
		}
		else {
			((Button)findViewById(R.id.BtnStateDelete)).setText("����");
		}
		
	}
}