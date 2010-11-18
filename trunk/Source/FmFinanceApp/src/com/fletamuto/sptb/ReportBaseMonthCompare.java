package com.fletamuto.sptb;

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
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportBaseMonthCompare extends ReportBaseCompare {
	
	protected int mMonth;
	protected int mYear;
	protected ItemAdapter mAdapterItem;
	
	protected abstract void setAdapterList();
	protected abstract void setListViewText(FinanceItem financeItem, View convertView);
	
	
	public int getMonth() {
		return mMonth;
	}
	
	public int getYear() {
		return mYear;
	}
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_month_compare, true);
    	
    	getData();
    	setButtonClickListener();
    	
    	updateChildView();
    }
    
    
    protected void getData() {
		mFinanceItems = DBMgr.getItems(mType, mYear, mMonth);
		mTotalAmout = DBMgr.getTotalAmountMonth(mType, mYear, mMonth);
		updateMapCategory();
	}
    
    @Override
    protected void initialize() {
    	mMonth = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_MONTH, Calendar.getInstance().get(Calendar.MONTH)+1);
    	mYear = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_YEAR, Calendar.getInstance().get(Calendar.YEAR));
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
		LinearLayout monthLayout = (LinearLayout)findViewById(R.id.LLMonth);
		LinearLayout dayOfMonthLayout = (LinearLayout)findViewById(R.id.LLDayofMonth);
		ToggleButton tbMonth = (ToggleButton)findViewById(R.id.TBMonth);
		TextView tvMonth = (TextView)findViewById(R.id.TVCurrentMonth);
		tvMonth.setText(String.format("%d년 %d월", mYear, mMonth));
		TextView tvTotalAmount = (TextView)findViewById(R.id.TVTotalAmount);
		tvTotalAmount.setText(String.format("금액 : %,d", mTotalAmout));
		
		monthLayout.setVisibility(View.INVISIBLE);
		dayOfMonthLayout.setVisibility(View.INVISIBLE);
		
		if (tbMonth.isChecked()) {
			monthLayout.setVisibility(View.VISIBLE);
		}
		else {
			dayOfMonthLayout.setVisibility(View.VISIBLE);
		}
		
		updateBarGraph();
		addButtonInLayout();
		
		setAdapterList();
	}
	
	
	
	protected void setAdapterList(int resource) {
    	if (mFinanceItems == null) return;
        
    	final ListView listCard = (ListView)findViewById(R.id.LVIncomeDayOfMonth);
    	mAdapterItem = new ItemAdapter(this, resource, mFinanceItems);
    	listCard.setAdapter(mAdapterItem);
    	
    	listCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
    }
	
	public class ItemAdapter extends ArrayAdapter<FinanceItem> {
    	private int mResource;
    	private LayoutInflater mInflater;

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
	
	public void moveNextMonth() {
		if (12 == mMonth) {
			mYear++;
			mMonth = 1;
		}
		else {
			mMonth++;
		}
		
		getData();
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
		getData();
		updateChildView();
	}
}