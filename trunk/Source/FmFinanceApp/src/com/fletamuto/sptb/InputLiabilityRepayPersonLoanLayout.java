package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityPersonLoanItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.ReceiveMethod;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputLiabilityRepayPersonLoanLayout extends InputLiabilityRepayLayout {
	/** 대출 정보*/
	private LiabilityPersonLoanItem mPersonLoan;
	private ReceiveMethod mReciveMethod = new ReceiveMethod();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		setContentView(R.layout.input_liability_repay_person_loan, true);
		setBtnClickListener();
		updateChildView();
	}

	
	@Override
	protected void setTitleBtn() {
		setTitle("빌린돈 상환");
		super.setTitleBtn();
	}


	@Override
	protected void createItemInstance() {
		mPersonLoan = (LiabilityPersonLoanItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		if (mPersonLoan == null) finish();
		
		super.createItemInstance();
	}

	@Override
	protected boolean getItemInstance(int id) {
		return true;
	}

	
	public ExpenseItem createExpenseItem(ExpenseItem expense) {
		super.createExpenseItem(expense);
		
		if (mReciveMethod.getType() == ReceiveMethod.CASH) {
			expense.createPaymentMethod(PaymentMethod.CASH);
		}
		else {
			expense.createPaymentMethod(PaymentMethod.ACCOUNT);
			((PaymentAccountMethod) expense.getPaymentMethod()).setAccount(mReciveMethod.getAccount());
		}
	
		return expense;
	}
	
	private void saveUpdateItem() {
		finish();
	}

	@Override
	protected void setBtnClickListener() {
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		findViewById(R.id.BtnRepayDate).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayPersonLoanLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE);
			}
		});
		
		findViewById(R.id.BtnRepayPrincipal).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayPersonLoanLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT_PRINCIPAL);
			}
		});
		
		findViewById(R.id.BtnRepayInterest).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayPersonLoanLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT_INTEREST);
			}
		});
		
		setReceiveToggleBtnClickListener();
	}

	@Override
	protected void updateChildView() {
		((TextView)findViewById(R.id.TVRepayPerson)).setText(mPersonLoan.getLoanPeopleName());
		((TextView)findViewById(R.id.TVRepayRemainderAmount)).setText(String.format("%,d원", mPersonLoan.getAmount()));
		
		updateBtnDateText();
		updatePrincipalAmountText();
		updateInterestAmountText();
		updateEditMemoText();
		updateReceiveMethod();
	}
	
	protected void updateEditMemoText() {
//		((EditText)findViewById(R.id.ETTransferMemo)).setText(mTrans.getMemo());
	}

	protected void updateBtnDateText() {	
		((Button)findViewById(R.id.BtnRepayDate)).setText(mChangeItem.getChangeDateString());
	}

	@Override
	protected void updateItem() {
//		String memo = ((TextView)findViewById(R.id.ETTransferMemo)).getText().toString();
//		mTrans.setMemo(memo);
	} 

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == MsgDef.ActRequest.ACT_SELECT_DATE_EXPIRY) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");

    			mChangeItem.getChangeDate().set(Calendar.YEAR, values[0]);
    			mChangeItem.getChangeDate().set(Calendar.MONTH, values[1]);
    			mChangeItem.getChangeDate().set(Calendar.DAY_OF_MONTH, values[2]);
				
    			updateChangeDate();
    		}
    	}	
		else if (requestCode == MsgDef.ActRequest.ACT_AMOUNT_PRINCIPAL) {
			if (resultCode == RESULT_OK) {
				updatePrincipalAmount(data.getLongExtra("AMOUNT", 0L));
			}
		}
		else if (requestCode == MsgDef.ActRequest.ACT_AMOUNT_INTEREST) {
			if (resultCode == RESULT_OK) {
				updateInterestAmount(data.getLongExtra("AMOUNT", 0L));
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

	protected void updateChangeDate() {
		updateBtnDateText();
	}


	private void updatePrincipalAmount(long principalAmount) {
		mChangeItem.getPrincipal().setAmount(principalAmount);
		updatePrincipalAmountText();
	}
	
	private void updatePrincipalAmountText() {
		Button btnPrincipalAmout = (Button) findViewById(R.id.BtnRepayPrincipal);
		btnPrincipalAmout.setText(String.format("%,d원", mChangeItem.getPrincipal().getAmount()));
	}
	
	private void updateInterestAmount(long interestAmount) {
		mChangeItem.getInterest().setAmount(interestAmount);
		updateInterestAmountText();
	}
	
	private void updateInterestAmountText() {
		Button btnInterestAmout = (Button) findViewById(R.id.BtnRepayInterest);
		btnInterestAmout.setText(String.format("%,d원", mChangeItem.getInterest().getAmount()));
	}


	@Override
	LiabilityItem getLiabilityItem() {
		return mPersonLoan;
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
				
				Intent intent = new Intent(InputLiabilityRepayPersonLoanLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
			}
		});
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
	
	protected void updateAccount(AccountItem selectedAccount) {
		mReciveMethod.setAccount(selectedAccount);
		mReciveMethod.setType(ReceiveMethod.ACCOUNT);
		updateReceiveMethod();
	}
}