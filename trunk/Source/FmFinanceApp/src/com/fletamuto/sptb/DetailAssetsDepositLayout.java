package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class DetailAssetsDepositLayout extends DetailBaseLayout {  	
	private AssetsDepositItem mDeposit;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_assets_deposit, true);
        
        findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
        
        setButtonListener();
        updateChildView();
//        findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
//        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.INVISIBLE);
//        
//        updateDeleteBtnText();
//        setPorgress();
    }

	public void setButtonListener() {
		findViewById(R.id.BtnStateDelete).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onDeleteBtnClick();
			}
		});
	}

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVDepositName);
		tvName.setText(mDeposit.getTitle());
		TextView tvAmount = (TextView)findViewById(R.id.TVDepositAmount);
		tvAmount.setText(String.format("%,d원", mDeposit.getAmount()));
		TextView tvNumber = (TextView)findViewById(R.id.TVDepositNumber);
		tvNumber.setText(mDeposit.getAccount().getNumber());
		TextView tvInstitution = (TextView)findViewById(R.id.TVDepositInstitution);
		tvInstitution.setText(mDeposit.getAccount().getCompany().getName());
		TextView tvCreateDate = (TextView)findViewById(R.id.TVDepositCreateDate);
		tvCreateDate.setText(mDeposit.getCreateDateString());
		TextView tvEndDate = (TextView)findViewById(R.id.TVDepositEndDate);
		tvEndDate.setText(mDeposit.getExpriyDateString());
		TextView tvRate = (TextView)findViewById(R.id.TVDepositRateDate);
		tvRate.setText(String.format("%d%%", mDeposit.getRate()));
		TextView tvExpect = (TextView)findViewById(R.id.TVDepositExpectAmount);
		tvExpect.setText(String.format("%,d원", mDeposit.getExpectAmount()));
		TextView tvMemo = (TextView)findViewById(R.id.TVDepositMemo);
		tvMemo.setText(mDeposit.getMemo());
		setPorgress();
		updateDeleteBtnText();
	}
	
	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("정기예금 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mDeposit = (AssetsDepositItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
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

	protected void updateDeleteBtnText() {
		if (mDeposit.isOverExpirationDate()) {
			((Button)findViewById(R.id.BtnStateDelete)).setText("해약");
		}
		else {
			((Button)findViewById(R.id.BtnStateDelete)).setText("만기");
		}
		
	}

	@Override
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputAssetsDepositLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mDeposit.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
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
			if (category.getName().compareTo(mDeposit.getCategory().getName()) == 0) {
				category.setExtndType(ItemDef.ExtendAssets.DEPOSIT);
				income.setCategory(category);
				break;
			}
		}
		
		income.setAmount(mDeposit.getTotalAmount());
//		income.setCount(getItem().getCount());
//		income.setCreateDate(getItem().getCreateDate());
		
		return income;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
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

}