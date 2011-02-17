package com.fletamuto.sptb;


import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.OpenUsedItem;
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
/*	
	private View popupview;
*/
	private LinearLayout linear;
	private View popupviewBookmark;
	private PopupWindow popupBookmark, popupBookmarkEdit;
	private TextView tv;
	private ArrayList<FinanceItem> expenseAllItems;
	private ArrayList<FinanceItem> itemsTemp;
	//달력 입력과 자주 사용 되는 지출을 위해 end
	
//	private AccountItem fromItem;
	private long beforeAmount;
	
	private LinearLayout mLLBookark;
	private SlidingDrawer mSlidingDrawer;
	  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense, true);
        
        updateChildView();
        
        //달력을 이용한 날짜 입력을 위해
/*
        final Intent intent = getIntent();
        linear = (LinearLayout) findViewById(R.id.inputAssetsExpense);
        popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
*/
        
        switch(getIntent().getIntExtra("Action", 0)) {
        case 0:	//일반 호출
        	setTitle(getResources().getString(R.string.input_expense_name));
            initBookmark();
        	break;
        case 1:	//즐겨찾기 추가 호출
        	setTitle(getResources().getString(R.string.input_bookmark_name));
        	intentAction = 1;
        	break;
        case 2:	//즐겨찾기 편집 호출
        	setTitle(getResources().getString(R.string.input_bookmark_name));
        	intentAction = 2;
        	break;
        }
        initWidget();
        
    }
    
    /**
     * 즐겨찾기 추가, 편집 호출 구분
     */
    int intentAction = 0;
    
    /**
     * 인텐트에 담긴 액션에 따라서 화면에 보이는 위젯 개수를 변경하는 메소드 
     */
    private void initWidget() {
    	if(intentAction > 0) {	//일반 호출이 아닌 경우(추가/편집)
    		((View)findViewById(R.id.TVExpenseDate)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseDate)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.TVExpenseTag)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseTag)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.LLRepeat)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseRepeat)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.GONE);
    	}
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnExpenseDate);
        setAmountBtnClickListener(R.id.BtnExpenseAmount);
        setCategoryClickListener(R.id.BtnExpenseCategory);
        setPaymentToggleBtnClickListener();
        setTagButtonListener();
        setRepeatBtnClickListener(R.id.BtnExpenseRepeat);
        setBookmarkTvClickListener(R.id.TVExpenseBookmark);
        setDeleteBtnListener(R.id.BtnExpenseDelete);
	}
    
    @Override
    protected void initialize() {
    	//mLLBookark = (LinearLayout) findViewById(R.id.LLBookmark); TODO 기존 북마크가 들어갔던 뷰
    	mSlidingDrawer =  (SlidingDrawer) findViewById(R.id.SlidingDrawer);
    	super.initialize();
    }
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	if (mInputMode == InputMode.ADD_MODE) {
    		setTitleBtnText(FmTitleLayout.BTN_LEFT_01, "수입");
            setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);
    	}
    }
    
    @Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(InputExpenseLayout.this, InputIncomeLayout.class);
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
    		
    		if (saveNewItem(null) == true) {
    			updateAccountBalance();
    			saveRepeat();
    		}
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
//    		PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
//    		if(mExpensItem.getPaymentMethod().getType() == PaymentMethod.ACCOUNT) {
//    			PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
//    			fromItem = accountMethod.getAccount();
//
//    			long fromItemBalance = fromItem.getBalance();
//
//				fromItem.setBalance(fromItemBalance + beforeAmount - mExpensItem.getAmount());
//				DBMgr.updateAccount(fromItem);
//    		}
    		saveUpdateItem();
    	}
    }

    protected void updateAccountBalance() {
    	PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
    	int paymentMethodType = mExpensItem.getPaymentMethod().getType();
    	AccountItem account = null; 
    	
    	if (paymentMethodType == PaymentMethod.CASH) {
    		account = DBMgr.getAccountMyPoctet();
    	} 
    	else if (paymentMethodType == PaymentMethod.ACCOUNT) {
    		PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
			account = accountMethod.getAccount();
    	}
    	
    	if (account != null) {
    		long remainPocketBalace = account.getBalance() - mExpensItem.getAmount();
    		account.setBalance((remainPocketBalace < 0L) ? 0L : remainPocketBalace);
    		DBMgr.updateAccount(account);
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
			findViewById(R.id.LLBookmarkSliding).setVisibility(View.GONE);
			findViewById(R.id.BtnExpenseDelete).setVisibility(View.GONE);
			setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.INVISIBLE);
		}
		else {
			findViewById(R.id.BtnExpenseCategory).setEnabled(true);
			findViewById(R.id.BtnExpenseAmount).setEnabled(true);
			findViewById(R.id.TBExpenseMethodCard).setVisibility(View.VISIBLE);
			findViewById(R.id.LLRepeat).setVisibility(View.VISIBLE);
			findViewById(R.id.LLBookmarkSliding).setVisibility(View.VISIBLE);
			findViewById(R.id.BtnExpenseDelete).setVisibility(View.VISIBLE);
			setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);
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
	
	protected void initBookmark() {
		//if (mLLBookark == null) return;
		((ImageButton)findViewById(R.id.BTBookmarkAdd)).setOnClickListener(mSlidingTitleBarBtn);
		((Button)findViewById(R.id.BTBookmarkIncome)).setOnClickListener(mSlidingTitleBarBtn);
		((Button)findViewById(R.id.BTBookmarkExpense)).setOnClickListener(mSlidingTitleBarBtn);
		
//		expenseAllItems = DBMgr.getAllItems(ExpenseItem.TYPE);
//		
//		ArrayList<CategoryTemp> categorysTemp = new ArrayList<CategoryTemp>();
//		
//		for (int i = expenseAllItems.size()-1 ; i >= 0 ; i--) {
//			Boolean duplicationCheck = false;
//			CategoryTemp categoryTemp = new CategoryTemp();
//			if (categorysTemp.isEmpty() == true) {				
//				categoryTemp.item = expenseAllItems.get(i);
//				categoryTemp.count++;
//				categorysTemp.add(categoryTemp);
//			} else {
//				for (int j=0; j < categorysTemp.size(); j++) {
//
//					if (categorysTemp.get(j).item.getCategory().getName().equals(expenseAllItems.get(i).getCategory().getName()) && 
//							categorysTemp.get(j).item.getSubCategory().getName().equals(expenseAllItems.get(i).getSubCategory().getName())  &&
//							categorysTemp.get(j).item.getAmount() == expenseAllItems.get(i).getAmount()) {
//						categorysTemp.get(j).count++;
//						duplicationCheck = true;
//						break;
//					} 
//				}
//				if (duplicationCheck == false) {
//					categoryTemp.item = expenseAllItems.get(i);
//					categoryTemp.count++;
//					categorysTemp.add(categoryTemp);
//				}
//			}			
//		}
//		
//		itemsTemp = new ArrayList<FinanceItem>();
//		
//		for (int i=0; i < categorysTemp.size(); i++) {
//			itemsTemp.add(null);
//		}
//
//		for (int i=0; i < categorysTemp.size(); i++) {
//			int idx=0;
//			for (int j=0; j < categorysTemp.size(); j++) {
//				if (i==j) {
//					
//				}else {
//					if (categorysTemp.get(i).count < categorysTemp.get(j).count) {
//						idx++;
//					} else if (categorysTemp.get(i).count == categorysTemp.get(j).count && i>j) {
//						idx++;
//					}
//				}
//			}
//
//			itemsTemp.set(idx, categorysTemp.get(i).item);			
//		}
//
//		// TODO 즐겨찾기에 들어가는 데이터를 만드는 부분 - 데이터 가져오는 방법은 동일 하므로 일부 그대로 응용
//		/*for (int i=0; i < 5; i++) {
//			if (itemsTemp.size() - 1 - i < 0) break;
//			Button btnBookmark = new Button(getApplicationContext());
//			btnBookmark.setText(itemsTemp.get(i).getCategory().getName() + " - " + itemsTemp.get(i).getSubCategory().getName()
//					+ "\t\t" + String.format("%,d원", itemsTemp.get(i).getAmount()));
//			btnBookmark.setId(itemsTemp.get(i).getID());
//			mLLBookark.addView(btnBookmark, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//			btnBookmark.setOnClickListener(mClickListener);
//		}*/
//		for (int i=0; i < 5; i++) {
//			if (itemsTemp.size() - 1 - i < 0) break;
//			BookMarkItemData bookMarkItemData = new BookMarkItemData();
//			bookMarkItemData.iconResource = R.drawable.icon;
//			bookMarkItemData.memo = itemsTemp.get(i).getMemo();
//			bookMarkItemData.category = itemsTemp.get(i).getCategory().getName() + " - " + itemsTemp.get(i).getSubCategory().getName();
//			
//			switch(((ExpenseItem)itemsTemp.get(i)).getType()) {	// FIXME 수정 필요
//			case PaymentMethod.CASH:
//				bookMarkItemData.method = "현금";
//				break;
//			case PaymentMethod.CARD:
//				bookMarkItemData.method = "카드";
//				break;
//			case PaymentMethod.ACCOUNT:
//				bookMarkItemData.method = "계좌";
//				break;
//			}
//			
//			bookMarkItemData.amount = String.format("%,d원", itemsTemp.get(i).getAmount());
//			//btnBookmark.setOnClickListener(mClickListener);
//			bookMarkItemDatas.add(bookMarkItemData);
//		}
		bookmarkList = (ListView)findViewById(R.id.LLBookmark);
//		//Toast.makeText(this, ""+bookMarkItemDatas.size(), Toast.LENGTH_LONG).show();	//아이템을 제대로 가지고 오는지 확인
//		
		bookmarkDrag = (RelativeLayout)findViewById(R.id.BookMarkDragItem);
		icon = (ImageView)findViewById(R.id.BookMarkItemIcon);
		title = (TextView)findViewById(R.id.BookMarkItemTitle);
		category = (TextView)findViewById(R.id.BookMarkItemCategory);
		method = (TextView)findViewById(R.id.BookMarkItemMethod);
		amount = (TextView)findViewById(R.id.BookMarkItemAmount);
//		
		updateOpenUsedItem();
//		bookMarkAdapter = new BookMarkAdapter(this, R.layout.input_bookmark_item, DBMgr.getOpenUsedItems(ExpenseItem.TYPE));
//		bookmarkList.setAdapter(bookMarkAdapter);
		bookmarkList.setOnItemClickListener(mItemClickListener);
		bookmarkList.setOnItemLongClickListener(mItemLongClickListener);
		bookmarkList.setOnTouchListener(mItemTouchListener);
	}
	//드래그 앤 드롭에서 사용할 뷰들 정의
	ListView bookmarkList;
	RelativeLayout bookmarkDrag;
	ImageView icon;
	TextView title;
	TextView category;
	TextView method;
	TextView amount;
	//드래그 앤 드롭에서 사용할 데이터
	ArrayList<OpenUsedItem> bookMarkItemDatas = new ArrayList<OpenUsedItem>();
	BookMarkAdapter bookMarkAdapter;
	float mPositionX, mPositionY;
	int mPosition;
	boolean longTouch = false;
	boolean isIncome = false;	//false면 Expense
	public static boolean editableList = false;
	
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> items, View v, int position, long id) {
			if(!longTouch) {
				((Button)findViewById(R.id.BtnExpenseCategory)).setText(((TextView)v.findViewById(R.id.BookMarkItemCategory)).getText());
				((Button)findViewById(R.id.BtnExpenseAmount)).setText(((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText());
				((EditText)findViewById(R.id.ETExpenseMemo)).setText(((TextView)v.findViewById(R.id.BookMarkItemTitle)).getText());
				
//				((ToggleButton)findViewById(R.id.TBExpenseMethodCash)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("현금"))?true:false);
//				((ToggleButton)findViewById(R.id.TBExpenseMethodCard)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("카드"))?true:false);
//				((ToggleButton)findViewById(R.id.TBExpenseMethodAccount)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("계좌"))?true:false);
				
				
				//mExpensItem = (ExpenseItem)itemsTemp.get(position);
				mExpensItem = new ExpenseItem();
				
				Category category = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getCategory();
				Category subCategory = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getSubCategory();
				mExpensItem.setCategory(category.getID(), category.getName());
				mExpensItem.setSubCategory(subCategory.getID(), subCategory.getName());
				mExpensItem.setAmount(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getAmount());
				mExpensItem.setCard(((ExpenseItem)(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem())).getCard());
				mExpensItem.setPaymentMethod((((ExpenseItem)(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem())).getPaymentMethod()));

				setItem(mExpensItem);
				updatePaymentMethod();

				if (popupBookmark != null) {
					popupBookmark.dismiss();
				}
				
				if (mSlidingDrawer != null) {
					mSlidingDrawer.toggle();
				}
			}
		}
	};
	View.OnClickListener mSlidingTitleBarBtn = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BTBookmarkAdd:
				inputOpenUsedItem();
//				if(!editableList) {
//					editableList = true;	//수정 가능 상태
//				}
//				else { 
//					editableList = false;	//수정 불가능 상태
//				}
				break;
			case R.id.BTBookmarkIncome:
			case R.id.BTBookmarkExpense:
				if(!isIncome) {
					((Button)findViewById(R.id.BTBookmarkIncome)).setBackgroundColor(Color.parseColor("#006600"));
					((Button)findViewById(R.id.BTBookmarkIncome)).setTextColor(Color.parseColor("#ffffffff"));
					((Button)findViewById(R.id.BTBookmarkExpense)).setBackgroundColor(Color.parseColor("#77ee77"));
					((Button)findViewById(R.id.BTBookmarkExpense)).setTextColor(Color.parseColor("#44000000"));
					isIncome = true;
					//getIncomeItemList();
				} else {
					((Button)findViewById(R.id.BTBookmarkExpense)).setBackgroundColor(Color.parseColor("#006600"));
					((Button)findViewById(R.id.BTBookmarkExpense)).setTextColor(Color.parseColor("#ffffffff"));
					((Button)findViewById(R.id.BTBookmarkIncome)).setBackgroundColor(Color.parseColor("#77ee77"));
					((Button)findViewById(R.id.BTBookmarkIncome)).setTextColor(Color.parseColor("#44000000"));
					isIncome = false;
					//getExpenseItemList();
				}
			}
			updateOpenUsedItem();
		}
	};
	AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(320, 50);
		public boolean onItemLongClick(AdapterView<?> item, View v, int position, long id) {
			icon.setImageDrawable(((ImageView)v.findViewById(R.id.BookMarkItemIcon)).getDrawable());
			title.setText(((TextView)v.findViewById(R.id.BookMarkItemTitle)).getText());
			category.setText(((TextView)v.findViewById(R.id.BookMarkItemCategory)).getText());
			method.setText(((TextView)v.findViewById(R.id.BookMarkItemMethod)).getText());
			amount.setText(((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText());
			
			//layoutParams.leftMargin = (int) mPositionX - (layoutParams.width / 2);
			layoutParams.topMargin = (int) mPositionY - (layoutParams.height / 2) - 70;
			bookmarkDrag.setLayoutParams(layoutParams);
			
			bookmarkDrag.setVisibility(View.VISIBLE);
			
			longTouch = true;
			
			//하나의 아이템을 저장해둘 데이터 객체
			mPosition = position;	//드롭 이벤트를 발생 시킬 경우에 데이터 객체에서 해당 내용을 삭제하기 위한 포지션
			
			return false;
		}
	};
	View.OnTouchListener mItemTouchListener = new View.OnTouchListener() {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(320, 50);
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				// 전달 되는 View 객체가 이벤트가 일어난 위치가 아니라 항상 아이템의 0번을 가져오므로(현재 보여지는 리스트 뷰의 최상단 아이템을 가져오는 것으로 생각 됨)
				// 직접 사용할 수 없으므로 다른 이벤트 리스너에서 사용할 수 있도록 이벤트가 발생된 위치 값만 세팅
				mPositionX = event.getRawX();
				mPositionY = event.getRawY();
				
			} else if(event.getAction() == MotionEvent.ACTION_MOVE) {
				// 이벤트가 발생되면 항상 실행되지만 화면에 보여지지 않기 때문에 별도의 처리 안함.
				// 이벤트가 빠르게 발생되기 때문에 자주 사용되는 변수들은 전역으로 설정하는 것이 좋음.
				if(longTouch) {
					//layoutParams.leftMargin = (int) event.getRawX() - (layoutParams.width / 2);	//터치가 이루어진 위치에 이미지의 중심이 오도록 함
					layoutParams.topMargin = (int) event.getRawY() - (layoutParams.height / 2) - 70;
					bookmarkDrag.setLayoutParams(layoutParams);
					
					return true;
				}
			} else if(event.getAction() == MotionEvent.ACTION_UP) {
				// 드래그가 끝 났을 경우에 처리해야할 부분을 정의
				// 별다른 처리가 없었으므로 이미지만 GONE 시킴
				if(longTouch) {
					bookmarkDrag.setVisibility(View.GONE);

					//Toast.makeText(ListViewItemTest.this, ""+((ListView)v).getFirstVisiblePosition(), Toast.LENGTH_LONG).show();
					int topChildView = ((ListView)v).getFirstVisiblePosition();	//보여지는 최상단의 아이템의 인덱스를 얻어옴
					int chk = (int) (event.getY() / ((ListView)v).getChildAt(0).getHeight());
					
					if((topChildView + chk) >= 0) {
						ArrayList<OpenUsedItem> markItemDatas = DBMgr.getOpenUsedItems(ExpenseItem.TYPE);
						OpenUsedItem markItemData = markItemDatas.get(mPosition);
						markItemDatas.remove(mPosition);
						markItemDatas.add(topChildView + chk, markItemData);
						
						updateOpenUsedItem();	//값이 유지되지 않고 DB에서 새로 얻어와서 이동 갱신 불가상태
						bookmarkList.setSelectionFromTop(topChildView, 0);	//어댑터가 다시 세팅되면 0번 아이템이 선택되기 때문에 기억시켜둔 마지막 위치로 이동
					}
					longTouch = false;
					return true;
				}
			}
			return false;
		}
	};
	
	
	
	
	protected void setBookmark () {
//		popupviewBookmark = View.inflate(getApplicationContext(), R.layout.bookmark_expense_popup, null);
//		popupBookmark = new PopupWindow(popupviewBookmark, 320, 300, true);
//		
//		
//		LinearLayout btnlist = (LinearLayout) popupviewBookmark.findViewById(R.id.bookmarkexpensepopupsub2);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		
//		expenseAllItems = DBMgr.getAllItems(ExpenseItem.TYPE);
//				
//		ArrayList<CategoryTemp> categorysTemp = new ArrayList<CategoryTemp>();
//		
//		for (int i = expenseAllItems.size()-1 ; i >= 0 ; i--) {
//			Boolean duplicationCheck = false;
//			CategoryTemp categoryTemp = new CategoryTemp();
//			if (categorysTemp.isEmpty() == true) {				
//				categoryTemp.item = expenseAllItems.get(i);
//				categoryTemp.count++;
//				categorysTemp.add(categoryTemp);
//			} else {
//				for (int j=0; j < categorysTemp.size(); j++) {
//
//					if (categorysTemp.get(j).item.getCategory().getName().equals(expenseAllItems.get(i).getCategory().getName()) && 
//							categorysTemp.get(j).item.getSubCategory().getName().equals(expenseAllItems.get(i).getSubCategory().getName())  &&
//							categorysTemp.get(j).item.getAmount() == expenseAllItems.get(i).getAmount()) {
//						categorysTemp.get(j).count++;
//						duplicationCheck = true;
//						break;
//					} 
//				}
//				if (duplicationCheck == false) {
//					categoryTemp.item = expenseAllItems.get(i);
//					categoryTemp.count++;
//					categorysTemp.add(categoryTemp);
//				}
//			}			
//		}
//		
//		itemsTemp = new ArrayList<FinanceItem>();
//		
//		for (int i=0; i < categorysTemp.size(); i++) {
//			itemsTemp.add(null);
//		}
//
//		for (int i=0; i < categorysTemp.size(); i++) {
//			int idx=0;
//			for (int j=0; j < categorysTemp.size(); j++) {
//				if (i==j) {
//					
//				}else {
//					if (categorysTemp.get(i).count < categorysTemp.get(j).count) {
//						idx++;
//					} else if (categorysTemp.get(i).count == categorysTemp.get(j).count && i>j) {
//						idx++;
//					}
//				}
//			}
//
//			itemsTemp.set(idx, categorysTemp.get(i).item);
//		}
//
//		for (int i=0; i < 5; i++) {
//			if (itemsTemp.size() - 1 - i < 0) break;
//			Button btnBookmark = new Button(getApplicationContext());
//			btnBookmark.setText(itemsTemp.get(i).getCategory().getName() + " - " + itemsTemp.get(i).getSubCategory().getName()
//					+ "\t\t" + String.format("%,d원", itemsTemp.get(i).getAmount()));
//			btnBookmark.setId(itemsTemp.get(i).getID());
//			btnlist.addView(btnBookmark, params);
//			btnBookmark.setOnClickListener(mClickListener);
//		}
//				
//		Button btnBack = (Button) popupviewBookmark.findViewById (R.id.bookmarkBack);
//		btnBack.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				popupBookmark.dismiss();
//			}
//		});
		

		//popupBookmark.showAtLocation(linear, Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
	//	popupBookmark.showAsDropDown(anchor, xoff, yoff)
//		popupBookmark.update();
		
//		Animation Ani = AnimationUtils.loadAnimation(this, R.anim.popup_effect);
//		linear.startAnimation(Ani);
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

					if (popupBookmark != null) {
						popupBookmark.dismiss();
					}
					
					if (mSlidingDrawer != null) {
						mSlidingDrawer.toggle();
					}
					
				}				
			}
		}
	};
	//자주 사용 되는 지출 구현 end	
	
	protected void inputOpenUsedItem() {
		Intent intent = new Intent(this, InputExpenseLayout.class);
		intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
		intent.putExtra("Action", 1);	//추가 화면 호출
		startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
	}
	
	@Override
	protected void updateOpenUsedItem() {
		bookMarkAdapter = new BookMarkAdapter(this, R.layout.input_bookmark_item, DBMgr.getOpenUsedItems(ExpenseItem.TYPE));
		bookmarkList.setAdapter(bookMarkAdapter);
	}
	
}
