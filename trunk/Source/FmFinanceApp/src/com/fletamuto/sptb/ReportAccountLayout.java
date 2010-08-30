package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.EditCategoryLayout.CategoryItemAdapter;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportAccountLayout extends FmBaseActivity {
	public static final int ACT_ADD_ACCOUNT = 0;
	
	private ArrayList<AccountItem> mArrAccount;
	protected CategoryItemAdapter mAdapterAccount;
	private String [] mAccountTypes;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.empty_list, true);
        
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "추가");
        setTitleButtonListener();
        setAddButtonListener();
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        
        getAccountItems();
        setAdapterList();
    }
	
	protected void getAccountItems() {
		mArrAccount = DBMgr.getInstance().getAccountAllItems();
		mAccountTypes = getResources().getStringArray(R.array.account_type);
    }
	
	protected void setAdapterList() {
    	if (mArrAccount == null) return;
        
    	final ListView listAccount = (ListView)findViewById(R.id.LVBase);
    	mAdapterAccount = new CategoryItemAdapter(this, R.layout.report_list_account, mArrAccount);
    	listAccount.setAdapter(mAdapterAccount);
    	
    	listAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
    	
    
    }
	
	public void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ReportAccountLayout.this, InputAccountLayout.class);		
				startActivityForResult(intent, ACT_ADD_ACCOUNT);
			}
		});
	}
	
	protected void setListViewText(AccountItem account, View convertView) {
			
			((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setText("번호 : " + account.getNumber());			
			((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("잔액 : %,d원", account.getBalance()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setText("종류 : " + getAccoutTypeName(account.getType()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setText("기관 : " + account.getInstitution().getName());
	}
	
	protected String getAccoutTypeName(int index) {
		if (mAccountTypes == null) return null;
		if (index >= mAccountTypes.length) return null;
		return mAccountTypes[index];
	}
	
	public class CategoryItemAdapter extends ArrayAdapter<AccountItem> {
    	int mResource;
    	LayoutInflater mInflater;

		public CategoryItemAdapter(Context context, int resource,
				 List<AccountItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AccountItem item = (AccountItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			setListViewText(item, convertView);
			
//			setDeleteBtnListener(convertView, item.getId(), position);
//			setEditBtnListener(convertView, item, position);
			
			return convertView;
		}
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ACCOUNT) {
			if (resultCode == RESULT_OK) {
				int accountID = data.getIntExtra("ACCOUNT_ID", -1);
				if (accountID == -1) return;
				
				AccountItem account = DBMgr.getInstance().getAccountItem(accountID);
				if (account == null) return;
				mAdapterAccount.add(account);
				mAdapterAccount.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}