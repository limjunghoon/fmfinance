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
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.view.FmBaseLayout;

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
	private int mEditPositieon = -1;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.edit_account, true);
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
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "추가");
		setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
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
			
				mEditPositieon = position;
				AccountItem item = (AccountItem)mAccountListItems.get(position);
				if (item.getType() != AccountItem.MY_POCKET) {
					startEditInputActivity(item.getID(), InputAccountLayout.class);
				}
			}
		});
    }
	
	protected void startEditInputActivity(int itemId, Class<?> cls) {
		Intent intent = new Intent(this, cls);
    	intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, itemId);
    	startActivityForResult(intent, ACT_EDIT_ITEM);
	}

	public void setAddButtonListener() {
		setTitleButtonListener(FmBaseLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
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
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setText("종류 : " + AccountItem.getTypeName(account.getType()));
		}
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
			setDeleteBtnListener(convertView, item, position);
			
			return convertView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mAccountListItems.get(position).isSeparator();
		}
    }
	
	private void setDeleteBtnListener(View convertView, final AccountItem item, final int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAccountDelete);
    	if (item.getType() == AccountItem.MY_POCKET) {
    		btnDelete.setVisibility(View.INVISIBLE);
    	}
    	btnDelete.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				if (DBMgr.deleteAccount(item.getID()) == 0 ) {
					Log.e(LogTag.LAYOUT, "can't delete accoutn Item  ID : " + item.getID());
				}
				mAccountListItems.remove(position);
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
				int accountID = data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1);
				if (accountID == -1) return;
				
				AccountItem account = DBMgr.getAccountItem(accountID);
				if (account == null) return;
				mAdapterAccount.add(account);
				mAdapterAccount.notifyDataSetChanged();
			}
		}
		else if (requestCode == ACT_EDIT_ITEM) {
			if (resultCode == RESULT_OK) {
				int accountID = data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1);
				if (accountID == -1 || mEditPositieon == -1) return;
				
				AccountItem account = DBMgr.getAccountItem(accountID);
				if (account == null) return;
				mAccountListItems.set(mEditPositieon, account);
				mAdapterAccount.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}