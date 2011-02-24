package com.fletamuto.sptb;


import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityPersonLoanItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputLiabilityPersonLoanLayout extends InputLiabilityExtendLayout {
	
	private LiabilityPersonLoanItem mPersonLoan;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_liability_person_loan, true);
        updateChildView();
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnPersonLoanDate);
    	setExpiryBtnClickListener(R.id.BtnPersonLoanExpiryDate);
        setAmountBtnClickListener(R.id.BtnPersonLoanAmount);
	}
   

	@Override
	protected void createItemInstance() {
		mPersonLoan = new LiabilityPersonLoanItem();
		setItem(mPersonLoan);
	}

	@Override
	protected boolean getItemInstance(int id) {
		mPersonLoan = (LiabilityPersonLoanItem) DBMgr.getItem(LiabilityItem.TYPE, id);
		if (mPersonLoan == null) return false;
		setItem(mPersonLoan);
		return true;
	}

	 private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityPersonLoanLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE_EXPIRY);
			}
		 });
	}
	 
	protected void updateExpiryDate() {
    	updateBtnExpiryDateText(R.id.BtnPersonLoanExpiryDate);
    }
	 
	 protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mPersonLoan.getExpiryDateString());
    }


	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnAmountText(R.id.BtnPersonLoanAmount);
		updateBtnExpiryDateText(R.id.BtnPersonLoanExpiryDate);
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETPersonLoanMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETPersonLoanTitle)).getText().toString();
    	getItem().setTitle(title);
    	
    	String person = ((TextView)findViewById(R.id.ETPersonLoanPerson)).getText().toString();
    	mPersonLoan.setLoanPeopleName(person);
	}
	
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnPersonLoanDate);
    }
    
    @Override
    protected void updateAmount(Long amount) {
    	super.updateAmount(amount);
    	updateBtnAmountText(R.id.BtnPersonLoanAmount);
    }
    
	@Override
	protected boolean saveNewItem(Class<?> cls) {
	   	if (DBMgr.addExtendLiabilityPersonLoan(mPersonLoan) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mPersonLoan.getID());
    		return false;
    	}
	   	
	   	if (cls != null) {
    		Intent intent = new Intent(InputLiabilityPersonLoanLayout.this, cls);
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
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_SELECT_DATE_EXPIRY) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");

    			mPersonLoan.getExpiryDate().set(Calendar.YEAR, values[0]);
    			mPersonLoan.getExpiryDate().set(Calendar.MONTH, values[1]);
    			mPersonLoan.getExpiryDate().set(Calendar.DAY_OF_MONTH, values[2]);
				
    			updateExpiryDate();
    		}
    	}
	

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
