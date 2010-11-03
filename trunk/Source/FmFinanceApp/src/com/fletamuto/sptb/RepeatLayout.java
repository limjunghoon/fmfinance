package com.fletamuto.sptb;

import java.util.Calendar;

import com.fletamuto.sptb.data.Repeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class RepeatLayout extends FmBaseActivity {  	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat, true);
        
        
        setButtonListener();
        
        setTitle("반복설정");
    }
    
    protected void setButtonListener() {
    	final Button btnNormal = (Button)findViewById(R.id.BtnRepeatNormal);
    	final Button btnWeekend = (Button)findViewById(R.id.BtnRepeatWeekend);
    	final ToggleButton tbMonDay = (ToggleButton)findViewById(R.id.TBMonDay);
    	final ToggleButton tbTuesDay = (ToggleButton)findViewById(R.id.TBTuesday);
    	final ToggleButton tbWendesDay = (ToggleButton)findViewById(R.id.TBWndnesDay);
    	final ToggleButton tbThursDay = (ToggleButton)findViewById(R.id.TBThursDay);
    	final ToggleButton tbFriDay = (ToggleButton)findViewById(R.id.TBFriDay);
    	final ToggleButton tbSaturDay = (ToggleButton)findViewById(R.id.TBSaturDay);
    	final ToggleButton tbSunDay = (ToggleButton)findViewById(R.id.TBSunDay);
    	
    	btnNormal.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				boolean checked = true;
				
				if (tbMonDay.isChecked() && tbTuesDay.isChecked() && tbWendesDay.isChecked() &&
					tbThursDay.isChecked() && tbFriDay.isChecked()) {
					checked = false;
				}
						
				tbMonDay.setChecked(checked);
				tbTuesDay.setChecked(checked);
				tbWendesDay.setChecked(checked);
				tbThursDay.setChecked(checked);
				tbFriDay.setChecked(checked);
			}
		});
    	
    	btnWeekend.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				boolean checked = true;
				
				if (tbSaturDay.isChecked() && tbSunDay.isChecked()) {
					checked = false;
				}
				
				tbSaturDay.setChecked(checked);
				tbSunDay.setChecked(checked);
			}
		});
	}

	@Override
	protected void setTitleBtn() {
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
        
        setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
        	final ToggleButton tbMonDay = (ToggleButton)findViewById(R.id.TBMonDay);
        	final ToggleButton tbTuesDay = (ToggleButton)findViewById(R.id.TBTuesday);
        	final ToggleButton tbWendesDay = (ToggleButton)findViewById(R.id.TBWndnesDay);
        	final ToggleButton tbThursDay = (ToggleButton)findViewById(R.id.TBThursDay);
        	final ToggleButton tbFriDay = (ToggleButton)findViewById(R.id.TBFriDay);
        	final ToggleButton tbSaturDay = (ToggleButton)findViewById(R.id.TBSaturDay);
        	final ToggleButton tbSunDay = (ToggleButton)findViewById(R.id.TBSunDay);
        	
			public void onClick(View v) {
				RadioButton rbWeekly = (RadioButton)findViewById(R.id.RBWeekly);
				Intent intent = new Intent();
				
				if (rbWeekly.isChecked()) {
					int weekly = 0;
					if (tbMonDay.isChecked()) {
						weekly |= Repeat.MONDAY;
					}
					if (tbTuesDay.isChecked()) {
						weekly |= Repeat.TUESDAY;
					}
					if (tbWendesDay.isChecked()) {
						weekly |= Repeat.WEDNESDAY;
					}
					if (tbThursDay.isChecked()) {
						weekly |= Repeat.THURSDAY;
					}
					if (tbFriDay.isChecked()) {
						weekly |= Repeat.FRIDAY;
					}
					if (tbSaturDay.isChecked()) {
						weekly |= Repeat.SATURDAY;
					}
					if (tbSunDay.isChecked()) {
						weekly |= Repeat.SUNDAY;
					}
					intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.WEEKLY);
					intent.putExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, weekly);
					
				} else {
					EditText etDaily = (EditText)findViewById(R.id.ETMonthly);
					String daily = etDaily.getText().toString();
					intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.MONTHLY);
					intent.putExtra(MsgDef.ExtraNames.RPEAT_DAILY, Integer.parseInt(daily));
				}
				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		super.setTitleBtn();
	}
}