package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceCurrentDate;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.view.FmBaseLayout;

public abstract class ReportBaseLayout extends FmBaseActivity {
	public static final int VIEW_NORMAL = 0;
	public static final int VIEW_TWO_LINE = 1;
	
	protected ArrayList<FinanceItem> mItems = null;
	protected ArrayList<FinanceItem> mListItems = new ArrayList<FinanceItem>();
	protected ReportItemAdapter mItemAdapter = null;
//	private int mLatestSelectPosition = -1;
	protected int mCategoryID = -1;
	protected String mCategoryName;
	
	private int mViewMode = VIEW_NORMAL; 
	
	private AccountItem fromItem;
	
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	protected abstract void setDeleteBtnListener(View convertView, int itemId, int position);
	protected abstract int getItemType();
	protected abstract int getAdapterResource();
	protected abstract void onClickAddButton();
	protected abstract void onClickListItem(AdapterView<?> parent, View view, int position, long id);
	protected abstract void updateListItem();
	protected abstract int getLayoutResources(FinanceItem item);
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_base, true);
        
//        getDate();
//        setAdapterList();
//        updateChildView();
    }
	
	@Override
	protected void onResume() {
		getData();
        setAdapterList();
        updateChildView();
        
		super.onResume();
	}
	
	protected void setViewMode(int viewMode) {
		mViewMode = viewMode;
	}
	
	@Override
	protected void setTitleBtn() {
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "Ãß°¡");
        setAddButtonListener();
        setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
        
        if (isDisplayCategory()) {
        	setTitle(mCategoryName);
        }
        
		super.setTitleBtn();
	}
	
	
	public void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onClickAddButton();
			}
		});
	}
	
	public void initialize() {
		if (mViewMode == VIEW_TWO_LINE) {
			findViewById(R.id.LVSeparation).setVisibility(View.VISIBLE);
			findViewById(R.id.LLListTitle).setVisibility(View.VISIBLE);
		}
		
		setButtonClickListener();
		mCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
		mCategoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
	}
	
	protected void getData() {
		
		if (getItemsFromDB(getItemType()) == false) {
			Log.e(LogTag.LAYOUT, ":: ERROR GET DATE");
		}
		
		mListItems.clear();
		updateListItem();
	}
	
	protected void setAdapterList() {
    	if (mItems == null) return;
        
    	final ListView listItem = (ListView)findViewById(R.id.LVCurrentList);
    	mItemAdapter = new ReportItemAdapter(this, getAdapterResource(), mListItems);
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onClickListItem(parent, view, position, id);
			}
		});
    }
	
//	
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		mLatestSelectPosition = position;
//    }
//	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(ReportBaseLayout.this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	protected boolean getItemsFromDB(int itemType) {
		
		if (isDisplayCategory()) {
			mItems = DBMgr.getItemsFromCategoryID(itemType, mCategoryID);
		}
		else {
			mItems = DBMgr.getAllItems(itemType);
		}
    	
        if (mItems == null) {
        	return false;
        }
        return true;
    }
	
	protected boolean getItemsFromDB(int itemType, Calendar calendar) {
		mItems = DBMgr.getItems(itemType, calendar);
		
        if (mItems == null) {
        	return false;
        }
        return true;
    }
	
	private boolean isDisplayCategory() {
		return (mCategoryID != -1);
	}

	public class ReportItemAdapter extends ArrayAdapter<FinanceItem> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			FinanceItem item = (FinanceItem)getItem(position);
			
			if (mViewMode == VIEW_NORMAL) {
				if (item.isSeparator()) {
					return createSeparator(mInflater, parent, item);
				}
				else {
					convertView = mInflater.inflate(getLayoutResources(item), parent, false);
				}
				
				setListViewText(item, convertView);
				setDeleteBtnListener(convertView, item.getID(), position);
			}
			else 
			{
				if (convertView == null) {
					convertView = mInflater.inflate(mResource, parent, false);
				}
				
				convertView.findViewById(R.id.LLSectionTitle).setVisibility(item.isSeparator() ? View.VISIBLE : View.GONE);
				convertView.findViewById(R.id.LLSectionBody).setVisibility(item.isSeparator() ? View.GONE : View.VISIBLE);
				
				setListViewText(item, convertView);
			}
			
			
			
			return convertView;
		}
		
		public View createSeparator(LayoutInflater inflater, ViewGroup parent, FinanceItem item) {
			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
			tvTitle.setText(item.getSeparatorTitle());
			tvTitle.setTextColor(Color.BLACK);
			convertView.setBackgroundColor(Color.WHITE);
			return convertView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mListItems.get(position).isSeparator();
		}
    }
	
	Button.OnClickListener deleteBtnListener = new  Button.OnClickListener(){
    	
		public void onClick(View arg0) {
			Integer position = (Integer)arg0.getTag(R.id.delete_position);
			Integer id = (Integer)arg0.getTag(R.id.delete_id);
			if (deleteItemToDB(id) == 0) {
				Log.e(LogTag.LAYOUT, "== noting delete id : " + id);
			}
			else {
				ExpenseItem item = (ExpenseItem)mItems.get(position.intValue());
		    	PaymentMethod paymentMethod = item.getPaymentMethod();
		    	
				if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
					PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
					if (accountMethod.getAccount() == null) {
						accountMethod.setAccount(DBMgr.getAccountItem(paymentMethod.getMethodItemID()));
					}
					fromItem = accountMethod.getAccount();
					
	    			long fromItemBalance = fromItem.getBalance();
					fromItem.setBalance(fromItemBalance + item.getAmount());
					DBMgr.updateAccount(fromItem);
				}
				mItems.remove(position.intValue());
				mItemAdapter.notifyDataSetChanged();
			}
		}
	};
//	
//	@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    
//    	if (requestCode == ACT_ITEM_EDIT) {
//    		if (resultCode == RESULT_OK) {
//    			FinanceItem item = getItemInstance(data.getIntExtra("EDIT_ITEM_ID", -1));
//    			
//    			if (mLatestSelectPosition != -1 && item != null) {
//    				mItems.set(mLatestSelectPosition, item);
//    				mItemAdapter.notifyDataSetChanged();
//    			}
//    		}
//    	}
//    	
//    	super.onActivityResult(requestCode, resultCode, data);
//    }
	
	protected int deleteItemToDB(int id) {
		return DBMgr.deleteItem(getItemType(), id);
	}

	protected FinanceItem getItemInstance(int id) {
		return DBMgr.getItem(getItemType(), id);
	}
	
	protected void updateChildView() {
		
	}
	
	private void setButtonClickListener() {
		Button btnPreviousDay= (Button)findViewById(R.id.BtnPreviusDay);
		Button btnNextDay = (Button)findViewById(R.id.BtnNextDay);
		
		btnPreviousDay.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				movePreviousDay();
			}

			
		});
		
		btnNextDay.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveNextDay();
			}
		});
	}
	
	protected void movePreviousDay() {
		FinanceCurrentDate.moveCurrentDay(-1);
		
		getData();
        setAdapterList();
        updateChildView();
	}
	
	protected void moveNextDay() {
		FinanceCurrentDate.moveCurrentDay(1);
		getData();
        setAdapterList();
        updateChildView();
	}
	
}
