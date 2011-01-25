package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsFundLayout extends InputAssetsExtendLayout {
	private AssetsFundItem mFund;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_fund, true);
    	
    	updateChildView();
    	
    	//달력을 이용한 날짜 입력을 위해
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssetsFund);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
       
    }
    
    @Override
	protected void setBtnClickListener() {
    	 setDateBtnClickListener(R.id.BtnFundDate); 
         setAmountBtnClickListener(R.id.BtnFundPrice);
         setExpiryBtnClickListener(R.id.BtnFundExpiryDate);
         setRepeatBtnClickListener(R.id.BtnFundRepeat);
	}

	@Override
	protected void updateRepeat(int type, int value) {
		if (type == Repeat.MONTHLY) {
			mFund.setRepeatMonthly(value);
		}
		else if (type == Repeat.WEEKLY) {
			mFund.setRepeatWeekly(value);
		}
		else {
			
		}
		updateRepeatText(R.id.BtnFundRepeat);
	}

	@Override
	protected void updateDate() {
		updateBtnDateText(R.id.BtnFundDate);
	}
	

	@Override
	protected void createItemInstance() {
		mFund = new AssetsFundItem();
		setItem(mFund);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mFund == null) return false;
		setItem(mFund);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateExpiryDate();
	//	updateBtnAmountText(R.id.BtnFundAmount);
		updateEditMemoText(R.id.ETFundMemo);
		updateKindSpinner();
		updateRepeatText(R.id.BtnFundRepeat);
	}

	protected void updateKindSpinner() {
		Spinner spKind = (Spinner) findViewById(R.id.SpFundKind);
		spKind.setSelection(mFund.getKind());
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETFundMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETFundTitle)).getText().toString();
    	getItem().setTitle(title);
    	
    	String store = ((TextView)findViewById(R.id.ETFundStore)).getText().toString();
    	mFund.setStore(store);
	}
	
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnFundPrice);
	}
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		if (DBMgr.addExtendAssetsFund(mFund) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mFund.getID());
    		return false;
    	}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsFundLayout.this, cls);
    		startActivity(intent);
    	}
    	else {
    		Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
    	}
    	
		return true;
	}
	
	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {

				monthlyCalendar.showMonthlyCalendarPopup();
				monthlyCalendar.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
					
					public void onDismiss() {
						if (monthlyCalendar.getSelectCalendar() == null) return;
						mFund.getExpiryDate().set(Calendar.YEAR, monthlyCalendar.getSelectCalendar().get(Calendar.YEAR));
						mFund.getExpiryDate().set(Calendar.MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.MONTH));
						mFund.getExpiryDate().set(Calendar.DAY_OF_MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.DAY_OF_MONTH));
						updateExpiryDate();
					}
				});
			}
		 });
	}
	
	protected void updateExpiryDate() {
		updateBtnExpiryDateText(R.id.BtnFundExpiryDate);
	}
	
	protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mFund.getExpriyDateString());
    }
	
	/**
     * 반복버튼 클릭 시
     */
	protected void setRepeatBtnClickListener(int resource) {
    	((Button)findViewById(resource)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Repeat repeat = mFund.getRepeat();
				int repeatType = repeat.getType();
				Intent intent = new Intent(InputAssetsFundLayout.this, RepeatLayout.class);
				
				if (repeatType != Repeat.ONCE) {
					intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, repeatType);
					
					if (repeatType == Repeat.WEEKLY) {
						intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, repeatType);
						intent.putExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, repeat.getWeeklyRepeat());
					}
					else if (repeatType == Repeat.MONTHLY) {
						intent.putExtra(MsgDef.ExtraNames.RPEAT_TYPE, repeatType);
						intent.putExtra(MsgDef.ExtraNames.RPEAT_DAILY, repeat.getDayofMonth());
					}
				}
				intent.putExtra(MsgDef.ExtraNames.RPEAT_STYLE, RepeatLayout.STYLE_ONLY_MONTHLY);
				startActivityForResult(intent, ACT_REPEAT);
			}
		});
	}
}
