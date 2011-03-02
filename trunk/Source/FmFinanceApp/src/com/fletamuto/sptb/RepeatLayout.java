package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.fletamuto.common.control.BaseSlidingActivity;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.util.FinanceCurrentDate;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class RepeatLayout extends BaseSlidingActivity/*FmBaseActivity*/ {  	
	public static final int STYLE_NORMAL = 0;
	public static final int STYLE_ONLY_MONTHLY = 1;
	
	private int mStyle = STYLE_ONLY_MONTHLY;
	private int mType = Repeat.ONCE;
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat);
        
        initialize();
        
        //sliding 을 위해 View 설정 하고 animation 시작 함수 호출 하는 부분 start
        setSlidingView(findViewById(R.id.repeatLL));
        appearAnimation();
        //end
        
        setButtonListener();
        updateRepeatView();
    }
    
    protected void initialize() {
    	
    	mStyle = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_STYLE, STYLE_NORMAL) ;
        if (mStyle == STYLE_ONLY_MONTHLY) {
        	mType = Repeat.MONTHLY;
        	findViewById(R.id.RBWeekly).setVisibility(View.GONE);
        	findViewById(R.id.LLWeekend).setVisibility(View.GONE);
        	findViewById(R.id.LLWeekly).setVisibility(View.GONE);
        	findViewById(R.id.RBWeekly).setVisibility(View.GONE);
        }
        else {
        	mType = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.ONCE) ;
        }
    }
	
    
	private void updateRepeatView() {
//		RadioButton rbWeekly = (RadioButton)findViewById(R.id.RBWeekly);
//		RadioButton rbMonthly = (RadioButton)findViewById(R.id.RBMonthly);
		EditText etDaily = (EditText)findViewById(R.id.ETMonthly);
        int value = 0;
        
        if (mType == Repeat.WEEKLY) {
 //       	rbWeekly.setChecked(true);
        	value = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, Repeat.getCalendarWeekToRepeatWeek(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_WEEK))) ;
        	updateWeekButton(value);
        	etDaily.setText(String.valueOf(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_MONTH)));
        }
        else if (mType == Repeat.MONTHLY) {
 //       	rbMonthly.setChecked(true);
        	value = getIntent().getIntExtra(MsgDef.ExtraNames.RPEAT_DAILY, Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) ;
        	updateWeekButton(Repeat.getCalendarWeekToRepeatWeek(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_WEEK)));
        	etDaily.setText(String.valueOf(value));
        }
        else {
//        	rbWeekly.setChecked(true);
        	updateWeekButton(Repeat.getCalendarWeekToRepeatWeek(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_WEEK)));
        	etDaily.setText(String.valueOf(FinanceCurrentDate.getDate().get(Calendar.DAY_OF_MONTH)));
        }
        
        updateSelectedColor();
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
    	
    	tbMonDay.setOnClickListener(mSelectWeekRepeat);
    	tbTuesDay.setOnClickListener(mSelectWeekRepeat);
    	tbWendesDay.setOnClickListener(mSelectWeekRepeat);
    	tbThursDay.setOnClickListener(mSelectWeekRepeat);
    	tbFriDay.setOnClickListener(mSelectWeekRepeat);
    	tbSaturDay.setOnClickListener(mSelectWeekRepeat);
    	tbSunDay.setOnClickListener(mSelectWeekRepeat);

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
				
				mType = Repeat.WEEKLY;
				updateSelectedColor();
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
				
				mType = Repeat.WEEKLY;
				updateSelectedColor();
			}
		});
    	
    	findViewById(R.id.ETMonthly).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mType = Repeat.MONTHLY;
				updateSelectedColor();
			}
		});
    	
    	findViewById(R.id.LLSelectOnce).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mType = Repeat.ONCE;
				updateSelectedColor();
			}
		});
    	
    	setCompleateBtnListener();
	}
	
	View.OnClickListener mSelectWeekRepeat = new View.OnClickListener() {
		
		public void onClick(View v) {
			mType = Repeat.WEEKLY;
			updateSelectedColor();
		}
	};
	
	
	protected void setCompleateBtnListener() {
        
		findViewById(R.id.repeatTitleBtnRight).setOnClickListener(new View.OnClickListener() {
			final ToggleButton tbMonDay = (ToggleButton)findViewById(R.id.TBMonDay);
        	final ToggleButton tbTuesDay = (ToggleButton)findViewById(R.id.TBTuesday);
        	final ToggleButton tbWendesDay = (ToggleButton)findViewById(R.id.TBWndnesDay);
        	final ToggleButton tbThursDay = (ToggleButton)findViewById(R.id.TBThursDay);
        	final ToggleButton tbFriDay = (ToggleButton)findViewById(R.id.TBFriDay);
        	final ToggleButton tbSaturDay = (ToggleButton)findViewById(R.id.TBSaturDay);
        	final ToggleButton tbSunDay = (ToggleButton)findViewById(R.id.TBSunDay);
        	
			public void onClick(View v) {
				Intent intent = new Intent();
				
				if (mType == Repeat.WEEKLY) {
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
					
				} else if (mType == Repeat.MONTHLY) {
					EditText etDaily = (EditText)findViewById(R.id.ETMonthly);
					String daily = etDaily.getText().toString();
					int dayOfMonth = Integer.parseInt(daily);
					if (dayOfMonth > 31) dayOfMonth = 31;
					intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.MONTHLY);
					intent.putExtra(MsgDef.ExtraNames.RPEAT_DAILY, dayOfMonth);
				} else {
					intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, Repeat.ONCE);
				}
				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	/**
	 * 선택된 반복 색상설정
	 */
	void updateSelectedColor() {
		LinearLayout llSelectOnce =  (LinearLayout) findViewById(R.id.LLSelectOnce);
		LinearLayout llSelectWeek =  (LinearLayout) findViewById(R.id.LLSelectWeek);
		LinearLayout llSelectMonth =  (LinearLayout) findViewById(R.id.LLSelectMonth);
		
		llSelectOnce.setBackgroundColor(Color.WHITE);
		llSelectWeek.setBackgroundColor(Color.WHITE);
		llSelectMonth.setBackgroundColor(Color.WHITE);
		
		if (mType == Repeat.ONCE) {
			llSelectOnce.setBackgroundColor(Color.MAGENTA);
		}
		else if (mType == Repeat.WEEKLY) {
			llSelectWeek.setBackgroundColor(Color.MAGENTA);
		}
		else if (mType == Repeat.MONTHLY) {
			llSelectMonth.setBackgroundColor(Color.MAGENTA);
		}
	}
	
}