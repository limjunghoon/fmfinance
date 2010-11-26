package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fletamuto.sptb.data.LiabilityPersonLoanItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputLiabilityPersonLoanLayout extends InputExtendLayout {
	
	private LiabilityPersonLoanItem mPersonLoan;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_liability_person_loan, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnPersonLoanDate); 
        setAmountBtnClickListener(R.id.BtnPersonLoanAmount);
    }
   

	@Override
	protected void createItemInstance() {
		mPersonLoan = new LiabilityPersonLoanItem();
		setItem(mPersonLoan);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mPersonLoan == null) return false;
		setItem(mPersonLoan);
		return true;
	}



	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnAmountText(R.id.BtnPersonLoanAmount);
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
	
	
}
