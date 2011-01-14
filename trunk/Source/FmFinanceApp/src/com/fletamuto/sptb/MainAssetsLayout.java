package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

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
	
	protected LinearLayout mLLReport[] = new LinearLayout[MAX_REPORT];
//	protected ReportItemAdapter mItemAdapter = null;
	

//	
	private long mTotalAmount[] = new long[MAX_REPORT];


    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main_asserts, true);
//    	setButtonClickListener();
//    	
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
		
		((TextView)findViewById(R.id.TVTotalAssetsAmount)).setText(String.format("자산 : %,d원", mTotalAmount[REPORT_ASSETS]));
		((TextView)findViewById(R.id.TVTotalLiabilityAmount)).setText(String.format("부채 : %,d원", mTotalAmount[REPORT_LIABILITY]));
		((TextView)findViewById(R.id.TVTotalPropertyAmount)).setText(String.format("합계 : %,d원", mTotalAmount[REPORT_ASSETS] - mTotalAmount[REPORT_LIABILITY]));
		
		makeReportList();
		
	}
	
	protected void makeReportList() {
		clearReportListAll();
		
		makeReportListAssets();
		makeReportListLiability();
	}

	protected void makeReportListAssets() {
		int assetsSize = mAssetsCategoryItems.size();
		if (assetsSize == 0) {
			mLLReport[REPORT_ASSETS].setVisibility(View.GONE);
			return;
		}
		mLLReport[REPORT_ASSETS].setVisibility(View.VISIBLE);
		
		addTitleLayout(REPORT_ASSETS, "자산", mTotalAmount[REPORT_ASSETS]);
		
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
			}
		}
	};
	
	protected void makeReportListLiability() {
		int liabilitySize = mLiabilityCategoryItems.size();
		if (liabilitySize == 0) {
			mLLReport[REPORT_LIABILITY].setVisibility(View.GONE);
			return;
		}
		mLLReport[REPORT_LIABILITY].setVisibility(View.VISIBLE);
		
		addTitleLayout(REPORT_LIABILITY, "부채", mTotalAmount[REPORT_LIABILITY]);
		
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
    
//    protected void setButtonClickListener() {
//		
//		Button btnExpense = (Button)findViewById(R.id.BtnAssets);
//		btnExpense.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(MainAssetsLayout.this, ReportAssetsLayout.class);
//				startActivity(intent);
//			}
//		});
//		
//		Button btnIncome = (Button)findViewById(R.id.BtnLiability);
//		btnIncome.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(MainAssetsLayout.this, ReportLiabilityLayout.class);
//				startActivity(intent);
//			}
//		});
//	}
//    
    protected void getListItem() {
    	clearTotalCount();
    	mAssetsItems = DBMgr.getAllItems(AssetsItem.TYPE);
    	updateListItem(mAssetsCategoryItems, mAssetsItems);
    	mLiabilityItems = DBMgr.getAllItems(LiabilityItem.TYPE);
    	updateListItem(mLiabilityCategoryItems, mLiabilityItems);
    }
//    
//    protected void setAdapterList() {
//    	setAdapterAssetsList();
//    	setAdapterLiabilityList();
//    }
//    
//    protected void setAdapterLiabilityList() {
//    	final ListView listItem = (ListView)findViewById(R.id.LVLiabilityCategory);
//    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_assets_category, getListItems(LiabilityItem.TYPE));
//    	listItem.setAdapter(mItemAdapter);
//    	
//    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
////				onClickListItem(parent, view, position, id);
//			}
//		});
//	}
//
//	protected void setAdapterAssetsList() {
//    	final ListView listItem = (ListView)findViewById(R.id.LVAssetsCategory);
//    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_assets_category, getListItems(AssetsItem.TYPE));
//    	listItem.setAdapter(mItemAdapter);
//    	
//    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
////				onClickListItem(parent, view, position, id);
//			}
//		});
//	}
//
//	protected ArrayList<CategoryAmount> getListItems(int type) {
//    	ArrayList<CategoryAmount> listItems = new ArrayList<CategoryAmount>();
//    	Collection<CategoryAmount> categoryAmountItems = null;
//    	
//    	if (type == AssetsItem.TYPE) {
//    		categoryAmountItems = mAssetsCategoryItems.values();
//    	}
//    	else if (type == LiabilityItem.TYPE) {
//    		categoryAmountItems = mLiabilityCategoryItems.values();
//    	}
//    	else {
//    		return null;
//    	}
//    	
//    	for (CategoryAmount iterator:categoryAmountItems) {
//			listItems.add(iterator);
//		}
//    	
//    	return listItems;
//    }
//    
//    public class ReportItemAdapter extends ArrayAdapter<CategoryAmount> {
//    	private LayoutInflater mInflater;
//    	private int mResource;
//
//		public ReportItemAdapter(Context context, int resource,
//				 List<CategoryAmount> objects) {
//			super(context, resource, objects);
//			this.mResource = resource;
//			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			CategoryAmount item = (CategoryAmount)getItem(position);
//			
//			if (convertView == null) {
//				convertView = mInflater.inflate(mResource, parent, false);
//			}
//			
//			((TextView)convertView.findViewById(R.id.TVAssetsCategoryName)).setText(String.format("%s(%d)", item.getName(), item.getCount()));
//    		((TextView)convertView.findViewById(R.id.TVAssetsCategoryTotalAmout)).setText(String.format("%,d원", item.getTotalAmount()));			
//			return convertView;
//		}
//    }
//    
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
    }
    
	protected void addTotalCount(FinanceItem item) {
		if (item.getType() == AssetsItem.TYPE) {
			mTotalAmount[REPORT_ASSETS] += item.getAmount();
		}
		else if (item.getType() == LiabilityItem.TYPE) {
			mTotalAmount[REPORT_LIABILITY] += item.getAmount();
		}
	}
	
	public void addTitleLayout(int reportIndex, String name, long amount) {
		LinearLayout llMain = mLLReport[reportIndex];
		LinearLayout llTitle = (LinearLayout)View.inflate(this, R.layout.main_assets_title, null);
		llTitle.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
		llMain.addView(llTitle, params);
		
		TextView tvTitle = (TextView) llTitle.findViewById(R.id.TVMainAssetsTitleName);
		tvTitle.setText(name);
		tvTitle.setTextColor(Color.BLACK);
		
		TextView tvAmount = (TextView) llTitle.findViewById(R.id.TVMainAssetsTitleTotalAmout);
		tvAmount.setText(String.format("%,d원", amount));
		tvAmount.setTextColor(Color.BLACK);
		
		
		
//		Collection<CategoryAmount> categoryAmountItems = mCategoryAmount.values();
//		
//		for (CategoryAmount iterator:categoryAmountItems) {
//			Button btnItem = new Button(getApplicationContext());
//			btnItem.setText(String.format("%s    : %,d원", iterator.getName(), iterator.getTotalAmount()));
//			btnItem.setOnClickListener(categoryListener);
//			btnItem.setTag(iterator);
//			ll.addView(btnItem);
//		}
		
	}
	
//
//
//	private void updateAssetsPorgress() {
//		ProgressBar progress = (ProgressBar)findViewById(R.id.AssetsLiabilityPrograss);
//
//		int max = (int)((mTotalAssetsAmount + mTotalLiabilityAmount)/100);
//		int pos = (int)(mTotalAssetsAmount/100);
//		
//		progress.setMax(max);
//		progress.setProgress(pos);
//		
//	}
}