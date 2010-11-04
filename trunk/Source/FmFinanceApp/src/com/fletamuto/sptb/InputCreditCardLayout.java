package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 신용카드 등록
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputCreditCardLayout extends InputBaseLayout {
	public static final int ACT_SELECT_COMPANY_NAME = 0;
	public static final int ACT_SELECT_ACCOUNT = 1;
	
	private CardItem mCreditCard;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_credit_card, true);
        
        updateChildView();
        setSelectCardCompenyNameBtnClickListener();
        setAccountBtnClickListener(R.id.BtnCreditCardAccount);
    }
    
	@Override
	protected void setTitleBtn() {
		setTitle(getResources().getString(R.string.input_credit_card_title));
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		super.setTitleBtn();
	}

	private void setSelectCardCompenyNameBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnCreditCardCompany);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputCreditCardLayout.this, SelectCardCompanyLayout.class);
		    	startActivityForResult(intent, ACT_SELECT_COMPANY_NAME);
			}
		});
		
	}

	@Override
	public boolean checkInputData() {
		if (mCreditCard.getCompenyName().getID() == -1) {
			displayAlertMessage("카드사가 선택되지 않았습니다.");
			return false;
		}
		return true;
	}

	@Override
	protected void createItemInstance() {
		mCreditCard = new CardItem(CardItem.CREDIT_CARD);
	}

	@Override
	protected boolean getItemInstance(int id) {
		// TODO Auto-generated method stub
		return false;
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

	private void saveUpdateItem() {
		// TODO Auto-generated method stub
		
	}

	private void saveNewItem() {
		if (DBMgr.addCardItem(mCreditCard) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mCreditCard.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.CARD_ID, mCreditCard.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void updateChildView() {
		updateCompenyNameText();
		updateAccountText();
		
	}

	@Override
	protected void updateItem() {
		String name = ((TextView)findViewById(R.id.ETCrediCardName)).getText().toString();
		mCreditCard.setName(name);
		
		String number = ((TextView)findViewById(R.id.ETCrediCardNumber)).getText().toString();
		mCreditCard.setNumber(number);
		
		String memo = ((TextView)findViewById(R.id.ETCreditCardMemo)).getText().toString();
		mCreditCard.setMemo(memo);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SELECT_COMPANY_NAME) {
    		if (resultCode == RESULT_OK) {
    			updateCompenyName( getCompenyName(data.getIntExtra("CARD_COMPENY_NAME_ID", -1)));
    		}
    	}
		else if (requestCode == ACT_SELECT_ACCOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateAccount( getAccount(data.getIntExtra("ACCOUNT_ID", -1)));
    		}
    	}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	private AccountItem getAccount(int id) {
		AccountItem account = DBMgr.getAccountItem(id);
		if (account == null) {
			Log.e(LogTag.LAYOUT, ":: not found account item ID :" + id);
		}
		return account;
	}

	private CardCompanyName getCompenyName(int id) {
		CardCompanyName cardCompenyName = DBMgr.getCardCompanyName(id);
		if (cardCompenyName == null) {
			Log.e(LogTag.LAYOUT, ":: not found cardcompany item ID :" + id);
		}
		return cardCompenyName;
	}
	
	private void updateAccount(AccountItem account) {
		if (account == null){
			return;
		}
		
		mCreditCard.setAccount(account);
		updateAccountText();
	}

	private void updateCompenyName(CardCompanyName cardCompenyName) {
		if (cardCompenyName == null){
			return;
		}
		
		mCreditCard.setCompenyName(cardCompenyName);
		updateCompenyNameText();
		
	}
	
	/**
	 * 카드사이름을 갱신한다.
	 */
	private void updateCompenyNameText() {
		if (mCreditCard.getCompenyName().getID() == -1) {
			((Button)findViewById(R.id.BtnCreditCardCompany)).setText("카드사를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnCreditCardCompany)).setText(mCreditCard.getCompenyName().getName());
		}
		
	}
	
	/**
	 * 결제할 계좌 은행이름을 갱신한다.
	 */
	private void updateAccountText() {
		if (mCreditCard.getAccount().getID() == -1) {
			((Button)findViewById(R.id.BtnCreditCardAccount)).setText("계좌를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnCreditCardAccount)).setText(String.format("%s : %s", mCreditCard.getAccount().getCompany().getName(), mCreditCard.getAccount().getNumber()));
		}
		
	}
	
	
	protected void setAccountBtnClickListener(int btnID) {
    	Button btnAccount = (Button)findViewById(btnID);
    	btnAccount.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputCreditCardLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, ACT_SELECT_ACCOUNT);
			}
		 });
    }
	
}
