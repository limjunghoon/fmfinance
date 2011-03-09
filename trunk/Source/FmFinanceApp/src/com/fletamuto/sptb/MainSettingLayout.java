package com.fletamuto.sptb;

import com.fletamuto.sptb.file.Output;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainSettingLayout extends FmBaseActivity {
	Output mOutput;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_setting);
        
        mOutput = new Output(this);
        
        setRootView(true);
        //Button btnRegistration = (Button)findViewById(R.id.BtnSettingRegistration);
        //btnRegistration.setOnClickListener();
        
        setButtonClickListener();
    }
	
	private void setButtonClickListener() {
		((Button)findViewById(R.id.BtnSettingRegistration)).setOnClickListener(onClickListener);
        ((Button)findViewById(R.id.BtnSettingDBBackup)).setOnClickListener(onClickListener);
        ((Button)findViewById(R.id.BtnSettingXLSExport)).setOnClickListener(onClickListener);
        ((Button)findViewById(R.id.BtnSettingSMSInfo)).setOnClickListener(onClickListener);
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BtnSettingRegistration:
				Intent intent = new Intent(MainSettingLayout.this, SettingRegstrationLayout.class);
				startActivity(intent);
				break;
			case R.id.BtnSettingDBBackup:
				mOutput.saveDB();
				break;
			case R.id.BtnSettingXLSExport:
				mOutput.savePOI();
				break;
			case R.id.BtnSettingSMSInfo:
				Intent intent2 = new Intent(MainSettingLayout.this, InputSMSActivity.class);
				startActivity(intent2);
				break;
			}
			
		}
	};
}