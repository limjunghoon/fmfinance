package com.fletamuto.sptb;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
/**
 * 새로운 지출을 입력하거나 기존의 지출정보를 수정할때 보여주는 레이아웃 창
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputExpenseLayout extends InputFinanceItemBaseLayout {
	protected final static int ACT_TAG_SELECTED = MsgDef.ActRequest.ACT_TAG_SELECTED;
	protected final static int ACT_CARD_SELECT = MsgDef.ActRequest.ACT_CARD_SELECT;
	protected final static int ACT_ACCOUNT_SELECT = MsgDef.ActRequest.ACT_ACCOUNT_SELECT;
	protected final static int ACT_BOOKMARK_SELECT = MsgDef.ActRequest.ACT_BOOKMARK_SELECT;
	
	private ExpenseItem mExpensItem;
	
	//달력 입력과 자주 사용 되는 지출을 위해 start
	private LinearLayout linear;
	private View popupview, popupviewBookmark;
	private PopupWindow popupBookmark, popupBookmarkEdit;
	private TextView tv;
	private ArrayList<FinanceItem> expenseAllItems;
	private ArrayList<FinanceItem> itemsTemp;
	//달력 입력과 자주 사용 되는 지출을 위해 end
	
	private AccountItem fromItem;
	private long beforeAmount;
	  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense, true);
        
        updateChildView();
        
        //달력을 이용한 날짜 입력을 위해
        final Intent intent = getIntent();
        linear = (LinearLayout) findViewById(R.id.inputAssetsExpense);
        popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
        setDateBtnClickListener(R.id.BtnExpenseDate);
        setAmountBtnClickListener(R.id.BtnExpenseAmount);
        setCategoryClickListener(R.id.BtnExpenseCategory);
        setPaymentToggleBtnClickListener();
        setTagButtonListener();
        setRepeatBtnClickListener(R.id.BtnExpenseRepeat);
        //자주 사용 되는 지출 구현
        setBookmarkTvClickListener(R.id.TVExpenseBookmark);
        setTitle(getResources().getString(R.string.input_expense_name));
    }
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	setTitleBtnText(FmTitleLayout.BTN_LEFT_01, "수입");
        setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);
    	
    }
    
    @Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(InputExpenseLayout.this, SelectCategoryIncomeLayout.class);
		startActivity(intent);
		
    	super.onClickLeft1TitleBtn();
    }

	/**
     * 날짜 갱신
     */
	protected void updateDate() {
    	updateBtnDateText(R.id.BtnExpenseDate);
    } 
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
    		if(mExpensItem.getPaymentMethod().getType() == PaymentMethod.ACCOUNT) {
    			PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
    			fromItem = accountMethod.getAccount();

    			long fromItemBalance = fromItem.getBalance();

				fromItem.setBalance(fromItemBalance - mExpensItem.getAmount());
				DBMgr.updateAccount(fromItem);
    		}
    		if (saveNewItem(null) == true) {
    			saveRepeat();
    		}
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
    		if(mExpensItem.getPaymentMethod().getType() == PaymentMethod.ACCOUNT) {
    			PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
    			fromItem = accountMethod.getAccount();

    			long fromItemBalance = fromItem.getBalance();

				fromItem.setBalance(fromItemBalance + beforeAmount - mExpensItem.getAmount());
				DBMgr.updateAccount(fromItem);
    		}
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
		mExpensItem = (ExpenseItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		
		if (mExpensItem == null) {
			mExpensItem = new ExpenseItem();
		}
		beforeAmount = mExpensItem.getAmount();
		setItem(mExpensItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mExpensItem = (ExpenseItem)DBMgr.getItem(ExpenseItem.TYPE, id);
		if (mExpensItem == null) return false;
		
		beforeAmount = mExpensItem.getAmount();
		setItem(mExpensItem);
		
		loadPaymnetMethod();
		loadRepeat();
		
		return true;
	}

	/**
	 * 지불방법을 DB로부터 가져온다.
	 */
	private boolean loadPaymnetMethod() {
		if (mExpensItem == null) return false;
		PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
		
		if (paymentMethod.getType() == PaymentMethod.CARD) {
			PaymentCardMethod cardMethod = (PaymentCardMethod) paymentMethod;
			if (cardMethod.getCard() == null) {
				cardMethod.setCard(DBMgr.getCardItem(paymentMethod.getMethodItemID()));
			}
		}
		else if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
			PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
			if (accountMethod.getAccount() == null) {
				accountMethod.setAccount(DBMgr.getAccountItem(paymentMethod.getMethodItemID()));
			}
		}
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
		else if (requestCode == ACT_TAG_SELECTED) {
			if (resultCode == RESULT_OK) {
    			updateTag(data.getIntExtra(MsgDef.ExtraNames.TAG_ID, -1), data.getStringExtra(MsgDef.ExtraNames.TAG_NAME));
    		}
		}
		else if (requestCode == ACT_CARD_SELECT) {
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
		else if (requestCode == ACT_ACCOUNT_SELECT) {
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
		else if (requestCode == ACT_REPEAT) {
			if (resultCode == RESULT_OK) {
				int repeatType = data.getIntExtra(MsgDef.ExtraNames.RPEAT_TYPE, -1);
				
				if (repeatType == Repeat.MONTHLY) {
					int daily = data.getIntExtra(MsgDef.ExtraNames.RPEAT_DAILY, -1);
					if (daily == -1) return;
					
					updateRepeat(Repeat.MONTHLY, daily);
				}
				else if (repeatType == Repeat.WEEKLY) {
					int weekly = data.getIntExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, -1);
					if (weekly == -1) return;
					
					updateRepeat(Repeat.WEEKLY, weekly);
				}
				else {
					return;
				}
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	protected void updateAccount(AccountItem selectedAccount) {
		PaymentAccountMethod paymentMethod = (PaymentAccountMethod) mExpensItem.getPaymentMethod();
		if (paymentMethod == null) return;
		
		paymentMethod.setAccount(selectedAccount);
		
		updatePaymentMethod();
	}

	protected void updateCard(CardItem card) {
		PaymentCardMethod paymentMethod = (PaymentCardMethod) mExpensItem.getPaymentMethod();
		if (paymentMethod == null) return;
		paymentMethod.setCard(card);
		updatePaymentMethod();
	}

	protected void updateTag(int id, String name) {
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
		updateChildViewState();
		updateDate();
		updateBtnCategoryText(R.id.BtnExpenseCategory);
		updateBtnAmountText(R.id.BtnExpenseAmount);
		updateEditMemoText(R.id.ETExpenseMemo);
		updatePaymentMethod();
		updateTagText();
		updateRepeatText(R.id.BtnExpenseRepeat);
	}
	
	protected void updateChildViewState() {
		Category category = mExpensItem.getCategory();
		
		if (category.getExtndType() == ItemDef.ExtendAssets.NONE) {
			findViewById(R.id.BtnExpenseCategory).setEnabled(false);
			findViewById(R.id.BtnExpenseAmount).setEnabled(false);
			findViewById(R.id.TBExpenseMethodCard).setVisibility(View.GONE);
			findViewById(R.id.LLRepeat).setVisibility(View.GONE);
		}
		else {
			findViewById(R.id.BtnExpenseCategory).setEnabled(true);
			findViewById(R.id.BtnExpenseAmount).setEnabled(true);
			findViewById(R.id.TBExpenseMethodCard).setVisibility(View.VISIBLE);
			findViewById(R.id.LLRepeat).setVisibility(View.VISIBLE);
		}
	}

	protected PaymentMethod createPaymentMethod(int paymentMethodSelected) {
		if (mExpensItem.createPaymentMethod(paymentMethodSelected) == null) {
			return null;
		}
		return mExpensItem.getPaymentMethod();
	}

	/**
	 * 지불정보 상태를 갱신한다.
	 */
	protected void updatePaymentMethod() {
		PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
		if (paymentMethod == null) {
			paymentMethod = createPaymentMethod(PaymentMethod.CASH);
		}
		
		ToggleButton btnCash = (ToggleButton)findViewById(R.id.TBExpenseMethodCash);
		ToggleButton btnCard = (ToggleButton)findViewById(R.id.TBExpenseMethodCard);
		ToggleButton btnAccount = (ToggleButton)findViewById(R.id.TBExpenseMethodAccount);
		
		btnCash.setChecked(false);
		btnCard.setChecked(false);
		btnAccount.setChecked(false);
		
		if (paymentMethod.getType() == PaymentMethod.CASH) {
			btnCash.setChecked(true);
		}
		else if (paymentMethod.getType() == PaymentMethod.CARD) {
			btnCard.setChecked(true);
		}
		else if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
			btnAccount.setChecked(true);
		}
		updatePaymentMethodText();
	}
	
	protected void updateRepeat(int type, int value) {
		
		if (type == Repeat.MONTHLY) {
			mExpensItem.setRepeatMonthly(value);
		}
		else if (type == Repeat.WEEKLY) {
			mExpensItem.setRepeatWeekly(value);
		}
		else {
			
		}
		updateRepeatText(R.id.BtnExpenseRepeat);
	}
	
    /**
     * 태그버튼 클릭 시
     */
	protected void setTagButtonListener() {

    	((Button)findViewById(R.id.BtnExpenseTag)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputExpenseLayout.this, SelectTagLayout.class);
				
				startActivityForResult(intent, ACT_TAG_SELECTED);
			}
		});
    }
    
    

    /**
     * 
     */
	protected void updatePaymentMethodText() {
		TextView tvPaymentMethod = (TextView)findViewById(R.id.TVPaymentMethod);
		tvPaymentMethod.setText(mExpensItem.getPaymentMethod().getText());
	}
	
	

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETExpenseMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
	
	
	//자주 사용 되는 지출 구현 start
	protected void setBookmarkTvClickListener(int tvID) {
		tv = (TextView) findViewById (tvID);
        tv.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setBookmark ();
			}
		});
	}
	class CategoryTemp {
		FinanceItem item = null;
		int count = 0;;
	}
	
	protected void setBookmark () {
		popupviewBookmark = View.inflate(getApplicationContext(), R.layout.bookmark_expense_popup, null);
		popupBookmark = new PopupWindow(popupviewBookmark, 320, 300, true);
		popupBookmark.showAtLocation(linear, Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
		
		LinearLayout btnlist = (LinearLayout) popupviewBookmark.findViewById(R.id.bookmarkexpensepopupsub2);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		expenseAllItems = DBMgr.getAllItems(ExpenseItem.TYPE);
				
		ArrayList<CategoryTemp> categorysTemp = new ArrayList<CategoryTemp>();
		
		for (int i = expenseAllItems.size()-1 ; i >= 0 ; i--) {
			Boolean duplicationCheck = false;
			CategoryTemp categoryTemp = new CategoryTemp();
			if (categorysTemp.isEmpty() == true) {				
				categoryTemp.item = expenseAllItems.get(i);
				categoryTemp.count++;
				categorysTemp.add(categoryTemp);
			} else {
				for (int j=0; j < categorysTemp.size(); j++) {

					if (categorysTemp.get(j).item.getCategory().getName().equals(expenseAllItems.get(i).getCategory().getName()) && 
							categorysTemp.get(j).item.getSubCategory().getName().equals(expenseAllItems.get(i).getSubCategory().getName())  &&
							categorysTemp.get(j).item.getAmount() == expenseAllItems.get(i).getAmount()) {
						categorysTemp.get(j).count++;
						duplicationCheck = true;
						break;
					} 
				}
				if (duplicationCheck == false) {
					categoryTemp.item = expenseAllItems.get(i);
					categoryTemp.count++;
					categorysTemp.add(categoryTemp);
				}
			}			
		}
		
		itemsTemp = new ArrayList<FinanceItem>();
		
		for (int i=0; i < categorysTemp.size(); i++) {
			itemsTemp.add(null);
		}

		for (int i=0; i < categorysTemp.size(); i++) {
			int idx=0;
			for (int j=0; j < categorysTemp.size(); j++) {
				if (i==j) {
					
				}else {
					if (categorysTemp.get(i).count < categorysTemp.get(j).count) {
						idx++;
					} else if (categorysTemp.get(i).count == categorysTemp.get(j).count && i>j) {
						idx++;
					}
				}
			}

			itemsTemp.set(idx, categorysTemp.get(i).item);			
		}

		for (int i=0; i < 5; i++) {
			if (itemsTemp.size() - 1 - i < 0) break;
			Button btnBookmark = new Button(getApplicationContext());
			btnBookmark.setText(itemsTemp.get(i).getCategory().getName() + " - " + itemsTemp.get(i).getSubCategory().getName()
					+ "\t\t" + String.format("%,d원", itemsTemp.get(i).getAmount()));
			btnBookmark.setId(itemsTemp.get(i).getID());
			btnlist.addView(btnBookmark, params);
			
			btnBookmark.setOnClickListener(mClickListener);
		}
				
		Button btnBack = (Button) popupviewBookmark.findViewById (R.id.bookmarkBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				popupBookmark.dismiss();
			}
		});
