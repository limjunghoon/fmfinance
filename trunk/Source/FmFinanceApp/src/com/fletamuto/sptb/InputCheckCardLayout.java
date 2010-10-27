package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompenyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * ���µ��
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputCheckCardLayout extends InputBaseLayout {
	public static final int ACT_SELECT_COMPANY_NAME = 0;
	public static final int ACT_SELECT_ACCOUNT = 1;
	
	private CardItem mCheckCard;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_check_card, true);
        
        setTitleButtonListener();
        
        updateChildView();
        setTitleButtonListener();
        setTitle(getResources().getString(R.string.input_check_card_title));
        setSelectCardCompenyNameBtnClickListener();
        setSaveBtnClickListener(R.id.BtnCheckCardSave);
        setAccountBtnClickListener(R.id.BtnCheckCardAccount);
    }

	private void setSelectCardCompenyNameBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnPrepaidCardCompany);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputCheckCardLayout.this, SelectCardCompanyLayout.class);
		    	startActivityForResult(intent, ACT_SELECT_COMPANY_NAME);
			}
		});
		
	}

	@Override
	public boolean checkInputData() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void createItemInstance() {
		mCheckCard = new CardItem(CardItem.CHECK_CARD);
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
		if (DBMgr.addCardItem(mCheckCard) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mCheckCard.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra("CARD_ID", mCheckCard.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void updateChildView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateItem() {
		String name = ((TextView)findViewById(R.id.ETCheckCardName)).getText().toString();
		mCheckCard.setName(name);
		
		String number = ((TextView)findViewById(R.id.ETCheckCardNumber)).getText().toString();
		mCheckCard.setNumber(number);
		
		String memo = ((TextView)findViewById(R.id.ETCheckCardMemo)).getText().toString();
		mCheckCard.setMemo(memo);
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

	private CardCompenyName getCompenyName(int id) {
		CardCompenyName cardCompenyName = DBMgr.getCardCompanyName(id);
		if (cardCompenyName == null) {
			Log.e(LogTag.LAYOUT, ":: not found cardcompany item ID :" + id);
		}
		return cardCompenyName;
	}
	
	private void updateAccount(AccountItem account) {
		if (account == null){
			return;
		}
		
		mCheckCard.setAccountID(account.getID());
		((Button)findViewById(R.id.BtnCheckCardAccount)).setText(String.format("%s : %s", account.getCompany().getName(), account.getNumber()));
	}

	private void updateCompenyName(CardCompenyName cardCompenyName) {
		if (cardCompenyName == null){
			return;
		}
		
		mCheckCard.setCompenyName(cardCompenyName);
		((Button)findViewById(R.id.BtnCheckCardCompany)).setText(cardCompenyName.getName());
	}
	
	protected void setAccountBtnClickListener(int btnID) {
    	Button btnAccount = (Button)findViewById(btnID);
    	btnAccount.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputCheckCardLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, ACT_SELECT_ACCOUNT);
			}
		 });
    }
	
}
