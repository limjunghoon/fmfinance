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

import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class EditCardLayout extends FmBaseActivity {  	
	
	public static final int ACT_ADD_CARD = 0;
	
//	private ArrayList<CardItem> mArrCard;
	private ArrayList<CardExpenseInfo> mArrCardExpenseInfo = new ArrayList<CardExpenseInfo>();
	protected CardItemAdapter mAdapterCard;
	private Calendar mCurrentCalendar = Calendar.getInstance();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_card, true);
        
        getCardItems();
        setAdapterList();
    }
	
	@Override
	protected void setTitleBtn() {
		setTitle("카드");
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "편집");
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
		setEditButtonListener();
		
		super.setTitleBtn();
	}
	
	protected void getCardItems() {
		ArrayList<CardItem> arrCard = DBMgr.getCardItems();
		int size = arrCard.size();
		for (int index = 0; index < size; index++) {
			CardExpenseInfo cardInfo = new CardExpenseInfo(arrCard.get(index));
			cardInfo.setTotalExpenseAmount(DBMgr.getCardTotalExpense(mCurrentCalendar.get(Calendar.YEAR), mCurrentCalendar.get(Calendar.MONTH)+1, cardInfo.getCard().getID()));
			mArrCardExpenseInfo.add(cardInfo);
		}
    }
	
	protected void setAdapterList() {
    	if (mArrCardExpenseInfo == null) return;
        
    	final ListView listCard = (ListView)findViewById(R.id.LVCard);
    	mAdapterCard = new CardItemAdapter(this, R.layout.report_list_card, mArrCardExpenseInfo);
    	listCard.setAdapter(mAdapterCard);
    	
    	listCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
    }
	
	public void setEditButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
//				Intent intent = new Intent(CardLayout.this, SelectInputCardLayout.class);		
//				startActivityForResult(intent, ACT_ADD_CARD);
			}
		});
	}
	
	protected void setListViewText(CardItem card, View convertView) {
			((TextView)convertView.findViewById(R.id.TVCardCompany)).setText("카드사 : " + card.getCompenyName().getName());			
			((TextView)convertView.findViewById(R.id.TVCardName)).setText("카드명 : " + card.getName());
			((TextView)convertView.findViewById(R.id.TVCardNumber)).setText("카드번호 : " + card.getNumber());
	}
	

	
	public class CardItemAdapter extends ArrayAdapter<CardExpenseInfo> {
    	int mResource;
    	LayoutInflater mInflater;

		public CardItemAdapter(Context context, int resource,
				 List<CardExpenseInfo> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CardExpenseInfo item = (CardExpenseInfo)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
//			setListViewText(item, convertView);			
//			setDeleteBtnListener(convertView, item.getID(), position);
			
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
				mArrCardExpenseInfo.remove(Itempsition);
				mAdapterCard.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == ACT_ADD_CARD) {
//			if (resultCode == RESULT_OK) {
//				int cardID = data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1);
//				if (cardID == -1) return;
//				
//				CardItem card = DBMgr.getCardItem(cardID);
//				if (card == null) return;
//				if (mAdapterCard == null) {
//					getCardItems();
//			        setAdapterList();
//				}
//				else {
//					mAdapterCard.add(card);
//					mAdapterCard.notifyDataSetChanged();
//				}
//				
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
}