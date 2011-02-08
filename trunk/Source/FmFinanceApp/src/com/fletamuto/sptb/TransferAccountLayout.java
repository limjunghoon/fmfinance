package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.TransferItem;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class TransferAccountLayout extends FmBaseActivity {  	
	private TransferItem mTrans = new TransferItem(); 

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.transfer_account, true);
        setBtnClickListener();
        updateChildView();
    }
	
	private void updateChildView() {
		updateBtnDateText();
		updateAccountText();
		updateAmountText();
		updateEditMemoText();
	}
	
	protected void updateEditMemoText() {
    	((EditText)findViewById(R.id.ETTransferMemo)).setText(mTrans.getMemo());
    }
	
	protected void updateBtnDateText() {	
    	((Button)findViewById(R.id.BtnTransferDate)).setText(mTrans.getOccurrentceDateString());
    }

	private void setBtnClickListener() {
		findViewById(R.id.BtnTransferToSelect).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(TransferAccountLayout.this, SelectAccountLayout.class);
//				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_MODE, SelectAccountLayout.MODE_TRASFER);
//				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_EXCEPTION, fromItem.getID());
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
			}
		});
		
		findViewById(R.id.BtnTransferAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(TransferAccountLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT);
			}
		});
	}

	@Override
	protected void initialize() {
		AccountItem fromAccount = (AccountItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM);
		if (fromAccount == null) finish();
		mTrans.setFromAccount(fromAccount);
		
		setFromAccountText();
		super.initialize();

	}
	
	private void setFromAccountText() {
		AccountItem fromAccount = mTrans.getFromAccount();
		if (fromAccount.getType() == AccountItem.MY_POCKET) {
			TextView tvFromAccount = (TextView)findViewById(R.id.TVFromAccount);
			tvFromAccount.setText("출금 : " + "내주머니");
		}
		else {
			TextView tvFromAccount = (TextView)findViewById(R.id.TVFromAccount);
			tvFromAccount.setText("출금 : " + mTrans.getFromAccount().getCompany().getName());
		}
		
	}

	@Override
	protected void setTitleBtn() {
		setTitle("계좌 이체");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		setCompleateButtonListener();
		super.setTitleBtn();
	}
	
	private void setCompleateButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
//				long fromItemBalance = fromItem.getBalance();
//				long toItemBalance = toItem.getBalance();
//				
//				fromItem.setBalance(fromItemBalance - mTrasferAmount);
//				toItem.setBalance(toItemBalance + mTrasferAmount);
//				DBMgr.updateAccount(fromItem);
//				DBMgr.updateAccount(toItem);
				finish();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == MsgDef.ActRequest.ACT_ACCOUNT_SELECT) {
			if (resultCode == RESULT_OK) {
				updateAccount((AccountItem)data.getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM));
			}
		}
		else if (requestCode == MsgDef.ActRequest.ACT_AMOUNT) {
			if (resultCode == RESULT_OK) {
				updateAmount(data.getLongExtra("AMOUNT", 0L));
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateAmount(long trasferAmount) {
//		mTrasferAmount = trasferAmount;
		updateAmountText();
	}

	private void updateAccount(AccountItem selectedAccount) {
		if (selectedAccount == null) return;
//		toItem = selectedAccount;
		updateAccountText();
	}
	
	protected void updateAccountText() {
		Button btnTrasferAccount = (Button) findViewById(R.id.BtnTransferToSelect);
		
		if (mTrans.getToAccount().getID() == -1) {
			btnTrasferAccount.setText("계좌를 선택하세요");
		}
		else {
			if (mTrans.getToAccount().getType() != AccountItem.MY_POCKET) {
				btnTrasferAccount.setText(mTrans.getToAccount().getCompany().getName());
			}
			else {
				btnTrasferAccount.setText("내 주머니");
			}
		}
	}
	
	private void updateAmountText() {
		Button btnTrasferAmout = (Button) findViewById(R.id.BtnTransferAmount);
		btnTrasferAmout.setText(String.format("%,d원", mTrans.getAmount()));
	
	}
}