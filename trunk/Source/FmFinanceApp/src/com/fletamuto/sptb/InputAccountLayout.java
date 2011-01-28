package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 계좌등록
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAccountLayout extends InputBaseLayout {
	public final static int ACT_COMPANY_SELECT = MsgDef.ActRequest.ACT_COMPANY_SELECT;
	public final static int MIN_DIGIT = 10;
	
	private AccountItem mAccount;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_account, true);
        
    	int type = getIntent().getIntExtra(MsgDef.ExtraNames.ACCOUNT_TYPE, AccountItem.ORDINARY_DEPOSIT); 
    	if (type != AccountItem.ORDINARY_DEPOSIT) {
    		findViewById(R.id.LLAccountType).setVisibility(View.GONE);
    		findViewById(R.id.LLAccountBalance).setVisibility(View.GONE);
    		mAccount.setType(type);
    	}
        
        updateChildView();
        
    }
    
	@Override
	protected void setBtnClickListener() {
		setAmountBtnClickListener(R.id.BtnAccountAmount);
        setSelectCompanyBtnClickListener();
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
	}
	
    
	@Override
	protected void setTitleBtn() {
		setTitle("계좌 등록");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		
		
		
		super.setTitleBtn();
	}
	
	@Override
	protected void deleteItem() {
		DBMgr.deleteAccount(mAccount.getID());
		super.deleteItem();
	}
	

	private void setSelectCompanyBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnAccountInstitution);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputAccountLayout.this, SelectCompanyLayout.class);
		    	startActivityForResult(intent, ACT_COMPANY_SELECT);
			}
		});
		
	}
	
	 protected void setAmountBtnClickListener(int btnID) {
    	Button btnAmount = (Button)findViewById(btnID);
    	btnAmount.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputAccountLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, ACT_AMOUNT);
			}
		 });
    }

	@Override
	public boolean checkInputData() {
		if (mAccount.getCompany().getID() == -1) {
			displayAlertMessage("회사가 선택되지 않았습니다.");
			return false;
		}
		
		return true;
	}

	@Override
	protected void createItemInstance() {
		 mAccount = new AccountItem();
	}

	@Override
	protected boolean getItemInstance(int id) {
		mAccount = DBMgr.getAccountItem(id);
		if (mAccount == null) return false;
		return true;
	}

	@Override
	protected void saveItem() {
		
		if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem();
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
		
	}

	private void saveUpdateItem() {
		if (DBMgr.updateAccount(mAccount) == false) {
			Log.e(LogTag.LAYOUT, "== NEW fail to the update item : " + mAccount.getID());
    		return;
		}
		
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ID, mAccount.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

	private void saveNewItem() {
		if (DBMgr.addAccountItem(mAccount) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mAccount.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ID, mAccount.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void updateChildView() {
		updateCompanyNameText();
		updateBalanceText();
		updateNumberText();
		updateTypeSpinner();
	}

	protected void updateTypeSpinner() {
//		Spinner typeSpinner = (Spinner) findViewById(R.id.SpAccountType);
//		typeSpinner.setSelection(1);
	}

	protected void updateNumberText() {
		EditText tvNumber = (EditText) findViewById(R.id.ETAccountNumber);
		tvNumber.setText(mAccount.getNumber());
	}

	@Override
	protected void updateItem() {
		String number = ((TextView)findViewById(R.id.ETAccountNumber)).getText().toString();
		mAccount.setNumber(number);
	}
	
	private FinancialCompany getInstitution(int id) {
		if (id == -1) {
			Log.e(LogTag.LAYOUT, "== noting select id : " + id);
			return null;
		}
		
		return DBMgr.getCompany(id);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_COMPANY_SELECT) {
    		if (resultCode == RESULT_OK) {
    			updateCompany( getInstitution(data.getIntExtra(MsgDef.ExtraNames.COMPANY_ID, -1)));
    		}
    	}
		else if (requestCode == ACT_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateBalance(data.getLongExtra("AMOUNT", 0L));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateBalance(long balance) {
		mAccount.setBalance(balance);
		updateBalanceText();
	}

	private void updateBalanceText() {
		((Button)findViewById(R.id.BtnAccountAmount)).setText(String.format("%,d원", mAccount.getBalance()));
	}

	private void updateCompany(FinancialCompany company) {
		if (company == null){
			return;
		}
		
		mAccount.setCompany(company);
		updateCompanyNameText();
	}

	/**
	 * 결제할 계좌 은행이름을 갱신한다.
	 */
	private void updateCompanyNameText() {
		if (mAccount.getCompany().getID() == -1) {
			((Button)findViewById(R.id.BtnAccountInstitution)).setText("회사를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnAccountInstitution)).setText(String.format("%s", mAccount.getCompany().getName()));
		}
		
	}


	
}
