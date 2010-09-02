package com.fletamuto.sptb;

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
}