package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompenyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * °èÁÂµî·Ï
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputCreditCardLayout extends InputBaseLayout {
	public static final int ACT_SELECT_COMPANY_NAME = 0;
	public static final int ACT_SELECT_ACCOUNT = 1;
	
	private CardItem mCreditCard = new CardItem(CardItem.CREDIT_CARD);
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_credit_card, true);
        
        setTitleButtonListener();
        
        updateChildView();
        setTitleButtonListener();
        setTitle(getResources().getString(R.string.input_credit_card_title));
        setSelectCardCompenyNameBtnClickListener();
        setAccountBtnClickListener(R.id.BtnCreditCardAccount);
    }

	private void setSelectCardCompenyNameBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnCreadCardComapy);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputCreditCardLayout.this, SelectCardCompanyLayout.class);
		    	startActivityForResult(intent, ACT_SELECT_COMPANY_NAME);
			}
		});
		
	}

	@Override
	public boolean checkInputData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void createItemInstance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean getItemInstance(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void saveItem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateChildView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateItem() {
		String name = ((TextView)findViewById(R.id.ETCrediCardName)).getText().toString();
		mCreditCard.setName(name);
		
		String number = ((TextView)findViewById(R.id.ETCrediCardNumber)).getText().toString();
		mCreditCard.setNumber(number);
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
		AccountItem account = DBMgr.getInstance().getAccountItem(id);
		if (account == null) {
			Log.e(LogTag.LAYOUT, ":: not found account item ID :" + id);
		}
		return account;
	}

	private CardCompenyName getCompenyName(int id) {
		CardCompenyName cardCompenyName = DBMgr.getInstance().getCardCompanyName(id);
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
		((Button)findViewById(R.id.BtnCreditCardAccount)).setText(String.format("%s : %s", account.getInstitution().getName(), account.getNumber()));
	}

	private void updateCompenyName(CardCompenyName cardCompenyName) {
		if (cardCompenyName == null){
			return;
		}
		
		mCreditCard.setCompenyName(cardCompenyName);
		((Button)findViewById(R.id.BtnCreadCardComapy)).setText(cardCompenyName.getName());
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
