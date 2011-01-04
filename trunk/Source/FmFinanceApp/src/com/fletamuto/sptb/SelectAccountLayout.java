package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.db.DBMgr;

public class SelectAccountLayout extends Activity {
	public static final int ACT_ADD_ACCOUNT = 0;
	public static final int MODE_NORMAL = 0;
	public static final int MODE_TRASFER = 1;
	
	private ArrayList<AccountItem> mArrAccount;
	protected CategoryItemAdapter mAdapterAccount;
	private int mExceptionID = -1;
	private int mMode = MODE_NORMAL;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
  //      requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_account);
        
        initalize();
        setAddButtonListener();
        getAccountItems();
        setAdapterList();
    }
    
	private void initalize() {
		mExceptionID = getIntent().getIntExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_EXCEPTION, -1);
		mMode = getIntent().getIntExtra(MsgDef.ExtraNames.SELECT_ACCOUNT_MODE, MODE_NORMAL);
	}

	protected void getAccountItems() {
		if (mMode == MODE_TRASFER) {
			mArrAccount = new ArrayList<AccountItem>();
			mArrAccount.add(DBMgr.getAccountMyPoctet());
			mArrAccount.addAll(DBMgr.getAccountAllItems());
			
			int size = mArrAccount.size();
			for (int index = 0; index < size; index++) {
				AccountItem account = mArrAccount.get(index); 
				if (account.getID() == mExceptionID) {
					mArrAccount.remove(index);
					index--; size--;
					break;
				}
				
				if (account.getType() == AccountItem.TIME_DEPOSIT || account.getType() == AccountItem.SAVINGS) {
					mArrAccount.remove(index);
					index--; size--;
				}
			}
		}
		else {
			mArrAccount = DBMgr.getAccountAllItems();
			int size = mArrAccount.size();
			for (int index = 0; index < size; index++) {
				AccountItem account = mArrAccount.get(index);
				
				if (account.getType() == AccountItem.TIME_DEPOSIT || account.getType() == AccountItem.SAVINGS) {
					mArrAccount.remove(index);
					index--; size--;
				}
			}
		}
    }
	
	protected void setAdapterList() {
    	if (mArrAccount == null) return;
        
    	final ListView listAccount = (ListView)findViewById(R.id.LVAccount);
    	mAdapterAccount = new CategoryItemAdapter(this, R.layout.text_list, mArrAccount);
    	listAccount.setAdapter(mAdapterAccount);
    	
    	listAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				AccountItem account = (AccountItem)view.getTag();
				Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ID, account.getID());
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, account);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
    }

	private void setAddButtonListener() {
		Button btnAdd = (Button)findViewById(R.id.BtnAccountAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(SelectAccountLayout.this, InputAccountLayout.class);		
				startActivityForResult(intent, ACT_ADD_ACCOUNT);
			}
		});
	}  
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ACCOUNT) {
			if (resultCode == RESULT_OK) {
				int accountID = data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1);
				if (accountID == -1) return;
				
				AccountItem account = DBMgr.getAccountItem(accountID);
				if (account == null) return;
				mAdapterAccount.add(account);
				mAdapterAccount.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
			
			TextView tvAccount = (TextView)convertView.findViewById(R.id.TVListItem);
			if (item.getType() == AccountItem.MY_POCKET) {
				tvAccount.setText("내 주머니");
			}
			else {
				tvAccount.setText(String.format("%s : %s", item.getCompany().getName(), item.getNumber()));
			}
			
			convertView.setTag(item);
			return convertView;
		}
    }
}