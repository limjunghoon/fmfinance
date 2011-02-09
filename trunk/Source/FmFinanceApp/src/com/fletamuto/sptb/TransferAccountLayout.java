package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class TransferAccountLayout extends InputBaseLayout {  	
	private TransferItem mTrans;
	private long mOrgAmount = 0L;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		setContentView(R.layout.transfer_account, true);
		setBtnClickListener();
		updateChildView();
	}
	
	@Override
	protected void setTitleBtn() {
		setTitle("계좌 이체");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
//		setCompleateButtonListener();
		super.setTitleBtn();
	}

	@Override
	public boolean checkInputData() {
		if (mTrans.getToAccount().getID() == -1) {
			displayAlertMessage("입금 계좌가 선택되지 않았습니다.");
			return false;
		}
		
		if (mTrans.getAmount() > mTrans.getFromAccount().getBalance()) {
			displayAlertMessage("출금계좌 잔액을 초과하는 금액입니다.");
			return false;
		}
		return true;
	}

	@Override
	protected void createItemInstance() {
		mTrans = new TransferItem();
		
		AccountItem fromAccount = (AccountItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM);
		if (fromAccount == null) finish();
		mTrans.setFromAccount(fromAccount);
		
		setFromAccountText();
		
	}

	@Override
	protected boolean getItemInstance(int id) {
		mTrans = DBMgr.getTranser(id);
		mOrgAmount = mTrans.getAmount();
		if (mTrans == null) return false;
		return true;
	}

	@Override
	protected void saveItem() {
		if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem();
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
	}
	
	private void saveNewItem() {
		if (DBMgr.addTranser(mTrans) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to create tranfer item");
		}
		long fromItemBalance = mTrans.getFromAccount().getBalance();
		long toItemBalance = mTrans.getToAccount().getBalance();
		
		mTrans.getFromAccount().setBalance(fromItemBalance  - mTrans.getAmount());
		mTrans.getToAccount().setBalance(toItemBalance - mTrans.getAmount());
		DBMgr.updateAccount(mTrans.getFromAccount());
		DBMgr.updateAccount(mTrans.getToAccount());
		finish();
	}
	
	private void saveUpdateItem() {
		if (DBMgr.updateTranser(mTrans) == false) {
			Log.e(LogTag.LAYOUT, ":: Fail to update tranfer item");
		}
		long fromItemBalance = mTrans.getFromAccount().getBalance();
		long toItemBalance = mTrans.getToAccount().getBalance();
		
		mTrans.getFromAccount().setBalance(fromItemBalance + mOrgAmount - mTrans.getAmount());
		mTrans.getToAccount().setBalance(toItemBalance + mOrgAmount + mTrans.getAmount());
		DBMgr.updateAccount(mTrans.getFromAccount());
		DBMgr.updateAccount(mTrans.getToAccount());
		finish();
	}

	@Override
	protected void setBtnClickListener() {
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		findViewById(R.id.BtnTransferToSelect).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(TransferAccountLayout.this, SelectAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_MODE, SelectAccountLayout.MODE_TRASFER);
				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_EXCEPTION, mTrans.getFromAccount().getID());
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
	protected void updateChildView() {
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

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETTransferMemo)).getText().toString();
		mTrans.setMemo(memo);
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


//	
//	private void updateChildView() {
//		updateBtnDateText();
//		updateAccountText();
//		updateAmountText();
//		updateEditMemoText();
//	}
//	
//	protected void updateEditMemoText() {
//    	((EditText)findViewById(R.id.ETTransferMemo)).setText(mTrans.getMemo());
//    }
//	
//	protected void updateBtnDateText() {	
//    	((Button)findViewById(R.id.BtnTransferDate)).setText(mTrans.getOccurrentceDateString());
//    }
//
//	private void setBtnClickListener() {
//		findViewById(R.id.BtnTransferToSelect).setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(TransferAccountLayout.this, SelectAccountLayout.class);
//				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_MODE, SelectAccountLayout.MODE_TRASFER);
//				intent.putExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_EXCEPTION, mTrans.getFromAccount().getID());
//				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
//			}
//		});
//		
//		findViewById(R.id.BtnTransferAmount).setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(TransferAccountLayout.this, InputAmountDialog.class);
//				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT);
//			}
//		});
//	}
//
//	@Override
//	protected void initialize() {
//		AccountItem fromAccount = (AccountItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM);
//		if (fromAccount == null) finish();
//		mTrans.setFromAccount(fromAccount);
//		
//		setFromAccountText();
//		super.initialize();
//
//	}
//	
//	private void setFromAccountText() {
//		AccountItem fromAccount = mTrans.getFromAccount();
//		if (fromAccount.getType() == AccountItem.MY_POCKET) {
//			TextView tvFromAccount = (TextView)findViewById(R.id.TVFromAccount);
//			tvFromAccount.setText("출금 : " + "내주머니");
//		}
//		else {
//			TextView tvFromAccount = (TextView)findViewById(R.id.TVFromAccount);
//			tvFromAccount.setText("출금 : " + mTrans.getFromAccount().getCompany().getName());
//		}
//		
//	}
//

//	
//	private void setCompleateButtonListener() {
//		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				if (DBMgr.addTranser(mTrans) == -1) {
//					Log.e(LogTag.LAYOUT, ":: Fail to create tranfer item");
//				}
//				long fromItemBalance = mTrans.getFromAccount().getBalance();
//				long toItemBalance = mTrans.getToAccount().getBalance();
//				
//				mTrans.getFromAccount().setBalance(fromItemBalance - mTrans.getAmount());
//				mTrans.getToAccount().setBalance(toItemBalance + mTrans.getAmount());
//				DBMgr.updateAccount(mTrans.getFromAccount());
//				DBMgr.updateAccount(mTrans.getToAccount());
//				finish();
//			}
//		});
//	}
//
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
		mTrans.setAmount(trasferAmount);
		updateAmountText();
	}

	private void updateAccount(AccountItem selectedAccount) {
		if (selectedAccount == null) return;
		mTrans.setToAccount(selectedAccount);
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