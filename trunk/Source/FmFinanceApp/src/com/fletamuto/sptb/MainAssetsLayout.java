package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.util.Percentage;

public class MainAssetsLayout extends FmBaseActivity {
	private static final int REPORT_ASSETS = 0;
	private static final int REPORT_LIABILITY = 1;
	private static final int REPORT_CARD = 2;
	private static final int REPORT_MYPORKET = 3;
	private static final int MAX_REPORT = 4;
	
	protected ArrayList<FinanceItem> mAssetsItems = null;
	protected Map<Integer, CategoryAmount> mAssetsCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ArrayList<FinanceItem> mLiabilityItems = null;
	protected Map<Integer, CategoryAmount> mLiabilityCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ArrayList<CardItem> mCardItems = null;
	protected Map<Integer, CategoryAmount> mCardCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected AccountItem mMyPocket;
	protected ArrayList<AccountItem> mAccountItems = null;
	
	protected LinearLayout mLLReport[] = new LinearLayout[MAX_REPORT];
	
//	protected ReportItemAdapter mItemAdapter = null;
	

//	
	private long mTotalAmount[] = new long[MAX_REPORT];
	private long mTatalAccountBalance = 0L;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main_asserts, true);
//    	setButtonClickListener();
//    	
    	setRootView(true);
    	getListItem();
