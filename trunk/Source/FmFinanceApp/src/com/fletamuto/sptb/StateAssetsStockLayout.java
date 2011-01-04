package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class StateAssetsStockLayout extends StateDefaultLayout {  	
	private AssetsStockItem mStock;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        updateDeleteBtnText();
        updateBuyBtnText();
    }

	@Override
	protected void initialize() {
		super.initialize();
		
		setGraphHeigth(260);
		getData();
	}
	
	@Override
	protected void updateChildView() {
		TextView tvAmount = (TextView)findViewById(R.id.TVStateTitle);
		tvAmount.setText(String.format("현재가:%,d원 수량:%d 총가격:%,d원", mStock.getPeresentPrice(), mStock.getTotalCount(), mStock.getAmount()));
		//tvAmount.setTextSize(10);
		
		TextView tvYear = (TextView) findViewById(R.id.TVCurrentYear);
		tvYear.setText(String.format("%d년", mYear));
		
		updateLineView();
	}

	@Override
	protected void onClickHistoryBtn() {
		Intent intent = new Intent(StateAssetsStockLayout.this, ReportAssetsHistoryLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, mItem);
		startActivityForResult(intent, MsgDef.ActRequest.ACT_STATE_HISTORY);
	}

	@Override
	protected void startChangeStateActivtiy() {
		addBuyStateActivtiy();

	}

	protected void addBuyStateActivtiy() {
		Intent intent = new Intent(this, getActivityClass());
		intent.putExtra(MsgDef.ExtraNames.INPUT_CHANGE_MODE, true);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mItem.getID());
		
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}

	@Override
	protected void getData() {
		mStock = (AssetsStockItem)getItem();
		mAmountMonthInYear = new ArrayList<Long>();
		
		ArrayList<FinanceItem> items = DBMgr.getAssetsStateItems(mItem.getID());
		
		int size = items.size();
		long count = 0L;
		int targetIndex = 0;
		
		Calendar calendar = Calendar.getInstance();	
		calendar.set(mYear, 0, 1, 0 ,0 ,0);
		
		for (int month = 0; month < 12; month++) {
			calendar.add(Calendar.MONTH, 1);
			for (int index = targetIndex; index < size; index++) {
				AssetsItem assets = (AssetsItem) items.get(index);
				if (calendar.before(assets.getCreateDate())) {
					continue;
				}
				
				if (assets.getState() == AssetsStockItem.SELL) {
					count -= assets.getCount();
				}
				else {
					count += assets.getCount();
				}
				targetIndex++;
			}
			mAmountMonthInYear.add(count);
		}
	}
	

	@Override
	protected Class<?> getActivityClass() {
		return InputAssetsStockLayout.class;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_EDIT_ITEM) {
    		if (resultCode == RESULT_OK) {
    			int id = data.getIntExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, -1);
    			if (id != -1) {
    				mStock = (AssetsStockItem) DBMgr.getItem(AssetsItem.TYPE, id);
    				updateChildView();
    			}
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
			if (resultCode == RESULT_OK) {

				mStock = (AssetsStockItem) DBMgr.getItem(mStock.getType(), mStock.getID());
				updateChildView();
				
				if (mStock.getTotalCount() <= 0L) {
					completionAssets(data.getIntExtra(MsgDef.ExtraNames.ADD_ITEM_ID, -1));
				}
//				
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void completionAssets(int incomeID) {
		mStock.setState(FinanceItem.STATE_COMPLEATE);
		DBMgr.updateFinanceItem(mStock);
		DBMgr.addIncomeFromAssets(incomeID, mStock.getID());
		finish();
	}

	@Override
	protected void onDeleteBtnClick() {
		Intent intent = new Intent(this, InputIncomeFromStockLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM, createIncomeItem());
		intent.putExtra(MsgDef.ExtraNames.STOCK_ID, mStock.getID());
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
		
//		income.setAmount(getItem().getTotalAmount());
		income.setAmount(mStock.getPeresentPrice());
//		income.setCount(getItem().getCount());
//		income.setCreateDate(getItem().getCreateDate());
		
		return income;
	}
	
	protected void updateDeleteBtnText() {
		((Button)findViewById(R.id.BtnStateDelete)).setText("매도");
	}
	
	protected void updateBuyBtnText() {
		((Button)findViewById(R.id.BtnStateEdit)).setText("매수");
	}
	
	
}