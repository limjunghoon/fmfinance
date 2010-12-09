package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class EditCardLayout extends FmBaseActivity {  	
	protected static final int ACT_EDIT_ITEM = MsgDef.ActRequest.ACT_EDIT_ITEM;
	public static final int ACT_ADD_CARD = 0;
	
	private ArrayList<CardItem> mArrCardItems = new ArrayList<CardItem>();
	protected CardItemAdapter mAdapterCard;
	private int mEditPositieon = -1;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_card, true);
        
        getCardItems();
        setAdapterList();
    }
	
	protected void getCardItems() {
		mArrCardItems = DBMgr.getCardItems();
    }
	
	@Override
	protected void setTitleBtn() {
		setTitle("카드 편집");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "추가");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		setAddButtonListener();
		
		super.setTitleBtn();
	}
	
	protected void setAdapterList() {
    	if (mArrCardItems == null) return;
        
    	final ListView listCard = (ListView)findViewById(R.id.LVCard);
    	mAdapterCard = new CardItemAdapter(this, R.layout.edit_list_card, mArrCardItems);
    	listCard.setAdapter(mAdapterCard);
    	
    	listCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CardItem item = (CardItem)mArrCardItems.get(position);
				mEditPositieon = position;
				startEditInputActivity(item.getID(), getEditCardClass(item.getType()));
			}
		});
    }
	
	protected Class<?> getEditCardClass(int type) {
		if (type == CardItem.CREDIT_CARD) {
			return InputCreditCardLayout.class;
		}
		else if (type == CardItem.CHECK_CARD) {
			return InputCheckCardLayout.class;
		}
		else if (type == CardItem.PREPAID_CARD) {
			return InputPrepaidCardLayout.class;
		}
		return null;
	}

	protected void startEditInputActivity(int itemId, Class<?> cls) {
		Intent intent = new Intent(this, cls);
    	intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, itemId);
    	startActivityForResult(intent, ACT_EDIT_ITEM);
	}
	
	public void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(EditCardLayout.this, SelectInputCardLayout.class);		
				startActivityForResult(intent, ACT_ADD_CARD);
			}
		});
	}
	
	protected void setListViewText(CardItem card, View convertView) {
		((TextView)convertView.findViewById(R.id.TVCardCompany)).setText("카드사 : " + card.getCompenyName().getName());			
		((TextView)convertView.findViewById(R.id.TVCardType)).setText("카드종류 : " + getCardTypeName(card.getType()));
		((TextView)convertView.findViewById(R.id.TVCardName)).setText("카드명 : " + card.getName());
		((TextView)convertView.findViewById(R.id.TVCardNumber)).setText("카드번호 : " + card.getNumber());
	}
	
	private CharSequence getCardTypeName(int type) {
		if (type == CardItem.CREDIT_CARD) {
			return  "신용카드";
		}
		else if (type == CardItem.CHECK_CARD) {
			return  "체크카드";
		}
		else if (type == CardItem.PREPAID_CARD) {
			return "선불카드";
		}
		
		return "";
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
			CardItem card = (CardItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			setListViewText(card, convertView);			
			setDeleteBtnListener(convertView, card.getID(), position);
			
			return convertView;
		}

		
    }
	
	private void setDeleteBtnListener(View convertView, int id, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAccountDelete);
    	final int ItemID = id;
    	final int Itempsition = position;
    	
		btnDelete.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				if (DBMgr.deleteCardItem(ItemID) == 0 ) {
					Log.e(LogTag.LAYOUT, "can't delete accoutn Item  ID : " + ItemID);
				}
				mArrCardItems.remove(Itempsition);
				mAdapterCard.notifyDataSetChanged();
			}
		});
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_CARD) {
			if (resultCode == RESULT_OK) {
				int cardID = data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1);
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
		else if (requestCode == ACT_EDIT_ITEM) {
			if (resultCode == RESULT_OK) {
				int cardID = data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1);
				if (cardID == -1 || mEditPositieon == -1) return;
				
				CardItem card = DBMgr.getCardItem(cardID);
				if (card == null) return;
				mArrCardItems.set(mEditPositieon, card);
				mAdapterCard.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}