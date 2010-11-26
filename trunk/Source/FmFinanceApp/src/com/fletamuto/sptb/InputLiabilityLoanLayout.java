package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputLiabilityLoanLayout extends InputExtendLayout {
	public final static int ACT_COMPANY_SELECT = MsgDef.ActRequest.ACT_COMPANY_SELECT;
	
	private LiabilityLoanItem mLoan;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_liability_loan, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnLoanDate); 
        setAmountBtnClickListener(R.id.BtnLoanAmount);
        setSelectCompanyBtnClickListener();
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
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mLoan == null) return false;
		setItem(mLoan);
		return true;
	}



	@Override
	protected void updateChildView() {
		updateDate();
		updateCompanyNameText();
		updateBtnAmountText(R.id.BtnLoanAmount);
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETLoanMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETLoanTitle)).getText().toString();
    	getItem().setTitle(title);
	}
	
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnLoanDate);
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
}
