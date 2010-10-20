package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportBaseCardLayout extends FmBaseActivity {
	public static final int ACT_ADD_CARD = 0;
	
	protected int mType = -1;
	private ArrayList<CardItem> mArrCard;
	protected CardItemAdapter mAdapterCard;
	
	public abstract void setType();
	public abstract void AddCardItem();

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setType();
        setContentView(R.layout.empty_list, true);
        
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "추가");
        setTitleButtonListener();
        setAddButtonListener();
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        
        getCardItems();
        setAdapterList();
    }
	
	protected void getCardItems() {
		mArrCard = DBMgr.getCardItems(mType);
    }
	
	protected void setAdapterList() {
    	if (mArrCard == null) return;
        
    	final ListView listCard = (ListView)findViewById(R.id.LVBase);
    	mAdapterCard = new CardItemAdapter(this, R.layout.report_list_card, mArrCard);
    	listCard.setAdapter(mAdapterCard);
    	
    	listCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
    }
	
	public void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				AddCardItem();
			}
		});
	}
	
	protected void setListViewText(CardItem card, View convertView) {
			((TextView)convertView.findViewById(R.id.TVCardCompany)).setText("카드사 : " + card.getCompenyName().getName());			
			((TextView)convertView.findViewById(R.id.TVCardName)).setText("카드명 : " + card.getName());
			((TextView)convertView.findViewById(R.id.TVCardNumber)).setText("카드번호 : " + card.getNumber());
	}
	

	
	public class CardItemAdapter extends ArrayAdapter<CardItem> {
    	int mResource;
    	LayoutInflater mInflater;

		public CardItemAdapter(Context context, int resource,
				 List<CardItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CardItem item = (CardItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			setListViewText(item, convertView);			
			setDeleteBtnListener(convertView, item.getID(), position);
			
			return convertView;
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
				mArrCard.remove(Itempsition);
				mAdapterCard.notifyDataSetChanged();
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_CARD) {
			if (resultCode == RESULT_OK) {
				int cardID = data.getIntExtra("CARD_ID", -1);
				if (cardID == -1) return;
				
				CardItem card = DBMgr.getCardItem(cardID);
				if (card == null) return;
				if (mAdapterCard == null) {
					getCardItems();
			        setAdapterList();
				}
				else {
					mAdapterCard.add(card);
					mAdapterCard.notifyDataSetChanged();
				}
				
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}