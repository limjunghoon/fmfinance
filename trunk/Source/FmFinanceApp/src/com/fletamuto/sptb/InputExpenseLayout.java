package com.fletamuto.sptb;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 새로운 지출을 입력하거나 기존의 지출정보를 수정할때 보여주는 레이아웃 창
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputExpenseLayout extends InputFinanceItemBaseLayout {
	private ExpenseItem mExpensItem;
	protected final static int ACT_TAG_SELECTED = MsgDef.ActRequest.ACT_TAG_SELECTED;
	protected final static int ACT_REPEAT = MsgDef.ActRequest.ACT_REPEAT;
	protected final static int ACT_CARD_SELECT = MsgDef.ActRequest.ACT_CARD_SELECT;
	protected final static int ACT_ACCOUNT_SELECT = MsgDef.ActRequest.ACT_ACCOUNT_SELECT;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnExpenseDate);
        setAmountBtnClickListener(R.id.BtnExpenseAmount);
        setCategoryClickListener(R.id.BtnExpenseCategory);
        setPaymentToggleBtnClickListener();
        setTagButtonListener();
        setRepeatBtnClickListener();
        setTitle(getResources().getString(R.string.input_expense_name));
    }

	/**
     * 날짜 갱신
     */
	protected void updateDate() {
    	updateBtnDateText(R.id.BtnExpenseDate);
    } 
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(ReportExpenseLayout.class);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    }

    /**
     * 금액을 갱신
     */
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnExpenseAmount);
	}

	@Override
	protected void createItemInstance() {
		mExpensItem = new ExpenseItem();
		setItem(mExpensItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mExpensItem = (ExpenseItem)DBMgr.getItem(ExpenseItem.TYPE, id);
		if (mExpensItem == null) return false;
		return true;
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputExpenseLayout.this, SelectCategoryExpenseLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		mExpensItem.setCategory(id, name);
		updateBtnCategoryText(R.id.BtnExpenseCategory);
	}
	
	protected void updateBtnCategoryText(int btnID) {
		String categoryText = getResources().getString(R.string.input_select_category);
		if (mExpensItem.isVaildCatetory()) {
			categoryText = String.format("%s - %s", mExpensItem.getCategory().getName(), mExpensItem.getSubCategory().getName());
		}
		 
    	((Button)findViewById(btnID)).setText(categoryText);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			mExpensItem.setSubCategory(data.getIntExtra("SUB_CATEGORY_ID", -1), data.getStringExtra("SUB_CATEGORY_NAME"));
    		}
    	}
		
		if (requestCode == ACT_TAG_SELECTED) {
			if (resultCode == RESULT_OK) {
    			updateTag(data.getIntExtra(MsgDef.ExtraNames.TAG_ID, -1), data.getStringExtra(MsgDef.ExtraNames.TAG_NAME));
    		}
		}
		
		if (requestCode == ACT_CARD_SELECT) {
			if (resultCode == RESULT_OK) {
    			int selectedID = data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1);
    			if (selectedID == -1) return;
    			
    			CardItem selectedCard = DBMgr.getCardItem(selectedID);
    			if (selectedCard.getType() == CardItem.CREDIT_CARD) {
    				int installmentPlan = data.getIntExtra(MsgDef.ExtraNames.INSTALLMENT_PLAN, -1);
    				if (installmentPlan == -1) return;
    								
    	        	PaymentCardMethod paymentMethod = (PaymentCardMethod) mExpensItem.getPaymentMethod();
    	        	if (paymentMethod == null || paymentMethod.getType() != PaymentCardMethod.CARD) return;
    	        	
    	        	paymentMethod.setInstallmentPlan(installmentPlan);
    			}
    			
    			updateCard(selectedCard);
    		}
			else {
				updatePaymentMethod();
			}
		}
		
		if (requestCode == ACT_ACCOUNT_SELECT) {
			if (resultCode == RESULT_OK) {
				int selectedID = data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1);
				if (selectedID == -1) return;
			
				AccountItem selectedAccount = DBMgr.getAccountItem(selectedID);
				updateAccount(selectedAccount);
			}
			else {
				updatePaymentMethod();
			}
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	private void updateAccount(AccountItem selectedAccount) {
		PaymentAccountMethod paymentMethod = (PaymentAccountMethod) mExpensItem.getPaymentMethod();
		if (paymentMethod == null) return;
		
		paymentMethod.setAccount(selectedAccount);
		
		updatePaymentMethod();
	}

	private void updateCard(CardItem card) {
		PaymentCardMethod paymentMethod = (PaymentCardMethod) mExpensItem.getPaymentMethod();
		if (paymentMethod == null) return;
		
		paymentMethod.setCard(card);
		
		updatePaymentMethod();
		
	}

	private void updateTag(int id, String name) {
		mExpensItem.setTag(id, name);
		updateTagText();
	}
	
	protected void updateTagText() {
		String tagText = getResources().getString(R.string.input_expense_selected_tag);
		ExpenseTag tag = mExpensItem.getTag();

		if (isValidTag()) {
			tagText = String.format("%s", tag.getName());
		}
		 
    	((Button)findViewById(R.id.BtnExpenseTag)).setText(tagText);
	}
	
	public boolean isValidTag() {
		ExpenseTag tag = mExpensItem.getTag();
		return  !(tag == null || tag.getID() == ExpenseTag.NONE_ID);
	}


	/**
	 * 지불방법 토글버튼 클릭 시 
	 */
	protected void setPaymentToggleBtnClickListener() {
		
		((ToggleButton)findViewById(R.id.TBExpenseMethodCash)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				createPaymentMethod(PaymentMethod.CASH);
				updatePaymentMethod();
			}
		});
		
		((ToggleButton)findViewById(R.id.TBExpenseMethodCard)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				createPaymentMethod(PaymentMethod.CARD);
				
				Intent intent = new Intent(InputExpenseLayout.this, SelectCardLayout.class);
				startActivityForResult(intent, ACT_CARD_SELECT);
			}
		});
		
		((ToggleButton)findViewById(R.id.TBExpenseMethodAccount)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				createPaymentMethod(PaymentMethod.ACCOUNT);
				
				Intent intent = new Intent(InputExpenseLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, ACT_ACCOUNT_SELECT);
			}
		});
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnCategoryText(R.id.BtnExpenseCategory);
		updateBtnAmountText(R.id.BtnExpenseAmount);
		updateEditMemoText(R.id.ETExpenseMemo);
		updatePaymentMethod();
		updateTagText();
	}
	
	public PaymentMethod createPaymentMethod(int paymentMethodSelected) {
		if (mExpensItem.createPaymentMethod(paymentMethodSelected) == null) {
			return null;
		}
		return mExpensItem.getPaymentMethod();
	}

	/**
	 * 지불정보 상태를 갱신한다.
	 */
	private void updatePaymentMethod() {
		PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
		if (paymentMethod == null) {
			paymentMethod = createPaymentMethod(PaymentMethod.CASH);
		}
		
		ToggleButton btnCash = (ToggleButton)findViewById(R.id.TBExpenseMethodCash);
		ToggleButton btnCard = (ToggleButton)findViewById(R.id.TBExpenseMethodCard);
		ToggleButton btnAccount = (ToggleButton)findViewById(R.id.TBExpenseMethodAccount);
		
		if (paymentMethod.getType() == PaymentMethod.CASH) {
			btnCash.setChecked(true);
			btnCard.setChecked(false);
			btnAccount.setChecked(false);
		}
		else if (paymentMethod.getType() == PaymentMethod.CARD) {
			btnCash.setChecked(false);
			btnCard.setChecked(true);
			btnAccount.setChecked(false);
		}
		else if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
			btnCash.setChecked(false);
			btnCard.setChecked(false);
			btnAccount.setChecked(true);
		}
		
		updatePaymentMethodText();
	}
	
    /**
     * 태그버튼 클릭 시
     */
    private void setTagButtonListener() {

    	((Button)findViewById(R.id.BtnExpenseTag)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputExpenseLayout.this, SelectTagLayout.class);
				
				startActivityForResult(intent, ACT_TAG_SELECTED);
			}
		});
    }
    
    /**
     * 반복버튼 클릭 시
     */
    private void setRepeatBtnClickListener() {
    	((Button)findViewById(R.id.BtnExpenseRepeat)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputExpenseLayout.this, RepeatLayout.class);
				startActivityForResult(intent, ACT_REPEAT);
			}
		});
	}

	private void updatePaymentMethodText() {
		TextView tvPaymentMethod = (TextView)findViewById(R.id.TVPaymentMethod);
		tvPaymentMethod.setText(mExpensItem.getPaymentMethod().getText());
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETExpenseMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
	
	
}
