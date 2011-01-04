package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.util.Revenue;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeFromStockLayout extends InputIncomeLayout {
	private AssetsStockItem mStock = new AssetsStockItem();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        findViewById(R.id.LLCount).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.TVIncomeAmount)).setText("현재가");

    }
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	setTitle("주식 매도");
    }
  
  
    protected void initialize() {
    	super.initialize();
    	mStock.setID(getIntent().getIntExtra(MsgDef.ExtraNames.STOCK_ID, -1));
	}

    @Override
    protected void updateChildView() {
    	
    	super.updateChildView();
    }

 
    
    @Override
    protected void updateItem() {
    	super.updateItem();
    	
    	int count = Integer.parseInt(((TextView)findViewById(R.id.ETIncomeCount)).getText().toString());
    	long amount = getItem().getAmount();
    	mStock.setCreateDate(getItem().getCreateDate());
    	mStock.setAmount(amount);
    	mStock.setCount(count);
    	mStock.setPriceType(AssetsStockItem.SELL);
    	getItem().setAmount(amount * count);
    }
    
    @Override
    protected boolean saveNewItem(Class<?> cls) {
    	
    	boolean ret = super.saveNewItem(cls);
    	if (ret == true) {
    		
    		if (DBMgr.updateAssetsStock(mStock) == -1) {
    			Log.e(LogTag.LAYOUT, "::: Fail to buy stock ");
    			return false;
    		}
    	}
    	return ret;
    }
    
    @Override
    public boolean checkInputData() {
    	int stockCount = getStockCount();
    	if (stockCount < mStock.getCount()) {
    		displayAlertMessage("입력한 수량이 보유한 수량보다 많습니다. 보유한 주식수는 [" + stockCount +"]주 입니다.");
    		((TextView)findViewById(R.id.ETIncomeCount)).setText(String.valueOf(stockCount));
    		return false;
    	}
    	return super.checkInputData();
    }
    
    public int getStockCount() {
    	int count = 0;
    	ArrayList<FinanceItem> items = DBMgr.getAssetsStateItems(mStock.getID());
    	int size = items.size();
    	Calendar createDate = mStock.getCreateDate();
    	createDate.add(Calendar.DAY_OF_MONTH, 1);
    	Calendar targetDate = Calendar.getInstance();
    	targetDate.set(createDate.get(Calendar.YEAR), createDate.get(Calendar.MONTH), createDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    	
    	for (int index = 0; index < size; index++) {
    		AssetsItem assets = (AssetsItem) items.get(index);
			if (targetDate.before(assets.getCreateDate())) {
				continue;
			}
			
			if (assets.getState() == AssetsStockItem.SELL) {
				count -= assets.getCount();
			}
			else {
				count += assets.getCount();
			}
    	}
    	
    	return count;
    }
    
    
}
