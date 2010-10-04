package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainSettingLayout extends FmBaseActivity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_setting, true);
        
        setTitleButtonListener();
       
        Button btnRegistration = (Button)findViewById(R.id.BtnSettingRegistration);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainSettingLayout.this, SettingRegstrationLayout.class);
				startActivity(intent);
			}
		});
    }
}