package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.CardPayment;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.view.FmBaseLayout;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class CardPaymentLayout extends FmBaseActivity {  	
	
	private CardExpenseInfo mCardInfo;
	protected ArrayList<FinanceItem> mCardExpenseItems = null;
	protected ReportCardExpenseItemAdapter mCardExpenseAdapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardpayment, true);
        
        getCardExpenseItems();
        updateChildView();
        setButtonListener();
        
    }
	
	public void setButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				AccountItem account = DBMgr.getAccountItem(mCardInfo.getCard().getAccount().getID());
				if (account != null) {
					long balance = account.getBalance();
					
					if (balance < mCardInfo.getBillingExpenseAmount()) {
						account.setBalance(0L);
					}else {
						account.setBalance(balance - mCardInfo.getBillingExpenseAmount());
					}
					
					DBMgr.updateAccount(account);
					
				}
				
				CardPayment cardPayment = new CardPayment();
				cardPayment.setCardId(mCardInfo.getCard().getID());
				cardPayment.setPaymentAmount(mCardInfo.getBillingExpenseAmount());
				cardPayment.setPaymentDate(mCardInfo.getSettlementDate());
				cardPayment.setRemainAmount(0L);
				cardPayment.setState(CardPayment.DONE);
				DBMgr.addCardPaymentItem(cardPayment);
				
				finish();
			}
		});
	}
	
	public void getCardExpenseItems() {
		CardItem card = mCardInfo.getCard();
		Calendar tatgetDate = mCardInfo.getSettlementDate();
		mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), card.getStartBillingPeriod(tatgetDate), card.getEndBillingPeriod(tatgetDate));
	}
	
	public void updateChildView() {
		TextView tvAmount = (TextView) findViewById(R.id.BtnAmount);
        tvAmount.setText(String.format("%,d원", mCardInfo.getBillingExpenseAmount()));
        
        TextView tvSettlementDate = (TextView)findViewById(R.id.TVCardSettlementDate);
        tvSettlementDate.setText("결제일 : " + FinanceDataFormat.getDateFormat(mCardInfo.getSettlementDate().getTime()));
        
        TextView tvCardname = (TextView) findViewById(R.id.TVCardName);
        tvCardname.setText(mCardInfo.getCard().getCompenyName().getName());
        
        String startDate = FinanceDataFormat.getDateFormat(mCardInfo.getCard().getStartBillingPeriod(mCardInfo.getSettlementDate()).getTime());
		String endDate = FinanceDataFormat.getDateFormat(mCardInfo.getCard().getEndBillingPeriod(mCardInfo.getSettlementDate()).getTime());
		((TextView) findViewById(R.id.TVPeriod)).setText(startDate + " ~ " + endDate);
		
		setAlarmAdapterList();
	}
	
	
	
	@Override
	protected void setTitleBtn() {
		setTitle("카드 결제");
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "완료");
		setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
		
		
		
		super.setTitleBtn();
	}
	
	@Override
	protected void initialize() {
		mCardInfo = (CardExpenseInfo) getIntent().getSerializableExtra(MsgDef.ExtraNames.CARD_EXPENSE_INFO_ITEM);
		super.initialize();
	}
	
	public class ReportCardExpenseItemAdapter extends ArrayAdapter<FinanceItem> {
    	int mResource;
    	private LayoutInflater mInflater;

		public ReportCardExpenseItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinanceItem item = (FinanceItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
				
				ViewHolder viewHolder = new ViewHolder(
						(TextView)convertView.findViewById(R.id.TVTitle),
						(TextView)convertView.findViewById(R.id.TVListLeft), 
						(TextView)convertView.findViewById(R.id.TVListCenterTop), 
						(TextView)convertView.findViewById(R.id.TVListCenterBottom), 
						(TextView)convertView.findViewById(R.id.TVListRightTop), 
						(TextView)convertView.findViewById(R.id.TVListRightBottom));
				
				convertView.setTag(viewHolder);
			}
			setExpenseListViewText((ExpenseItem)item, convertView);
			
			return convertView;
		}
    }
	
	protected void setAlarmAdapterList() {
    	final ListView listAlarm = (ListView)findViewById(R.id.LVCardExpense);
    	
    	mCardExpenseAdapter= new ReportCardExpenseItemAdapter(this, R.layout.report_list_normal, mCardExpenseItems);
    	listAlarm.setAdapter(mCardExpenseAdapter);
    	
    	listAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
			}
		});
    	
    }
	
	protected void setExpenseListViewText(ExpenseItem expense, View convertView) {
    	ViewHolder viewHolder = (ViewHolder) convertView.getTag();
    	viewHolder.getLeftTextView().setText(expense.getCategory().getName());
		TextView tvSubCategory = viewHolder.getCenterTopTextView() ;
		tvSubCategory.setText(expense.getSubCategory().getName());
		
		TextView tvAmount = viewHolder.getRightTopTextView(); 
		tvAmount.setText(String.format("%,d원", -expense.getAmount()));
		tvAmount.setTextColor(Color.RED);
		
		TextView tvMemo = viewHolder.getCenterBottomTextView() ;
		if (expense.getMemo().length() != 0) {
			tvMemo.setText(expense.getMemo());
		}
		else {
			tvMemo.setVisibility(View.GONE);
		}
		
		TextView tvMothod = viewHolder.getRightBottomTextView(); 
		tvMothod.setText(expense.getPaymentMethod().getText());
    }
	

}