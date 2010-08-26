package com.fletamuto.sptb;

import android.os.Bundle;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAccountLayout extends FmBaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_account, true);
      
        setTitleButtonListener();
    }
    
}
