package com.fletamuto.sptb;


import java.util.ArrayList;

import android.app.AlertDialog;
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
/**
 * ���ο� ������ �Է��ϰų� ������ ���������� �����Ҷ� �����ִ� ���̾ƿ� â
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputExpenseLayout extends InputFinanceItemBaseLayout {
	protected final static int ACT_TAG_SELECTED = MsgDef.ActRequest.ACT_TAG_SELECTED;
	protected final static int ACT_CARD_SELECT = MsgDef.ActRequest.ACT_CARD_SELECT;
	protected final static int ACT_ACCOUNT_SELECT = MsgDef.ActRequest.ACT_ACCOUNT_SELECT;
	protected final static int ACT_BOOKMARK_SELECT = MsgDef.ActRequest.ACT_BOOKMARK_SELECT;
	
	/** ���⳻��*/
	private ExpenseItem mExpensItem;
	
	private LinearLayout linear;
	private View popupviewBookmark;
	private PopupWindow popupBookmark, popupBookmarkEdit;
	private TextView tv;
	private ArrayList<FinanceItem> itemsTemp;
	//�޷� �Է°� ���� ��� �Ǵ� ������ ���� end
	
	private SlidingDrawer mSlidingDrawer;
	
	/**
     * ACTION_DEFAULT = 0, ���� �߰�/���� ȭ��
     */
    private final static int ACTION_DEFAULT = 0;
    /**
     * ACTION_BOOMARK_EDIT = 1, ���ã�� �߰�/���� ���¿��� ȣ�� ��
     */
    private final static int ACTION_BOOMARK_EDIT = 1;
    /**
     * ACTION_BOOMARK_OTHER_ACTIVITY = 2, �ٸ� Ÿ���� ���ã�� �߰�/���� ȣ���� ��û
     */
    private final static int ACTION_BOOMARK_OTHER_ACTIVITY = 2;
    /**
     * ACTION_BOOMARK_EDIT_ACTIVITY = 3, �ٸ� Ÿ���� ���ã�� �߰�/���� ȣ���� ��û
     */
    private final static int ACTION_BOOMARK_EDIT_ACTIVITY = 3;
    /**
     * ȣ���� Intent�� ���� �� ����
     */
    private int intentAction = ACTION_DEFAULT;
    
    //�巡�� �� ��ӿ��� ����� ��� ����
	ListView bookmarkList;
	RelativeLayout bookmarkDrag;
	ImageView icon;
	TextView title;
	TextView category;
	TextView method;
	TextView amount;
	//�巡�� �� ��ӿ��� ����� ������
	ArrayList<OpenUsedItem> bookMarkItemDatas = new ArrayList<OpenUsedItem>();
	BookMarkAdapter bookMarkAdapter;
	float mPositionX, mPositionY;
	int mPosition;
	boolean isLongTouch = false;
	boolean isIncome = false;	//false�� Expense
	boolean isEditable = false;
	boolean isEditableList = false;
	  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense, true);
        
        updateChildView();
        
        //�޷��� �̿��� ��¥ �Է��� ����
