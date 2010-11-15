package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportBaseMonthCompare extends FmBaseActivity {
	
	private ArrayList<FinanceItem> mIncomeItems;
	protected ItemAdapter mAdapterItem;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_compare_income, true);
    	
    	getData();
    	setButtonClickListener();
    	
    	setAdapterList();
    	updateChildView();
    	
    	addButtonInLayout();
    }
    
	private void setButtonClickListener() {
		final ToggleButton tbMonth = (ToggleButton)findViewById(R.id.TBMonth);
		final ToggleButton tbDayMonth = (ToggleButton)findViewById(R.id.TBDayOfMonth);
		
		tbMonth.setChecked(true);
		tbMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				tbMonth.setChecked(true);
				tbDayMonth.setChecked(false);
				updateChildView();
			}
		});
		
		tbDayMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				tbDayMonth.setChecked(true);
				tbMonth.setChecked(false);
				updateChildView();
			}
		});
		
		Button btnPreviousMonth = (Button)findViewById(R.id.BtnPreviusMonth);
		Button btnNextMonth = (Button)findViewById(R.id.BtnNextMonth);
		
		btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				LinearLayout ll = (LinearLayout)findViewById(R.id.LLItemButtons);
				ll.removeAllViewsInLayout();
				ll.invalidate();
			}
		});
		
		btnNextMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				addButtonInLayout();
			}
		});
	}

	private void getData() {
		mIncomeItems = DBMgr.getItems(IncomeItem.TYPE, 2010, 11);
	}

	@Override
	protected void setTitleBtn() {
    	setTitle("월 수입");

		super.setTitleBtn();
	}

	private void updateChildView() {
		LinearLayout monthLayout = (LinearLayout)findViewById(R.id.LLMonth);
		LinearLayout dayOfMonthLayout = (LinearLayout)findViewById(R.id.LLDayofMonth);
		ToggleButton tbMonth = (ToggleButton)findViewById(R.id.TBMonth);
		
		monthLayout.setVisibility(View.INVISIBLE);
		dayOfMonthLayout.setVisibility(View.INVISIBLE);
		
		if (tbMonth.isChecked()) {
			monthLayout.setVisibility(View.VISIBLE);
		}
		else {
			dayOfMonthLayout.setVisibility(View.VISIBLE);
		}
		
		updateBarGraph();
	}

	private void updateBarGraph() {
		final PieGraph pieGraph;	
       
		pieGraph = (PieGraph) findViewById (R.id.pgraph);
		pieGraph.setItemValues(new long[] {100, 100});
		pieGraph.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		pieGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		    		return true;
		    	}
				return false;
			}
		});
		
	}
	
	protected void setAdapterList() {
    	if (mIncomeItems == null) return;
        
    	final ListView listCard = (ListView)findViewById(R.id.LVIncomeDayOfMonth);
    	mAdapterItem = new ItemAdapter(this, R.layout.report_list_income, mIncomeItems);
    	listCard.setAdapter(mAdapterItem);
    	
    	listCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
    }
	
	public class ItemAdapter extends ArrayAdapter<FinanceItem> {
    	int mResource;
    	LayoutInflater mInflater;

		public ItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinanceItem item = (FinanceItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
				
				setListViewText(item, convertView);	
			}
			
			return convertView;
		}
    }
	
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	IncomeItem item = (IncomeItem)financeItem;
    	((TextView)convertView.findViewById(R.id.TVIncomeReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("메모 : " + item.getMemo());
		((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("분류 : " + item.getCategory().getName());
	}
    
	
	public void addButtonInLayout() {
		LinearLayout ll = (LinearLayout)findViewById(R.id.LLItemButtons);
		
		for (int index = 0; index < 10; index++) {
			Button btn = new Button(getApplicationContext());
			ll.addView(btn);
		}
		
	}
}