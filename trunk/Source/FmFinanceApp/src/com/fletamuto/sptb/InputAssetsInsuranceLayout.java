package com.fletamuto.sptb;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsInsuranceLayout extends InputExtendLayout {
	private AssetsInsuranceItem mInsurance;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_insurance, true);
    	
    	updateChildView();
        setCreateDateBtnClickListener(R.id.BtnInsuranceCreateDate); 
        setAmountBtnClickListener(R.id.BtnInsuranceAmount);
        setExpiryBtnClickListener(R.id.BtnInsuranceExpiryDate);
    }
    
    DatePickerDialog.OnDateSetListener expiryDlg = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mInsurance.getExpiryDate().set(Calendar.YEAR, year);
			mInsurance.getExpiryDate().set(Calendar.MONTH, monthOfYear);
			mInsurance.getExpiryDate().set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};
	

	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				new DatePickerDialog(InputAssetsInsuranceLayout.this, expiryDlg, 
						mInsurance.getExpiryDate().get(Calendar.YEAR),
						mInsurance.getExpiryDate().get(Calendar.MONTH), 
						mInsurance.getExpiryDate().get(Calendar.DAY_OF_MONTH)).show(); 				
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
		updateBtnDateText(R.id.BtnInsuranceCreateDate);
	}
	
	protected void updateExpiryDate() {
		updateBtnExpiryDateText(R.id.BtnInsuranceExpiryDate);
	}
	
	protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mInsurance.getExpriyDateString());
    }

	@Override
	protected void createItemInstance() {
		mInsurance = new AssetsInsuranceItem();
		setItem(mInsurance);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mInsurance == null) return false;
		setItem(mInsurance);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateExpiryDate();
		updateBtnAmountText(R.id.BtnInsuranceAmount);
		updateEditMemoText(R.id.ETInsuranceMemo);
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETInsuranceMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETInsuranceTitle)).getText().toString();
    	getItem().setTitle(title);
    	
    	String company = ((TextView)findViewById(R.id.ETInsuranceCompany)).getText().toString();
    	mInsurance.setCompany(company);
	}
	
    @Override
	protected void updateAmount(Long amount) {
		mInsurance.setPayment(amount);
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnInsuranceAmount);
		
	}
    
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		if (DBMgr.addExtendAssetsInsurance(mInsurance) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mInsurance.getID());
    		return false;
    	}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsInsuranceLayout.this, cls);
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
