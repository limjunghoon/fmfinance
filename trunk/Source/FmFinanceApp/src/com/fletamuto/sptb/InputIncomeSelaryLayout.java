package com.fletamuto.sptb;


import android.os.Bundle;
import android.widget.TextView;

import com.fletamuto.sptb.data.IncomeSalaryItem;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeSelaryLayout extends InputIncomeExtendLayout {
	private IncomeSalaryItem mSalary;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_income_salary, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnSalaryDate); 
        setAmountBtnClickListener(R.id.BtnSalaryAmount);
    }

	@Override
	protected void createItemInstance() {
		mSalary = new IncomeSalaryItem();
		setItem(mSalary);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mSalary == null) return false;
		setItem(mSalary);
		return true;
	}



	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnAmountText(R.id.BtnSalaryAmount);
		updateEditMemoText(R.id.ETSalaryMemo);
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETSalaryMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
	
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnSalaryDate);
    }
    
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnSalaryAmount);
	}
}
