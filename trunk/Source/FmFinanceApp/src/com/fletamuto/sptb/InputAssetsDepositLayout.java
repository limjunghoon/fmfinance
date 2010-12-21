package com.fletamuto.sptb;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsDepositLayout extends InputExtendLayout {
	public static final int ACT_ADD_ACCOUNT = MsgDef.ActRequest.ACT_ADD_ACCOUNT;
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
    
    DatePickerDialog.OnDateSetListener expiryDlg = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mDeposit.getExpiryDate().set(Calendar.YEAR, year);
			mDeposit.getExpiryDate().set(Calendar.MONTH, monthOfYear);
			mDeposit.getExpiryDate().set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};
	

	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(InputAssetsDepositLayout.this, expiryDlg, 
						mDeposit.getExpiryDate().get(Calendar.YEAR),
						mDeposit.getExpiryDate().get(Calendar.MONTH), 
						mDeposit.getExpiryDate().get(Calendar.DAY_OF_MONTH)).show(); 				
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
		// TODO Auto-generated method stub
		
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
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
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
			((Button)findViewById(R.id.BtnDepositAccount)).setText("계좌를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnDepositAccount)).setText(String.format("%s : %s", mDeposit.getAccount().getCompany().getName(), mDeposit.getAccount().getNumber()));
		}
	}
   
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ACCOUNT) {
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
  
}
