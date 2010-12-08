package com.fletamuto.sptb;

import java.util.ArrayList;
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
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class EditAccountLayout extends FmBaseActivity {  	
	protected static final int ACT_EDIT_ITEM = MsgDef.ActRequest.ACT_EDIT_ITEM;
	public static final int ACT_ADD_ACCOUNT = 0;
	private ArrayList<AccountItem> mAccountListItems = new ArrayList<AccountItem>();
	protected AccountItemAdapter mAdapterAccount;
	private String [] mAccountTypes;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.edit_account, true);
        
        mAccountTypes = getResources().getStringArray(R.array.account_type);
        setAdapterList();
    }

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() {
		mAccountListItems = (ArrayList<AccountItem>) getIntent().getSerializableExtra(MsgDef.ExtraNames.GET_ACCOUNT_ITEMS);
		super.initialize();
	}

	@Override
	protected void setTitleBtn() {
		setTitle("계좌 편집");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "추가");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		setAddButtonListener();
		
		super.setTitleBtn();
	}

	protected void setAdapterList() {
    	if (mAccountListItems == null) return;
        
    	final ListView listAccount = (ListView)findViewById(R.id.LVAccount);
    	mAdapterAccount = new AccountItemAdapter(this, R.layout.edit_list_account, mAccountListItems);
    	listAccount.setAdapter(mAdapterAccount);
    	
    	listAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				AccountItem item = (AccountItem)mAccountListItems.get(position);
		    	startEditInputActivity(InputAccountLayout.class, item.getID());
			}
		});
    }
	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(this, cls);
    	intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, itemId);
    	startActivityForResult(intent, ACT_EDIT_ITEM);
	}

	public void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(EditAccountLayout.this, InputAccountLayout.class);		
				startActivityForResult(intent, ACT_ADD_ACCOUNT);
			}
		});
	}
	
	protected void setListViewText(AccountItem account, View convertView) {
			
		if (account.getType() == AccountItem.MY_POCKET) {
			((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setVisibility(View.GONE);			
			((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("잔액 : %,d원", account.getBalance()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setVisibility(View.GONE);
		}
		else {
			((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setText("번호 : " + account.getNumber());			
			((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("잔액 : %,d원", account.getBalance()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setText("종류 : " + getAccoutTypeName(account.getType()));
		}
		
	}
	
	protected String getAccoutTypeName(int index) {
		if (mAccountTypes == null) return null;
		if (index >= mAccountTypes.length) return null;
		
		if (index == AccountItem.MY_POCKET) {
			return "내 주머니";
		}
		return mAccountTypes[0];
	}
	
	public class AccountItemAdapter extends ArrayAdapter<AccountItem> {
    	int mResource;
    	LayoutInflater mInflater;

		public AccountItemAdapter(Context context, int resource,
				 List<AccountItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AccountItem item = (AccountItem)getItem(position);
			
			if (item.isSeparator()) {
				return createSeparator(mInflater, parent, item);
			}
			else {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			setListViewText(item, convertView);			
			setDeleteBtnListener(convertView, item.getID(), position);
			
			return convertView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mAccountListItems.get(position).isSeparator();
		}

		
    }
	
	private void setDeleteBtnListener(View convertView, int id, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAccountDelete);
    	final int ItemID = id;
    	final int Itempsition = position;
    	
    	btnDelete.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				if (DBMgr.deleteAccount(ItemID) == 0 ) {
					Log.e(LogTag.LAYOUT, "can't delete accoutn Item  ID : " + ItemID);
				}
				mAccountListItems.remove(Itempsition);
				mAdapterAccount.notifyDataSetChanged();
			}
		});
	}
	
	
	public View createSeparator(LayoutInflater inflater, ViewGroup parent, AccountItem item) {
		View convertView = inflater.inflate(R.layout.list_separators, parent, false);
		TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
		tvTitle.setText(item.getSeparatorTitle());
		tvTitle.setTextColor(Color.BLACK);
		convertView.setBackgroundColor(Color.WHITE);
		return convertView;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ACCOUNT) {
			if (resultCode == RESULT_OK) {
				int accountID = data.getIntExtra("ACCOUNT_ID", -1);
				if (accountID == -1) return;
				
				AccountItem account = DBMgr.getAccountItem(accountID);
				if (account == null) return;
				mAdapterAccount.add(account);
				mAdapterAccount.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}