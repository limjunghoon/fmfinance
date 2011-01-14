package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class BudgetLayout extends FmBaseActivity {  	
	protected int mMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
	protected int mYear = Calendar.getInstance().get(Calendar.YEAR);
	protected ArrayList<BudgetItem> mBudgetItems = new ArrayList<BudgetItem>();
	protected BudgetItemAdapter mBudgetAdapter;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_budget);
        
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
        setEditButtonListener();
        setTitle(getResources().getString(R.string.btn_category_select));
        setTitleBtnVisibility(FmMainMenuLayout.BTN_RIGTH_01, View.VISIBLE);
        
		super.setTitleBtn();
	}
	
	public void setEditButtonListener() {
		setTitleButtonListener(FmMainMenuLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
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
    	if (mBudgetItems != null) {
    		mBudgetItems.clear();
    	}
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
	//			onClickListItem(parent, view, position, id);
			}
		});
    }
	
	public class BudgetItemAdapter extends ArrayAdapter<BudgetItem> {
    	int resource;

		public BudgetItemAdapter(Context context, int resource,
				 List<BudgetItem> objects) {
			super(context, resource, objects);
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout reportListView;
			BudgetItem item = (BudgetItem)getItem(position);
			
			if (convertView == null) {
				reportListView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater li;
				li = (LayoutInflater)getContext().getSystemService(inflater);
				li.inflate(resource, reportListView, true);
			}
			else {
				reportListView = (LinearLayout)convertView;
			}
			
			setListChildViewText(item, reportListView);
			
			return reportListView;
		}
    }
	
	void setListChildViewText(BudgetItem item, View convertView) {
		if (item == null || convertView == null) {
			return;
		}
		
		TextView tvTitle = (TextView)convertView.findViewById(R.id.TVBudgetCategory);
		if (item.getExpenseCategory() != null) {
			tvTitle.setText(item.getExpenseCategory().getName());
		}
		else {
			tvTitle.setText("총 예산");
		}
		
		ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.PBBudget);
		long budgetAmount = item.getAmount();
		long expenseAmount = item.getExpenseAmountMonth();
		long sumAmount = budgetAmount - expenseAmount;
		 
		TextView tvBudgetAmount = (TextView)convertView.findViewById(R.id.TVBudgetAmount);
		tvBudgetAmount.setText(String.format("%,d원", budgetAmount));
		
		TextView tvExpenseAmount = (TextView)convertView.findViewById(R.id.TVExpenseCategoryAmount);
		tvExpenseAmount.setTextColor(Color.RED);
		if (expenseAmount == 0) {
			tvExpenseAmount.setText(String.format("%,d원", expenseAmount));
		} 
		else {
			tvExpenseAmount.setText(String.format("-%,d원", expenseAmount));
		}
		
		if (sumAmount < 0) {
			progress.setMax(100);
			progress.setProgress(5);
		}
		else {
			// 테스트 코드
			int max = (int)(budgetAmount/100);
			int pos = max - (int)(expenseAmount/100);
			
			progress.setMax(max);
			progress.setProgress(pos);
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
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}