/*		
		Button btnEdit = (Button) popupviewBookmark.findViewById (R.id.bookmarkEdit);
		btnEdit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setBookmarkEdit();
				
				
			}
		});
*/
	}
		
	protected void setBookmarkEdit() {
		
		popupviewBookmark = View.inflate(getApplicationContext(), R.layout.bookmark_expense_edit_popup, null);
		popupBookmarkEdit = new PopupWindow(popupviewBookmark, 320, 300, true);
		popupBookmarkEdit.showAtLocation(linear, Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
		
		Button btnBack = (Button) popupviewBookmark.findViewById (R.id.editBookmarkBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				popupBookmarkEdit.dismiss();
			}
		});
		
		Button btnAdd = (Button) popupviewBookmark.findViewById (R.id.editBookmarkAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputExpenseLayout.this, BookmarkExpenseAddLayout.class);
				startActivityForResult(intent, ACT_BOOKMARK_SELECT);
			}
		});
	}
		
	Button.OnClickListener mClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			for (int i = 0 ; i < itemsTemp.size() ; i++) {
				if (v.getId() == itemsTemp.get(i).getID()) {
					itemsTemp.get(i).setCreateDate(mExpensItem.getCreateDate());
					itemsTemp.get(i).setCreateTime(mExpensItem.getCreateTime());
					
					itemsTemp.get(i).setRepeat(mExpensItem.getRepeat());
					
					mExpensItem = (ExpenseItem)itemsTemp.get(i);
					setItem(mExpensItem);
					
					updateBtnCategoryText(R.id.BtnExpenseCategory);
					updateBtnAmountText(R.id.BtnExpenseAmount);
					updateEditMemoText(R.id.ETExpenseMemo);
					updatePaymentMethod();
					updateTagText();

					popupBookmark.dismiss();
				}				
			}
		}
	};			
	//자주 사용 되는 지출 구현 end	
	
}
