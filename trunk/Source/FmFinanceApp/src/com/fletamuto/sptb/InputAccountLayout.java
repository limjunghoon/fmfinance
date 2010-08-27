package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinancialInstitution;
import com.fletamuto.sptb.db.DBMgr;

/**
 * °èÁÂµî·Ï
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAccountLayout extends InputBaseLayout {
	public final static int ACT_SELECT_INSTITUTION = 1;
	public final static int MIN_DIGIT = 10;
	
	private AccountItem mAccount = new AccountItem();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_account, true);
        
        setTitleButtonListener();
        
        updateChildView();
        setTitleButtonListener();
        setTitle(getResources().getString(R.string.input_account_title));
        setSaveBtnClickListener(R.id.BtnAccountSave);
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

	@Override
	public boolean checkInputData() {
		if (mAccount.getNumber().length() == MIN_DIGIT)
			return false;
		
		return true;
	}

	@Override
	protected void createItemInstance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean getItemInstance(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void saveItem() {
		// TODO Auto-generated method stub
		
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
	
	private FinancialInstitution getInstitution(int id) {
		if (id == -1) {
			Log.e(LogTag.LAYOUT, "== noting select id : " + id);
			return null;
		}
		
		return DBMgr.getInstance().getInstitution(id);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SELECT_INSTITUTION) {
    		if (resultCode == RESULT_OK) {
    			updateInstitution( getInstitution(data.getIntExtra("INSTITUTION_ID", -1)));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateInstitution(FinancialInstitution institution) {
		if (institution == null){
			return;
		}
		
		mAccount.setInstitution(institution);
		((Button)findViewById(R.id.BtnAccountInstitution)).setText(institution.getName());
	}

	
}
