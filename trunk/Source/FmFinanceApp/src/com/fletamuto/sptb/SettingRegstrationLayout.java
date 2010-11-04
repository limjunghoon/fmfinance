package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class SettingRegstrationLayout extends FmBaseActivity {
	private final ChangeActivity changeActivity = new ChangeActivity();
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_registration, true);
      
        setBtnClickListener();
    }

	private void setBtnClickListener() {
		setChangeActivityBtnClickListener(R.id.BtnRegAccount);
		setChangeActivityBtnClickListener(R.id.BtnRegCreditCard);
		setChangeActivityBtnClickListener(R.id.BtnRegCheckCard);
		setChangeActivityBtnClickListener(R.id.BtnRegPrepaidCard);
	}
	
	protected void setChangeActivityBtnClickListener(int id) {
    	((Button)findViewById(id)).setOnClickListener(changeActivity);
    }
	
	public class ChangeActivity implements View.OnClickListener {
    	
    	/** 버튼 클릭 시 */
		public void onClick(View arg0) {
			Class<?> changeClass = null;
			int id = arg0.getId();
			
			if (id == R.id.BtnRegAccount) changeClass = ReportAccountLayout.class;
			else if (id == R.id.BtnRegCreditCard) changeClass = ReportCreditCardLayout.class;
			else if (id == R.id.BtnRegCheckCard) changeClass = ReportCheckCardLayout.class;
			else if (id == R.id.BtnRegPrepaidCard) changeClass = ReportPrepaidCardLayout.class;
			else {
				Log.e(LogTag.LAYOUT, "== unregistered event hander ");
				return;
			}
			
	    	Intent intent = new Intent(SettingRegstrationLayout.this, changeClass);
			startActivity(intent);
		}
		

    }
}
