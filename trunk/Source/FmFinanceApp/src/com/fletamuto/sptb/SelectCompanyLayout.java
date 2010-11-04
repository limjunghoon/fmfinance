package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.db.DBMgr;

public class SelectCompanyLayout extends SelectGridBaseLayout {
	public static final int ACT_COMPANY_NAME_EDIT = MsgDef.ActRequest.ACT_COMPANY_NAME_EDIT;
	protected ArrayList<FinancialCompany> mCompanyNames = null;
	private CompenyNameButtonAdpter mAdapterCompanyName;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
    }
	
	protected void getCompanyNameList() {
		mCompanyNames = DBMgr.getCompanys();
	}
	
	@Override
	public void getData() {
		getCompanyNameList();
	}

	@Override
	public void setAdaper() {
		if (mCompanyNames == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterCompanyName = new CompenyNameButtonAdpter(this, R.layout.grid_select, mCompanyNames);
    	gridCategory.setAdapter(mAdapterCompanyName);
	}
	
	protected void onClickCompanyNameButton(FinancialCompany cardCompenyName) {
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.COMPANY_ID, cardCompenyName.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

    View.OnClickListener compenyNameListener = new View.OnClickListener() {
		public void onClick(View v) {
			FinancialCompany compenyName = (FinancialCompany)v.getTag();
			onClickCompanyNameButton(compenyName);
		}
	};

	private class CompenyNameButtonAdpter extends ArrayAdapter<FinancialCompany> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CompenyNameButtonAdpter(Context context, int resource,
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
			
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			button.setText(FinancialCompany.getName());
			button.setOnClickListener(compenyNameListener);
			button.setTag(FinancialCompany);
			
			return convertView;
		}
	}

	@Override
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectCompanyLayout.this, EditSelecCompanyLayout.class);
		startActivityForResult(intent, ACT_COMPANY_NAME_EDIT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_COMPANY_NAME_EDIT) {
			if (resultCode == RESULT_OK) {
				updateAdapter();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void clearAdapter() {
		if (mAdapterCompanyName != null) {
			mAdapterCompanyName.clear();
		}
	}
}
