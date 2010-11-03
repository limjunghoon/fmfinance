package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.CardCompenyName;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputPrepaidCardLayout extends InputBaseLayout {
	public static final int ACT_SELECT_COMPANY_NAME = 0;
	public static final int ACT_SELECT_ACCOUNT = 1;
	public static final int ACT_BALANCE = 2;
	
	private CardItem mPrepaidCard;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_prepaid_card, true);
        
        updateChildView();
        setSelectCardCompenyNameBtnClickListener();
        setBalanceBtnClickListener(R.id.BtnPrepaidCardBalance);
    }
    
    @Override
	protected void setTitleBtn() {
		setTitle(getResources().getString(R.string.input_prepaid_card_title));
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		
		setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		super.setTitleBtn();
	}

	private void setSelectCardCompenyNameBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnPrepaidCardCompany);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputPrepaidCardLayout.this, SelectCardCompanyLayout.class);
		    	startActivityForResult(intent, ACT_SELECT_COMPANY_NAME);
			}
		});
		
	}
	
	 protected void setBalanceBtnClickListener(int btnID) {
    	Button btnAmount = (Button)findViewById(btnID);
    	btnAmount.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputPrepaidCardLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, ACT_BALANCE);
			}
		 });
    }

	@Override
	public boolean checkInputData() {
		if (mPrepaidCard.getCompenyName().getID() == -1) {
			displayAlertMessage("카드사가 선택되지 않았습니다.");
			return false;
		}
		return true;
	}

	@Override
	protected void createItemInstance() {
		mPrepaidCard = new CardItem(CardItem.PREPAID_CARD);
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
		if (DBMgr.addCardItem(mPrepaidCard) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mPrepaidCard.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.CARD_ID, mPrepaidCard.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void updateChildView() {
		updateCompenyNameText();
		
	}

	@Override
	protected void updateItem() {
		String name = ((TextView)findViewById(R.id.ETPrepaidCardName)).getText().toString();
		mPrepaidCard.setName(name);
		
		String number = ((TextView)findViewById(R.id.ETPrepaidCardNumber)).getText().toString();
		mPrepaidCard.setNumber(number);
		
		String memo = ((TextView)findViewById(R.id.ETPrepaidCardMemo)).getText().toString();
		mPrepaidCard.setMemo(memo);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SELECT_COMPANY_NAME) {
    		if (resultCode == RESULT_OK) {
    			updateCompenyName( getCompenyName(data.getIntExtra("CARD_COMPENY_NAME_ID", -1)));
    		}
    	}
		else if (requestCode == ACT_BALANCE) {
    		if (resultCode == RESULT_OK) {
    			updateBalance(data.getLongExtra("AMOUNT", 0L));
    		}
    	}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	private void updateBalance(long balance) {
		mPrepaidCard.setBalance(balance);
		((Button)findViewById(R.id.BtnPrepaidCardBalance)).setText(String.format("%,d원", mPrepaidCard.getBalance()));
		
	}

	private CardCompenyName getCompenyName(int id) {
		CardCompenyName cardCompenyName = DBMgr.getCardCompanyName(id);
		if (cardCompenyName == null) {
			Log.e(LogTag.LAYOUT, ":: not found cardcompany item ID :" + id);
		}
		return cardCompenyName;
	}
	

	private void updateCompenyName(CardCompenyName cardCompenyName) {
		if (cardCompenyName == null){
			return;
		}
		
		mPrepaidCard.setCompenyName(cardCompenyName);
		updateCompenyNameText();
	}
	
	/**
	 * 카드사이름을 갱신한다.
	 */
	private void updateCompenyNameText() {
		if (mPrepaidCard.getCompenyName().getID() == -1) {
			((Button)findViewById(R.id.BtnPrepaidCardCompany)).setText("카드사를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnPrepaidCardCompany)).setText(mPrepaidCard.getCompenyName().getName());
		}
		
	}
	
	protected void setAccountBtnClickListener(int btnID) {
    	Button btnAccount = (Button)findViewById(btnID);
    	btnAccount.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputPrepaidCardLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, ACT_SELECT_ACCOUNT);
			}
		 });
    }
	
}
