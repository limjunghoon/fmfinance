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

public class SelectInstitutionLayout extends FmBaseActivity {
	protected ArrayList<FinancialCompany> mArrInstitution = null;
	InstitutionButtonAdpter mAdapterInstituion;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.select_grid_base, true);
        setTitleButtonListener();
        setEditButtonListener();
        setTitle(getResources().getString(R.string.btn_category_select));
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        
        getInstitutionList();
        setInstitutionAdaper();
    }
	
	protected void getInstitutionList() {
		mArrInstitution = DBMgr.getInstitutions();
	}
	
	protected void setInstitutionAdaper() {
		if (mArrInstitution == null) return;
        
    	final GridView gridInstitution = (GridView)findViewById(R.id.GVSelect);
    	mAdapterInstituion = new InstitutionButtonAdpter(this, R.layout.grid_select, mArrInstitution);
    	gridInstitution.setAdapter(mAdapterInstituion);
    	
	}
	
	protected void updateAdapterInstitution() {
		if (mArrInstitution != null) {
			mArrInstitution.clear();
		}
		getInstitutionList();
		setInstitutionAdaper();
	}
	
	protected void onClickInstitutionButton(FinancialCompany institution) {
		Intent intent = new Intent();
		intent.putExtra("INSTITUTION_ID", institution.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

    View.OnClickListener institutionListener = new View.OnClickListener() {
		public void onClick(View v) {
			FinancialCompany institution = (FinancialCompany)v.getTag();
			onClickInstitutionButton(institution);
		}
	};
	
	public void setEditButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
			}
		});
	}

	private class InstitutionButtonAdpter extends ArrayAdapter<FinancialCompany> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public InstitutionButtonAdpter(Context context, int resource,
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
			button.setOnClickListener(institutionListener);
			button.setTag(instituion);
			
			return convertView;
		}
	}
}
