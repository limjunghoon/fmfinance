package com.fletamuto.common.control;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.MsgDef;
import com.fletamuto.sptb.R;
import com.fletamuto.sptb.data.ItemDef;

public class InputAmountDialog extends BaseSlidingActivity {
	private Long mAmount = 0L;
	final static int MAX_VALUE_DIGIT = 9;
	
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
        mAmount = getIntent().getLongExtra(MsgDef.ExtraNames.AMOUNT, 0L);
        
        displayAmount();
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
    	Button btnRemoveAll = (Button)findViewById(R.id.BtnAmountRemoveAll);
    	btnRemoveAll.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				clear();
				displayAmount();
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
    	Button btnOK = (Button)findViewById(R.id.BtnAmountOK);
    	btnOK.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("AMOUNT", mAmount);
				setResult(RESULT_OK, intent);
				finish();
				
			}
		 });
    	
    	Button btnCancel = (Button)findViewById(R.id.BtnAmountCancel);
    	btnCancel.setOnClickListener(new Button.OnClickListener() {
		
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
		mAmount.toString();
		tvAmount.setText(String.format("%,d원", mAmount));
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