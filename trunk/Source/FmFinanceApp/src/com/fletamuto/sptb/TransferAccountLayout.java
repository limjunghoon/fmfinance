package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class TransferAccountLayout extends FmBaseActivity {  	
	private AccountItem fromItem;
	private AccountItem toItem;
	private long mTrasferAmount = 0L;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.transfer_account, true);
        setBtnClickListener();
        updateChildView();
    }
	
	private void updateChildView() {
		updateAccountText();
		updateAmountText();
	}

	private void setBtnClickListener() {
		findViewById(R.id.BtnAccountSelect).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(TransferAccountLayout.this, SelectAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_MODE, SelectAccountLayout.MODE_TRASFER);
				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_EXCEPTION, fromItem.getID());
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
			}
		});
		
		findViewById(R.id.BtnAccountAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(TransferAccountLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT);
			}
		});
	}

	@Override
	protected void initialize() {
		fromItem = (AccountItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM);
		super.initialize();
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
				long fromItemBalance = fromItem.getBalance();
				long toItemBalance = toItem.getBalance();
				
				fromItem.setBalance(fromItemBalance - mTrasferAmount);
				toItem.setBalance(toItemBalance + mTrasferAmount);
				DBMgr.updateAccount(fromItem);
				DBMgr.updateAccount(toItem);
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
		mTrasferAmount = trasferAmount;
		updateAmountText();
	}

	private void updateAccount(AccountItem selectedAccount) {
		if (selectedAccount == null) return;
		toItem = selectedAccount;
		updateAccountText();
	}
	
	protected void updateAccountText() {
		Button btnTrasferAccount = (Button) findViewById(R.id.BtnAccountSelect);
		
		if (toItem == null) {
			btnTrasferAccount.setText("계좌를 선택하세요");
		}
		else {
			if (toItem.getType() != AccountItem.MY_POCKET) {
				btnTrasferAccount.setText(toItem.getCompany().getName());
			}
			else {
				btnTrasferAccount.setText("내 주머니");
			}
		}
	}
	
	private void updateAmountText() {
		Button btnTrasferAmout = (Button) findViewById(R.id.BtnAccountAmount);
		btnTrasferAmout.setText(String.format("%,d원", mTrasferAmount));
	
	}
}