/*
        final Intent intent = getIntent();
        linear = (LinearLayout) findViewById(R.id.inputAssetsExpense);
        popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
*/
        
        switch(getIntent().getIntExtra("Action", 0)) {
        case ACTION_DEFAULT:	//�Ϲ� ȣ��
        	setTitle(getResources().getString(R.string.input_expense_name));
            initBookmark();
        	break;
        case ACTION_BOOMARK_EDIT:	//���ã�� �߰�/���� ȣ��
        	setTitle(getResources().getString(R.string.input_bookmark_name));
        	intentAction = ACTION_BOOMARK_EDIT;
        	mExpensItem.setOpenUsedItem(true);
        	if(getIntent().getBooleanExtra("Fill", false)) {
        		fill();
        	}
        	break;
        case ACTION_BOOMARK_OTHER_ACTIVITY :	//���� �ٸ� Ÿ���� ���ã�⸦ ���ý� ��� ��Ƽ��Ƽ�� ��û ó��
        	setTitle(getResources().getString(R.string.input_expense_name));
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
        case ACTION_BOOMARK_EDIT_ACTIVITY:	//�ٸ� Ÿ�� ���ã�� �߰�/���� ȣ��
        	setTitle(getResources().getString(R.string.input_bookmark_name));
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
        }
        initWidget();
        
    }
    
    /**
     * ����Ʈ�� ��� �׼ǿ� ���� ȭ�鿡 ���̴� ���� ������ �����ϴ� �޼ҵ� 
     */
    private void initWidget() {
    	if(intentAction == ACTION_BOOMARK_EDIT || intentAction == ACTION_BOOMARK_EDIT_ACTIVITY) {	//�߰�/������ ��쿡 ó��
    		((View)findViewById(R.id.TVExpenseDate)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseDate)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.TVExpenseTag)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseTag)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.LLRepeat)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnExpenseRepeat)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.GONE);
    	}
    }
    /**
     * �������� �ѱ� �� ������ �̸� ä��� ���� �޼ҵ�
     */
    private void fill() {
    	String fillText[] = getIntent().getStringArrayExtra("FillText");
    	((Button)findViewById(R.id.BtnExpenseCategory)).setText(fillText[0]);
		((Button)findViewById(R.id.BtnExpenseAmount)).setText(fillText[1]);
		((EditText)findViewById(R.id.ETExpenseMemo)).setText(fillText[2]);
		
		mExpensItem = new ExpenseItem();
		
		int position = getIntent().getIntExtra("FillPosition", 0);
		
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
    	//mLLBookark = (LinearLayout) findViewById(R.id.LLBookmark); TODO ���� �ϸ�ũ�� ���� ��
    	mSlidingDrawer =  (SlidingDrawer) findViewById(R.id.SlidingDrawer);
    	super.initialize();
    }
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	if (mInputMode == InputMode.ADD_MODE) {
    		setTitleBtnText(FmTitleLayout.BTN_LEFT_01, "����");
            setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);
    	}
    }
    
    @Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(InputExpenseLayout.this, InputIncomeLayout.class);
    	intent.putExtra("Action", intentAction);	//���� ȣ��� ������ Intent�� ���� - ���� ���ã�� �߰�/���� ������ ��쿡 ���� ���ã�� �߰�/�������� ���� ���� ��  
		startActivity(intent);
		
    	super.onClickLeft1TitleBtn();
    }

	/**
     * ��¥ ����
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
     * �ܾ��� �����Ѵ�.
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
     * �ݾ��� ����
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
	 * ���ҹ���� DB�κ��� �����´�.
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
			}
			else {
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
					return;
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
	 * ���ҹ�� ��۹�ư Ŭ�� �� 
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
	 * �������� ���¸� �����Ѵ�.
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
     * �±׹�ư Ŭ�� ��
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
	
	
	//���� ��� �Ǵ� ���� ���� start
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
		((LinearLayout)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.VISIBLE);
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
//		// TODO ���ã�⿡ ���� �����͸� ����� �κ� - ������ �������� ����� ���� �ϹǷ� �Ϻ� �״�� ����
//		/*for (int i=0; i < 5; i++) {
//			if (itemsTemp.size() - 1 - i < 0) break;
//			Button btnBookmark = new Button(getApplicationContext());
//			btnBookmark.setText(itemsTemp.get(i).getCategory().getName() + " - " + itemsTemp.get(i).getSubCategory().getName()
//					+ "\t\t" + String.format("%,d��", itemsTemp.get(i).getAmount()));
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
//			switch(((ExpenseItem)itemsTemp.get(i)).getType()) {	// FIXME ���� �ʿ�
//			case PaymentMethod.CASH:
//				bookMarkItemData.method = "����";
//				break;
//			case PaymentMethod.CARD:
//				bookMarkItemData.method = "ī��";
//				break;
//			case PaymentMethod.ACCOUNT:
//				bookMarkItemData.method = "����";
//				break;
//			}
//			
//			bookMarkItemData.amount = String.format("%,d��", itemsTemp.get(i).getAmount());
//			//btnBookmark.setOnClickListener(mClickListener);
//			bookMarkItemDatas.add(bookMarkItemData);
//		}
		bookmarkList = (ListView)findViewById(R.id.LLBookmark);

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
	
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> items, View v, int position, long id) {
			if(!isEditableList) {
				if(!isIncome) {
					((Button)findViewById(R.id.BtnExpenseCategory)).setText(((TextView)v.findViewById(R.id.BookMarkItemCategory)).getText());
					((Button)findViewById(R.id.BtnExpenseAmount)).setText(((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText());
					((EditText)findViewById(R.id.ETExpenseMemo)).setText(((TextView)v.findViewById(R.id.BookMarkItemTitle)).getText());
					
//					((ToggleButton)findViewById(R.id.TBExpenseMethodCash)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("����"))?true:false);
//					((ToggleButton)findViewById(R.id.TBExpenseMethodCard)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("ī��"))?true:false);
//					((ToggleButton)findViewById(R.id.TBExpenseMethodAccount)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("����"))?true:false);
					
					
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
				} else {
					String[] fillText = new String[3];
					fillText[0] = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getCategory().getName() + " - " + DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getSubCategory().getName();
					fillText[1] = String.valueOf(DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getAmount());
					fillText[2] = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getMemo();
					
					Intent intent = new Intent(InputExpenseLayout.this, InputIncomeLayout.class);
					intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
					intent.putExtra("Action", ACTION_BOOMARK_OTHER_ACTIVITY);	//��� ��Ƽ��Ƽ ��û ó�� - FIXME �߰� �ʿ�
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
						fillText[0] = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getCategory().getName() + " - " + DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getSubCategory().getName();
						fillText[1] = String.valueOf(DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getAmount());
						fillText[2] = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getMemo();
						
						Intent intent = new Intent(InputExpenseLayout.this, InputExpenseLayout.class);
						intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
						intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//�߰� ȭ�� ȣ��
						intent.putExtra("Fill", true);
						intent.putExtra("FillPosition", position);
						intent.putExtra("FillText", fillText);
						startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
					} else {
						String[] fillText = new String[3];
						fillText[0] = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getCategory().getName() + " - " + DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getSubCategory().getName();
						fillText[1] = String.valueOf(DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getAmount());
						fillText[2] = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position).getItem().getMemo();
						
						Intent intent = new Intent(InputExpenseLayout.this, InputIncomeLayout.class);
						intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
						intent.putExtra("Action", ACTION_BOOMARK_EDIT_ACTIVITY);	//��� ��Ƽ��Ƽ ��û ó�� - FIXME �߰� �ʿ�
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
				isEditableList = true;	//���� ���� ����
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
				
				mPosition = position;	//��� �̺�Ʈ�� �߻� ��ų ��쿡 ������ ��ü���� �ش� ������ �����ϱ� ���� ������
			}
			return false;
		}
	};
	View.OnTouchListener mItemTouchListener = new View.OnTouchListener() {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(320, 50);
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				// ���� �Ǵ� View ��ü�� �̺�Ʈ�� �Ͼ ��ġ�� �ƴ϶� �׻� �������� 0���� �������Ƿ�(���� �������� ����Ʈ ���� �ֻ�� �������� �������� ������ ���� ��)
				// ���� ����� �� �����Ƿ� �ٸ� �̺�Ʈ �����ʿ��� ����� �� �ֵ��� �̺�Ʈ�� �߻��� ��ġ ���� ����
				mPositionX = event.getRawX();
				mPositionY = event.getRawY();
				
			} else if(event.getAction() == MotionEvent.ACTION_MOVE) {
				// �̺�Ʈ�� �߻��Ǹ� �׻� ��������� ȭ�鿡 �������� �ʱ� ������ ������ ó�� ����.
				// �̺�Ʈ�� ������ �߻��Ǳ� ������ ���� ���Ǵ� �������� �������� �����ϴ� ���� ����.
				if(isLongTouch) {
					//layoutParams.leftMargin = (int) event.getRawX() - (layoutParams.width / 2);	//��ġ�� �̷���� ��ġ�� �̹����� �߽��� ������ ��
					layoutParams.topMargin = (int) event.getRawY() - (layoutParams.height / 2) - 70;
					bookmarkDrag.setLayoutParams(layoutParams);
					
					return true;
				}
			} else if(event.getAction() == MotionEvent.ACTION_UP) {
				// �巡�װ� �� ���� ��쿡 ó���ؾ��� �κ��� ����
				// ���ٸ� ó���� �������Ƿ� �̹����� GONE ��Ŵ
				if(isLongTouch) {
					bookmarkDrag.setVisibility(View.GONE);

					int topChildView = ((ListView)v).getFirstVisiblePosition();	//�������� �ֻ���� �������� �ε����� ����
					int chk = (int) (event.getY() / ((ListView)v).getChildAt(0).getHeight());
					int newPosition = topChildView + chk;
					
					if((topChildView + chk) >= 0) {
						ArrayList<OpenUsedItem> markItemDatas = null;
						if(!isIncome) {
							markItemDatas = DBMgr.getOpenUsedItems(ExpenseItem.TYPE);
						} else {
							markItemDatas = DBMgr.getOpenUsedItems(IncomeItem.TYPE);
						}
						
						// FIXME �켱���� ���ſ� �ڵ�� ���� �ʿ�
						OpenUsedItem markItemData = markItemDatas.get(mPosition);
						markItemDatas.add(topChildView + chk, markItemData);
						if(newPosition >= mPosition)
							markItemDatas.remove(mPosition);
						else
							markItemDatas.remove(mPosition+1);
						
						//������ �Ѱ� ������ ���� - �ݺ��� �ۼ� �ؾ� �� - �ۼ���
						for(int i = 0, size = markItemDatas.size(); i < size; i++) {
							DBMgr.updateOpenUsedItem(markItemDatas.get(i).getType(), markItemDatas.get(i).getID(), markItemDatas.get(i).getItem().getID(), i);
						}
						
						//���� �׽�Ʈ ��
						//bookmarkList.setAdapter(new BookMarkAdapter(InputExpenseLayout.this, R.layout.input_bookmark_item, markItemDatas));
						updateOpenUsedItem();	//���� �������� �ʰ� DB���� ���� ���ͼ� �̵� ���� �Ұ�����
						bookmarkList.setSelectionFromTop(topChildView, 0);	//����Ͱ� �ٽ� ���õǸ� 0�� �������� ���õǱ� ������ �����ѵ� ������ ��ġ�� �̵�
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
			isEditableList = false;	//���� �Ұ��� ����
			updateOpenUsedItem();
		} else {
			super.onBackPressed();
		}
	}
	
	

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
//					+ "\t\t" + String.format("%,d��", itemsTemp.get(i).getAmount()));
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
	//���� ��� �Ǵ� ���� ���� end	
	
	protected void inputOpenUsedItem() {
		Intent intent = new Intent(this, (isIncome)?InputIncomeLayout.class:InputExpenseLayout.class);
		intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
		intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//�߰� ȭ�� ȣ��
		startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
	}
	
	protected void inputIncomeOpenUsedItem() {
		Intent intent = new Intent(this, InputIncomeLayout.class);
		intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
		intent.putExtra("Action", ACTION_BOOMARK_EDIT_ACTIVITY);	//�߰� ȭ�� ȣ��
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
		
		if (account.getBalance() < mExpensItem.getAmount()) {
			new AlertDialog.Builder(this)
			.setTitle("�ܾ��ʰ�")
			.setMessage("����ݾ��� �ܾ��� �ʰ��߽��ϴ�.\n �ܾ��� �����Ͻðڽ��ϱ�?")
			.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(InputExpenseLayout.this, InputAmountDialog.class);
					intent.putExtra(MsgDef.ExtraNames.AMOUNT, account.getBalance());
					startActivityForResult(intent, MsgDef.ActRequest.ACT_BALANCE_AFTER_CHCKE_ITEM);
				}
			})
			.setNegativeButton("����", new DialogInterface.OnClickListener() {
				
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
