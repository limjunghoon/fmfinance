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

import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.db.DBMgr;

public class SelectCardCompanyLayout extends SelectGridBaseLayout {
	public static final int ACT_COMPANY_CARD_NAME_EDIT = MsgDef.ActRequest.ACT_COMPANY_CARD_NAME_EDIT;
	protected ArrayList<CardCompanyName> mCardCompenyNames = null;
	private CardCompenyNameButtonAdpter mAdapterCardCompenyName;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
    }
	
	protected void getCardCompenyNameList() {
		mCardCompenyNames = DBMgr.getCardCompanyNames();
	}
	
	@Override
	public void getData() {
		
		getCardCompenyNameList();
	}

	@Override
	public void setAdaper() {
		if (mCardCompenyNames == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterCardCompenyName = new CardCompenyNameButtonAdpter(this, R.layout.grid_select, mCardCompenyNames);
    	gridCategory.setAdapter(mAdapterCardCompenyName);
	}
	
	protected void onClickCardCompenyNameButton(CardCompanyName cardCompenyName) {
		Intent intent = new Intent();
		intent.putExtra("CARD_COMPENY_NAME_ID", cardCompenyName.getID());
		setResult(RESULT_OK, intent);
		finish();
	}

    View.OnClickListener cardCompenyNameListener = new View.OnClickListener() {
		public void onClick(View v) {
			CardCompanyName cardCompenyName = (CardCompanyName)v.getTag();
			onClickCardCompenyNameButton(cardCompenyName);
		}
	};
	


	private class CardCompenyNameButtonAdpter extends ArrayAdapter<CardCompanyName> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CardCompenyNameButtonAdpter(Context context, int resource,
				 List<CardCompanyName> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CardCompanyName cardCompanyName = (CardCompanyName)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			button.setText(cardCompanyName.getName());
			button.setOnClickListener(cardCompenyNameListener);
			button.setTag(cardCompanyName);
			
			return convertView;
		}
	}

	@Override
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectCardCompanyLayout.this, EditSelecCardCompanyNameLayout.class);
		startActivityForResult(intent, ACT_COMPANY_CARD_NAME_EDIT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_COMPANY_CARD_NAME_EDIT) {
			if (resultCode == RESULT_OK) {
				updateAdapter();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void clearAdapter() {
		if (mAdapterCardCompenyName != null) {
			mAdapterCardCompenyName.clear();
		}
	}
}
