package com.fletamuto.sptb;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeSalaryItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.ReceiveMethod;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeSelaryLayout extends InputIncomeExtendLayout {
	public static final int ACT_TAKE_HOME_PAY = MsgDef.ActRequest.ACT_TAKE_HOME_PAY;
	
	private IncomeSalaryItem mSalary;
	private long mInsuranceAmount = 0;
	private long mTaxAmount = 0;
	private long mPensionAmount = 0;
	private long mEtcAmount  = 0;
	private ReceiveMethod mReciveMethod = new ReceiveMethod();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_income_salary, true);
        
        updateChildView();
        
        //달력을 이용한 날짜 입력을 위해
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputIncomeSalary);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
        
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnSalaryDate);         
        setAmountBtnClickListener(R.id.BtnSalaryAmount);
        setRepeatBtnClickListener(R.id.BtnSalaryRepeat);
        setReceiveToggleBtnClickListener();
        takeHomePayBtnClickListener();
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
		updateRepeatText(R.id.BtnSalaryRepeat);
		updateBtnTakeHomePayText();
		updateReceiveMethod();
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
		updateTakeHomePay();
		updateBtnAmountText(R.id.BtnSalaryAmount);
	}
    
    protected void updateTakeHomePay() {
    	updateBtnTakeHomePayText();
    }
    
	protected void updateBtnTakeHomePayText() {
		long takeHomePay = mSalary.getAmount() - getTotalExpenseAmount();
		((Button)findViewById(R.id.BtnSalaryTakeHomePay)).setText(String.format("%,d원", takeHomePay));
	}

	private void takeHomePayBtnClickListener() {
		Button btnTakeHomePay = (Button)findViewById(R.id.BtnSalaryTakeHomePay);
		btnTakeHomePay.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputIncomeSelaryLayout.this, InputTakeHomePayLayout.class);
				intent.putExtra(MsgDef.ExtraNames.SALARY_TOTAL_AMOUNT, mSalary.getAmount());
				intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_INSURANCE, mInsuranceAmount);
				intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_TAX, mTaxAmount);
				intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_PENSION, mPensionAmount);
				intent.putExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_ETC, mEtcAmount);
				
				startActivityForResult(intent, ACT_TAKE_HOME_PAY);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_TAKE_HOME_PAY) {
    		if (resultCode == RESULT_OK) {
    			mInsuranceAmount = data.getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_INSURANCE, 0);
    			mTaxAmount = data.getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_TAX, 0);
    			mPensionAmount = data.getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_PENSION, 0);
    			mEtcAmount = data.getLongExtra(MsgDef.ExtraNames.TAKE_HOME_PAY_ETC, 0);
    			updateChildView();
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_ACCOUNT_SELECT) {
			if (resultCode == RESULT_OK) {
				int selectedID = data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1);
				if (selectedID == -1) return;
			
				AccountItem selectedAccount = DBMgr.getAccountItem(selectedID);
				updateAccount(selectedAccount);
			}
			else {
				updateAccount(new AccountItem());
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected long getTotalExpenseAmount() {
		return (mInsuranceAmount+mTaxAmount+mPensionAmount+mEtcAmount);
	}
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		ArrayList<Category> categories = DBMgr.getCategory(ExpenseItem.TYPE, ItemDef.ExtendIncome.SALARY);
		if (categories.size() != 1) return false;
		Category mainCategory = categories.get(0);
		if (mainCategory == null) return false;
		
		ArrayList<Category> subCategories = DBMgr.getSubCategory(ExpenseItem.TYPE, mainCategory.getID());
		
		mSalary.setExpenseAssurance(createExpense(mainCategory, subCategories.get(0), mInsuranceAmount));
		mSalary.setExpenseTax(createExpense(mainCategory, subCategories.get(1), mTaxAmount));
		mSalary.setExpensePension(createExpense(mainCategory, subCategories.get(2), mPensionAmount));
		mSalary.setExpenseEtc(createExpense(mainCategory, subCategories.get(3), mEtcAmount));
		
	   	if (DBMgr.addExtendIncomeSalary(mSalary) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mSalary.getID());
    		return false;
    	}
		
		return true;
	}

	ExpenseItem createExpense(Category mainCategory, Category subCategory, long amount) {
		if (amount == 0L) return null;
		
		ExpenseItem item = new ExpenseItem();	
		item.createPaymentMethod(PaymentMethod.CASH);
		item.setAmount(amount);
		item.setCategory(mainCategory.getID(), mainCategory.getName());
		item.setSubCategory(subCategory.getID(), subCategory.getName());
		
		return item;
	} 
	
	protected void updateRepeat(int type, int value) {
		
		if (type == Repeat.MONTHLY) {
			mSalary.setRepeatMonthly(value);
		}
		else if (type == Repeat.WEEKLY) {
			mSalary.setRepeatWeekly(value);
		}
		else {
			
		}
		updateRepeatText(R.id.BtnSalaryRepeat);
	}
	
	/**
	 * 수령방법 토글버튼 클릭 시 
	 */
	protected void setReceiveToggleBtnClickListener() {
		
		((ToggleButton)findViewById(R.id.TBIncomeMethodCash)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mReciveMethod.setType(PaymentMethod.CASH);
				updateReceiveMethod();
			}
		});
		
		
		((ToggleButton)findViewById(R.id.TBIncomeMethodAccount)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent intent = new Intent(InputIncomeSelaryLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
			}
		});
	}
	
	protected void updateAccount(AccountItem selectedAccount) {
		mSalary.setAccount(selectedAccount);
		mReciveMethod.setAccount(selectedAccount);
		mReciveMethod.setType(ReceiveMethod.ACCOUNT);
		updateReceiveMethod();
	}
	
	protected void updateReceiveMethod() {		
		ToggleButton btnCash = (ToggleButton)findViewById(R.id.TBIncomeMethodCash);
		ToggleButton btnAccount = (ToggleButton)findViewById(R.id.TBIncomeMethodAccount);
		
		if (mReciveMethod.getType() == PaymentMethod.CASH) {
			btnCash.setChecked(true);
			btnAccount.setChecked(false);
		}
		else if (mReciveMethod.getType() == PaymentMethod.ACCOUNT) {
			btnCash.setChecked(false);
			btnAccount.setChecked(true);
		}
		updatePaymentMethodText();
	}
	
    /**
     * 
     */
	protected void updatePaymentMethodText() {
		TextView tvPaymentMethod = (TextView)findViewById(R.id.TVPaymentMethod);
		tvPaymentMethod.setText(mReciveMethod.getText());
	}
}
