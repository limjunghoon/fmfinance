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

import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * @author yongbban
 *
 */
public class EditSelecCardCompanyNameLayout extends EditSelectItemBaseLayout {  	
	public static final int ACT_COMPANY_CARD_NAME_ADD = MsgDef.ActRequest.ACT_COMPANY_CARD_NAME_ADD;
	protected ArrayList<CardCompanyName> mArrCardCompanyName = null;
	private CardCompanyNameButtonAdpter mAdapterCardCompanyName;
	
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        
    }

	@Override
	protected void createItem(String itemName) {
		if (itemName.length() == 0) return;
		
		CardCompanyName cardCompanyName = new CardCompanyName();
		cardCompanyName.setName(itemName);
		
		if (DBMgr.addCardCompanyName(cardCompanyName) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to create COMPANY_CARD_NAME Item");
		}
		updateCardCompanyNameAdapter();
	}
	
	@Override
	protected void updateItem(int editItemID, String itemName, int editPosition) {
		if (itemName.length() == 0) return;
		
		CardCompanyName cardCompanyName = DBMgr.getCardCompanyName(editItemID);
		if (cardCompanyName == null) {
			Log.e(LogTag.LAYOUT, ":: Fail to load CardCompanyName");
		}
		
		cardCompanyName.setName(itemName);
		if (DBMgr.updateCardCompanyName(cardCompanyName) == false) {
			Log.e(LogTag.LAYOUT, ":: Fail update the CardCompanyName");
			return;
		}

		
		mArrCardCompanyName.set(editPosition, cardCompanyName);
		mAdapterCardCompanyName.notifyDataSetChanged();
	}
	
    private void updateCardCompanyNameAdapter() {
    	
    	if (mAdapterCardCompanyName != null) {
    		mAdapterCardCompanyName.clear();
    	}
    	
    	getData();
    	setAdaper();
	}
	

	@Override
	protected void onDeleteButtonClick(int deleteItemID, int deletePosition) {
		if (DBMgr.deleteCardCompanyName(deleteItemID) == 0) {
			return;
		}
	
		mArrCardCompanyName.remove(deletePosition);
		mAdapterCardCompanyName.notifyDataSetChanged();
	}
	

	protected void getCardCompanyNameList() {
		mArrCardCompanyName = DBMgr.getCardCompanyNames();
		
	}
	
	@Override
	public void getData() {
		getCardCompanyNameList();
	}

	@Override
	public void setAdaper() {
		if (mArrCardCompanyName == null) return;
        
    	final ListView listEditSelectItem = (ListView)findViewById(R.id.ListViewEditSelect);
    	mAdapterCardCompanyName = new CardCompanyNameButtonAdpter(this, R.layout.edit_select_item, mArrCardCompanyName);
    	listEditSelectItem.setAdapter(mAdapterCardCompanyName);
    	
    	setListViewListener(listEditSelectItem);
    	
	}
	
	
	View.OnClickListener CardCompanyNameListener = new View.OnClickListener() {
		public void onClick(View v) {
			CardCompanyName cardCompanyName= (CardCompanyName)v.getTag();
			onClickcardCompanyNameButton(cardCompanyName);
		}
	};
	
	protected void onClickcardCompanyNameButton(CardCompanyName cardCompanyName) {
//		Intent intent = new Intent();
//		intent.putExtra(MsgDef.ExtraNames.cardCompanyName_ID, cardCompanyName.getID());
//		intent.putExtra(MsgDef.ExtraNames.cardCompanyName_NAME, cardCompanyName.getName());
//		setResult(RESULT_OK, intent);
//		finish();
	}
	
	
	private class CardCompanyNameButtonAdpter extends ArrayAdapter<CardCompanyName> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CardCompanyNameButtonAdpter(Context context, int resource,
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
			
			TextView cardCompanyNameName = (TextView)convertView.findViewById(R.id.TVEditSelectItem);
			cardCompanyNameName.setText(cardCompanyName.getName());
			
			setDeleteBtnListener(convertView, cardCompanyName.getID(), position);
			setEditBtnListener(convertView, cardCompanyName.getID(), cardCompanyName.getName() ,position);
			
			return convertView;
		}
	}
}
