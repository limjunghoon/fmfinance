package com.fletamuto.sptb;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;

public class SelectInputCard extends ActivityGroup {
	
	LocalActivityManager mLocalActivityManager;
	FrameLayout frameLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.input_card);
		
		frameLayout = (FrameLayout) findViewById(R.id.inputCardFL);
		
		ChangePage(1);
		
		((Button) findViewById(R.id.inputCard01)).setOnClickListener(mClickLintener);
		((Button) findViewById(R.id.inputCard02)).setOnClickListener(mClickLintener);
		((Button) findViewById(R.id.inputCard03)).setOnClickListener(mClickLintener);
		
        Button saveBtn = (Button) findViewById(R.id.BtnInputCardSave);

        saveBtn.setOnClickListener(new View.OnClickListener() {
        			
        			public void onClick(View v) {
        				        				
        			}
        		});

		

	}
	
	Button.OnClickListener mClickLintener = new Button.OnClickListener() {
		public void onClick (View v) {
			switch (v.getId()) {
			case R.id.inputCard01:
				ChangePage(1);
				break;
			case R.id.inputCard02:
				ChangePage(2);
				break;
			case R.id.inputCard03:
				ChangePage(3);
				break;
			}
		}
	};
	
	void ChangePage(int page) {
		
		mLocalActivityManager = this.getLocalActivityManager();
		frameLayout.removeAllViews();
		
		switch (page) {
		case 1:
			Intent intentInputCreditCard = new Intent(this, InputCreditCardLayout.class);
			intentInputCreditCard.putExtra("showTitle", false);
			Window wInputCreditCard = mLocalActivityManager.startActivity("신용카드", intentInputCreditCard);
			View activityInputCreditCard = wInputCreditCard.getDecorView();
			frameLayout.addView(activityInputCreditCard);
			break;
		case 2:
			Intent intentInputCheckCard = new Intent(this, InputCheckCardLayout.class);
			intentInputCheckCard.putExtra("showTitle", false);
			Window wInputCheckCard = mLocalActivityManager.startActivity("체크카드", intentInputCheckCard);
			View activityInputCheckCard = wInputCheckCard.getDecorView();
			frameLayout.addView(activityInputCheckCard);
			break;
		case 3:
			Intent intentInputPrepaidCard = new Intent(this, InputPrepaidCardLayout.class);
			intentInputPrepaidCard.putExtra("showTitle", false);
			Window wInputPrepaidCard = mLocalActivityManager.startActivity("선불카드", intentInputPrepaidCard);
			View activityInputPrepaidCard = wInputPrepaidCard.getDecorView();
			frameLayout.addView(activityInputPrepaidCard);
			break;
		}
		
		
		
	}

}
