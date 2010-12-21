package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsStockLayout extends InputExtendLayout {
	private AssetsStockItem mStock;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_stock, true);
    	
    	updateChildView();
    	
    	//달력을 이용한 날짜 입력을 위해
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssetsStock);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
        setDateBtnClickListener(R.id.BtnStocktDate);
        setAmountBtnClickListener(R.id.BtnStockPrice);
    }

	@Override
	protected void updateRepeat(int type, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateDate() {
		updateBtnDateText(R.id.BtnStocktDate);
	}
	

	@Override
	protected void createItemInstance() {
		mStock = new AssetsStockItem();
		setItem(mStock);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mStock = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mStock == null) return false;
		setItem(mStock);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
	//	updateBtnAmountText(R.id.BtnStockAmount);
		updateEditMemoText(R.id.ETStockMemo);
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETStockMemo)).getText().toString();
		mStock.setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETStockTitle)).getText().toString();
    	mStock.setTitle(title);
    	
    	String count = ((TextView)findViewById(R.id.ETStockCount)).getText().toString();
    	mStock.setCount(Long.valueOf(count));
    	
    	if (InputMode.ADD_MODE == mInputMode) {
    		mStock.setTotalCount(Long.valueOf(count));
    	}
    	
    	String store = ((TextView)findViewById(R.id.ETStockStore)).getText().toString();
    	mStock.setStore(store);
    	
    	long TatalAmount = mStock.getCount() * mStock.getPrice();
    	mStock.setAmount(TatalAmount);
	}
	
    @Override
	protected void updateAmount(Long amount) {
    	mStock.setPrice(amount);
		updateBtnAmountText(R.id.BtnStockPrice);
	}
    
    @Override
    protected void updateBtnAmountText(int btnID) {
    	((Button)findViewById(btnID)).setText(String.format("%,d원", mStock.getPrice()));
    }
    
    @Override
    public boolean checkInputData() {
    	if (mStock.getPrice() == 0L) {
    		displayAlertMessage(getResources().getString(R.string.input_warning_msg_not_amount));
    		return false;
    	}
    	
    	if (mStock.getCount() == 0L) {
    		displayAlertMessage("수량이 입력되지 않았습니다. ");
    		return false;
    	}
    	
    	return super.checkInputData();
    }

    
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		if (DBMgr.addExtendAssetsStock(mStock) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mStock.getID());
    		return false;
    	}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsStockLayout.this, cls);
    		startActivity(intent);
    	}
    	else {
    		Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
    	}
    	
		return true;
	}
  
}
