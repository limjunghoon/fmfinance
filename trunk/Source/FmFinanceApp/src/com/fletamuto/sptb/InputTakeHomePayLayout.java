package com.fletamuto.sptb;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 월금에대한 실 수령액을 설정한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputTakeHomePayLayout extends InputBaseLayout {
	public final static int ACT_TAKE_HOME_PAY_INSURANCE_AMOUNT = MsgDef.ActRequest.ACT_TAKE_HOME_PAY_INSURANCE_AMOUNT;
	public final static int ACT_TAKE_HOME_PAY_TAX_AMOUNT = MsgDef.ActRequest.ACT_TAKE_HOME_PAY_TAX_AMOUNT;
	public final static int ACT_TAKE_HOME_PAY_PENSION_AMOUNT = MsgDef.ActRequest.ACT_TAKE_HOME_PAY_PENSION_AMOUNT;
	public final static int ACT_TAKE_HOME_PAY_ETC_AMOUNT = MsgDef.ActRequest.ACT_TAKE_HOME_PAY_ETC_AMOUNT;
	
	
	/** 원금 총 금액 */
	private long mTotalAmount = 0;
	
	/** 보험 */
	private long mInsuranceAmount = 0;
	
	/** 세금 */
	private long mTaxAmount = 0;
	
	/** 연금 */
	private long mPensionAmount = 0;
	
	/** 기타 금액 */
	private long mEtcAmount = 0;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_income_take_home_pay, true);
        
        getExteaInfo();
        updateChildView();
        setButtonClickListener();
       
    }
    
	@Override
	protected void setTitleBtn() {
		setTitle("실 수령액");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
        
        setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		super.setTitleBtn();
	}

	private void setButtonClickListener() {
		Button btnInsurance = (Button)findViewById(R.id.BtnTakeHomePayInsurance);
		btnInsurance.setTextColor(Color.RED);
		btnInsurance.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
//				Intent intent = new Intent(InputTakeHomePayLayout.this, InputAmountDialog.class);
//				startActivityForResult(intent, ACT_TAKE_HOME_PAY_INSURANCE_AMOUNT);
			}
		});
		
		Button btnTax = (Button)findViewById(R.id.BtnTakeHomePayTax);
		btnTax.setTextColor(Color.RED);
		btnTax.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
//				Intent intent = new Intent(InputTakeHomePayLayout.this, InputAmountDialog.class);
//				startActivityForResult(intent, ACT_TAKE_HOME_PAY_TAX_AMOUNT);
			}
		});
		
		Button btnPension = (Button)findViewById(R.id.BtnTakeHomePayPension);
		btnPension.setTextColor(Color.RED);
		btnPension.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
//				Intent intent = new Intent(InputTakeHomePayLayout.this, InputAmountDialog.class);
//				startActivityForResult(intent, ACT_TAKE_HOME_PAY_PENSION_AMOUNT);
			}
		});
		
		
		Button btnEtc = (Button)findViewById(R.id.BtnTakeHomePayEtc);
		btnEtc.setTextColor(Color.RED);
		btnEtc.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
//				Intent intent = new Intent(InputTakeHomePayLayout.this, InputAmountDialog.class);
//				startActivityForResult(intent, ACT_TAKE_HOME_PAY_ETC_AMOUNT);
			}
		});
	}

	private void getExteaInfo() {
		mTotalAmount = getIntent().getLongExtra(MsgDef.ExtraNames.SALARY_TOTAL_AMOUNT, 0);
		mInsuranceAmount = getIntent().getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_INSURANCE, 0);
		mTaxAmount = getIntent().getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_TAX, 0);
		mPensionAmount = getIntent().getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_PENSION, 0);
		mEtcAmount = getIntent().getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_ETC, 0);
	}

	@Override
	public boolean checkInputData() {
		// TODO Auto-generated method stub
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
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_INSURANCE, mInsuranceAmount);
		intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_TAX, mTaxAmount);
		intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_PENSION, mPensionAmount);
		intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_ETC, mEtcAmount);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void updateChildView() {
		TextView tvTotalAmount = (TextView)findViewById(R.id.TVTotalAmount);
		tvTotalAmount.setText(String.format("%,d", mTotalAmount));
		TextView tvTotalExpense = (TextView)findViewById(R.id.TVTakeHomePayExpense);
		tvTotalExpense.setText(String.format("-%,d", getTotalExpenseAmount()));
		tvTotalExpense.setTextColor(Color.RED);
		TextView tvTakeHomePay = (TextView)findViewById(R.id.TVTakeHomePay);
		tvTakeHomePay.setText(String.format("%,d", mTotalAmount-getTotalExpenseAmount()));
		tvTakeHomePay.setTextColor(Color.BLUE);
		
		Button btnInsurance = (Button)findViewById(R.id.BtnTakeHomePayInsurance);
		btnInsurance.setText(String.format("%,d", mInsuranceAmount));
		Button btnTax = (Button)findViewById(R.id.BtnTakeHomePayTax);
		btnTax.setText(String.format("%,d", mTaxAmount));
		Button btnPension = (Button)findViewById(R.id.BtnTakeHomePayPension);
		btnPension.setText(String.format("%,d", mPensionAmount));
		Button btnEtc = (Button)findViewById(R.id.BtnTakeHomePayEtc);
		btnEtc.setText(String.format("%,d", mEtcAmount));
	}
	
	protected long getTotalExpenseAmount() {
		return (mInsuranceAmount+mTaxAmount+mPensionAmount+mEtcAmount);
	}

	@Override
	protected void updateItem() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_TAKE_HOME_PAY_INSURANCE_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			mInsuranceAmount = data.getLongExtra("AMOUNT", 0L); 
    			updateChildView();
    		}
    	}
		else if (requestCode == ACT_TAKE_HOME_PAY_TAX_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			mTaxAmount = data.getLongExtra("AMOUNT", 0L);
    			updateChildView();
    		}
    	}
		else if (requestCode == ACT_TAKE_HOME_PAY_PENSION_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			mPensionAmount = data.getLongExtra("AMOUNT", 0L);
    			updateChildView();
    		}
    	}
		else if (requestCode == ACT_TAKE_HOME_PAY_ETC_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			mEtcAmount = data.getLongExtra("AMOUNT", 0L);
    			updateChildView();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
