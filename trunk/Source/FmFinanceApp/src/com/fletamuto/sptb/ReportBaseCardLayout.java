package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class ReportBaseCardLayout extends FmBaseActivity {
	public static final int ACT_ADD_CARD = MsgDef.ActRequest.ACT_ADD_CARD;
	
	protected int mType = -1;
//	private ArrayList<CardItem> mArrCard;
	private long mTatalExpenseAmount = 0L;
	private ArrayList<CardExpenseInfo> mArrCardExpenseInfo = new ArrayList<CardExpenseInfo>();
	protected CardItemAdapter mAdapterCard;
	private Calendar mCurrentCalendar = Calendar.getInstance();
	
	public abstract void setType();
	public abstract void AddCardItem();

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setType();
        setContentView(R.layout.main_card, true);
        
        getCardItems();
        setAdapterList();
    }
	
	@Override
	protected void setTitleBtn() {
		setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "추가");
        setAddButtonListener();
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        
		super.setTitleBtn();
	}
	
	protected void getCardItems() {
		mArrCardExpenseInfo.clear();
		ArrayList<CardItem> arrCard = DBMgr.getCardItems(mType);
		mTatalExpenseAmount = 0L;
		int size = arrCard.size();
		for (int index = 0; index < size; index++) {
			CardItem card = arrCard.get(index);
			if (card.getAccount().getID() != -1) {
				card.setAccount(DBMgr.getAccountItem(card.getAccount().getID()));
			}
			CardExpenseInfo cardInfo = new CardExpenseInfo(card);
			long totalExpenseAmount = DBMgr.getCardTotalExpense(mCurrentCalendar.get(Calendar.YEAR), mCurrentCalendar.get(Calendar.MONTH)+1, cardInfo.getCard().getID());
			long billingExpenseAmout = DBMgr.getCardTotalExpense(card.getID(), card.getStartBillingPeriod(Calendar.getInstance()), card.getEndBillingPeriod(Calendar.getInstance()));
			long billingNextExpenseAmout = DBMgr.getCardTotalExpense(card.getID(), card.getNextStartBillingPeriod(Calendar.getInstance()), card.getNextEndBillingPeriod(Calendar.getInstance()));
			cardInfo.setTotalExpenseAmount(totalExpenseAmount);
			cardInfo.setBillingExpenseAmount(billingExpenseAmout);
			cardInfo.setNextBillingExpenseAmount(billingNextExpenseAmout);
			mTatalExpenseAmount += totalExpenseAmount;
			mArrCardExpenseInfo.add(cardInfo);
		}
    }
	
	protected void setAdapterList() {
    	if (mArrCardExpenseInfo == null) return;
        
    	final ListView listCard = (ListView)findViewById(R.id.LVCard);
    	mAdapterCard = new CardItemAdapter(this, R.layout.report_list_credit_card, mArrCardExpenseInfo);
    	listCard.setAdapter(mAdapterCard);
    	
    	listCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CardExpenseInfo item = (CardExpenseInfo)mArrCardExpenseInfo.get(position);
				startDetailInputActivity(item, getDetailCardClass(item.getCard().getType()));
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
	
	protected void updateListView(CardExpenseInfo cardInfo, View convertView) {
		CardItem card = cardInfo.getCard();
		
		if (card.getType() == CardItem.CREDIT_CARD) {
			((TextView)convertView.findViewById(R.id.TVCreditCardName)).setText(card.getCompenyName().getName());
			((TextView)convertView.findViewById(R.id.TVCreditCardType)).setText(getCardTypeName(card.getType()));
			((TextView)convertView.findViewById(R.id.TVCreditCardTotalExpeanseAmount)).setText(String.format("총 지출 금액  : %,d 원", cardInfo.getTotalExpenseAmount()));
			((TextView)convertView.findViewById(R.id.TVCreditCardExpectAmount)).setText(String.format("%d일 결제 예정금액 : %,d 원",card.getSettlementDay(), cardInfo.getBillingExpenseAmount()));
		}
		else if (card.getType() == CardItem.CHECK_CARD) {
			((TextView)convertView.findViewById(R.id.TVCheckCardName)).setText(card.getCompenyName().getName());
			((TextView)convertView.findViewById(R.id.TVCheckCardType)).setText(getCardTypeName(card.getType()));
			((TextView)convertView.findViewById(R.id.TVCheckCardTotalExpeanseAmount)).setText(String.format("총 지출 금액  : %,d 원", cardInfo.getTotalExpenseAmount()));
			((TextView)convertView.findViewById(R.id.TVCheckCardAccount)).setText(String.format("계좌 잔액  : %,d 원", card.getAccount().getBalance()));
		}
		else if (card.getType() == CardItem.PREPAID_CARD) {
			((TextView)convertView.findViewById(R.id.TVPrepaidCardName)).setText(card.getCompenyName().getName());
			((TextView)convertView.findViewById(R.id.TVPrepaidCardType)).setText(getCardTypeName(card.getType()));
			((TextView)convertView.findViewById(R.id.TVPrepaidCardTotalExpeanseAmount)).setText(String.format("총 지출 금액  : %,d 원", cardInfo.getTotalExpenseAmount()));
			((TextView)convertView.findViewById(R.id.TVPrepaidRemainAmount)).setText(String.format("남은 금액  : %,d 원", card.getBalance() - cardInfo.getTotalExpenseAmount()));
			setBudgetPorgress(convertView, cardInfo);
		}
	}
	
	
	
	private void setBudgetPorgress(View convertView, CardExpenseInfo cardInfo) {
		ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.PBPrepaidCardExpense);
		long maxBalance = cardInfo.getCard().getBalance();
		long totalExpenseAmount = cardInfo.getTotalExpenseAmount();
		long sumAmount = maxBalance - totalExpenseAmount;
		
		
		if (sumAmount < 0) {
			progress.setMax(100);
			progress.setProgress(5);
		}
		else {
			// 테스트 코드
			int max = (int)(maxBalance/100);
			int pos = max - (int)(totalExpenseAmount/100);
			
			progress.setMax(max);
			progress.setProgress(pos);
		}
		
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
			
			convertView = mInflater.inflate(getLayoutResource(item.getCard().getType()), parent, false);
			updateListView(item, convertView);			
			return convertView;
		}

		private int getLayoutResource(int type) {
			if (type == CardItem.CREDIT_CARD) {
				return mResource;
			}
			else if (type == CardItem.CHECK_CARD) {
				return R.layout.report_list_check_card;
			}
			else if (type == CardItem.PREPAID_CARD) {
				return R.layout.report_list_prepaid_card;
			}
			else {
				return mResource;
			}
			
		}
    }
	
