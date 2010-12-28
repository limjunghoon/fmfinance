package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.util.Revenue;

/**
 * 수입을 입력 또는 수정하는 화면을 제공한다.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeFromAssetsLayout extends InputIncomeLayout {
	private long mOrigineAmount = 0L;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        findViewById(R.id.TVIncomeRate).setVisibility(View.VISIBLE);

    }
  
    protected void initialize() {
    	super.initialize();
    	
    	mOrigineAmount = getItem().getAmount();
	}

    @Override
    protected void updateChildView() {
    	updateRate();
    	
    	super.updateChildView();
    }
    
    protected void updateRate() {
    	TextView tvRate = (TextView) findViewById(R.id.TVIncomeRate);
    	tvRate.setText(String.format("%d -> %d 손익률(%s)", mOrigineAmount, getItem().getAmount(), Revenue.getString(mOrigineAmount, getItem().getAmount())));
    }
    
    protected void updateAmount(Long amount) {
    	super.updateAmount(amount);
    	
    	updateRate();
    }
}
