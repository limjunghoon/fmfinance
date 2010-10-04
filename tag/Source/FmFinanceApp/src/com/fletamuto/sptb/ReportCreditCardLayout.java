package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.CardItem;

public class ReportCreditCardLayout extends ReportBaseCardLayout {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

	@Override
	public void setType() {
		mType = CardItem.CREDIT_CARD;
	}

	@Override
	public void AddCardItem() {
		Intent intent = new Intent(ReportCreditCardLayout.this, InputCreditCardLayout.class);		
		startActivityForResult(intent, ACT_ADD_CARD);
	}
}