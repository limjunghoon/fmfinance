package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsDepositLayout extends InputAssetsExtendLayout {
	public static final int ACT_ADD_ACCOUNT = MsgDef.ActRequest.ACT_ADD_ACCOUNT;
	public static final int ACT_EDIT_ACCOUNT = MsgDef.ActRequest.ACT_EDIT_ACCOUNT;
	
	private AssetsDepositItem mDeposit;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_deposit, true);
    	
    	updateChildView();
    	
    	setCreateDateBtnClickListener(R.id.BtnDepositCreateDate); 
        setAmountBtnClickListener(R.id.BtnDepositAmount);
        setExpiryBtnClickListener(R.id.BtnDepositExpiryDate);
        setAccountBtnClickListener();
    }
    
    @Override
    public void finish() {
    	if (mDeposit.getID() == -1 && mDeposit.getAccount().getID() != -1) {
    		DBMgr.deleteAccount(mDeposit.getAccount().getID());
    	}
    	
    	super.finish();
    }
    
    public boolean checkInputData() {
    	if (mDeposit.getCreateDate().after(Calendar.getInstance())) {
    		displayAlertMessage("개설일 다시 지정해 주세요");
    		return false;
    	}
    	
    	if (mDeposit.getCreateDate().after(mDeposit.getExpiryDate())) {
    		displayAlertMessage("만기일을 다시 지정해 주세요");
    		return false;
    	}
    	
    	return super.checkInputData();
    };

	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				monthlyCalendar.showMonthlyCalendarPopup();
				monthlyCalendar.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
					
					public void onDismiss() {
						if (monthlyCalendar.getSelectCalendar() == null) return;
						mDeposit.getExpiryDate().set(Calendar.YEAR, monthlyCalendar.getSelectCalendar().get(Calendar.YEAR));
						mDeposit.getExpiryDate().set(Calendar.MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.MONTH));
						mDeposit.getExpiryDate().set(Calendar.DAY_OF_MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.DAY_OF_MONTH));
						updateExpiryDate();
					}
				});			
			}
		 });
	}

	private void setCreateDateBtnClickListener(int resource) {
		//달력을 이용한 날짜 입력을 위해
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssetsDeposit);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
		setDateBtnClickListener(resource);
	}

	@Override
	protected void updateRepeat(int type, int value) {
		
	}

	@Override
	protected void updateDate() {
		updateBtnDateText(R.id.BtnDepositCreateDate);
	}
	
	protected void updateExpiryDate() {
		updateBtnExpiryDateText(R.id.BtnDepositExpiryDate);
	}
	
	protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mDeposit.getExpriyDateString());
    }

	@Override
	protected void createItemInstance() {
		mDeposit = new AssetsDepositItem();
		setItem(mDeposit);
	}

	@Override
	protected boolean getItemInstance(int id) {
		mDeposit = (AssetsDepositItem) DBMgr.getItem(AssetsItem.TYPE, id);
		if (mDeposit == null) return false;
		setItem(mDeposit);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateExpiryDate();
		updateBtnAmountText(R.id.BtnDepositAmount);
		updateEditMemoText(R.id.ETDepositMemo);
		updateEditTitleText(R.id.ETDepositTitle);
		updateAccountText();
		updateRateText();
	}

	private void updateRateText() {
		((EditText)findViewById(R.id.ETDepositRate)).setText(String.valueOf(mDeposit.getRate()));
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETDepositMemo)).getText().toString();
		mDeposit.setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETDepositTitle)).getText().toString();
    	mDeposit.setTitle(title);
    	
    	String rate = ((TextView)findViewById(R.id.ETDepositRate)).getText().toString();
    	mDeposit.setRate(Integer.parseInt(rate));
    	
    	mDeposit.getAccount().setBalance(mDeposit.getAmount());
	}
	
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnDepositAmount);
	}
    
    
    protected void setAccountBtnClickListener() {
    	Button btnAccount = (Button)findViewById(R.id.BtnDepositAccount);
    	btnAccount.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputAssetsDepositLayout.this, InputAccountLayout.class);
				
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_TYPE, AccountItem.TIME_DEPOSIT);
				intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mDeposit.getAccount().getID());
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ACCOUNT);
			}
		});
    }
    
    private void updateAccount(AccountItem account) {
		if (account == null){
			return;
		}
		
		mDeposit.setAccount(account);
		updateAccountText();
	}
    
    /**
	 *  계좌 이름을 갱신한다.
	 */
	private void updateAccountText() {
		if (mDeposit.getAccount().getID() == -1) {
			((Button)findViewById(R.id.BtnDepositAccount)).setText("계좌를 입력해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnDepositAccount)).setText(String.format("%s : %s", mDeposit.getAccount().getCompany().getName(), mDeposit.getAccount().getNumber()));
		}
	}
   
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ACCOUNT || requestCode == ACT_EDIT_ACCOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateAccount( getAccount(data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1)));
    		}
    	}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected AccountItem getAccount(int id) {
		AccountItem account = DBMgr.getAccountItem(id);
		if (account == null) {
			Log.e(LogTag.LAYOUT, ":: not found account item ID :" + id);
		}
		return account;
	}
    
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		if (DBMgr.addExtendAssetsDeposit(mDeposit) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mDeposit.getID());
    		return false;
    	}
		
		if (mDeposit.getAccount().getID() != -1) {
			DBMgr.updateAccount(mDeposit.getAccount());
		}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsDepositLayout.this, cls);
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
		saveUpdateItem();
	}
  
}
