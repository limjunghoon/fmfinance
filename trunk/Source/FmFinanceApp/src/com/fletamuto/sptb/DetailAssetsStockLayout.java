package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AssetsChangeItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class DetailAssetsStockLayout extends DetailBaseLayout {  	
	private AssetsStockItem mStock;
	protected ArrayList<AssetsChangeItem> mListItems = new ArrayList<AssetsChangeItem>();
//	protected ReportSavingsPaymentItemAdapter mSavingsPaymentItemAdapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_assets_stock, true);
        
        //findViewById(R.id.LLPrograss).setVisibility(View.VISIBLE);
        
        setButtonListener();
        updateChildView();
    }

	public void setButtonListener() {
		findViewById(R.id.BtnStockBuy).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				addBuyStateActivtiy();
			}
		});
		
		findViewById(R.id.BtnStockSell).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				addSellStateActivtiy();
			}
		});
		
		final ToggleButton tbPayment = (ToggleButton) findViewById(R.id.TBStockPeruse);
		tbPayment.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				findViewById(R.id.LLStockPayment).setVisibility((tbPayment.isChecked()) ? View.VISIBLE : View.GONE);
			}
		});
	}
	
	protected void addBuyStateActivtiy() {
		Intent intent = new Intent(this, InputAssetsStockLayout.class);
		intent.putExtra(MsgDef.ExtraNames.INPUT_CHANGE_MODE, true);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mStock.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	private void addSellStateActivtiy() {
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
			if (category.getName().compareTo(mStock.getCategory().getName()) == 0) {
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

	public void updateChildView() {
		TextView tvName = (TextView)findViewById(R.id.TVStockName);
		tvName.setText(mStock.getTitle());
		TextView tvTotalAmount = (TextView)findViewById(R.id.TVStockTotalAmount);
		tvTotalAmount.setText(String.format("%,d원", mStock.getTotalAmount()));
		TextView tvPeresentAmount = (TextView)findViewById(R.id.TVStockAmount);
		tvPeresentAmount.setText(String.format("%,d원", mStock.getPeresentPrice()));
		TextView tvCount = (TextView)findViewById(R.id.TVStockCount);
		tvCount.setText(String.format("%d주", mStock.getTotalCount()));
		
		TextView tvMemo = (TextView)findViewById(R.id.TVStockMemo);
		tvMemo.setText(mStock.getMemo());
		
		addSavingsPaymentList();
	}
	
	@Override
  	protected void setTitleBtn() {
		super.setTitleBtn();
		
		setTitle("주식 내역");
  	}

	@Override
	protected void initialize() {
		super.initialize();
		
		mStock = (AssetsStockItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		mListItems = DBMgr.getAssetsChangeStateItems(mStock.getID());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_EDIT_ITEM) {
    		if (resultCode == RESULT_OK) {
    			int id = data.getIntExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, -1);
    			if (id != -1) {
    				mStock = (AssetsStockItem) DBMgr.getItem(AssetsItem.TYPE, id);
    				mListItems = DBMgr.getAssetsChangeStateItems(mStock.getID());
    				updateChildView();
    			}
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_ADD_ITEM) {
			if (resultCode == RESULT_OK) {

				mStock = (AssetsStockItem) DBMgr.getItem(mStock.getType(), mStock.getID());
				mListItems = DBMgr.getAssetsChangeStateItems(mStock.getID());
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
	public void onEditBtnClick() {
		Intent intent = new Intent(this, InputAssetsStockLayout.class);
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mStock.getID());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}

	protected void addSavingsPaymentList() {
		LinearLayout llPayment = (LinearLayout) findViewById(R.id.LLStockPayment);
		llPayment.removeAllViewsInLayout();
		
		int size = mListItems.size();
		for (int index = 0; index < size; index++) {
			AssetsChangeItem item = mListItems.get(index);
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.report_detail_stock_payment, null);
			((TextView)llMember.findViewById(R.id.TVPaymentDate)).setText(item.getChangeDateString());
			((TextView)llMember.findViewById(R.id.TVPaymentStore)).setText(item.getStore());
			
			TextView tvPaymentType = ((TextView)llMember.findViewById(R.id.TVPaymentType));
			if (item.getPriceType() == AssetsItem.BUY) {
				tvPaymentType.setText("매수");
				tvPaymentType.setTextColor(Color.BLUE);
			}
			else {
				tvPaymentType.setText("매도");
				tvPaymentType.setTextColor(Color.RED);
			}
			
			((TextView)llMember.findViewById(R.id.TVPaymentCount)).setText(String.format("%d주", item.getCount()));
			((TextView)llMember.findViewById(R.id.TVPaymentAmount)).setText(String.format("%,d원", item.getChangeAmount()));
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			llPayment.addView(llMember, params);
		}
	}
}