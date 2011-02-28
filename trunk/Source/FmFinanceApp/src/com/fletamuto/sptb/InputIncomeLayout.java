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
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.OpenUsedItem;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.ReceiveMethod;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
/**
 * ������ �Է� �Ǵ� �����ϴ� ȭ���� �����Ѵ�.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeLayout extends InputFinanceItemBaseLayout {
	protected final static int ACT_TAG_SELECTED = MsgDef.ActRequest.ACT_TAG_SELECTED;
	protected final static int ACT_CARD_SELECT = MsgDef.ActRequest.ACT_CARD_SELECT;
	protected final static int ACT_ACCOUNT_SELECT = MsgDef.ActRequest.ACT_ACCOUNT_SELECT;
	protected final static int ACT_BOOKMARK_SELECT = MsgDef.ActRequest.ACT_BOOKMARK_SELECT;
	
	
	private IncomeItem mIncomeItem;
	private ReceiveMethod mReciveMethod = new ReceiveMethod();
	
	private View popupviewBookmark;
	private PopupWindow popupBookmark, popupBookmarkEdit;
	private LinearLayout linear;
	private SlidingDrawer mSlidingDrawer;
	
	private AccountItem fromItem;
	private long beforeAmount;
	
	/**
     * ACTION_DEFAULT = 0, ���� �߰�/���� ȭ��
     */
    private final static int ACTION_DEFAULT = 0;
    /**
     * ACTION_BOOMARK_EDIT = 1, ���ã�� �߰�/���� ���¿��� ȣ�� ��
     */
    private final static int ACTION_BOOMARK_EDIT = 1;
    /**
     * ACTION_BOOMARK_OTHER_ACTIVITY = 2, �ٸ� Ÿ���� ���ã�⸦ ȣ���Ͽ� ������ �ڵ����� ä��
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
	boolean isIncome = true;	//false�� Expense
	boolean isEditable = false;
	boolean isEditableList = false;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_income, true);
        
        updateChildView();
        
        switch(getIntent().getIntExtra("Action", 0)) {
        case ACTION_DEFAULT:	//�Ϲ� ȣ��
        	setTitle(getResources().getString(R.string.input_income_name));
            initBookmark();
        	break;
        case ACTION_BOOMARK_EDIT:	//���ã�� �߰�/���� ȣ��
        	setTitle(getResources().getString(R.string.input_bookmark_name));
        	intentAction = ACTION_BOOMARK_EDIT;
        	if(getIntent().getBooleanExtra("Fill", false)) {
        		fill();
        	}
        	break;
        case ACTION_BOOMARK_OTHER_ACTIVITY :	//���� �ٸ� Ÿ���� ���ã�⸦ ���ý� ��� ��Ƽ��Ƽ�� ��û ó��
        	setTitle(getResources().getString(R.string.input_income_name));
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
        	//inputExpenseOpenUsedItem();
        	break;
        }
        initWidget();
    }
    
    /**
     * ����Ʈ�� ��� �׼ǿ� ���� ȭ�鿡 ���̴� ���� ������ �����ϴ� �޼ҵ� 
     */
    private void initWidget() {
    	if(intentAction == ACTION_BOOMARK_EDIT || intentAction == ACTION_BOOMARK_EDIT_ACTIVITY) {	//�Ϲ� ȣ���� �ƴ� ���(�߰�/����)
    		((View)findViewById(R.id.TVIncomeDate)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnIncomeDate)).setVisibility(View.GONE);
    		
    		((View)findViewById(R.id.TVIncomeRepeat)).setVisibility(View.GONE);
    		((View)findViewById(R.id.BtnIncomeRepeat)).setVisibility(View.GONE);
    		
    		//((View)findViewById(R.id.LLBookmarkSliding)).setVisibility(View.GONE);
    	}
    }
    private void fill() {
    	String fillText[] = getIntent().getStringArrayExtra("FillText");
    	((Button)findViewById(R.id.BtnIncomeCategory)).setText(fillText[0]);
		((Button)findViewById(R.id.BtnIncomeAmount)).setText(fillText[1]);
		((EditText)findViewById(R.id.ETIncomeMemo)).setText(fillText[2]);
		
		mIncomeItem = new IncomeItem();
		
		int position = getIntent().getIntExtra("FillPosition", 0);
		
		Category category = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getCategory();
		Category subCategory = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getSubCategory();
		mIncomeItem.setCategory(category.getID(), category.getName());
		mIncomeItem.setSubCategory(subCategory.getID(), subCategory.getName());
		mIncomeItem.setAmount(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getAmount());
		//mIncomeItem.setCard(((ExpenseItem)(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem())).getCard());
		//mIncomeItem.setPaymentMethod((((ExpenseItem)(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem())).getPaymentMethod()));

		setItem(mIncomeItem);
		updateReceiveMethod();

		if (popupBookmark != null) {
			popupBookmark.dismiss();
		}
		
		if (mSlidingDrawer != null) {
			mSlidingDrawer.toggle();
		}
    }
    
    @Override
	protected void setBtnClickListener() {
    	setDateBtnClickListener(R.id.BtnIncomeDate); 
        setAmountBtnClickListener(R.id.BtnIncomeAmount);
        setRepeatBtnClickListener(R.id.BtnIncomeRepeat);
        setCategoryClickListener(R.id.BtnIncomeCategory);
        setReceiveToggleBtnClickListener();
	}
    
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	setTitle(mIncomeItem.getCategory().getName());
    	
    	if (mInputMode == InputMode.ADD_MODE) {
    		setTitleBtnText(FmTitleLayout.BTN_LEFT_01, "����");
        	setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);
    	}
    }
    
    /**
     * �з���ư Ŭ���� ������ ����
     * @param btnID �з���ư ���̵�
     */
    protected void setCategoryClickListener(int btnID) {
    	((Button)findViewById(btnID)).setOnClickListener(new Button.OnClickListener() {
    		
    		public void onClick(View v) {
    			Intent intent = new Intent(InputIncomeLayout.this, SelectCategoryIncomeLayout.class);
    			startActivityForResult(intent, ACT_CATEGORY);
    		}
        });    
    }    
    
    
    @Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(InputIncomeLayout.this, InputExpenseLayout.class);
    	intent.putExtra("Action", intentAction);	//���� ȣ��� ������ Intent�� ���� - ���� ���ã�� �߰�/���� ������ ��쿡 ���� ���ã�� �߰�/�������� ���� ���� ��
		startActivity(intent);
		
    	super.onClickLeft1TitleBtn();
    }
  
    protected void initialize() {
    	super.initialize();
    	
    	if (mIncomeItem.getCategory().getID() == -1) {
    		int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1) ;
            String categoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
            updateCategory(categoryID, categoryName);
    	}
    	
        if (mIncomeItem.getAccount().getID() != -1) {
        	mReciveMethod.setAccount(mIncomeItem.getAccount());
        	mReciveMethod.setType(ReceiveMethod.ACCOUNT);
        }
        
	}
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnIncomeDate);
    }
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		if(mReciveMethod.getType() == PaymentMethod.ACCOUNT) {
    			fromItem = mIncomeItem.getAccount();

    			long fromItemBalance = fromItem.getBalance();

				fromItem.setBalance(fromItemBalance + mIncomeItem.getAmount());
				DBMgr.updateAccount(fromItem);
    		}
    		if (saveNewItem(null) == true) {
    			saveRepeat();
    		}
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		if(mReciveMethod.getType() == PaymentMethod.ACCOUNT) {
    			fromItem = mIncomeItem.getAccount();

    			long fromItemBalance = fromItem.getBalance();

				fromItem.setBalance(fromItemBalance - beforeAmount + mIncomeItem.getAmount());
				DBMgr.updateAccount(fromItem);
    		}
    		saveUpdateItem();
    	}
    }
    
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnIncomeAmount);
	}

	@Override
	protected void createItemInstance() {
		mIncomeItem = (IncomeItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ITEM);
		if (mIncomeItem == null) {
			mIncomeItem = new IncomeItem();
		}
		beforeAmount = mIncomeItem.getAmount();
		setItem(mIncomeItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mIncomeItem = (IncomeItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mIncomeItem == null) return false;
		beforeAmount = mIncomeItem.getAmount();
		setItem(mIncomeItem);
		
		loadPaymnetMethod();
		
		return true;
	}

	/**
	 * ���ҹ���� DB�κ��� �����´�.
	 */
	private boolean loadPaymnetMethod() {
		if (mIncomeItem == null) return false;

		if (mReciveMethod.getType() == PaymentMethod.ACCOUNT) {
			if (mIncomeItem.getAccount() == null) {
				mIncomeItem.setAccount(DBMgr.getAccountItem(mReciveMethod.getMethodItemID()));
			}
		}
		return true;
		
	}
	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputIncomeLayout.this, SelectCategoryIncomeLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		mIncomeItem.setCategory(id, name);
		updateBtnCategoryText(R.id.BtnIncomeCategory);
	}

	@Override
	protected void updateChildView() {
		updateChildViewState();
		updateDate();
		updateBtnAmountText(R.id.BtnIncomeAmount);
		updateEditMemoText(R.id.ETIncomeMemo);
		updateRepeatText(R.id.BtnIncomeRepeat);
		updateBtnCategoryText(R.id.BtnIncomeCategory);
		updateReceiveMethod();
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETIncomeMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	if (mIncomeItem.getCategory().getID() == -1) {
    		ArrayList<Category> nothginCategory = DBMgr.getCategory(IncomeItem.TYPE, ItemDef.NOT_CATEGORY);
    		if (nothginCategory.size() != 0) {
    			Category mainCategory = nothginCategory.get(0);
    			mIncomeItem.setCategory(mainCategory);
    		}
    	}
	}
	
	protected void updateRepeat(int type, int value) {
		
		if (type == Repeat.MONTHLY) {
			mIncomeItem.setRepeatMonthly(value);
		}
		else if (type == Repeat.WEEKLY) {
			mIncomeItem.setRepeatWeekly(value);
		}
		else {
			
		}
		updateRepeatText(R.id.BtnIncomeRepeat);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			updateCategory(data.getIntExtra("CATEGORY_ID", -1), data.getStringExtra("CATEGORY_NAME"));
//    			mExpensItem.setSubCategory(data.getIntExtra("SUB_CATEGORY_ID", -1), data.getStringExtra("SUB_CATEGORY_NAME"));
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
		else if (requestCode == MsgDef.ActRequest.ACT_ACCOUNT_SELECT) {
			if (resultCode == RESULT_OK) {
				int selectedID = data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1);
				if (selectedID == -1) return;
			
				AccountItem selectedAccount = DBMgr.getAccountItem(selectedID);
				updateAccount(selectedAccount);
			}
			else {
				updateAccount(new AccountItem());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * ���ɹ�� ��۹�ư Ŭ�� �� 
	 */
	protected void setReceiveToggleBtnClickListener() {
		
		((ToggleButton)findViewById(R.id.TBIncomeMethodCash)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mReciveMethod.setType(PaymentMethod.CASH);
				updateReceiveMethod();
			}
		});
		
		
		((ToggleButton)findViewById(R.id.TBIncomeMethodAccount)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent intent = new Intent(InputIncomeLayout.this, SelectAccountLayout.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ACCOUNT_SELECT);
			}
		});
	}
	
	protected void updateAccount(AccountItem selectedAccount) {
		mIncomeItem.setAccount(selectedAccount);
		mReciveMethod.setAccount(selectedAccount);
		mReciveMethod.setType(ReceiveMethod.ACCOUNT);
		updateReceiveMethod();
	}
	
	protected void updateReceiveMethod() {		
		ToggleButton btnCash = (ToggleButton)findViewById(R.id.TBIncomeMethodCash);
		ToggleButton btnAccount = (ToggleButton)findViewById(R.id.TBIncomeMethodAccount);
		
		if (mReciveMethod.getType() == PaymentMethod.CASH) {
			btnCash.setChecked(true);
			btnAccount.setChecked(false);
		}
		else if (mReciveMethod.getType() == PaymentMethod.ACCOUNT) {
			btnCash.setChecked(false);
			btnAccount.setChecked(true);
		}
		updatePaymentMethodText();
	}
	
    /**
     * 
     */
	protected void updatePaymentMethodText() {
		TextView tvPaymentMethod = (TextView)findViewById(R.id.TVPaymentMethod);
		tvPaymentMethod.setText(mReciveMethod.getText());
	}
	

	protected void updateChildViewState() {
		Category category = getItem().getCategory();
		
		if (category.getExtndType() == ItemDef.ExtendAssets.DEPOSIT) {
			findViewById(R.id.LLRepeat).setVisibility(View.GONE);
		}
//		else {
//			findViewById(R.id.BtnExpenseCategory).setEnabled(true);
//			findViewById(R.id.BtnExpenseAmount).setEnabled(true);
//			findViewById(R.id.TBExpenseMethodCard).setVisibility(View.VISIBLE);
//			findViewById(R.id.LLRepeat).setVisibility(View.VISIBLE);
//		}
	}
	
	
	protected void updateBtnCategoryText(int btnID) {
		String categoryText = getResources().getString(R.string.input_select_category);
		if (mIncomeItem.isVaildCatetory()) {
			categoryText = String.format("%s", mIncomeItem.getCategory().getName());
		}
		 
    	((Button)findViewById(btnID)).setText(categoryText);
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
					if(isIncome) {
						((Button)findViewById(R.id.BtnIncomeCategory)).setText(((TextView)v.findViewById(R.id.BookMarkItemCategory)).getText());
						((Button)findViewById(R.id.BtnIncomeAmount)).setText(((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText());
						((EditText)findViewById(R.id.ETIncomeMemo)).setText(((TextView)v.findViewById(R.id.BookMarkItemTitle)).getText());
						
//						((ToggleButton)findViewById(R.id.TBExpenseMethodCash)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("����"))?true:false);
//						((ToggleButton)findViewById(R.id.TBExpenseMethodCard)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("ī��"))?true:false);
//						((ToggleButton)findViewById(R.id.TBExpenseMethodAccount)).setSelected((((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText().equals("����"))?true:false);
						
						
						//mExpensItem = (ExpenseItem)itemsTemp.get(position);
						mIncomeItem = new IncomeItem();
						
						Category category = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getCategory();
						Category subCategory = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getSubCategory();
						mIncomeItem.setCategory(category.getID(), category.getName());
						mIncomeItem.setSubCategory(subCategory.getID(), subCategory.getName());
						mIncomeItem.setAmount(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getAmount());
						//mIncomeItem.setCard(((ExpenseItem)(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem())).getCard());
						//mIncomeItem.setPaymentMethod((((ExpenseItem)(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem())).getPaymentMethod()));

						setItem(mIncomeItem);
						updateReceiveMethod();

						if (popupBookmark != null) {
							popupBookmark.dismiss();
						}
						
						if (mSlidingDrawer != null) {
							mSlidingDrawer.toggle();
						}
					} else {
						String[] fillText = new String[3];
						fillText[0] = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getCategory().getName() + " - " + DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getSubCategory().getName();
						fillText[1] = String.valueOf(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getAmount());
						fillText[2] = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getMemo();
						
						Intent intent = new Intent(InputIncomeLayout.this, InputExpenseLayout.class);
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
						if(isIncome) {
							String[] fillText = new String[3];
							fillText[0] = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getCategory().getName() + " - " + DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getSubCategory().getName();
							fillText[1] = String.valueOf(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getAmount());
							fillText[2] = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getMemo();
							
							Intent intent = new Intent(InputIncomeLayout.this, InputIncomeLayout.class);
							intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
							intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//�߰� ȭ�� ȣ��
							intent.putExtra("Fill", true);
							intent.putExtra("FillPosition", position);
							intent.putExtra("FillText", fillText);
							startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
						} else {
							String[] fillText = new String[3];
							fillText[0] = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getCategory().getName() + " - " + DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getSubCategory().getName();
							fillText[1] = String.valueOf(DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getAmount());
							fillText[2] = DBMgr.getOpenUsedItems(ExpenseItem.TYPE).get(position).getItem().getMemo();
							
							Intent intent = new Intent(InputIncomeLayout.this, InputExpenseLayout.class);
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
		if(isEditableList) {
			isEditableList = false;	//���� �Ұ��� ����
			updateOpenUsedItem();
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
				Intent intent = new Intent(InputIncomeLayout.this, BookmarkExpenseAddLayout.class);
				startActivityForResult(intent, ACT_BOOKMARK_SELECT);
			}
		});
	}
	
	
	protected void inputOpenUsedItem() {
		Intent intent = new Intent(this, (isIncome)?InputIncomeLayout.class:InputExpenseLayout.class);
		intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
		intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//�߰� ȭ�� ȣ��
		startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
	}
	
	protected void inputExpenseOpenUsedItem() {
		Intent intent = new Intent(this, InputExpenseLayout.class);
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
}