//    	setAdapterList();
    	updateChildView();
    }
    
    @Override
    protected void initialize() {
    	super.initialize();
    	
    	mLLReport[REPORT_ASSETS] = (LinearLayout) findViewById(R.id.LLMainAssetsAssetsReport);
    	mLLReport[REPORT_LIABILITY]  = (LinearLayout) findViewById(R.id.LLMainAssetsLiabilityReport);
    	mLLReport[REPORT_CARD]  = (LinearLayout) findViewById(R.id.LLMainAssetsCardReport);
    	mLLReport[REPORT_MYPORKET]  = (LinearLayout) findViewById(R.id.LLMainAssetsMyPoketReport);
    }
    
    @Override
	protected void setTitleBtn() {
		setTitle("자산내역");
		super.setTitleBtn();
	}

	@Override
    protected void onResume() {
//    	getListItem();
//    	setAdapterList();
    	
    	super.onResume();
    }
	
	protected void updateChildView() {
		updateBarGraph();
		
		((TextView)findViewById(R.id.TVTotalAssetsAmount)).setText(String.format("자산 : %,d원 (%s)", mTotalAmount[REPORT_ASSETS], Percentage.getString(mTotalAmount[REPORT_ASSETS], mTotalAmount[REPORT_ASSETS] + mTotalAmount[REPORT_LIABILITY])));
		((TextView)findViewById(R.id.TVTotalLiabilityAmount)).setText(String.format("부채 : %,d원 (%s)", mTotalAmount[REPORT_LIABILITY], Percentage.getString(mTotalAmount[REPORT_LIABILITY], mTotalAmount[REPORT_ASSETS] + mTotalAmount[REPORT_LIABILITY])));
		((TextView)findViewById(R.id.TVTotalPropertyAmount)).setText(String.format("합계 : %,d원", mTotalAmount[REPORT_ASSETS] - mTotalAmount[REPORT_LIABILITY]));
		
		makeReportList();
		
	}
	
	protected void makeReportList() {
		clearReportListAll();
		
		makeReportListAssets();
		makeReportListLiability();
		makeReportListCard();
		makeReportListMyPocket();
	}

	protected void makeReportListCard() {
		addTitleLayout(REPORT_CARD, "카드", mTotalAmount[REPORT_CARD]);
		
		//if (mCardItems.size() == 0) return;
		Collection<CategoryAmount> categoryAmountItems = mCardCategoryItems.values();
		
		for (CategoryAmount iterator:categoryAmountItems) {
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.main_assets_member, null);
			LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			TextView tvName = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberName);
			tvName.setText(iterator.getName());
			TextView tvCount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberCount);
			tvCount.setText(String.format("%d건", iterator.getCount()));
			TextView tvAmount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberTotalAmout);
			tvAmount.setText(String.format("%,d원", iterator.getTotalAmount()));
			
			llMember.setTag(iterator);
			llMember.setOnClickListener(mMemberClickLinter);
			mLLReport[REPORT_CARD].addView(llMember, params);
		}
		mLLReport[REPORT_CARD].invalidate();
		
	}

	protected void makeReportListMyPocket() {
		addTitleLayout(REPORT_MYPORKET, "현금", mTotalAmount[REPORT_MYPORKET]);
		
		LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.main_assets_member, null);
		LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
		TextView tvName = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberName);
		tvName.setText("내 주머니");
		TextView tvAmount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberTotalAmout);
		tvAmount.setText(String.format("%,d원", mTotalAmount[REPORT_MYPORKET]));
		llMember.setTag(mMyPocket);
		llMember.setOnClickListener(mMemberClickLinter);
		mLLReport[REPORT_MYPORKET].addView(llMember, params);
		mLLReport[REPORT_MYPORKET].invalidate();
	}

	protected void makeReportListAssets() {
		addTitleLayout(REPORT_ASSETS, "자산", mTotalAmount[REPORT_ASSETS]);
		
//		int assetsSize = mAssetsCategoryItems.size();
		int accountSize = mAccountItems.size();
//		if (assetsSize + accountSize == 0) {
//			mLLReport[REPORT_ASSETS].setVisibility(View.GONE);
//			return;
//		}
//		mLLReport[REPORT_ASSETS].setVisibility(View.VISIBLE);
		
		if (accountSize > 0) {
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.main_assets_member, null);
			LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			TextView tvName = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberName);
			tvName.setText("요구불");
			TextView tvCount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberCount);
			tvCount.setText(String.format("%d건", accountSize));
			TextView tvAmount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberTotalAmout);
			tvAmount.setText(String.format("%,d원", mTatalAccountBalance));
			TextView tvPercent = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberPercent);
			tvPercent.setText(Percentage.getString(mTatalAccountBalance, mTotalAmount[REPORT_ASSETS]));
			llMember.setTag(ItemDef.FinanceDef.ACCOUNT);
			llMember.setOnClickListener(mMemberClickLinter);
			mLLReport[REPORT_ASSETS].addView(llMember, params);
		}
			
		Collection<CategoryAmount> categoryAmountItems = mAssetsCategoryItems.values();
		
		for (CategoryAmount iterator:categoryAmountItems) {
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.main_assets_member, null);
			LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			TextView tvName = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberName);
			tvName.setText(iterator.getName());
			TextView tvCount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberCount);
			tvCount.setText(String.format("%d건", iterator.getCount()));
			TextView tvAmount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberTotalAmout);
			tvAmount.setText(String.format("%,d원", iterator.getTotalAmount()));
			TextView tvPercent = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberPercent);
			tvPercent.setText(Percentage.getString(iterator.getTotalAmount(), mTotalAmount[REPORT_ASSETS]));
			llMember.setTag(iterator);
			llMember.setOnClickListener(mMemberClickLinter);
			mLLReport[REPORT_ASSETS].addView(llMember, params);
		}
		mLLReport[REPORT_ASSETS].invalidate();
	}
	
	private View.OnClickListener mMemberClickLinter = new View.OnClickListener() {
		
		public void onClick(View v) {
			
			if (v.getTag() instanceof CategoryAmount) {
				CategoryAmount categoryAmount = (CategoryAmount) v.getTag();
				if (categoryAmount.getType() == AssetsItem.TYPE) {
					Intent intent = new Intent(MainAssetsLayout.this, ReportAssetsLayout.class);
					intent.putExtra(MsgDef.ExtraNames.ITEM_ID, categoryAmount.getCategoryID());
					startActivity(intent);
				}
				else if (categoryAmount.getType() == LiabilityItem.TYPE) {
					Intent intent = new Intent(MainAssetsLayout.this, ReportLiabilityLayout.class);
					intent.putExtra(MsgDef.ExtraNames.ITEM_ID, categoryAmount.getCategoryID());
					startActivity(intent);
				}
				else if (categoryAmount.getType() == CardItem.TYPE) {
					if (categoryAmount.getCategoryID() == CardItem.CREDIT_CARD) {
						Intent intent = new Intent(MainAssetsLayout.this, ReportCreditCardLayout.class);
						startActivity(intent);
					}
					else if (categoryAmount.getCategoryID() == CardItem.CHECK_CARD) {
						Intent intent = new Intent(MainAssetsLayout.this, ReportCheckCardLayout.class);
						startActivity(intent);
					}
					else if (categoryAmount.getCategoryID() == CardItem.PREPAID_CARD) {
						Intent intent = new Intent(MainAssetsLayout.this, ReportPrepaidCardLayout.class);
						startActivity(intent);
					}
				}
			}
			else if (v.getTag() == Integer.valueOf(ItemDef.FinanceDef.ACCOUNT)) {
				Intent intent = new Intent(MainAssetsLayout.this, ReportDemandAccountLayout.class);
				startActivity(intent);
			}
		}
	};
	
	protected void makeReportListLiability() {
		addTitleLayout(REPORT_LIABILITY, "부채", mTotalAmount[REPORT_LIABILITY]);
		
//		int liabilitySize = mLiabilityCategoryItems.size();
//		if (liabilitySize == 0) {
//			mLLReport[REPORT_LIABILITY].setVisibility(View.GONE);
//			return;
//		}
		mLLReport[REPORT_LIABILITY].setVisibility(View.VISIBLE);
		
		
		
		Collection<CategoryAmount> categoryAmountItems = mLiabilityCategoryItems.values();
		
		for (CategoryAmount iterator:categoryAmountItems) {
			LinearLayout llMember = (LinearLayout)View.inflate(this, R.layout.main_assets_member, null);
			LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
			TextView tvName = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberName);
			tvName.setText(iterator.getName());
			TextView tvCount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberCount);
			tvCount.setText(String.format("%d건", iterator.getCount()));
			TextView tvAmount = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberTotalAmout);
			tvAmount.setText(String.format("%,d원", iterator.getTotalAmount()));
			TextView tvPercent = (TextView) llMember.findViewById(R.id.TVMainAssetsMemberPercent);
			tvPercent.setText(Percentage.getString(iterator.getTotalAmount(), mTotalAmount[REPORT_LIABILITY]));
			llMember.setOnClickListener(mMemberClickLinter);
			llMember.setTag(iterator);
			mLLReport[REPORT_LIABILITY].addView(llMember, params);
		}
		mLLReport[REPORT_LIABILITY].invalidate();
	}
	
	protected void clearReportListAll() {
		for (int index = 0; index < MAX_REPORT; index++) {
			mLLReport[index].removeAllViews();
		}
	}

	protected void updateBarGraph() {
		final PieGraph pieGraph;
		ArrayList<Long> pieGraphValues = new ArrayList<Long>();
		
		pieGraphValues.add(mTotalAmount[REPORT_ASSETS]);
		pieGraphValues.add(mTotalAmount[REPORT_LIABILITY]);
       
		pieGraph = (PieGraph) findViewById (R.id.pgraph);
		pieGraph.setItemValues(pieGraphValues);
		pieGraph.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int sel;
		    		sel = pieGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			Toast.makeText(pieGraph.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
		    			return true;
		    		}
		    	}
				return false;
			}
		});
	}
       
    protected void getListItem() {
    	clearTotalCount();
    	mAssetsItems = DBMgr.getAllItems(AssetsItem.TYPE);
    	updateListItem(mAssetsCategoryItems, mAssetsItems);
    	mLiabilityItems = DBMgr.getAllItems(LiabilityItem.TYPE);
    	updateListItem(mLiabilityCategoryItems, mLiabilityItems);
    	mAccountItems = getAccountItems(); 
    	
    	mCardItems = DBMgr.getCardItems();
    	updateCardListItem();
    	
    	mMyPocket = DBMgr.getAccountMyPoctet();
    	mTotalAmount[REPORT_MYPORKET] = mMyPocket.getBalance();
    }
    


	private ArrayList<AccountItem> getAccountItems() {
		ArrayList<AccountItem> accountItems = DBMgr.getAccountAllItems();
		int size = accountItems.size();
		for (int index = 0; index < size; index++) {
			AccountItem account = accountItems.get(index); 
			
			if (account.getType() == AccountItem.TIME_DEPOSIT || account.getType() == AccountItem.SAVINGS) {
				accountItems.remove(index);
				index--; size--;
				continue;
			}
			mTotalAmount[REPORT_ASSETS] += account.getBalance();
			mTatalAccountBalance += account.getBalance();
		}
		
		return accountItems;
	}

	protected void updateCardListItem() {
		mCardCategoryItems.clear();
		
		int itemSize = mCardItems.size();
		for (int index = 0; index < itemSize; index++) {
			CardItem item = mCardItems.get(index);
			Integer cardType = item.getType();
			CategoryAmount categoryAmount = mCardCategoryItems.get(cardType);
			
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(ItemDef.FinanceDef.CARD);
				categoryAmount.set(item.getType(), item.getCardTypeName(), item.getBalance());
				mCardCategoryItems.put(cardType, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getBalance());
			}
			
			mTotalAmount[REPORT_CARD] += item.getBalance();
		}
	}

    protected void updateListItem(Map<Integer, CategoryAmount> mapCategoryItems, ArrayList<FinanceItem> arrItems) {
    	mapCategoryItems.clear();
		
		int itemSize = arrItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = arrItems.get(index);
			Category category = item.getCategory();
			if (category == null) {
				Log.w(LogTag.LAYOUT, ":: INVAILD CATEGORU :: ");
				continue;
			}
			
			Integer categoryID = category.getID();
			CategoryAmount categoryAmount = mapCategoryItems.get(categoryID);
			
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(item.getType());
				categoryAmount.set(categoryID, category.getName(), item.getAmount());
				mapCategoryItems.put(categoryID, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getAmount());
			}
			
			addTotalCount(item);
		}
	}
   
    
    protected void clearTotalCount() {
    	for (int index = 0; index < MAX_REPORT; index++) {
    		mTotalAmount[index] = 0L; 
    	}
    	mTatalAccountBalance = 0L;
    }
    
	protected void addTotalCount(FinanceItem item) {
		if (item.getType() == AssetsItem.TYPE) {
			mTotalAmount[REPORT_ASSETS] += item.getAmount();
		}
		else if (item.getType() == LiabilityItem.TYPE) {
			mTotalAmount[REPORT_LIABILITY] += item.getAmount();
		}
	}
	
	public void addTitleLayout(final int reportIndex, String name, long amount) {
		LinearLayout llMain = mLLReport[reportIndex];
		LinearLayout llTitle = (LinearLayout)View.inflate(this, R.layout.main_assets_title, null);
		llTitle.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
		llMain.addView(llTitle, params);
		
		
		TextView tvTitle = (TextView) llTitle.findViewById(R.id.TVMainAssetsTitleName);
		tvTitle.setText(name);
		tvTitle.setTextColor(Color.BLACK);
		
		if (!(reportIndex == REPORT_MYPORKET || reportIndex == REPORT_CARD)) {
			TextView tvAmount = (TextView) llTitle.findViewById(R.id.TVMainAssetsTitleTotalAmout);
			tvAmount.setText(String.format("%,d원", amount));
			tvAmount.setTextColor(Color.BLACK);
		}
		
		llTitle.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = null;
				
				if (reportIndex == REPORT_ASSETS) {
					intent = new Intent(MainAssetsLayout.this, ReportAssetsLayout.class);
				}
				else if (reportIndex == REPORT_LIABILITY) {
					intent = new Intent(MainAssetsLayout.this, ReportLiabilityLayout.class);
				}
				else if (reportIndex == REPORT_CARD) {
					intent = new Intent(MainAssetsLayout.this, ReportCreditCardLayout.class);				
				}
				else if (reportIndex == REPORT_MYPORKET) {
					
				}
				
				if (intent != null) {
					startActivity(intent);
				}
				
			}
		});
		
	}
}