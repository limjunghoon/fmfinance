package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.db.DBMgr;


public class EditBudgetLayout  extends FmBaseActivity {  	
	protected int mMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
	protected int mYear = Calendar.getInstance().get(Calendar.YEAR);
	protected ArrayList<BudgetItem> mBudgetItems = new ArrayList<BudgetItem>();
	protected BudgetItemAdapter mBudgetAdapter;
	private int mSelectedPosition = -1;
	
	/** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.edit_budget, true);
        mBudgetItems = (ArrayList<BudgetItem>) getIntent().getSerializableExtra(MsgDef.ExtraNames.BUDGET_ITEM_LIST);
        setAdapterList();
    }
    
    protected void setAdapterList() {
    	if (mBudgetItems == null) return;
        
    	final ListView listItem = (ListView)findViewById(R.id.LVBudget);
    	mBudgetAdapter = new BudgetItemAdapter(this, R.layout.budget_edit_list_item, mBudgetItems);
    	listItem.setAdapter(mBudgetAdapter);
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
			setBudAmount(position, item, reportListView);
			return reportListView;
		}
    }
    
    void setBudAmount(final int position, final BudgetItem item, View convertView) {
    	Button btnAmount= (Button)convertView.findViewById(R.id.BtnBudgetAmount);
    	btnAmount.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mSelectedPosition = position;
				Intent intent = new Intent(EditBudgetLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_AMOUNT);
			}
		});
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
			tvTitle.setText("ÃÑ ¿¹»ê");
		}
		
		Button btnAmount= (Button)convertView.findViewById(R.id.BtnBudgetAmount);
		btnAmount.setText(String.format("%,d¿ø", item.getAmount()));
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			long amount = data.getLongExtra("AMOUNT", 0L);
    			updateBudgetAmount(amount);
//    			private int mSelectedBudgetID = -1;
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateBudgetAmount(long amount) {
		BudgetItem item = mBudgetItems.get(mSelectedPosition);
		item.setAmount(amount);
		
		if (item.getID() == -1) {
			DBMgr.addBudget(item);
		}
		else {
			
		}
		mBudgetItems.set(mSelectedPosition, item);
		mBudgetAdapter.notifyDataSetChanged();
	}
}
