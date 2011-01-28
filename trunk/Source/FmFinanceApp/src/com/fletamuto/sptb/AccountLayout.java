package com.fletamuto.sptb;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

/**
 * Ä«µå ·¹ÀÌ¾Æ¿ô Å¬·¹½º
 * @author yongbban
 * @version  1.0.0.1
 */
public class AccountLayout extends FmBaseActivity {  	
	
	public static final int ACT_EDIT_ACCOUNT = MsgDef.ActRequest.ACT_EDIT_ACCOUNT;
	private long mTatalBalance = 0L;
	private ArrayList<AccountItem> mAccountListItems = new ArrayList<AccountItem>();
	protected AccountItemAdapter mAdapterAccount;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_account, true);
        updateData();
    }
	
	protected void updateData() {
		getAccountItems();
        setAdapterList();
        updateChildView();
	}
	
	private void updateChildView() {
		TextView tvBalance = (TextView)findViewById(R.id.TVAccountTatalBalance);
		tvBalance.setText(String.format("ÃÑ ÀÜ¾×			%,d¿ø", mTatalBalance));
	}

	@Override
	protected void setTitleBtn() {
		setTitle("°èÁÂ");
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "ÆíÁý");
		setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
		setEidtButtonListener();
		
		super.setTitleBtn();
	}
	
	protected void getAccountItems() {
		mAccountListItems.clear();
		
		addSeparator(DBMgr.getAccountAllItems());
    }
	
	private void addSeparator(ArrayList<AccountItem> arrAccount) {
		if (arrAccount == null) return;
		
		int companyID = -1;
		AccountItem separator = new AccountItem();
		AccountItem account = DBMgr.getAccountMyPoctet();
		separator.setSeparatorTitle("³» ÁÖ¸Ó´Ï");
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
		setTitleButtonListener(FmBaseLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
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
			((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("ÀÜ¾× : %,d¿ø", account.getBalance()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setVisibility(View.GONE);
			((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setVisibility(View.GONE);
		}
		else {
			((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setText("¹øÈ£ : " + account.getNumber());			
			((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("ÀÜ¾× : %,d¿ø", account.getBalance()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setText("Á¾·ù : " + AccountItem.getTypeName(account.getType()));
			((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setVisibility(View.GONE);
		}
	}
	
	
	public class AccountItemAdapter extends ArrayAdapter<AccountItem> {
    	private int mResource;
    	private LayoutInflater mInflater;
    	
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
//    	Button btnTransfer = (Button)convertView.findViewById(R.id.BtnReportAccountTransfer);
//    	if (item.getType() == AccountItem.TIME_DEPOSIT || item.getType() == AccountItem.SAVINGS) {
//    		btnTransfer.setVisibility(View.GONE);
//    	}
//    	else {
//    		btnTransfer.setEnabled(item.getBalance() != 0L);
//    	}
//    	
//		btnTransfer.setOnClickListener(new View.OnClickListener() {
//	
//			public void onClick(View v) {
//				Intent intent = new Intent(AccountLayout.this, TransferAccountLayout.class);
//				AccountItem item = mAccountListItems.get(position);
//				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, item);
//				startActivityForResult(intent, MsgDef.ActRequest.ACT_TRANFER_ACCOUNT);
//			}
//		});
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_EDIT_ACCOUNT) {
			if (resultCode == RESULT_OK) {
				updateData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}