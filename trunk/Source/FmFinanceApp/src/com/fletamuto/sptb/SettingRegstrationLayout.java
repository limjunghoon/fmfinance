package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class SettingRegstrationLayout extends FmBaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_registration, true);
      
        setTitleButtonListener();
        
        Button btnRegAccount = (Button)findViewById(R.id.BtnRegAccount);
        btnRegAccount.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(SettingRegstrationLayout.this, InputAccountLayout.class);
				startActivity(intent);
			}
		});
    }
    
}
