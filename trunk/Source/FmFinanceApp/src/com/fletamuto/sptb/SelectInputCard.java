package com.fletamuto.sptb;

import java.io.Serializable;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

public class SelectInputCard extends ActivityGroup {
	public static final int ACT_ADD_CARD = MsgDef.ActRequest.ACT_ADD_CARD;
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
		
		Button btnTest = (Button) findViewById(R.id.BtnInputCardSave);		
		
		switch (page) {
		case 1:
			Intent intentInputCreditCard = new Intent(this, InputCreditCardLayout.class);
			intentInputCreditCard.putExtra("showTitle", false);
			
			EventButton event = new EventButton(btnTest);
			intentInputCreditCard.putExtra("saveCard", event);
			
			Window wInputCreditCard = mLocalActivityManager.startActivity("CreditCard", intentInputCreditCard);
			View activityInputCreditCard = wInputCreditCard.getDecorView();
			frameLayout.addView(activityInputCreditCard);
			break;
		case 2:
			Intent intentInputCheckCard = new Intent(this, InputCheckCardLayout.class);
			intentInputCheckCard.putExtra("showTitle", false);
			
			EventButton event2 = new EventButton(btnTest);
			intentInputCheckCard.putExtra("saveCard", event2);
			
			Window wInputCheckCard = mLocalActivityManager.startActivity("CheckCard", intentInputCheckCard);
			View activityInputCheckCard = wInputCheckCard.getDecorView();
			frameLayout.addView(activityInputCheckCard);
			break;
		case 3:
			Intent intentInputPrepaidCard = new Intent(this, InputPrepaidCardLayout.class);
			intentInputPrepaidCard.putExtra("showTitle", false);
			
			EventButton event3 = new EventButton(btnTest);
			intentInputPrepaidCard.putExtra("saveCard", event3);
			
			Window wInputPrepaidCard = mLocalActivityManager.startActivity("PrepaidCard", intentInputPrepaidCard);
			View activityInputPrepaidCard = wInputPrepaidCard.getDecorView();
			frameLayout.addView(activityInputPrepaidCard);
			break;
		}
	}
	
	public void savedCard (Intent data) {
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.CARD_ID, data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1));
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("saveCardData");
		registerReceiver(mTestBR, filter);
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mTestBR);
		
	}
	
	BroadcastReceiver mTestBR = new BroadcastReceiver() {
		public void onReceive (Context context, Intent data) {
			savedCard(data);
		}
	};
	
	class EventButton implements Serializable {

		private static final long serialVersionUID = -4738820990944841465L;
		private Button mButton;
		
		EventButton(Button button) {
			mButton = button;
		}
		
		Button getButton() {
			return mButton;
		}		
	}

}
