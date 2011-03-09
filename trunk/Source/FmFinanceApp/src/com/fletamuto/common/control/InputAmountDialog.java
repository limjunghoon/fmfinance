package com.fletamuto.common.control;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.MsgDef;
import com.fletamuto.sptb.R;

public class InputAmountDialog extends BaseSlidingActivity {
	public final static int MAX_VALUE_DIGIT = 9;
	public final static int NUMBER_SEPARATION = 4;
	
	private Long mAmount = 0L;
	private boolean mFristInput = true;
	 
	
	String mAmountChar[]  = {"영", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"}; 
	String mAmountDigit[]  = {"십", "백", "천"};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.input_amount);
        
      //sliding 을 위해 View 설정 하고 animation 시작 함수 호출 하는 부분 start
        setSlidingView(findViewById(R.id.LLAmount));
        appearAnimation();
        //end
        
        setInputNumberListener();
        setRemoveNumberListener();
        setOkCancelListener();
        
        ((TextView)findViewById(R.id.TVAmount)).setTextColor(Color.BLACK);
        ((TextView)findViewById(R.id.TVAmountChar)).setTextColor(Color.BLACK);
        mAmount = getIntent().getLongExtra(MsgDef.ExtraNames.AMOUNT, 0L);
        setTitleName();
        
        displayAmount();
    }
    
    public void setTitleName() {
    	String title = getIntent().getStringExtra(MsgDef.ExtraNames.AMOUNT_TITLE);
        TextView tvTitle = (TextView) findViewById(R.id.TVAmountTitle);
        if (title == null) {
        	tvTitle.setText("금액 입력");
        }
        else {
        	tvTitle.setText(title);
        }
    }
    
    private void setInputNumberListener() {
    	InputAmount inputAmount = new InputAmount();
        ((Button)findViewById(R.id.BtnAmount_1)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_2)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_3)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_4)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_5)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_6)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_7)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_8)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_9)).setOnClickListener(inputAmount);
        ((Button)findViewById(R.id.BtnAmount_0)).setOnClickListener(inputAmount);

    }
    
    private void setRemoveNumberListener() {
    	Button btnRemoveAll = (Button)findViewById(R.id.BtnAmount_00);
    	btnRemoveAll.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				
				if  (mAmount.toString().length() < MAX_VALUE_DIGIT-1) {
					mAmount *= 100;
					displayAmount();
				}
			}
		 });
    	
    	Button btnRemove = (Button)findViewById(R.id.BtnAmountRemove);
    	btnRemove.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				remove();
				displayAmount();
			}
		 });
    }
    
    private void setOkCancelListener() {
    	findViewById(R.id.BtnAmountOK).setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("AMOUNT", mAmount);
				setResult(RESULT_OK, intent);
				finish();
				
			}
		 });
    	
    	findViewById(R.id.BtnAmountCancel).setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		 });
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

	public long getAmount() {
		return mAmount;
	}
	
	public void inputNumber(int number) {
    	if (checkNumber() == false) return;
    	mAmount *= 10;
    	mAmount += number;
	}
 
	public void displayAmount() {
		TextView tvAmount = (TextView)findViewById(R.id.TVAmount);
		tvAmount.setText(String.format("%,d원", mAmount));
		
		displayAmountChar();
	}
	
	public void displayAmountChar() {
		String amountChar  = "";
		
		int Separation1 = (int)(mAmount%10000);
		int Separation2 = (int) ((mAmount/10000)%10000);
		int Separation3 = (int) (mAmount/100000000);
		
		if (Separation3 == 0 && Separation2 == 0 && Separation1 == 0) {
			amountChar = "영원";
		}
		else {
			if (Separation3 > 0 ) {
				amountChar = displayAmountSeparationChar(Separation3) + "억";
			}
			
			if (Separation2 > 0 ) {
				amountChar += displayAmountSeparationChar(Separation2) + "만";
			}
			
			amountChar += displayAmountSeparationChar(Separation1) + "원";
		}
		
		((TextView)findViewById(R.id.TVAmountChar)).setText(amountChar);
	}


	private String displayAmountSeparationChar(int separation1) {
		String digitText = "";
		int digitNumber[] = {0, 0, 0, 0};
		for (int index = 0; index < NUMBER_SEPARATION; index++) {
			digitNumber[index] = separation1%10;
			separation1 /= 10;
		}
		
		for (int index = 0; index < NUMBER_SEPARATION; index++) {
			int digit = NUMBER_SEPARATION - index - 1;
			
			if (digitNumber[digit] == 0) continue;
			
			if (digitNumber[digit] != 1 || digit == 0) {
				digitText += mAmountChar[digitNumber[digit]];
			}
			
			if (digit >= 1) {
				digitText += mAmountDigit[digit-1];
			}
		}
		
		return digitText;
	}


	public boolean checkNumber() {
		String amount = mAmount.toString();
		return (amount.length() > MAX_VALUE_DIGIT) ? false : true;
	}

	class InputAmount implements android.view.View.OnClickListener {

		public void onClick(View arg0) {
			if (mFristInput == true) {
				mFristInput = false;
				clear();
			}
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