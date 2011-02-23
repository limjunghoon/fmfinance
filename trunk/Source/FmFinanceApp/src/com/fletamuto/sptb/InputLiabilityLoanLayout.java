package com.fletamuto.sptb;


import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputLiabilityLoanLayout extends InputLiabilityExtendLayout {
	public final static int ACT_COMPANY_SELECT = MsgDef.ActRequest.ACT_COMPANY_SELECT;
	
	private LiabilityLoanItem mLoan;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_liability_loan, true);
        updateChildView();
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnLoanDate); 
        setAmountBtnClickListener(R.id.BtnLoanAmount);
        setExpiryBtnClickListener(R.id.BtnLoanExpiryDate);
        setPaymentBtnClickListener(R.id.BtnLoanPaymentDate);
        setAccountBtnClickListener(R.id.BtnLoanAccount);
        setSelectCompanyBtnClickListener();
	}
    
    protected void setAccountBtnClickListener(int btnID) {
    	Button btnAccount = (Button)findViewById(btnID);
    	btnAccount.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityLoanLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
			}
		 });
    }
    
    private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityLoanLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE_EXPIRY);
			}
		 });
	}
    
    private void setPaymentBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityLoanLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE_PAYMENT);
			}
		 });
	}
    
	protected void setSelectCompanyBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnLoanCompany);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityLoanLayout.this, SelectCompanyLayout.class);
		    	startActivityForResult(intent, ACT_COMPANY_SELECT);
			}
		});
		
	}

	@Override
	protected void createItemInstance() {
		mLoan = new LiabilityLoanItem();
		setItem(mLoan);
	}

	@Override
	protected boolean getItemInstance(int id) {
		mLoan = (LiabilityLoanItem) DBMgr.getItem(LiabilityItem.TYPE, id);
		if (mLoan == null) return false;
		setItem(mLoan);
		return true;
	}



	@Override
	protected void updateChildView() {
		updateDate();
		updateExpiryDate();
		updatePaymentDate();
		updateCompanyNameText();
		updateAccountText();
		updateBtnAmountText(R.id.BtnLoanAmount);
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETLoanMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETLoanTitle)).getText().toString();
    	getItem().setTitle(title);
	}
	
    protected void updateExpiryDate() {
    	updateBtnExpiryDateText(R.id.BtnLoanExpiryDate);
    }
    
    protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mLoan.getExpiryDateString());
    }
    
    protected void updatePaymentDate() {
    	updateBtnPaymentDateText(R.id.BtnLoanPaymentDate);
    }
    
    protected void updateBtnPaymentDateText(int btnID) {
    	if (mLoan.getPaymentDate() == null) {
    		((Button)findViewById(btnID)).setText("납입일을 선택해 주세요");
    	}
    	else {
    		((Button)findViewById(btnID)).setText(mLoan.getPaymentDateString());
    	}
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnLoanDate);
    }
    
    @Override
    public boolean checkInputData() {
    	if (mLoan.getPaymentDate() == null) {
    		displayAlertMessage("납입일이 설정되지 않았습니다.");
    		return false;
    	}
    	
    	if (mLoan.getCompany().getID() == -1) {
    		displayAlertMessage("회사가 설정되지 않았습니다.");
    		return false;
    	}
    	
    	if (mLoan.getCreateDate().after(mLoan.getExpiryDate())) {
    		displayAlertMessage("만기일을 다시 지정해 주세요");
    		return false;
    	}
    	
    	if (mLoan.getCreateDate().after(mLoan.getPaymentDate())) {
    		displayAlertMessage("납입일을 다시 지정해 주세요");
    		return false;
    	}
    	
    	if (mLoan.getAccount().getID() == -1) {
    		displayAlertMessage("계좌가 설정되지 않았습니다.");
    		return false;
    	}
    	
    	return super.checkInputData();
    }
    
    @Override
    protected void updateAmount(Long amount) {
    	super.updateAmount(amount);
    	updateBtnAmountText(R.id.BtnLoanAmount);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_COMPANY_SELECT) {
    		if (resultCode == RESULT_OK) {
    			updateCompany( getCompany(data.getIntExtra(MsgDef.ExtraNames.COMPANY_ID, -1)));
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_SELECT_DATE_EXPIRY) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");

    			mLoan.getExpiryDate().set(Calendar.YEAR, values[0]);
    			mLoan.getExpiryDate().set(Calendar.MONTH, values[1]);
    			mLoan.getExpiryDate().set(Calendar.DAY_OF_MONTH, values[2]);
				
    			updateExpiryDate();
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_SELECT_DATE_PAYMENT) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");

    			if (mLoan.getPaymentDate() == null) {
    				mLoan.setPaymentDate(Calendar.getInstance());
    			}
    			mLoan.getPaymentDate().set(Calendar.YEAR, values[0]);
    			mLoan.getPaymentDate().set(Calendar.MONTH, values[1]);
    			mLoan.getPaymentDate().set(Calendar.DAY_OF_MONTH, values[2]);
				
    			updatePaymentDate();
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_ACCOUNT_SELECT) {
    		if (resultCode == RESULT_OK) {
    			updateAccount( DBMgr.getAccountItem(data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1)));
    		}
    	}

		super.onActivityResult(requestCode, resultCode, data);
	}
    
    private FinancialCompany getCompany(int id) {
		if (id == -1) {
			Log.e(LogTag.LAYOUT, "== noting select id : " + id);
			return null;
		}
		
		return DBMgr.getCompany(id);
	}
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
	   	if (DBMgr.addExtendLiabilityLoan(mLoan) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mLoan.getID());
    		return false;
    	}
	   	
	   	if (cls != null) {
    		Intent intent = new Intent(InputLiabilityLoanLayout.this, cls);
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
	protected void updateRepeat(int type, int value) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateCompany(FinancialCompany company) {
		if (company == null){
			return;
		}
		mLoan.setCompany(company);
		updateCompanyNameText();
	}
	
	/**
	 * 결제할 계좌 은행이름을 갱신한다.
	 */
	private void updateCompanyNameText() {
		if (mLoan.getCompany().getID() == -1) {
			((Button)findViewById(R.id.BtnLoanCompany)).setText("회사를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnLoanCompany)).setText(String.format("%s", mLoan.getCompany().getName()));
		}
	}
	
	private void updateAccount(AccountItem account) {
		if (account == null){
			return;
		}
		
		mLoan.setAccount(account);
		updateAccountText();
	}
	
	private void updateAccountText() {
		if (mLoan.getAccount().getID() == -1) {
			((Button)findViewById(R.id.BtnLoanAccount)).setText("계좌를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnLoanAccount)).setText(String.format("%s : %s", mLoan.getAccount().getCompany().getName(), mLoan.getAccount().getNumber()));
		}
		
	}
}
