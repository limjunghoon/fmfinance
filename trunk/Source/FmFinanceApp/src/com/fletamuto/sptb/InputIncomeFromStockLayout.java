package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.util.Revenue;

/**
 * ������ �Է� �Ǵ� �����ϴ� ȭ���� �����Ѵ�.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeFromStockLayout extends InputIncomeLayout {
	private AssetsStockItem mStock = new AssetsStockItem();
	private long mStockTotalCount = 1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        findViewById(R.id.LLCount).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.TVIncomeAmount)).setText("���簡");

    }
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	setTitle("�ֽ� �ŵ�");
    }
  
  
    protected void initialize() {
    	super.initialize();
    	mStock.setID(getIntent().getIntExtra(MsgDef.ExtraNames.STOCK_ID, -1));
    	mStockTotalCount = getIntent().getLongExtra(MsgDef.ExtraNames.STOCK_TOTAL_COUNT, 1);
	}

    @Override
    protected void updateChildView() {
    	
    	super.updateChildView();
    }

    
    protected void updateAmount(Long amount) {
    	super.updateAmount(amount);
    	
    }
    
    @Override
    protected void updateItem() {
    	super.updateItem();
    	
    	int count = Integer.parseInt(((TextView)findViewById(R.id.ETIncomeCount)).getText().toString());
    	long amount = getItem().getAmount();
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
    	if (mStockTotalCount < mStock.getCount()) {
    		displayAlertMessage("�Է��� ������ ������ �������� �����ϴ�. ������ �ֽļ��� [" + mStockTotalCount +"]�� �Դϴ�.");
    		((TextView)findViewById(R.id.ETIncomeCount)).setText(String.valueOf(mStockTotalCount));
    		return false;
    	}
    	return super.checkInputData();
    }
}
