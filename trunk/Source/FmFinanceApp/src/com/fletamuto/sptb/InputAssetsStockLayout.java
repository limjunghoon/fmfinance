package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsStockLayout extends InputAssetsExtendLayout {
	private AssetsStockItem mStock;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_stock, true);
    	
    	updateChildView();
    	
    	//달력을 이용한 날짜 입력을 위해
/*
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssetsStock);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
*/
        
        
        
        findViewById(R.id.ETStockTitle).setEnabled((mInputMode == InputMode.STATE_CHANGE_MODE) ? false :true);
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnStocktDate);
        setAmountBtnClickListener(R.id.BtnStockPrice);
	}
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	if (mInputMode == InputMode.STATE_CHANGE_MODE) {
    		setTitle("주식 추가매수");
    	}
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
		mStock = (AssetsStockItem) DBMgr.getItem(AssetsItem.TYPE, id);
		if (mInputMode == InputMode.STATE_CHANGE_MODE) {
			mStock.setCount(0);
			mStock.setCreateDate(Calendar.getInstance());
		}
		if (mStock == null) return false;
		setItem(mStock);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnAmountText(R.id.BtnStockPrice);
		updateEditMemoText(R.id.ETStockMemo);
		updateEditTitleText(R.id.ETStockTitle);
		updateEditStoreText();
	}

	protected void updateEditStoreText() {
		((TextView)findViewById(R.id.ETStockStore)).setText(mStock.getStore());
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETStockMemo)).getText().toString();
		mStock.setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETStockTitle)).getText().toString();
    	mStock.setTitle(title);
    	
    	String count = ((TextView)findViewById(R.id.ETStockCount)).getText().toString();
    	if (count.length() == 0) {
    		count = "0";
    	}
    	
		mStock.setCount(Integer.valueOf(count));
		
		if (InputMode.ADD_MODE == mInputMode) {
    		mStock.setTotalCount(Long.valueOf(count));
    	}
    	
    	String store = ((TextView)findViewById(R.id.ETStockStore)).getText().toString();
    	mStock.setStore(store);
    	
    	mStock.setAmount(mStock.getPrice());
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
    	if (mStock.getTitle().length() == 0) {
    		displayAlertMessage("종목이 입력되지 않았습니다.");
    		return false;
    	}
    	
    	if (mStock.getPrice() == 0L) {
    		displayAlertMessage(getResources().getString(R.string.input_warning_msg_not_amount));
    		return false;
    	}
    	
    	if (mStock.getCount() == 0) {
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

  
	@Override
	protected void saveUpdateStateItem() {
		if (DBMgr.updateAssetsStock(mStock) == -1) {
			Log.e(LogTag.LAYOUT, "::: Fail to buy stock ");
		}
//		saveUpdateItem();
		// 자산에 현재가 변경
		// 히스토리에 신규 자산 추가
		// 지출 내역 적용
	}
}
