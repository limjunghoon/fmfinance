package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class StateAssetsDepositLayout extends StateDefaultLayout {  	
	private AssetsDepositItem mDeposit;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.INVISIBLE);
        
        updateDeleteBtnText();
        
        setPorgress();
    }


	@Override
	protected void initialize() {
		super.initialize();
		
		getData();
	}
	
	@Override
	protected void updateChildView() {
		TextView tvAmount = (TextView)findViewById(R.id.TVStateTitle);
		tvAmount.setText(String.format("금액 : %,d원   이율 : %d%%", mDeposit.getAmount(), mDeposit.getRate()));
		
		TextView tvYear = (TextView) findViewById(R.id.TVCurrentYear);
		tvYear.setText(String.format("%d년", mYear));
		
		updateLineView();
	}

	@Override
	protected void onClickHistoryBtn() {
		Intent intent = new Intent(StateAssetsDepositLayout.this, ReportAssetsHistoryLayout.class);
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
		mDeposit = (AssetsDepositItem)getItem();
	}
	
	private void setPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.PBState);
		int monthPeriodTerm = mDeposit.getMonthPeriodTerm();
		int monthProgressCount = mDeposit.getMonthProcessCount() ;
		
		progress.setMax(monthPeriodTerm);
		progress.setProgress(monthProgressCount);
		
		TextView tvProgrss = (TextView) findViewById(R.id.TVStatePrograss);
		tvProgrss.setText(String.format("진행(%d/%d)", mDeposit.getMonthProcessCount(), mDeposit.getMonthPeriodTerm()));
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
    				mDeposit = (AssetsDepositItem) DBMgr.getItem(AssetsItem.TYPE, id);
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
		mDeposit.setState(FinanceItem.STATE_COMPLEATE);
		DBMgr.updateFinanceItem(mDeposit);
		DBMgr.addIncomeFromAssets(incomeID, mDeposit.getID());
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
		if (mDeposit.isOverExpirationDate()) {
			((Button)findViewById(R.id.BtnStateDelete)).setText("해약");
		}
		else {
			((Button)findViewById(R.id.BtnStateDelete)).setText("만기");
		}
		
	}
}