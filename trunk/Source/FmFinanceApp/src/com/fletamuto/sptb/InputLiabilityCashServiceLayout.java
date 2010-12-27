package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.LiabilityCashServiceItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputLiabilityCashServiceLayout extends InputLiabilityExtendLayout {
	public final static int ACT_CARD_SELECT = MsgDef.ActRequest.ACT_CARD_SELECT;
	
	private LiabilityCashServiceItem mCashService;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_liability_cash_service, true);
        
        updateChildView();
        
        //달력을 이용한 날짜 입력을 위해
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputLiabilityCashService);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
        setDateBtnClickListener(R.id.BtnCashServiceDate); 
        setAmountBtnClickListener(R.id.BtnCashServiceAmount);
        setSelectCardBtnClickListener();
    }
    
	protected void setSelectCardBtnClickListener() {
		Button button = (Button)findViewById(R.id.BtnCashServiceCard);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputLiabilityCashServiceLayout.this, SelectCardLayout.class);
		    	startActivityForResult(intent, ACT_CARD_SELECT);
			}
		});
		
	}

	@Override
	protected void createItemInstance() {
		mCashService = new LiabilityCashServiceItem();
		setItem(mCashService);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mCashService == null) return false;
		setItem(mCashService);
		return true;
	}



	@Override
	protected void updateChildView() {
		updateDate();
		updateCardNameText();
		updateBtnAmountText(R.id.BtnCashServiceAmount);
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETCashServiceMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETCashServiceTitle)).getText().toString();
    	getItem().setTitle(title);
	}
	
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnCashServiceDate);
    }
    
    @Override
    protected void updateAmount(Long amount) {
    	super.updateAmount(amount);
    	updateBtnAmountText(R.id.BtnCashServiceAmount);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ACT_CARD_SELECT) {
			if (resultCode == RESULT_OK) {
    			int selectedID = data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1);
    			if (selectedID == -1) return;
    			
    			CardItem selectedCard = DBMgr.getCardItem(selectedID);
    			updateCard(selectedCard);
    		}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
    
   
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
	   	if (DBMgr.addExtendLiabilityCashService(mCashService) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mCashService.getID());
    		return false;
    	}
	   	
	   	if (cls != null) {
    		Intent intent = new Intent(InputLiabilityCashServiceLayout.this, cls);
    		startActivity(intent);
    	}
    	else {
    		Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
    	}
		
		return true;
	}

	@Override
	protected void updateRepeat(int type, int value) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 결제할 계좌 은행이름을 갱신한다.
	 */
	private void updateCardNameText() {
		if (mCashService.getCard().getID() == -1) {
			((Button)findViewById(R.id.BtnCashServiceCard)).setText("카드를 선택합니다.");
		}
		else {
			((Button)findViewById(R.id.BtnCashServiceCard)).setText(String.format("%s", mCashService.getCard().getCompenyName().getName()));
		}
	}
	
	protected void updateCard(CardItem card) {
		mCashService.setCard(card);
		updateCardNameText();
	}
	
	
}
