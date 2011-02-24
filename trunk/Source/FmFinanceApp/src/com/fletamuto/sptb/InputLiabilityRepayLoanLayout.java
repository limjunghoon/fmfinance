package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityChangeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.LiabilityLoanItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCashMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputLiabilityRepayLoanLayout extends InputLiabilityRepayLayout {
	/** 대출 정보*/
	private LiabilityLoanItem mLoan;
	
	/** 변경정보*/
//	private LiabilityChangeItem mChangeItem;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		setContentView(R.layout.input_liability_repay, true);
		setBtnClickListener();
		updateChildView();
	}

	
	@Override
	protected void setTitleBtn() {
		setTitle("대출 상환");
		super.setTitleBtn();
	}


	@Override
	protected void createItemInstance() {
		mLoan = (LiabilityLoanItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		if (mLoan == null) finish();
		
		super.createItemInstance();
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mTrans = DBMgr.getTranser(id);
//		mOrgAmount = mTrans.getAmount();
//		if (mTrans == null) return false;
		return true;
	}

	
	public ExpenseItem createExpenseItem(ExpenseItem expense) {
		super.createExpenseItem(expense);
		
		if (mLoan.getAccount().getID() == -1) {
			expense.createPaymentMethod(PaymentMethod.CASH);
		}
		else {
			expense.createPaymentMethod(PaymentMethod.ACCOUNT);
			((PaymentAccountMethod) expense.getPaymentMethod()).setAccount(mLoan.getAccount());
		}
	
		return expense;
	}
	
	private void saveUpdateItem() {
//		if (DBMgr.updateTranser(mTrans) == false) {
//			Log.e(LogTag.LAYOUT, ":: Fail to update tranfer item");
//		}
//		long fromItemBalance = mTrans.getFromAccount().getBalance();
//		long toItemBalance = mTrans.getToAccount().getBalance();
//		
//		mTrans.getFromAccount().setBalance(fromItemBalance + mOrgAmount - mTrans.getAmount());
//		mTrans.getToAccount().setBalance(toItemBalance + mOrgAmount + mTrans.getAmount());
//		DBMgr.updateAccount(mTrans.getFromAccount());
//		DBMgr.updateAccount(mTrans.getToAccount());
		finish();
	}

	@Override
	protected void setBtnClickListener() {
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		findViewById(R.id.BtnRepayDate).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayLoanLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE);
			}
		});
		
		findViewById(R.id.BtnRepayPrincipal).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayLoanLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT_PRINCIPAL);
			}
		});
		
		findViewById(R.id.BtnRepayInterest).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayLoanLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT_INTEREST);
			}
		});
	}

	@Override
	protected void updateChildView() {
		((TextView)findViewById(R.id.TVRepayName)).setText(mLoan.getTitle());
		((TextView)findViewById(R.id.TVRepayInstitution)).setText(mLoan.getCompany().getName());
		((TextView)findViewById(R.id.TVRepayRemainderAmount)).setText(String.format("%,d원", mLoan.getAmount()));
		
		updateBtnDateText();
		updatePrincipalAmountText();
		updateInterestAmountText();
		updateEditMemoText();
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
		return mLoan;
	}
}