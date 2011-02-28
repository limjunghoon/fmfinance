package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.LiabilityCashServiceItem;
import com.fletamuto.sptb.data.LiabilityChangeItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputLiabilityRepayCashServiceLayout extends InputLiabilityRepayLayout {
	/** 대출 정보*/
	private LiabilityCashServiceItem mCashService;
	
	/** 계좌정보*/
	private AccountItem mAccount;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		setContentView(R.layout.input_liability_repay_cash_service, true);
		setBtnClickListener();
		updateChildView();
	}

	
	@Override
	protected void setTitleBtn() {
		setTitle("현금 서비스 상환");
		super.setTitleBtn();
	}


	@Override
	protected void createItemInstance() {
		mCashService = (LiabilityCashServiceItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		if (mCashService == null) finish();
		mChangeItem = new LiabilityChangeItem(mCashService);
		mChangeItem.setChangeDate(Calendar.getInstance());
		mChangeItem.getPrincipal().setAmount(mCashService.getAmount());
		mAccount = mCashService.getCard().getAccount();
	}

	@Override
	protected boolean getItemInstance(int id) {

		return true;
	}

	
	public ExpenseItem createExpenseItem(ExpenseItem expense) {
		super.createExpenseItem(expense);
		
		if (mAccount.getID() == -1) {
			expense.createPaymentMethod(PaymentMethod.CASH);
		}
		else {
			expense.createPaymentMethod(PaymentMethod.ACCOUNT);
			((PaymentAccountMethod) expense.getPaymentMethod()).setAccount(mAccount);
		}
	
		return expense;
	}
	
	

	@Override
	protected void setBtnClickListener() {
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		findViewById(R.id.BtnRepayDate).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayCashServiceLayout.this, MonthlyCalendar.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_SELECT_DATE);
			}
		});
		
		findViewById(R.id.BtnRepayAccount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayCashServiceLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
			}
		});
		
		findViewById(R.id.BtnRepayInterest).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityRepayCashServiceLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT_INTEREST);
			}
		});
	}

	@Override
	protected void updateChildView() {
		((TextView)findViewById(R.id.TVRepayName)).setText(mCashService.getCard().getName());
		((TextView)findViewById(R.id.TVRepayAmount)).setText(String.format("%,d원", mCashService.getAmount()));
		
		updateBtnDateText();
		updateAccountText();
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
		
		if (requestCode == MsgDef.ActRequest.ACT_SELECT_DATE) {
    		if (resultCode == RESULT_OK) {
    			
    			int[] values = data.getIntArrayExtra("SELECTED_DATE");

    			mChangeItem.getChangeDate().set(Calendar.YEAR, values[0]);
    			mChangeItem.getChangeDate().set(Calendar.MONTH, values[1]);
    			mChangeItem.getChangeDate().set(Calendar.DAY_OF_MONTH, values[2]);
				
    			updateChangeDate();
    		}
    	}	
		else if (requestCode == MsgDef.ActRequest.ACT_ACCOUNT_SELECT) {
			if (resultCode == RESULT_OK) {
				int selectedID = data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1);
				if (selectedID == -1) return;
			
				AccountItem selectedAccount = DBMgr.getAccountItem(selectedID);
				updateAccount(selectedAccount);
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
	
	private void updateAccount(AccountItem account) {
		if (account == null){
			return;
		}
		
		mAccount = account;
		updateAccountText();
	}
	
	 /**
	 *  계좌 이름을 갱신한다.
	 */
	private void updateAccountText() {
		if (mAccount.getID() == -1) {
			((Button)findViewById(R.id.BtnRepayAccount)).setText("계좌를 입력해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnRepayAccount)).setText(String.format("%s : %s", mAccount.getCompany().getName(), mAccount.getNumber()));
		}
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
		return mCashService;
	}

}