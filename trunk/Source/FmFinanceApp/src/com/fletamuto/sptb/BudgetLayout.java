package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
        setContentView(R.layout.main_budget, true);
        
        setTitle("예산");
        setButtonClickListener();
        updateChildView();
        
        getItemsFromDB();
        setAdapterList();
    }
    
    protected boolean getItemsFromDB() {
    	mBudgetItems = DBMgr.getBudget(mYear, mMonth);
		
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
			
			setListViewText(item, reportListView);
			
			return reportListView;
		}
    }
	
	void setListViewText(BudgetItem item, View convertView) {
		if (item == null || convertView == null) {
			return;
		}
		
		TextView tvTitle = (TextView)convertView.findViewById(R.id.TVBudgetCategory);
		if (item.getExpenseCategory() != null) {
			tvTitle.setText(item.getExpenseCategory().getName());
		}
		else {
			tvTitle.setText("메인");
		}
		
	}
}