//	private void setDeleteBtnListener(View convertView, int id, int position) {
////    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAccountDelete);
////    	final int ItemID = id;
////    	final int Itempsition = position;
////    	
////		btnDelete.setOnClickListener(new View.OnClickListener() {
////	
////			public void onClick(View v) {
////				if (DBMgr.deleteAccount(ItemID) == 0 ) {
////					Log.e(LogTag.LAYOUT, "can't delete accoutn Item  ID : " + ItemID);
////				}
////				mArrCard.remove(Itempsition);
////				mAdapterCard.notifyDataSetChanged();
////			}
////		});
//	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_CARD) {
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
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected Class<?> getDetailCardClass(int type) {
		if (type == CardItem.CREDIT_CARD) {
			return CardDetailCreditLayout.class;
		}
		else if (type == CardItem.CHECK_CARD) {
			return CardDetailCheckLayout.class;
		}
		else if (type == CardItem.PREPAID_CARD) {
			return CardDetailPrepaidLayout.class;
		}
		return null;
	}
	
	protected void startDetailInputActivity(CardExpenseInfo item,
			Class<?> cls) {
		if (cls == null) return;
		
		Intent intent = new Intent(this, cls);
    	intent.putExtra(MsgDef.ExtraNames.CARD_EXPENSE_INFO_ITEM, item);
    	intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mCurrentCalendar.get(Calendar.MONTH) + 1);
    	intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mCurrentCalendar.get(Calendar.YEAR));
    	startActivity(intent);
	}
}