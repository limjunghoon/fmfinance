package com.fletamuto.sptb;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class InputAssetsOrLiability extends TabActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        TabHost thAssetsLiability = getTabHost();
       
        thAssetsLiability.addTab(thAssetsLiability.newTabSpec("tagAssets")
        		.setIndicator(getResources().getString(R.string.input_assets_tab_name))
        		.setContent(new Intent(this, InputAssetsLayout.class)));
        
        thAssetsLiability.addTab(thAssetsLiability.newTabSpec("tagLiability")
        		.setIndicator(getResources().getString(R.string.input_liability_tab_name))
        		.setContent(new Intent(this, InputLiabilityLayout.class)));
        
        
    }
}
