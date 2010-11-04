package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.db.DBMgr;

/**
 * @author yongbban
 *
 */
public class EditSelecCompanyLayout extends EditSelectItemBaseLayout {  	
	public static final int ACT_COMPANY_NAME_ADD = MsgDef.ActRequest.ACT_COMPANY_NAME_ADD;
	protected ArrayList<FinancialCompany> mArrFinancialCompany = null;
	private FinancialCompanyButtonAdpter mAdapterFinancialCompany;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
    }

	@Override
	protected void createItem(String itemName) {
		if (itemName.length() == 0) return;
		
		FinancialCompany FinancialCompany = new FinancialCompany();
		FinancialCompany.setName(itemName);
		
		if (DBMgr.addCompany(FinancialCompany) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to create COMPANY_NAME Item");
		}
		updateFinancialCompanyAdapter();
	}
	
	@Override
	protected void updateItem(int editItemID, String itemName, int editPosition) {
		if (itemName.length() == 0) return;
		
		FinancialCompany FinancialCompany = DBMgr.getCompany(editItemID);
		if (FinancialCompany == null) {
			Log.e(LogTag.LAYOUT, ":: Fail to load FinancialCompany");
		}
		
		FinancialCompany.setName(itemName);
		if (DBMgr.updateCompany(FinancialCompany) == false) {
			Log.e(LogTag.LAYOUT, ":: Fail update the FinancialCompany");
			return;
		}

		
		mArrFinancialCompany.set(editPosition, FinancialCompany);
		mAdapterFinancialCompany.notifyDataSetChanged();
	}
	
    private void updateFinancialCompanyAdapter() {
    	
    	if (mAdapterFinancialCompany != null) {
    		mAdapterFinancialCompany.clear();
    	}
    	
    	getData();
    	setAdaper();
	}
	

	@Override
	protected void onDeleteButtonClick(int deleteItemID, int deletePosition) {
		if (DBMgr.deleteCompany(deleteItemID) == 0) {
			return;
		}
	
		mArrFinancialCompany.remove(deletePosition);
		mAdapterFinancialCompany.notifyDataSetChanged();
	}
	

	protected void getFinancialCompanyList() {
		mArrFinancialCompany = DBMgr.getCompanys();
		
	}
	
	@Override
	public void getData() {
		getFinancialCompanyList();
	}

	@Override
	public void setAdaper() {
		if (mArrFinancialCompany == null) return;
        
    	final ListView listEditSelectItem = (ListView)findViewById(R.id.ListViewEditSelect);
    	mAdapterFinancialCompany = new FinancialCompanyButtonAdpter(this, R.layout.edit_select_item, mArrFinancialCompany);
    	listEditSelectItem.setAdapter(mAdapterFinancialCompany);
    	
    	setListViewListener(listEditSelectItem);
    	
	}
	
	
	View.OnClickListener CompanyListener = new View.OnClickListener() {
		public void onClick(View v) {
			FinancialCompany FinancialCompany= (FinancialCompany)v.getTag();
			onClickCompanyButton(FinancialCompany);
		}
	};
	
	protected void onClickCompanyButton(FinancialCompany FinancialCompany) {
//		Intent intent = new Intent();
//		intent.putExtra(MsgDef.ExtraNames.FinancialCompany_ID, FinancialCompany.getID());
//		intent.putExtra(MsgDef.ExtraNames.FinancialCompany_NAME, FinancialCompany.getName());
//		setResult(RESULT_OK, intent);
//		finish();
	}
	
	
	private class FinancialCompanyButtonAdpter extends ArrayAdapter<FinancialCompany> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public FinancialCompanyButtonAdpter(Context context, int resource,
				 List<FinancialCompany> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinancialCompany FinancialCompany = (FinancialCompany)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			TextView FinancialCompanyName = (TextView)convertView.findViewById(R.id.TVEditSelectItem);
			FinancialCompanyName.setText(FinancialCompany.getName());
			
			setDeleteBtnListener(convertView, FinancialCompany.getID(), position);
			setEditBtnListener(convertView, FinancialCompany.getID(), FinancialCompany.getName() ,position);
			
			return convertView;
		}
	}
}
