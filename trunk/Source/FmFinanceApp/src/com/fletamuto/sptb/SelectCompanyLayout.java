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

public class SelectCompanyLayout extends FmBaseActivity {
	protected ArrayList<FinancialCompany> mArrCompany = null;
	CompanyButtonAdpter mAdapterInstituion;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.select_grid_base, true);
        setTitleButtonListener();
        setEditButtonListener();
        setTitle(getResources().getString(R.string.btn_category_select));
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        
        getCompanyList();
        setCompanyAdaper();
    }
	
	protected void getCompanyList() {
		mArrCompany = DBMgr.getCompany();
	}
	
	protected void setCompanyAdaper() {
		if (mArrCompany == null) return;
        
    	final GridView gridCompany = (GridView)findViewById(R.id.GVSelect);
    	mAdapterInstituion = new CompanyButtonAdpter(this, R.layout.grid_select, mArrCompany);
    	gridCompany.setAdapter(mAdapterInstituion);
    	
	}
	
	protected void updateAdapterCompany() {
		if (mArrCompany != null) {
			mArrCompany.clear();
		}
		getCompanyList();
		setCompanyAdaper();
	}
	
	protected void onClickCompanyButton(FinancialCompany Company) {
		Intent intent = new Intent();
		intent.putExtra("Company_ID", Company.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

    View.OnClickListener CompanyListener = new View.OnClickListener() {
		public void onClick(View v) {
			FinancialCompany Company = (FinancialCompany)v.getTag();
			onClickCompanyButton(Company);
		}
	};
	
	public void setEditButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
			}
		});
	}

	private class CompanyButtonAdpter extends ArrayAdapter<FinancialCompany> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CompanyButtonAdpter(Context context, int resource,
				 List<FinancialCompany> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinancialCompany instituion = (FinancialCompany)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			button.setText(instituion.getName());
			button.setOnClickListener(CompanyListener);
			button.setTag(instituion);
			
			return convertView;
		}
	}
}
