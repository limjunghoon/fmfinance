package com.fletamuto.sptb;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.OpenUsedItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.db.ExpenseDBConnector;
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
	
	/** 지출내역*/
	private ExpenseItem mExpensItem;
	
	private LinearLayout linear;
	private View popupviewBookmark;
	private PopupWindow popupBookmark, popupBookmarkEdit;
	private TextView tv;
	private ArrayList<FinanceItem> itemsTemp;
	//달력 입력과 자주 사용 되는 지출을 위해 end
	
	private SlidingDrawer mSlidingDrawer;
	
	/**
     * ACTION_DEFAULT = 0, 지출 추가/편집 화면
     */
    private final static int ACTION_DEFAULT = 0;
    /**
     * ACTION_BOOMARK_EDIT = 1, 즐겨찾기 추가/편집 상태에서 호출 됨
     */
    private final static int ACTION_BOOMARK_EDIT = 1;
    /**
     * ACTION_BOOMARK_OTHER_ACTIVITY = 2, 다른 타입의 즐겨찾기 추가/편집 호출을 요청
     */
    private final static int ACTION_BOOMARK_OTHER_ACTIVITY = 2;
    /**
     * ACTION_BOOMARK_EDIT_ACTIVITY = 3, 다른 타입의 즐겨찾기 추가/편집 호출을 요청
     */
    private final static int ACTION_BOOMARK_EDIT_ACTIVITY = 3;
    /**
     * ACTION_SMS_RECEIVE = 4, SMS 수신 메시지에 의한 실행
     */
    public final static int ACTION_SMS_RECEIVE = 4;
    /**
     * 호출한 Intent의 상태 값 저장
     */
    private int intentAction = ACTION_DEFAULT;
    
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
	boolean isLongTouch = false;
	boolean isIncome = false;	//false면 Expense
	boolean isEditable = false;
	boolean isEditableList = false;
	  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense, true);
        
        initChildView();
        updateChildView();
        
        switch(getIntent().getIntExtra("Action", 0)) {
        case ACTION_DEFAULT:	//일반 호출
            initBookmark();
        	break;
        case ACTION_BOOMARK_EDIT:	//즐겨찾기 추가/편집 호출
        	intentAction = ACTION_BOOMARK_EDIT;
        	mExpensItem.setOpenUsedItem(true);
        	if(getIntent().getBooleanExtra("Fill", false)) {
        		fill();
        	}
        	break;
        case ACTION_BOOMARK_OTHER_ACTIVITY :	//서로 다른 타입의 즐겨찾기를 선택시 상대 액티비티에 요청 처리
        	intentAction = ACTION_BOOMARK_OTHER_ACTIVITY;
        	initBookmark();
        	if(getIntent().getBooleanExtra("Fill", false)) {
        		fill();
        	}
			if (popupBookmark != null) {
				popupBookmark.dismiss();
			}
			
			if (mSlidingDrawer != null) {
				mSlidingDrawer.toggle();
			}
        	break;
        case ACTION_BOOMARK_EDIT_ACTIVITY:	//다른 타입 즐겨찾기 추가/편집 호출
        	intentAction = ACTION_BOOMARK_EDIT_ACTIVITY;
        	if(getIntent().getBooleanExtra("Fill", false)) {
        		fill();
        	}
			if (popupBookmark != null) {
				popupBookmark.dismiss();
			}
			
			if (mSlidingDrawer != null) {
				mSlidingDrawer.toggle();
			}
        	//inputIncomeOpenUsedItem();
        	break;
        case ACTION_SMS_RECEIVE:
        	intentAction = ACTION_SMS_RECEIVE;
        	if(getIntent().getSerializableExtra("SMS") != null) {
        		mExpensItem = (ExpenseItem) getIntent().getSerializableExtra("SMS");
        		setItem(mExpensItem);
        		updateChildView();	//TODO
        	}
        	break;
        }
        initWidget();
        
    }
    
    @Override
    public void setTitleName() {
    	int action = getIntent().getIntExtra("Action", ACTION_DEFAULT);
    	
    	if (action == ACTION_BOOMARK_EDIT || action == ACTION_BOOMARK_EDIT_ACTIVITY) {
    		setTitle(getResources().getString(R.string.input_bookmark_name));
    	}
    	else {
    		String titleName = getIntent().getStringExtra(MsgDef.ExtraNames.GET_TITLE);
    		if (titleName == null) {
    			setTitle(getResources().getString(R.string.input_expense_name));
    		}
    		else {
    			setTitle(titleName);
    		}
    		
    	}	
    	super.setTitleName();
    }
    
    /**
     * 자식뷰 초기화
     */
    public void initChildView() {
    	if (intentAction == ACTION_DEFAULT) {
    		findViewById(R.id.LLOption).setVisibility(View.VISIBLE);
    	}
    	
    	updateChildViewState();
	}

    /**
     * 인텐트에 담긴 액션에 따라서 화면에 보이는 위젯 개수를 변경하는 메소드 
     */
    private void initWidget() {
    	if(intentAction == ACTION_BOOMARK_EDIT || intentAction == ACTION_BOOMARK_EDIT_ACTIVITY) {	//추가/편집인 경우에 처리
    		((View)findViewById(R.id.TVExpenseDate)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseDate)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.TVExpenseTag)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseTag)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.LLRepeat)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseRepeat)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.GONE);
    		((View)findViewById(R.id.LLOption)).setVisibility(View.GONE);
    	}
    	
    	if (mExpensItem.getCategory().getExtndType() == ItemDef.ExtendAssets.NONE) {
    		((View)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.GONE);
    	}
    	
    	if (mInputMode == InputMode.ADD_MODE) {
    		((View)findViewById(R.id.LLOption)).setVisibility(View.GONE);
    	}
    	
    	if(intentAction == ACTION_SMS_RECEIVE) {
    		((View)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.GONE);
    		setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.INVISIBLE);
    		setSaveBtnClickListener(R.id.BtnTitleRigth01);
    	}
    }
    @Override
    protected void removeNotification() {
    	super.removeNotification();
    	NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(getIntent().getIntExtra("NotificationID", 0));
    }
    /**
     * 편집으로 넘길 때 내용을 미리 채우기 위한 메소드
     */
    private void fill() {
    	String fillText[] = getIntent().getStringArrayExtra("FillText");
    	((Button)findViewById(R.id.BtnExpenseCategory)).setText(fillText[0]);
		((Button)findViewById(R.id.BtnExpenseAmount)).setText(fillText[1]);
		((EditText)findViewById(R.id.ETExpenseMemo)).setText(fillText[2]);
		
		mExpensItem = new ExpenseItem();
		
		int position = getIntent().getIntExtra("FillPosition", 0);
		
		OpenUsedItem expenseItem = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position);
		
		Category category = expenseItem.getItem().getCategory();
		Category subCategory = expenseItem.getItem().getSubCategory();
		mExpensItem.setCategory(category.getID(), category.getName());
		mExpensItem.setSubCategory(subCategory.getID(), subCategory.getName());
		mExpensItem.setAmount(expenseItem.getItem().getAmount());
		mExpensItem.setCard(((ExpenseItem)(expenseItem.getItem())).getCard());
		mExpensItem.setPaymentMethod((((ExpenseItem)(expenseItem.getItem())).getPaymentMethod()));

		setItem(mExpensItem);
		updatePaymentMethod();

		if (popupBookmark != null) {
			popupBookmark.dismiss();
		}
		
		if (mSlidingDrawer != null) {
			mSlidingDrawer.toggle();
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
        setDeleteBtnListener(R.id.BtnExpenseDelete);
        
        setAddBtnClickListener(R.id.BtnAddOpenUsedItem);
	}
    
    private void setAddBtnClickListener(int btnID) {
    	findViewById(btnID).setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {
				mExpensItem.setMemo(((EditText)findViewById(R.id.ETExpenseMemo)).getText().toString());
				setItem(mExpensItem);
				new ExpenseDBConnector().addItem(mExpensItem);
    			addOpenUsedItem(mExpensItem);
    			updateOpenUsedItem();
			}
    	});
    }
    
    @Override
    protected void initialize() {
    	super.initialize();
    	mSlidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
    	
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
    	intent.putExtra("Action", intentAction);	//현재 호출된 상태의 Intent를 전달 - 지출 즐겨찾기 추가/편집 상태인 경우에 수입 즐겨찾기 추가/편집으로 가기 위한 값  
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
    		saveUpdateItem();
    	}
    }

    /**
     * 잔액을 갱신한다.
     */
    protected void updateAccountBalance() {
    	PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
    	int paymentMethodType = mExpensItem.getPaymentMethod().getType();
    	AccountItem account = null; 
    	
    	if (paymentMethodType == PaymentMethod.CASH) {
    		account = DBMgr.getAccountMyPocket();
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
		setItem(mExpensItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mExpensItem = (ExpenseItem)DBMgr.getItem(ExpenseItem.TYPE, id);
		if (mExpensItem == null) return false;
		
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
			if (mExpensItem.getCategory().getExtndType() == ItemDef.NOT_CATEGORY) {
				categoryText = String.format("%s", mExpensItem.getCategory().getName());
			} else if (mExpensItem.getSubCategory().getID() == -1 || mExpensItem.getSubCategory().getName() == "") {
				categoryText = String.format("%s", mExpensItem.getCategory().getName());
			} else {
				categoryText = String.format("%s - %s", mExpensItem.getCategory().getName(), mExpensItem.getSubCategory().getName());
			}
			
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
					updateRepeat(Repeat.ONCE, 0);
				}
			}
		}
		else if (requestCode == MsgDef.ActRequest.ACT_BALANCE_AFTER_CHCKE_ITEM) {
			AccountItem account = getPaymentAccount();
			if (account == null) return;
			
			account.setBalance(data.getLongExtra(MsgDef.ExtraNames.AMOUNT, 0L));
			DBMgr.updateAccount(account);
			
			saveItem();		
			setResult(RESULT_OK, new Intent());
			finish();
		} else if(requestCode == MsgDef.ActRequest.ACT_OPEN_USED_ITEM) {
			if(resultCode == RESULT_OK)
				updateOpenUsedItem();
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
				intent.putExtra(MsgDef.ExtraNames.INSTALLMENT_PLAN_MODE, true);
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
		updateRepeatText(R.id.BtnExpenseRepeat);
	}
	
	protected void updateChildViewState() {
		Category category = mExpensItem.getCategory();
		
		if (category.getExtndType() == ItemDef.ExtendAssets.NONE) {
			findViewById(R.id.BtnExpenseCategory).setEnabled(false);
			findViewById(R.id.BtnExpenseAmount).setEnabled(false);
			findViewById(R.id.TBExpenseMethodCard).setVisibility(View.GONE);
			findViewById(R.id.LLRepeat).setVisibility(View.GONE);
			findViewById(R.id.BtnExpenseDelete).setVisibility(View.GONE);
			findViewById(R.id.LLBookmarkSliding).setVisibility(View.GONE);
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
			mExpensItem.setRepeatOnce();
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
    	
    	if (mExpensItem.getCategory().getID() == -1) {
    		ArrayList<Category> nothginCategory = DBMgr.getCategory(ExpenseItem.TYPE, ItemDef.NOT_CATEGORY);
    		if (nothginCategory.size() != 0) {
    			Category mainCategory = nothginCategory.get(0);
    			mExpensItem.setCategory(mainCategory);
    		}
    	}
    	
    	if (mExpensItem.getSubCategory().getID() == -1) {
    		ArrayList<Category> nothginSubCategory = DBMgr.getSubCategory(ExpenseItem.TYPE, -1);
    		if (nothginSubCategory.size() != 0) {
    			Category subCategory = nothginSubCategory.get(0);
    			mExpensItem.setSubCategory(subCategory);
    		}
    	}
	}
	
	
	protected void initBookmark() {
		//if (mLLBookark == null) return;
		((LinearLayout)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.VISIBLE);
		((ImageButton)findViewById(R.id.BTBookmarkAdd)).setOnClickListener(mSlidingTitleBarBtn);
		((Button)findViewById(R.id.BTBookmarkIncome)).setOnClickListener(mSlidingTitleBarBtn);
		((Button)findViewById(R.id.BTBookmarkExpense)).setOnClickListener(mSlidingTitleBarBtn);
		
		bookmarkList = (ListView)findViewById(R.id.LLBookmark);

		bookmarkDrag = (RelativeLayout)findViewById(R.id.BookMarkDragItem);
		icon = (ImageView)findViewById(R.id.BookMarkItemIcon);
		title = (TextView)findViewById(R.id.BookMarkItemTitle);
		category = (TextView)findViewById(R.id.BookMarkItemCategory);
		method = (TextView)findViewById(R.id.BookMarkItemMethod);
		amount = (TextView)findViewById(R.id.BookMarkItemAmount);

		updateOpenUsedItem();
		bookmarkList.setOnItemClickListener(mItemClickListener);
		bookmarkList.setOnItemLongClickListener(mItemLongClickListener);
		bookmarkList.setOnTouchListener(mItemTouchListener);
	}
	
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> items, View v, int position, long id) {
			OpenUsedItem incomeItem = null;
			OpenUsedItem expenseItem = null;
			if(isIncome)
				incomeItem = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position);
			else
				expenseItem = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position);
			
			if(!isEditableList) {
				if(!isIncome) {
					((Button)findViewById(R.id.BtnExpenseCategory)).setText(((TextView)v.findViewById(R.id.BookMarkItemCategory)).getText());
					((Button)findViewById(R.id.BtnExpenseAmount)).setText(((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText());
					((EditText)findViewById(R.id.ETExpenseMemo)).setText(((TextView)v.findViewById(R.id.BookMarkItemTitle)).getText());
					
					mExpensItem = new ExpenseItem();
					
					Category category = expenseItem.getItem().getCategory();
					Category subCategory = expenseItem.getItem().getSubCategory();
					mExpensItem.setCategory(category.getID(), category.getName());
					mExpensItem.setSubCategory(subCategory.getID(), subCategory.getName());
					mExpensItem.setAmount(expenseItem.getItem().getAmount());
					mExpensItem.setCard(((ExpenseItem)(expenseItem.getItem())).getCard());
					mExpensItem.setPaymentMethod((((ExpenseItem)(expenseItem.getItem())).getPaymentMethod()));

					setItem(mExpensItem);
					updatePaymentMethod();

					if (popupBookmark != null) {
						popupBookmark.dismiss();
					}
					
					if (mSlidingDrawer != null) {
						mSlidingDrawer.toggle();
					}
				} else {
					String[] fillText = new String[3];
					fillText[0] = incomeItem.getItem().getCategory().getName();
					fillText[1] = String.valueOf(incomeItem.getItem().getAmount());
					fillText[2] = incomeItem.getItem().getMemo();
					
					Intent intent = new Intent(InputExpenseLayout.this, InputIncomeLayout.class);
					intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
					intent.putExtra("Action", ACTION_BOOMARK_OTHER_ACTIVITY);	//상대 액티비티 요청 처리
					intent.putExtra("Fill", true);
					intent.putExtra("FillText", fillText);
					intent.putExtra("FillPosition", position);
					startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
					finish();
				}
			} else {
				if(!isEditable) {
					isEditable = true;
					return;
				} else {
					if(!isIncome) {
						String[] fillText = new String[3];
						fillText[0] = expenseItem.getItem().getCategory().getName() + " - " + expenseItem.getItem().getSubCategory().getName();
						fillText[1] = String.valueOf(expenseItem.getItem().getAmount());
						fillText[2] = expenseItem.getItem().getMemo();
						
						Intent intent = new Intent(InputExpenseLayout.this, InputExpenseLayout.class);
						intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
						intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//추가 화면 호출
						intent.putExtra("Fill", true);
						intent.putExtra("FillPosition", position);
						intent.putExtra("FillText", fillText);
						startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
					} else {
						String[] fillText = new String[3];
						fillText[0] = incomeItem.getItem().getCategory().getName();
						fillText[1] = String.valueOf(incomeItem.getItem().getAmount());
						fillText[2] = incomeItem.getItem().getMemo();
						
						Intent intent = new Intent(InputExpenseLayout.this, InputIncomeLayout.class);
						intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
						intent.putExtra("Action", ACTION_BOOMARK_EDIT_ACTIVITY);	//상대 액티비티 요청 처리
						intent.putExtra("Fill", true);
						intent.putExtra("FillText", fillText);
						intent.putExtra("FillPosition", position);
						startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
					}
				}
			}
		}
	};
	View.OnClickListener mSlidingTitleBarBtn = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BTBookmarkAdd:
				inputOpenUsedItem();
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

			if(!isEditableList) {
				isEditableList = true;	//수정 가능 상태
				updateOpenUsedItem();
			} else {
				icon.setImageDrawable(((ImageView)v.findViewById(R.id.BookMarkItemIcon)).getDrawable());
				title.setText(((TextView)v.findViewById(R.id.BookMarkItemTitle)).getText());
				category.setText(((TextView)v.findViewById(R.id.BookMarkItemCategory)).getText());
				method.setText(((TextView)v.findViewById(R.id.BookMarkItemMethod)).getText());
				amount.setText(((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText());
				
				//layoutParams.leftMargin = (int) mPositionX - (layoutParams.width / 2);
				layoutParams.topMargin = (int) mPositionY - (layoutParams.height / 2) - 70;
				bookmarkDrag.setLayoutParams(layoutParams);
				
				bookmarkDrag.setVisibility(View.VISIBLE);
				
				isLongTouch = true;
				
				mPosition = position;	//드롭 이벤트를 발생 시킬 경우에 데이터 객체에서 해당 내용을 삭제하기 위한 포지션
			}
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
				if(isLongTouch) {
					//layoutParams.leftMargin = (int) event.getRawX() - (layoutParams.width / 2);	//터치가 이루어진 위치에 이미지의 중심이 오도록 함
					layoutParams.topMargin = (int) event.getRawY() - (layoutParams.height / 2) - 70;
					bookmarkDrag.setLayoutParams(layoutParams);
					
					return true;
				}
			} else if(event.getAction() == MotionEvent.ACTION_UP) {
				// 드래그가 끝 났을 경우에 처리해야할 부분을 정의
				// 별다른 처리가 없었으므로 이미지만 GONE 시킴
				if(isLongTouch) {
					bookmarkDrag.setVisibility(View.GONE);

					int topChildView = ((ListView)v).getFirstVisiblePosition();	//보여지는 최상단의 아이템의 인덱스를 얻어옴
					int chk = (int) (event.getY() / ((ListView)v).getChildAt(0).getHeight());
					int newPosition = topChildView + chk;
					
					if((topChildView + chk) >= 0) {
						ArrayList<OpenUsedItem> markItemDatas = null;
						if(!isIncome) {
							markItemDatas = DBMgr.getOpenUsedItems(ExpenseItem.TYPE);
						} else {
							markItemDatas = DBMgr.getOpenUsedItems(IncomeItem.TYPE);
						}
						
						OpenUsedItem markItemData = markItemDatas.get(mPosition);
						int size = markItemDatas.size();
						if(newPosition > size)
							newPosition = size;
						markItemDatas.add(newPosition, markItemData);
						if(newPosition >= mPosition)
							markItemDatas.remove(mPosition);
						else
							markItemDatas.remove(mPosition+1);
						
						//아이템 한개 순위만 변경 - 반복문 작성 해야 함 - 작성중
						for(int i = 0; i < size; i++) {
							DBMgr.updateOpenUsedItem(markItemDatas.get(i).getType(), markItemDatas.get(i).getID(), markItemDatas.get(i).getItem().getID(), i);
						}
						
						//동작 테스트 용
						updateOpenUsedItem();	//값이 유지되지 않고 DB에서 새로 얻어와서 이동 갱신 불가상태
						bookmarkList.setSelectionFromTop(topChildView, 0);	//어댑터가 다시 세팅되면 0번 아이템이 선택되기 때문에 기억시켜둔 마지막 위치로 이동
					}
					isLongTouch = false;
					return true;
				}
			}
			return false;
		}
	};
	
	@Override
	public void onBackPressed() {
		if(getIntent().getIntExtra("Action", ACTION_DEFAULT) > ACTION_DEFAULT) {
			super.onBackPressed();
		} else if(isEditableList) {
			isEditable = false;
			isEditableList = false;	//수정 불가능 상태
			updateOpenUsedItem();
		} else if(mSlidingDrawer.isOpened()) {
			mSlidingDrawer.animateClose();
		} else {
			super.onBackPressed();
		}
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
		Intent intent = new Intent(this, (isIncome)?InputIncomeLayout.class:InputExpenseLayout.class);
		intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
		intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//추가 화면 호출
		startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
	}
	
	protected void inputIncomeOpenUsedItem() {
		Intent intent = new Intent(this, InputIncomeLayout.class);
		intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
		intent.putExtra("Action", ACTION_BOOMARK_EDIT_ACTIVITY);	//추가 화면 호출
		startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
	}
	
	@Override
	protected void updateOpenUsedItem() {
		if(!isIncome) {
			bookMarkAdapter = new BookMarkAdapter(this, R.layout.input_bookmark_item, DBMgr.getOpenUsedItems(ExpenseItem.TYPE), isEditableList, new UpdateUsedItem());
		} else {
			bookMarkAdapter = new BookMarkAdapter(this, R.layout.input_bookmark_item, DBMgr.getOpenUsedItems(IncomeItem.TYPE), isEditableList, new UpdateUsedItem());
		}
		
		bookmarkList.setAdapter(bookMarkAdapter);
	}
	
	class UpdateUsedItem {
		public void updateUsedItemDelete() {
			updateOpenUsedItem();
		}
	}
	
	@Override
	protected boolean checkBalance() {
		final AccountItem account = getPaymentAccount();
		if (account == null) return true;
		
		// 최근수정된 날짜이전에 입력된 금액은 잔액을 수정하지 않는다. 
		if (account.getLastModifyDate().after(mExpensItem.getCreateDate())) {
			return true;
		}
		
		if (account.getBalance() < mExpensItem.getAmount()) {
			new AlertDialog.Builder(this)
			.setTitle("잔액초과")
			.setMessage("지출금액이 잔액을 초과했습니다.\n 잔액을 수정하시겠습니까?")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(InputExpenseLayout.this, InputAmountDialog.class);
					intent.putExtra(MsgDef.ExtraNames.AMOUNT, account.getBalance());
					startActivityForResult(intent, MsgDef.ActRequest.ACT_BALANCE_AFTER_CHCKE_ITEM);
				}
			})
			.setNegativeButton("무시", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					saveItem();		
					setResult(RESULT_OK, new Intent());
					finish();
				}
			})
			.show();
			
			return false;
		}
		
		return true;
	}
	
	public AccountItem getPaymentAccount() {
		PaymentMethod paymentMethod = mExpensItem.getPaymentMethod();
		AccountItem account = null;
		if (paymentMethod.getType() == PaymentMethod.CASH) {
			account = DBMgr.getAccountMyPocket();
		}
		else if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
			PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
			account = accountMethod.getAccount();
		}
		
		return account;
	}
}
