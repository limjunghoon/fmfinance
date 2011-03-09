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

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.view.FmBaseLayout;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class BudgetLayout extends FmBaseActivity {  	
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	
	protected int mMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
	protected int mYear = Calendar.getInstance().get(Calendar.YEAR);
	protected ArrayList<BudgetItem> mBudgetItems = new ArrayList<BudgetItem>();
	protected BudgetItemAdapter mBudgetAdapter;
	private int mSelectedPosition = -1;
	
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_budget, true);
        
        setRootView(true);
        setButtonClickListener();
        getItemsFromDB();
        setAdapterList();
        updateChildView();
    }
    
    @Override
    protected void onResume() {
    	updateChildView();
        getItemsFromDB();
        setAdapterList();
    	super.onResume();
    }
    
	@Override
	protected void setTitleBtn() {
		setTitle("예산");
//        setEditButtonListener();
//        setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
        
		super.setTitleBtn();
	}
	
	public void setEditButtonListener() {
		setTitleButtonListener(FmBaseLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(BudgetLayout.this, EditBudgetLayout.class);
				intent.putExtra(MsgDef.ExtraNames.BUDGET_ITEM_LIST, mBudgetItems);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_BUDGET);
			}
		});
	}
    
    protected boolean getItemsFromDB() {
    	mBudgetItems = DBMgr.getBudgetItems(mYear, mMonth);
		
        if (mBudgetItems == null) {
        	return false;
        }
        return true;
    }
    
    private void setButtonClickListener() {		
		Button btnPreviousMonth = (Button)findViewById(R.id.BtnPreviusMonth);
		Button btnNextMonth = (Button)findViewById(R.id.BtnNextMonth);
		
		btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				movePreviousMonth();
			}
		});
		
		btnNextMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveNextMonth();
			}
		});
	}

	protected void updateChildView() {
		TextView tvMonth = (TextView)findViewById(R.id.TVCurrentMonth);
		tvMonth.setText(String.format("%d년 %d월", mYear, mMonth));
	}
	
	public void moveNextMonth() {
		if (12 == mMonth) {
			mYear++;
			mMonth = 1;
		}
		else {
			mMonth++;
		}
		getItemsFromDB();
        setAdapterList();
		updateChildView();
	}
	
	public void movePreviousMonth() {
		if (1 == mMonth) {
			mYear--;
			mMonth = 12;
		}
		else {
			mMonth--;
		}
		getItemsFromDB();
        setAdapterList();
		updateChildView();
	}
	
	protected void setAdapterList() {
    	if (mBudgetItems == null) return;
        
    	final ListView listItem = (ListView)findViewById(R.id.LVBudget);
    	mBudgetAdapter = new BudgetItemAdapter(this, R.layout.budget_list_item, mBudgetItems);
    	listItem.setAdapter(mBudgetAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectedPosition = position;
				Intent intent = new Intent(BudgetLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT);
			}
		});
    	
    	listItem.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});
    }
	
	public static class ViewBudgetHolder {
		TextView mTvBudgetCategory;
		TextView mTvBudgetAmount;
		TextView mTvExpenseCategoryAmount;
		TextView mTvRemainAmount;
		ProgressBar mPbBudget;
		
		public ViewBudgetHolder(TextView tvCategory, TextView tvAmount, TextView tvExpenseCategoryAmount, TextView tvRemainAmount,ProgressBar pbBudget) {
			mTvBudgetCategory = tvCategory;
			mTvBudgetAmount	= tvAmount;
			mTvExpenseCategoryAmount = tvExpenseCategoryAmount;
			mTvRemainAmount = tvRemainAmount;
			mPbBudget = pbBudget;
		}
	}
	
	
	public class BudgetItemAdapter extends ArrayAdapter<BudgetItem> {
    	int mResource;
    	private LayoutInflater mInflater;

		public BudgetItemAdapter(Context context, int resource,
				 List<BudgetItem> objects) {
			super(context, resource, objects);
			
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BudgetItem item = (BudgetItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
				
				ViewBudgetHolder viewHolder = new ViewBudgetHolder(
						(TextView)convertView.findViewById(R.id.TVBudgetCategory),
						(TextView)convertView.findViewById(R.id.TVBudgetAmount), 
						(TextView)convertView.findViewById(R.id.TVExpenseCategoryAmount), 
						(TextView)convertView.findViewById(R.id.TVRemainAmount),
						(ProgressBar)convertView.findViewById(R.id.PBBudget));
				
				convertView.setTag(viewHolder);
			}
			
			
			setListChildViewText(item, convertView);
			
			return convertView;
		}
    }
	
	void setListChildViewText(BudgetItem item, View convertView) {
		
		if (item == null || convertView == null) {
			return;
		}
		
		ViewBudgetHolder viewHolder = (ViewBudgetHolder) convertView.getTag();
		
		TextView tvTitle = viewHolder.mTvBudgetCategory;
		if (item.getExpenseCategory() != null) {
			tvTitle.setText(item.getExpenseCategory().getName());
		}
		else {
			tvTitle.setText("총 예산");
		}
		
		ProgressBar progress = viewHolder.mPbBudget;
		long budgetAmount = item.getAmount();
		long expenseAmount = item.getExpenseAmountMonth();
		long sumAmount = budgetAmount - expenseAmount;
		 
		TextView tvBudgetAmount = viewHolder.mTvBudgetAmount;
		tvBudgetAmount.setText(String.format("%,d원", budgetAmount));
		
		TextView tvExpenseAmount = viewHolder.mTvExpenseCategoryAmount;
		tvExpenseAmount.setTextColor(Color.RED);
		if (expenseAmount == 0) {
			tvExpenseAmount.setText(String.format("%,d원", expenseAmount));
		} 
		else {
			tvExpenseAmount.setText(String.format("-%,d원", expenseAmount));
		}
		
		
		TextView tvRemainAmount = viewHolder.mTvRemainAmount;
		
		
		if (sumAmount < 0) {
			progress.setMax(100);
			progress.setProgress(5);
			tvRemainAmount.setText(String.format("%,d원", sumAmount));
			tvRemainAmount.setTextColor(Color.RED);
		}
		else {
			// 테스트 코드
			int max = (int)(budgetAmount/100);
			int pos = max - (int)(expenseAmount/100);
			
			progress.setMax(max);
			progress.setProgress(pos);
			
			tvRemainAmount.setText(String.format("%,d원", sumAmount));
			tvRemainAmount.setTextColor(Color.BLUE);
		}

	}
	
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_EDIT_BUDGET) {
    		if (resultCode == RESULT_OK) {
    			updateChildView();
    	        getItemsFromDB();
    	        setAdapterList();
    		}
    	}
		else if (requestCode == MsgDef.ActRequest.ACT_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateBudgetAmount(data.getLongExtra(MsgDef.ExtraNames.AMOUNT, 0L));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	 
	 private void updateBudgetAmount(long amount) {
		if (mSelectedPosition == -1) {
			return;
		}
		
		BudgetItem item = mBudgetItems.get(mSelectedPosition);
		item.setAmount(amount);
		
		if (item.getID() == -1) {
			if (DBMgr.addBudget(item) == -1) {
				Log.e(LogTag.LAYOUT, ":: FAIL TO ADD BUDGET");
			}
		}
		else {
			DBMgr.updateBudget(item);
		}
		mBudgetItems.set(mSelectedPosition, item);
		mBudgetAdapter.notifyDataSetChanged();
	}
	 
	 public void setMoveViewMotionEvent(MotionEvent event) {
	    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    		mTouchMove = event.getX();
	    		mTouchMoveFlag = true;
	    	}
	    	else if (event.getAction() == MotionEvent.ACTION_MOVE && mTouchMoveFlag == true) {
	    		
	    		if (mTouchMove-event.getX()< -MOVE_SENSITIVITY) {
	    			mTouchMoveFlag = false;
	    			moveNextMonth();
	    		}
	    		if (mTouchMove-event.getX()> MOVE_SENSITIVITY) {
	    			mTouchMoveFlag = false;
	    			movePreviousMonth();
	    		}
	    	}
	    }

}