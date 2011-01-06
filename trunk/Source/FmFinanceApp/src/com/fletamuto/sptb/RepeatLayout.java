package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.util.FinanceCurrentDate;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class RepeatLayout extends FmBaseActivity {  	
	public static final int STYLE_NORMAL = 0;
	public static final int STYLE_ONLY_MONTHLY = 1;
	
	private int mStyle = STYLE_ONLY_MONTHLY;
	private int mType = Repeat.WEEKLY;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat, true);
        
        
        updateRepeatView();
    }
    
    @Override
    protected void initialize() {
    	super.initialize();
    	
    	mStyle = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_STYLE, STYLE_NORMAL) ;
        if (mStyle == STYLE_ONLY_MONTHLY) {
        	mType = Repeat.MONTHLY;
        	findViewById(R.id.RBWeekly).setVisibility(View.GONE);
        	findViewById(R.id.LLWeekend).setVisibility(View.GONE);
        	findViewById(R.id.LLWeekly).setVisibility(View.GONE);
        	findViewById(R.id.RBWeekly).setVisibility(View.GONE);
        }
        else {
        	mType = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.WEEKLY) ;
        }
    }

	private void updateRepeatView() {
		RadioButton rbWeekly = (RadioButton)findViewById(R.id.RBWeekly);
		RadioButton rbMonthly = (RadioButton)findViewById(R.id.RBMonthly);
		EditText etDaily = (EditText)findViewById(R.id.ETMonthly);
        int value = 0;
        
        if (mType == Repeat.WEEKLY) {
        	rbWeekly.setChecked(true);
        	value = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, Repeat.getCalendarWeekToRepeatWeek(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_WEEK))) ;
        	updateWeekButton(value);
        	etDaily.setText(String.valueOf(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_MONTH)));
        }
        else if (mType == Repeat.MONTHLY) {
        	rbMonthly.setChecked(true);
        	value = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_DAILY, Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) ;
        	updateWeekButton(Repeat.getCalendarWeekToRepeatWeek(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_WEEK)));
        	etDaily.setText(String.valueOf(value));
        }
        else {
        	rbWeekly.setChecked(true);
        	updateWeekButton(Repeat.getCalendarWeekToRepeatWeek(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_WEEK)));
        	etDaily.setText(String.valueOf(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_MONTH)));
        }
	}

	protected void updateWeekButton(int weekly) {
		if (((weekly & Repeat.MONDAY) != 0)) {
			((ToggleButton)findViewById(R.id.TBMonDay)).setChecked(true);
		}
		if (((weekly & Repeat.TUESDAY) != 0)) {
			((ToggleButton)findViewById(R.id.TBTuesday)).setChecked(true);	
		}
		if (((weekly & Repeat.WEDNESDAY) != 0)) {
			((ToggleButton)findViewById(R.id.TBWndnesDay)).setChecked(true);
		}
		if (((weekly & Repeat.THURSDAY) != 0)) {
			((ToggleButton)findViewById(R.id.TBThursDay)).setChecked(true);
		}
		if (((weekly & Repeat.FRIDAY) != 0)) {
			((ToggleButton)findViewById(R.id.TBFriDay)).setChecked(true);
		}
		if (((weekly & Repeat.SATURDAY) != 0)) {
			((ToggleButton)findViewById(R.id.TBSaturDay)).setChecked(true);
		}
		if (((weekly & Repeat.SUNDAY) != 0)) {
			((ToggleButton)findViewById(R.id.TBSunDay)).setChecked(true);
		}
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
        setButtonListener();
        setTitle("반복설정");
        
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
					if (tbMonDay.isChecked()) 	weekly |= Repeat.MONDAY;
					if (tbTuesDay.isChecked()) 	weekly |= Repeat.TUESDAY;
					if (tbWendesDay.isChecked())weekly |= Repeat.WEDNESDAY;
					if (tbThursDay.isChecked()) weekly |= Repeat.THURSDAY;
					if (tbFriDay.isChecked()) 	weekly |= Repeat.FRIDAY;
					if (tbSaturDay.isChecked()) weekly |= Repeat.SATURDAY;
					if (tbSunDay.isChecked()) 	weekly |= Repeat.SUNDAY;
					
					intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.WEEKLY);
					intent.putExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, weekly);
					
				} else {
					EditText etDaily = (EditText)findViewById(R.id.ETMonthly);
					String daily = etDaily.getText().toString();
					int dayOfMonth = Integer.parseInt(daily);
					if (dayOfMonth > 31) dayOfMonth = 31;
					intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.MONTHLY);
					intent.putExtra(MsgDef.ExtraNames.RPEAT_DAILY, dayOfMonth);
				}
				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		super.setTitleBtn();
	}
}