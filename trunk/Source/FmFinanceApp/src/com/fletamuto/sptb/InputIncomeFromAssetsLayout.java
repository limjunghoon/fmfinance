package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.util.Revenue;

/**
 * ������ �Է� �Ǵ� �����ϴ� ȭ���� �����Ѵ�.
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
    	tvRate.setText(String.format("%d -> %d ���ͷ�(%s)", mOrigineAmount, getItem().getAmount(), Revenue.getString(mOrigineAmount, getItem().getAmount())));
    }
    
    protected void updateAmount(Long amount) {
    	super.updateAmount(amount);
    	
    	updateRate();
    }
}
