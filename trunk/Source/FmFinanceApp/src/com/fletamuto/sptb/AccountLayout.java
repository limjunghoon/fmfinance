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

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class AccountLayout extends FmBaseActivity {  	
	
	public static final int ACT_EDIT_ACCOUNT = MsgDef.ActRequest.ACT_EDIT_ACCOUNT;
	private long mTatalBalance = 0L;
	private ArrayList<AccountItem> mAccountListItems = new ArrayList<AccountItem>();
	protected AccountItemAdapter mAdapterAccount;
	private String [] mAccountTypes;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_account, true);
        
        getAccountItems();
        updateChildView();
        setAdapterList();
    }
	@Override
	protected void onResume() {
		getAccountItems();
        updateChildView();
        setAdapterList();
		super.onResume();
	}
	
	private void updateChildView() {
		TextView tvBalance = (TextView)findViewById(R.id.TVAccountTatalBalance);
		tvBalance.setText(String.format("총 잔액			%,d원", mTatalBalance));
	}

	@Override
	protected void setTitleBtn() {
		setTitle("계좌");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "편집");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		setEidtButtonListener();
		
		super.setTitleBtn();
	}
	
	protected void getAccountItems() {
		mAccountListItems.clear();
		
		addSeparator(DBMgr.getAccountAllItems());
		mAccountTypes = getResources().getStringArray(R.array.account_type);
    }
	
	private void addSeparator(ArrayList<AccountItem> arrAccount) {
		if (arrAccount == null) return;
		
		int companyID = -1;
		AccountItem separator = new AccountItem();
		AccountItem account = DBMgr.getAccountMyPoctet();
		separator.setSeparatorTitle("내 주머니");
		mAccountListItems.add(separator);
		mAccountListItems.add(account);
		mTatalBalance = account.getBalance();
		
		int size = arrAccount.size();
		for (int index = 0; index < size; index++) {
			account = arrAccount.get(index);			
			if (companyID != account.getCompany().getID()) {
				companyID = account.getCompany().getID();
				separator = new AccountItem();
				separator.setSeparatorTitle(account.getCompany().getName());
				mAccountListItems.add(separator);
			}
			mAccountListItems.add(account);
			mTatalBalance += account.getBalance();
		}
	}

	protected void setAdapterList() {
    	if (mAccountListItems == null) return;
        
    	final ListView listAccount = (ListView)findViewById(R.id.LVAccount);
    	mAdapterAccount = new AccountItemAdapter(this, R.layout.report_list_account, mAccountListItems);
    	listAccount.setAdapter(mAdapterAccount);
    	
    	listAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
    }
	
	public void setEidtButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(AccountLayout.this, EditAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.GET_ACCOUNT_ITEMS, mAccountListItems);
				startActivityForResult(intent, ACT_EDIT_ACCOUNT);
			}
		});
	}
	
	protected void setListViewText(AccountItem account, View convertView) {
			
		if (account.getType() == AccountItem.MY_POCKET) {
			((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setVisibility(View.GONE);			
			((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("잔액 : %,d원", account.getBalance()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setVisibility(View.GONE);
			((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setVisibility(View.GONE);
		}
		else {
			((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setText("번호 : " + account.getNumber());			
			((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("잔액 : %,d원", account.getBalance()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setText("종류 : " + getAccoutTypeName(account.getType()));
			//((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setText("기관 : " + account.getCompany().getName());
			((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setVisibility(View.GONE);
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
    	private int mResource;
    	private LayoutInflater mInflater;
    	
    	TextView tvSeparetorTitle;
  
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
			setTransferBtnListener(convertView, item, position);
			
			return convertView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mAccountListItems.get(position).isSeparator();
		}
		
		public View createSeparator(LayoutInflater inflater, ViewGroup parent, AccountItem item) {
			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
			tvTitle.setText(item.getSeparatorTitle());
			tvTitle.setTextColor(Color.BLACK);
			convertView.setBackgroundColor(Color.WHITE);
			return convertView;
		}

		
    }
	
	private void setTransferBtnListener(View convertView, AccountItem item, final int position) {
    	Button btnTransfer = (Button)convertView.findViewById(R.id.BtnReportAccountTransfer);
    	btnTransfer.setEnabled(item.getBalance() != 0L);
		btnTransfer.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				Intent intent = new Intent(AccountLayout.this, TransferAccountLayout.class);
				AccountItem item = mAccountListItems.get(position);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, item);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_TRANFER_ACCOUNT);
			}
		});
	}
	
	


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_EDIT_ACCOUNT) {
//			if (resultCode == RESULT_OK) {
//				int accountID = data.getIntExtra("ACCOUNT_ID", -1);
//				if (accountID == -1) return;
//				
//				AccountItem account = DBMgr.getAccountItem(accountID);
//				if (account == null) return;
//				mAdapterAccount.add(account);
//				mAdapterAccount.notifyDataSetChanged();
//			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}