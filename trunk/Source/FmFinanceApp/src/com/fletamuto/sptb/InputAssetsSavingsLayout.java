package com.fletamuto.sptb;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsSavingsLayout extends InputExtendLayout {
	public static final int ACT_ADD_ACCOUNT = MsgDef.ActRequest.ACT_ADD_ACCOUNT;
	private AssetsSavingsItem mSavings;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_savings, true);
    	
    	updateChildView();
    	
    	//달력을 이용한 날짜 입력을 위해
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssetsSavings);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
        setCreateDateBtnClickListener(R.id.BtnSavingsCreateDate); 
        setAmountBtnClickListener(R.id.BtnSavingsAmount);
        setExpiryBtnClickListener(R.id.BtnSavingsExpiryDate);
        setAccountBtnClickListener();
        
    }
    
    DatePickerDialog.OnDateSetListener expiryDlg = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mSavings.getExpiryDate().set(Calendar.YEAR, year);
			mSavings.getExpiryDate().set(Calendar.MONTH, monthOfYear);
			mSavings.getExpiryDate().set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};
	

	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(InputAssetsSavingsLayout.this, expiryDlg, 
						mSavings.getExpiryDate().get(Calendar.YEAR),
						mSavings.getExpiryDate().get(Calendar.MONTH), 
						mSavings.getExpiryDate().get(Calendar.DAY_OF_MONTH)).show(); 				
			}
		 });
	}

	private void setCreateDateBtnClickListener(int resource) {
		setDateBtnClickListener(resource);
	}

	@Override
	protected void updateRepeat(int type, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateDate() {
		updateBtnDateText(R.id.BtnSavingsCreateDate);
	}
	
	protected void updateExpiryDate() {
		updateBtnExpiryDateText(R.id.BtnSavingsExpiryDate);
	}
	
	protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mSavings.getExpriyDateString());
    }

	@Override
	protected void createItemInstance() {
		mSavings = new AssetsSavingsItem();
		setItem(mSavings);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mSavings == null) return false;
		setItem(mSavings);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateExpiryDate();
		updateBtnAmountText(R.id.BtnSavingsAmount);
		updateEditMemoText(R.id.ETSavingsMemo);
		updateAccountText();
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETSavingsMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETSavingsTitle)).getText().toString();
    	getItem().setTitle(title);
	}
	
    @Override
	protected void updateAmount(Long amount) {
    	mSavings.setPayment(amount);
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnSavingsAmount);
	}
    
    
    protected void setAccountBtnClickListener() {
    	Button btnAccount = (Button)findViewById(R.id.BtnSavingsAccount);
    	btnAccount.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputAssetsSavingsLayout.this, InputAccountLayout.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ACCOUNT);
			}
		});
    }
    
    private void updateAccount(AccountItem account) {
		if (account == null){
			return;
		}
		
		mSavings.setAccount(account);
		updateAccountText();
	}
    
    /**
	 *  계좌 이름을 갱신한다.
	 */
	private void updateAccountText() {
		if (mSavings.getAccount().getID() == -1) {
			((Button)findViewById(R.id.BtnSavingsAccount)).setText("계좌를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnSavingsAccount)).setText(String.format("%s : %s", mSavings.getAccount().getCompany().getName(), mSavings.getAccount().getNumber()));
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
		if (DBMgr.addExtendAssetsSavings(mSavings) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mSavings.getID());
    		return false;
    	}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsSavingsLayout.this, cls);
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
