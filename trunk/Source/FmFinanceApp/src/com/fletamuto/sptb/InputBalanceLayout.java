package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputBalanceLayout extends InputBaseLayout {  	
	private AccountItem mAccount;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		setContentView(R.layout.transfer_account, true);
		setBtnClickListener();
		updateChildView();
	}
	
	@Override
	protected void setTitleBtn() {
		setTitle("잔액수정");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		super.setTitleBtn();
	}

	@Override
	public boolean checkInputData() {
		
		return true;
	}

	@Override
	protected void createItemInstance() {
		mAccount = (AccountItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM);
		if (mAccount == null) finish();
	}

	@Override
	protected boolean getItemInstance(int id) {
		mAccount = DBMgr.getAccountItem(id);
		if (mAccount == null) return false;
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
//		if (DBMgr.addTranser(mTrans) == -1) {
//			Log.e(LogTag.LAYOUT, ":: Fail to create tranfer item");
//		}
//		long fromItemBalance = mTrans.getFromAccount().getBalance();
//		long toItemBalance = mTrans.getToAccount().getBalance();
//		
//		mTrans.getFromAccount().setBalance(fromItemBalance  - mTrans.getAmount());
//		mTrans.getToAccount().setBalance(toItemBalance + mTrans.getAmount());
//		DBMgr.updateAccount(mTrans.getFromAccount());
//		DBMgr.updateAccount(mTrans.getToAccount());
		finish();
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
		
		
	}

	@Override
	protected void updateChildView() {
//		updateBtnDateText();
//		updateAccountText();
//		updateAmountText();
//		updateEditMemoText();
	}
	
	protected void updateEditMemoText() {
//		((EditText)findViewById(R.id.ETTransferMemo)).setText(mTrans.getMemo());
	}

	protected void updateBtnDateText() {	
//		((Button)findViewById(R.id.BtnTransferDate)).setText(mTrans.getOccurrentceDateString());
	}

	@Override
	protected void updateItem() {
//		String memo = ((TextView)findViewById(R.id.ETTransferMemo)).getText().toString();
//		mTrans.setMemo(memo);
	} 
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_AMOUNT) {
			if (resultCode == RESULT_OK) {
//				updateAmount(data.getLongExtra("AMOUNT", 0L));
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateAmount(long trasferAmount) {
//		mAccount.setAmount(trasferAmount);
//		updateAmountText();
	}

	private void updateAccount(AccountItem selectedAccount) {
//		if (selectedAccount == null) return;
//		mTrans.setToAccount(selectedAccount);
//		updateAccountText();
	}
	
	private void updateAmountText() {
//		Button btnTrasferAmout = (Button) findViewById(R.id.BtnTransferAmount);
//		btnTrasferAmout.setText(String.format("%,d원", mTrans.getAmount()));
	
	}
}