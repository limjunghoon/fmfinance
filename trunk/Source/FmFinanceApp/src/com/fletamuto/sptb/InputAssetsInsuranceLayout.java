package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsInsuranceLayout extends InputAssetsExtendLayout {
	private AssetsInsuranceItem mInsurance;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_insurance, true);
    	
    	updateChildView();
    	
    	//달력을 이용한 날짜 입력을 위해
/*
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssetsInsurance);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
*/
        
        
    }
    
    @Override
	protected void setBtnClickListener() {
    	setCreateDateBtnClickListener(R.id.BtnInsuranceCreateDate); 
        setAmountBtnClickListener(R.id.BtnInsuranceAmount);
        setExpiryBtnClickListener(R.id.BtnInsuranceExpiryDate);
	}
    
    public boolean checkInputData() {
    	if (mInsurance.getCreateDate().after(Calendar.getInstance())) {
    		displayAlertMessage("개설일 다시 지정해 주세요");
    		return false;
    	}
    	
    	if (mInsurance.getCreateDate().after(mInsurance.getExpiryDate())) {
    		displayAlertMessage("만기일을 다시 지정해 주세요");
    		return false;
    	}
    	
    	return super.checkInputData();
    };

	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
/*
				monthlyCalendar.showMonthlyCalendarPopup();
				monthlyCalendar.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
					
					public void onDismiss() {
						if (monthlyCalendar.getSelectCalendar() == null) return;
						mInsurance.getExpiryDate().set(Calendar.YEAR, monthlyCalendar.getSelectCalendar().get(Calendar.YEAR));
						mInsurance.getExpiryDate().set(Calendar.MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.MONTH));
						mInsurance.getExpiryDate().set(Calendar.DAY_OF_MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.DAY_OF_MONTH));
						updateExpiryDate();
					}
				});		
*/
				Intent intent = new Intent(InputAssetsInsuranceLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent,37);
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
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
		//좀 있다 요청코드 만들기
    	if (requestCode == 37) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");

    			mInsurance.getCreateDate().set(Calendar.YEAR, values[0]);
    			mInsurance.getCreateDate().set(Calendar.MONTH, values[1]);
    			mInsurance.getCreateDate().set(Calendar.DAY_OF_MONTH, values[2]);
				
    			updateDate();
    		}
    	}
    }
  
}
