package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.db.DBMgr;

/**
 * °èÁÂµî·Ï
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAccountLayout extends InputBaseLayout {
	public final static int ACT_SELECT_INSTITUTION = 1;
	public final static int MIN_DIGIT = 10;
	
	private AccountItem mAccount;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_account, true);
        
        setTitleButtonListener();
        
        updateChildView();
        setTitleButtonListener();
        setTitle(getResources().getString(R.string.input_account_title));
        setSaveBtnClickListener(R.id.BtnAccountSave);
        setAmountBtnClickListener(R.id.BtnAccountAmount);
        setSelectInstitutionBtnClickListener();
    }

	private void setSelectInstitutionBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnAccountInstitution);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputAccountLayout.this, SelectInstitutionLayout.class);
		    	startActivityForResult(intent, ACT_SELECT_INSTITUTION);
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
		if (mAccount.getNumber().length() == MIN_DIGIT)
			return false;
		
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
		// TODO Auto-generated method stub
		
	}

	private void saveNewItem() {
		if (DBMgr.addAccountItem(mAccount) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mAccount.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra("ACCOUNT_ID", mAccount.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void updateChildView() {
		// TODO Auto-generated method stub
		
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
		
		return DBMgr.getInstitution(id);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SELECT_INSTITUTION) {
    		if (resultCode == RESULT_OK) {
    			updateInstitution( getInstitution(data.getIntExtra("INSTITUTION_ID", -1)));
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
		((Button)findViewById(R.id.BtnAccountAmount)).setText(String.format("%,d¿ø", mAccount.getBalance()));
		
	}

	private void updateInstitution(FinancialCompany company) {
		if (company == null){
			return;
		}
		
		mAccount.setCompany(company);
		((Button)findViewById(R.id.BtnAccountInstitution)).setText(company.getName());
	}

	
}
