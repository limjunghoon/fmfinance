package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.SelectInputCard.EventButton;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 계좌등록
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputCheckCardLayout extends InputBaseLayout {
	public static final int ACT_SELECT_COMPANY_NAME = 0;
	public static final int ACT_SELECT_ACCOUNT = 1;
	
	private CardItem mCheckCard;
	
	private boolean showTitle = true;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        showTitle = intent.getBooleanExtra("showTitle", true);
        if (showTitle) {
        	setContentView(R.layout.input_check_card, true);
        } else {
        	setContentView(R.layout.input_check_card);
        	
        	/*카드 선택의 추가에서 완료 버튼 누를 시 호출 됨*/
            EventButton btnEvent = (EventButton) intent.getSerializableExtra("saveCard");
            btnEvent.getButton().setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View v) {
    				updateItem();
    				
    				if (checkInputData() == true) {
    					saveItem();		
    		    	}
    			}
    		});
        }          
        
        updateChildView();
        
    }
    
    @Override
	protected void setBtnClickListener() {
    	setSelectCardCompenyNameBtnClickListener();
        setAccountBtnClickListener(R.id.BtnCheckCardAccount);
	}
    
    @Override
	protected void setTitleBtn() {
		setTitle(getResources().getString(R.string.input_check_card_title));
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		super.setTitleBtn();
	}
    
    @Override
	protected void deleteItem() {
		DBMgr.deleteCardItem(mCheckCard.getID());
		super.deleteItem();
	}

	private void setSelectCardCompenyNameBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnCheckCardCompany);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputCheckCardLayout.this, SelectCardCompanyLayout.class);
		    	startActivityForResult(intent, ACT_SELECT_COMPANY_NAME);
			}
		});
		
	}

	@Override
	public boolean checkInputData() {
		if (mCheckCard.getCompenyName().getID() == -1) {
			displayAlertMessage("카드사가 선택되지 않았습니다.");
			return false;
		}
		return true;
	}

	@Override
	protected void createItemInstance() {
		mCheckCard = new CardItem(CardItem.CHECK_CARD);
	}

	@Override
	protected boolean getItemInstance(int id) {
		mCheckCard = DBMgr.getCardItem(id);
		if (mCheckCard == null) return false;
		
		int accountID = mCheckCard.getAccount().getID(); 
		if (accountID != -1) {
			mCheckCard.setAccount(DBMgr.getAccountItem(accountID));
		}
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

	private void saveUpdateItem() {
		if (DBMgr.updateCardItem(mCheckCard) == false) {
			Log.e(LogTag.LAYOUT, "== NEW fail to the update item : " + mCheckCard.getID());
    		return;
		}
		
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.CARD_ID, mCheckCard.getID());
		if (showTitle) {
			setResult(RESULT_OK, intent);
			finish();
		} else {
			intent.setAction("saveCardData");
			sendBroadcast(intent);
		}
	}

	private void saveNewItem() {
		if (DBMgr.addCardItem(mCheckCard) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mCheckCard.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.CARD_ID, mCheckCard.getID());
		if (showTitle) {
			setResult(RESULT_OK, intent);
			finish();
		} else {
			intent.setAction("saveCardData");
			sendBroadcast(intent);
		}
	}

	@Override
	protected void updateChildView() {
		updateCompenyNameText();
		updateAccountText();
		updateCardNameText();
		updateCardNumberText();
		updateMemo();
	}
	
	private void updateMemo() {
		((TextView)findViewById(R.id.ETCheckCardMemo)).setText(mCheckCard.getMemo());
	}

	private void updateCardNumberText() {
		((TextView)findViewById(R.id.ETCheckCardNumber)).setText(mCheckCard.getNumber());
	}

	private void updateCardNameText() {
		((TextView)findViewById(R.id.ETCheckCardName)).setText(mCheckCard.getName());
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
		
		mCheckCard.setAccount(account);
		updateAccountText();
	}

	private void updateCompenyName(CardCompanyName cardCompenyName) {
		if (cardCompenyName == null){
			return;
		}
		
		mCheckCard.setCompenyName(cardCompenyName);
		updateCompenyNameText();
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
	

	/**
	 * 카드사이름을 갱신한다.
	 */
	private void updateCompenyNameText() {
		if (mCheckCard.getCompenyName().getID() == -1) {
			((Button)findViewById(R.id.BtnCheckCardCompany)).setText("카드사를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnCheckCardCompany)).setText(mCheckCard.getCompenyName().getName());
		}
		
	}
	
	/**
	 * 결제할 계좌 은행이름을 갱신한다.
	 */
	private void updateAccountText() {
		if (mCheckCard.getAccount().getID() == -1) {
			((Button)findViewById(R.id.BtnCheckCardAccount)).setText("계좌를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnCheckCardAccount)).setText(String.format("%s : %s", mCheckCard.getAccount().getCompany().getName(), mCheckCard.getAccount().getNumber()));
		}
		
	}
	
}
