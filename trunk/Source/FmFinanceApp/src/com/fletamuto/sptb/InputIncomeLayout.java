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
 * 수입을 입력 또는 수정하는 화면을 제공한다.
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
	
	/**
     * ACTION_DEFAULT = 0, 지출 추가/편집 화면
     */
    private final static int ACTION_DEFAULT = 0;
    /**
     * ACTION_BOOMARK_EDIT = 1, 즐겨찾기 추가/편집 상태에서 호출 됨
     */
    private final static int ACTION_BOOMARK_EDIT = 1;
    /**
     * ACTION_BOOMARK_OTHER_ACTIVITY = 2, 다른 타입의 즐겨찾기를 호출하여 내용을 자동으로 채움
     */
    private final static int ACTION_BOOMARK_OTHER_ACTIVITY = 2;
    /**
     * ACTION_BOOMARK_EDIT_ACTIVITY = 3, 다른 타입의 즐겨찾기 추가/편집 호출을 요청
     */
    private final static int ACTION_BOOMARK_EDIT_ACTIVITY = 3;
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
	boolean isIncome = true;	//false면 Expense
	boolean isEditable = false;
	boolean isEditableList = false;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_income, true);
        
        updateChildView();
        
        switch(getIntent().getIntExtra("Action", 0)) {
        case ACTION_DEFAULT:	//일반 호출
        	setTitle(getResources().getString(R.string.input_income_name));
            initBookmark();
        	break;
        case ACTION_BOOMARK_EDIT:	//즐겨찾기 추가/편집 호출
        	setTitle(getResources().getString(R.string.input_bookmark_name));
        	intentAction = ACTION_BOOMARK_EDIT;
        	if(getIntent().getBooleanExtra("Fill", false)) {
        		fill();
        	}
        	break;
        case ACTION_BOOMARK_OTHER_ACTIVITY :	//서로 다른 타입의 즐겨찾기를 선택시 상대 액티비티에 요청 처리
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
        case ACTION_BOOMARK_EDIT_ACTIVITY:	//다른 타입 즐겨찾기 추가/편집 호출
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
     * 인텐트에 담긴 액션에 따라서 화면에 보이는 위젯 개수를 변경하는 메소드 
     */
    private void initWidget() {
    	if(intentAction == ACTION_BOOMARK_EDIT || intentAction == ACTION_BOOMARK_EDIT_ACTIVITY) {	//일반 호출이 아닌 경우(추가/편집)
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
		
		OpenUsedItem incomeItem = DBMgr.getOpenUsedItems(IncomeItem.TYPE).get(position);
		
		Category category = incomeItem.getItem().getCategory();
		Category subCategory = incomeItem.getItem().getSubCategory();
		mIncomeItem.setCategory(category.getID(), category.getName());
		mIncomeItem.setSubCategory(subCategory.getID(), subCategory.getName());
		mIncomeItem.setAmount(incomeItem.getItem().getAmount());

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
    		setTitleBtnText(FmTitleLayout.BTN_LEFT_01, "지출");
        	setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);
    	}
    }
    
    /**
     * 분류버튼 클릭시 리슨너 설정
     * @param btnID 분류버튼 아이디
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
    	intent.putExtra("Action", intentAction);	//현재 호출된 상태의 Intent를 전달 - 지출 즐겨찾기 추가/편집 상태인 경우에 수입 즐겨찾기 추가/편집으로 가기 위한 값
		startActivity(intent);
		
    	super.onClickLeft1TitleBtn();
    }
  
    protected void initialize() {
    	super.initialize();
    	mSlidingDrawer =  (SlidingDrawer) findViewById(R.id.SlidingDrawer);
    	
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
    	AccountItem account = mIncomeItem.getAccount();
    	if (account.getID() == -1) {
    		account = DBMgr.getAccountMyPocket();
    	} 
   
    	if (account != null) {
    		account.setBalance(account.getBalance() + mIncomeItem.getAmount());
    		DBMgr.updateAccount(account);
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
		setItem(mIncomeItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mIncomeItem = (IncomeItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mIncomeItem == null) return false;
		setItem(mIncomeItem);
		
		loadPaymnetMethod();
		
		return true;
	}

	/**
	 * 지불방법을 DB로부터 가져온다.
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
	 * 수령방법 토글버튼 클릭 시 
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
				if(isIncome) {
					((Button)findViewById(R.id.BtnIncomeCategory)).setText(((TextView)v.findViewById(R.id.BookMarkItemCategory)).getText());
					((Button)findViewById(R.id.BtnIncomeAmount)).setText(((TextView)v.findViewById(R.id.BookMarkItemAmount)).getText());
					((EditText)findViewById(R.id.ETIncomeMemo)).setText(((TextView)v.findViewById(R.id.BookMarkItemTitle)).getText());


					mIncomeItem = new IncomeItem();

					Category category = incomeItem.getItem().getCategory();
					Category subCategory = incomeItem.getItem().getSubCategory();
					mIncomeItem.setCategory(category.getID(), category.getName());
					mIncomeItem.setSubCategory(subCategory.getID(), subCategory.getName());
					mIncomeItem.setAmount(incomeItem.getItem().getAmount());


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
					fillText[0] = expenseItem.getItem().getCategory().getName() + " - " + expenseItem.getItem().getSubCategory().getName();
					fillText[1] = String.valueOf(expenseItem.getItem().getAmount());
					fillText[2] = expenseItem.getItem().getMemo();

					Intent intent = new Intent(InputIncomeLayout.this, InputExpenseLayout.class);
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
					if(isIncome) {
						String[] fillText = new String[3];
						fillText[0] = incomeItem.getItem().getCategory().getName();
						fillText[1] = String.valueOf(incomeItem.getItem().getAmount());
						fillText[2] = incomeItem.getItem().getMemo();

						Intent intent = new Intent(InputIncomeLayout.this, InputIncomeLayout.class);
						intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
						intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//추가 화면 호출
						intent.putExtra("Fill", true);
						intent.putExtra("FillPosition", position);
						intent.putExtra("FillText", fillText);
						startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
					} else {
						String[] fillText = new String[3];
						fillText[0] = expenseItem.getItem().getCategory().getName() + " - " + expenseItem.getItem().getSubCategory().getName();
						fillText[1] = String.valueOf(expenseItem.getItem().getAmount());
						fillText[2] = expenseItem.getItem().getMemo();

						Intent intent = new Intent(InputIncomeLayout.this, InputExpenseLayout.class);
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
						//bookmarkList.setAdapter(new BookMarkAdapter(InputExpenseLayout.this, R.layout.input_bookmark_item, markItemDatas));
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
				Intent intent = new Intent(InputIncomeLayout.this, BookmarkExpenseAddLayout.class);
				startActivityForResult(intent, ACT_BOOKMARK_SELECT);
			}
		});
	}
	
	
	protected void inputOpenUsedItem() {
		Intent intent = new Intent(this, (isIncome)?InputIncomeLayout.class:InputExpenseLayout.class);
		intent.putExtra(MsgDef.ExtraNames.OPEN_USED_ITEM, true);
		intent.putExtra("Action", ACTION_BOOMARK_EDIT);	//추가 화면 호출
		startActivityForResult(intent, MsgDef.ActRequest.ACT_OPEN_USED_ITEM);
	}
	
	protected void inputExpenseOpenUsedItem() {
		Intent intent = new Intent(this, InputExpenseLayout.class);
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
}
