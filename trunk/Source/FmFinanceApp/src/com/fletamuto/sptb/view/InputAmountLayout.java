package com.fletamuto.sptb.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.R;

public class InputAmountLayout extends LinearLayout {
	final static int MAX_VALUE_DIGIT = 9;
	
	private LinearLayout mBody;
	private Long mAmount = 0L;
	
	public InputAmountLayout(Context context) {
		super(context);
		
		mBody = (LinearLayout)View.inflate(context, R.layout.input_amount, null);
		addView(mBody, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		setInputNumberListener();
		setRemoveNumberListener();
		setOkCancelListener();
		TextView tvAmount = (TextView)mBody.findViewById(R.id.TVAmount);
		tvAmount.setTextColor(Color.BLACK);
		
		displayAmount();
	}

	
//	
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.input_amount);
//        
//        setInputNumberListener();
//        setRemoveNumberListener();
//        setOkCancelListener();
//        
//        displayAmount();
//    }
    
    private void setInputNumberListener() {
    	InputAmount inputAmount = new InputAmount();
        ((Button)mBody.findViewById(R.id.BtnAmount_1)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_2)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_3)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_4)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_5)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_6)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_7)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_8)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_9)).setOnClickListener(inputAmount);
        ((Button)mBody.findViewById(R.id.BtnAmount_0)).setOnClickListener(inputAmount);
    }
    
    private void setRemoveNumberListener() {
    	Button btnRemoveAll = (Button)mBody.findViewById(R.id.BtnAmountRemoveAll);
    	btnRemoveAll.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				clear();
				displayAmount();
			}
		 });
    	
    	Button btnRemove = (Button)mBody.findViewById(R.id.BtnAmountRemove);
    	btnRemove.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				remove();
				displayAmount();
			}
		 });
    }
    
    private void setOkCancelListener() {
//    	Button btnOK = (Button)findViewById(R.id.BtnAmountOK);
//    	btnOK.setOnClickListener(new Button.OnClickListener() {
//		
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.putExtra("AMOUNT", Amount);
//				setResult(RESULT_OK, intent);
//				finish();
//				
//			}
//		 });
    	
//    	Button btnCancel = (Button)findViewById(R.id.BtnAmountCancel);
//    	btnCancel.setOnClickListener(new Button.OnClickListener() {
//		
//			public void onClick(View v) {
//				setResult(RESULT_CANCELED);
//				finish();
//			}
//		 });
    }
    
    public long getAmount() {
    	return mAmount;
    }
    
    public void clear() {
    	mAmount = 0L;
    }
    
    public void remove() {
    	if (mAmount == 0L) return;
    	mAmount /= 10;
    }
    
    public void setAmount(Long amount) {
    	mAmount = amount;
	}
	
	public void inputNumber(int number) {
    	if (checkNumber() == false) return;
    	mAmount *= 10;
    	mAmount += number;
	}
 
	public void displayAmount() {
		TextView tvAmount = (TextView)mBody.findViewById(R.id.TVAmount);
		mAmount.toString();
		tvAmount.setText(String.format("%,d¿ø", mAmount));
	}
	
	public boolean checkNumber() {
		String amount = mAmount.toString();
		return (amount.length() > MAX_VALUE_DIGIT) ? false : true;
	}

	class InputAmount implements android.view.View.OnClickListener {

		public void onClick(View arg0) {
			int inputNumber = 0;
			
			if (arg0.getId() == R.id.BtnAmount_1) inputNumber = 1;
			else if (arg0.getId() == R.id.BtnAmount_2) inputNumber = 2;
			else if (arg0.getId() == R.id.BtnAmount_3) inputNumber = 3;
			else if (arg0.getId() == R.id.BtnAmount_4) inputNumber = 4;
			else if (arg0.getId() == R.id.BtnAmount_5) inputNumber = 5;
			else if (arg0.getId() == R.id.BtnAmount_6) inputNumber = 6;
			else if (arg0.getId() == R.id.BtnAmount_7) inputNumber = 7;
			else if (arg0.getId() == R.id.BtnAmount_8) inputNumber = 8;
			else if (arg0.getId() == R.id.BtnAmount_9) inputNumber = 9;
			else if (arg0.getId() == R.id.BtnAmount_0) inputNumber = 0;
			else return;
			
			inputNumber(inputNumber);
			displayAmount();
		}
    }
    
}