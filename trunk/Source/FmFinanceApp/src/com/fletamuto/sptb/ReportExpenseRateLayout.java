package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.PaymentAccountMethod;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;

public class ReportExpenseRateLayout extends FmBaseActivity {
	private Calendar currentCalendar = Calendar.getInstance();
	protected  Map<String, ExpenseRateAmount> mRateAmount = new HashMap<String, ExpenseRateAmount>();
	protected ArrayList<FinanceItem> mItems;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.report_expense_rate, true);
    	
    	getData();
    	updateChildView();
    	setButtonClickListener();
    }
    
	private void setButtonClickListener() {
		Button btnPreviousMonth = (Button)findViewById(R.id.BtnPreviousMonth);
		Button btnNextMonth = (Button)findViewById(R.id.BtnNextMonth);
		
		btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				movePreviousMonth();
			}
		});
		
		btnNextMonth.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				moveNextMonth();
			}
		});
	}

	private void getData() {
		mItems = DBMgr.getItems(ExpenseItem.TYPE, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH)+1);
		updateMapCategory();
	}

	@Override
	protected void setTitleBtn() {
    	setTitle("월별 지출 비율");

		super.setTitleBtn();
	}

	private void updateChildView() {
		addButtonInLayout();
		updateBarGraph();
		
		TextView tvCurrentMonth = (TextView)findViewById(R.id.TVCurrentMonth);
		tvCurrentMonth.setText(String.format("%d년 %d월",  currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH)+1));
	}

	private void updateBarGraph() {
		final PieGraph pieGraph;	
		
		ArrayList<Long> pieGraphValues = new ArrayList<Long>();
		
		Collection<ExpenseRateAmount> reateAmountItems = mRateAmount.values();
		for (ExpenseRateAmount iterator:reateAmountItems) {
			pieGraphValues.add(iterator.getTotalAmount());
		}
       
		pieGraph = (PieGraph) findViewById (R.id.pgraph);
		pieGraph.setItemValues(pieGraphValues);
		pieGraph.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int sel;
		    		sel = pieGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		    		if (sel == -1) {
		    			return true;
		    		} else {
		    			Toast.makeText(pieGraph.getContext(), "ID = " + sel + " 그래프 터치됨", Toast.LENGTH_SHORT).show();
		    			return true;
		    		}
		    	}
				return false;
			}
		});
		
	}
	
	public void moveNextMonth() {
		currentCalendar.add(Calendar.MONTH, 1);
		getData();
    	updateChildView();
	}
	
	public void movePreviousMonth() {
		currentCalendar.add(Calendar.MONTH, -1);
		getData();
    	updateChildView();
	}
	
	public void updateMapCategory() {
		mRateAmount.clear();
		
		int itemSize = mItems.size();
		for (int index = 0; index < itemSize; index++) {
			ExpenseItem item = (ExpenseItem)mItems.get(index);
			PaymentMethod paymentMethod = item.getPaymentMethod();
			loadPaymnetMethod(item);
			String expenseRateID = getPaymentMethodUniqueID(paymentMethod);
			
			ExpenseRateAmount rateAmount = mRateAmount.get(expenseRateID);
			if (rateAmount == null) {
				rateAmount = new ExpenseRateAmount();
				rateAmount.set(expenseRateID, paymentMethod.getType(), paymentMethod.getMethodItemID(), paymentMethod.getName(), item.getAmount());
				mRateAmount.put(expenseRateID, rateAmount);
			}
			else {
				rateAmount.addAmount(item.getAmount());
			}
		}
	}
	
	public String getPaymentMethodUniqueID(PaymentMethod paymentMethod){
		String id = new String();
		if (paymentMethod.getType() == PaymentMethod.CASH) {
			id = "CASH:";
		}
		else if (paymentMethod.getType() == PaymentMethod.CARD) {
			id = "CARD:";
		}
		else if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
			id = "ACCOUNT:";	
		}
		
		id += paymentMethod.getMethodItemID();
		
		return id;
	}
	
	public class ExpenseRateAmount {
		private String mID;
		private int mPaymentMethodType;
		private int mMethodID;
		private long mTotalAmount = 0L;
		private String mName;
		
		public String getID(){
			return mID;
		}
		
		public int getPaymentMethodType() {
			return mPaymentMethodType;
		}
		
		public int getMethodID() {
			return mMethodID;
		}

		public long getTotalAmount() {
			return mTotalAmount;
		}
		public String getName() {
			return mName;
		}
		
		public void addAmount(long amount) {
			mTotalAmount += amount;
		}
		
		public void set(String id, int paymentMethodType, int methodID, String name, long amount) {
			mID = id;
			mPaymentMethodType = paymentMethodType;
			mMethodID = methodID;
			mName = name;
			mTotalAmount = amount;
		}
	}
	
	
	/**
	 * 지불방법을 DB로부터 가져온다.
	 */
	private boolean loadPaymnetMethod(ExpenseItem item) {
		if (item == null) return false;
		PaymentMethod paymentMethod = item.getPaymentMethod();
		
		if (paymentMethod.getType() == PaymentMethod.CARD) {
			PaymentCardMethod cardMethod = (PaymentCardMethod) paymentMethod;
			if (cardMethod.getCard() == null) {
				cardMethod.setCard(DBMgr.getCardItem(paymentMethod.getMethodItemID()));
			}
		}
		else if (paymentMethod.getType() == PaymentMethod.ACCOUNT) {
			PaymentAccountMethod accountMethod = (PaymentAccountMethod) paymentMethod;
			if (accountMethod.getAccount() == null) {
				accountMethod.setAccount(DBMgr.getAccountItem(paymentMethod.getMethodItemID()));
			}
		}
		return true;
	}
	
	public void addButtonInLayout() {
		LinearLayout ll = (LinearLayout)findViewById(R.id.LLItemButtons);
		ll.removeAllViews();
		
		Collection<ExpenseRateAmount> reateAmountItems = mRateAmount.values();
		
		for (ExpenseRateAmount iterator:reateAmountItems) {
			Button btnItem = new Button(getApplicationContext());
			btnItem.setText(String.format("%s    : %,d원", iterator.getName(), iterator.getTotalAmount()));
			btnItem.setOnClickListener(categoryListener);
			btnItem.setTag(iterator);
			ll.addView(btnItem);
		}
		ll.invalidate();
	}
	
	 protected View.OnClickListener categoryListener = new View.OnClickListener() {
			public void onClick(View v) {
//				ExpenseRateAmount rateAmount = (ExpenseRateAmount)v.getTag();
			}
		};
}