package com.fletamuto.sptb;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 수입, 지출, 자산, 부채  입력 또는 수정 기본 레이아웃 클래스
 * @author yongbban
 * @version 1.0.0.0
 */
public abstract class InputFinanceItemBaseLayout extends InputBaseLayout {
	
	protected final static int ACT_REPEAT = MsgDef.ActRequest.ACT_REPEAT;
	
	private FinanceItem mItem;
	
	protected abstract void updateDate();
	protected abstract void updateCategory(int id, String name);
	protected abstract void onCategoryClick();
	
	protected void setItem(FinanceItem item) {
		mItem = item;
	}
	
	DatePickerDialog.OnDateSetListener dateDlg = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mItem.getCreateDate().set(Calendar.YEAR, year);
			mItem.getCreateDate().set(Calendar.MONTH, monthOfYear);
			mItem.getCreateDate().set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDate();
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    }
	
	@Override
	protected void setTitleBtn() {
		setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, "완료");
        
        setSaveBtnClickListener(R.id.BtnTitleRigth01);
		
		super.setTitleBtn();
	}
	
		
	public FinanceItem getItem() {
		return mItem;
	}
    
    protected void updateBtnDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mItem.getCreateDateString());
    }
    
    protected void updateBtnAmountText(int btnID) {
    	((Button)findViewById(btnID)).setText(String.format("%,d원", mItem.getAmount()));
    }
    
    protected void updateEditMemoText(int editID) {
    	((EditText)findViewById(editID)).setText(mItem.getMemo());
    }
    
    protected void updateEditTitleText(int editID) {
    	((EditText)findViewById(editID)).setText(mItem.getTitle());
    }
    
    protected void updateBtnCategoryText(int btnID) {
    	String categoryText = getResources().getString(R.string.input_select_category);
    	if (mItem.isVaildCatetory()) {
    		categoryText = mItem.getCategory().getName();
    	}
    	((Button)findViewById(btnID)).setText(categoryText);
    }
    
    protected void updateAmount(Long amount) {
    	mItem.setAmount(amount);
    }
    
    /**
     * 날짜 버튼 클릭시 리슨너 설정
     * @param btnID 날짜버튼 아이디
     */

    protected void setDateBtnClickListener(int btnID) {
    	((Button)findViewById(btnID)).setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				/*
				new DatePickerDialog(InputFinanceItemBaseLayout.this, dateDlg, 
						mItem.getCreateDate().get(Calendar.YEAR),
						mItem.getCreateDate().get(Calendar.MONTH), 
						mItem.getCreateDate().get(Calendar.DAY_OF_MONTH)).show(); 		
				*/
				monthlyCalendar.showMonthlyCalendarPopup();
				monthlyCalendar.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
					
					public void onDismiss() {
						if (monthlyCalendar.getSelectCalendar() == null) return;
						mItem.getCreateDate().set(Calendar.YEAR, monthlyCalendar.getSelectCalendar().get(Calendar.YEAR));
						mItem.getCreateDate().set(Calendar.MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.MONTH));
						mItem.getCreateDate().set(Calendar.DAY_OF_MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.DAY_OF_MONTH));
						updateDate();
						
					}
				});
			}
		 });
    }

    
    /**
     * 금액버튼 클릭시 리슨너 설정
     * @param btnID 금액버튼 아이디
     */
    protected void setAmountBtnClickListener(int btnID) {
    	Button btnAmount = (Button)findViewById(btnID);
    	btnAmount.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputFinanceItemBaseLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, ACT_AMOUNT);
			}
		 });
    }
    
    /**
     * 아이템을 DB에 저장
     * @param cls The component class that is to be used for the intent.
     */
    protected boolean saveNewItem(Class<?> cls) {

    	if (DBMgr.addFinanceItem(mItem) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mItem.getID());
    		return false;
    	}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputFinanceItemBaseLayout.this, cls);
    		intent.putExtra(MsgDef.ExtraNames.ADD_ITEM_ID, mItem.getID());
    		startActivity(intent);
    	}
    	else {
    		Intent intent = new Intent();
    		intent.putExtra(MsgDef.ExtraNames.ADD_ITEM_ID, mItem.getID());
			setResult(RESULT_OK, intent);
			finish();
    	}
    	
		return true;
    }
    
    protected void saveUpdateItem() {
    	if (DBMgr.updateFinanceItem(mItem) == 0) {
    		Log.e(LogTag.LAYOUT, "== UPDATE fail to the save item : " + mItem.getID());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mItem.getID());
		setResult(RESULT_OK, intent);
		finish();
    }
    
    /**
     * 분류버튼 클릭시 리슨너 설정
     * @param btnID 분류버튼 아이디
     */
    protected void setCategoryClickListener(int btnID) {
    	((Button)findViewById(btnID)).setOnClickListener(new Button.OnClickListener() {
    		
    		public void onClick(View v) {
    			onCategoryClick();
    		}
        });    
    }    
    
    public Calendar getCreateDate() {
    	return mItem.getCreateDate();
    }
    
    public boolean checkInputData() {
    	if (mItem.getCategory().getID() == -1) {
    		displayAlertMessage(getResources().getString(R.string.input_warning_msg_not_category));
    		return false;
    	}
    	
    	if (mItem.getAmount() == 0L) {
    		displayAlertMessage(getResources().getString(R.string.input_warning_msg_not_amount));
    		return false;
    	}
    	
    	return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    	if (requestCode == ACT_AMOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateAmount(data.getLongExtra("AMOUNT", 0L));
    		}
    	}
    	else if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			updateCategory(data.getIntExtra("CATEGORY_ID", -1), data.getStringExtra("CATEGORY_NAME"));
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    protected void loadRepeat() {
    	if (mItem == null) return;
    	
    	Repeat repeat = mItem.getRepeat();
    	if (repeat.getID() == -1 || repeat.getItemID() != -1) return;
    
    	mItem.setRepeat(DBMgr.getRepeat(mItem.getRepeat().getID()));
    }
    
    protected void updateRepeatText(int resource) {
		Repeat repeat = mItem.getRepeat();
		if (repeat == null) return;
		
		int type = repeat.getType();
		Button btnRepeat = (Button)findViewById(resource);
		
		if (type == Repeat.MONTHLY) {
			btnRepeat.setText(repeat.getRepeatMessage());
		}
		else if (type == Repeat.WEEKLY) {
			btnRepeat.setText(repeat.getRepeatMessage());
		}
		else {
			btnRepeat.setText("반복을 설정합니다.");
		}
	}
    
    protected void saveRepeat() {
    	Repeat repeat = mItem.getRepeat();
		if (repeat.getType() == Repeat.ONCE) return;
		
		repeat.setItemID(mItem.getID());
		repeat.setItemType(mItem.getType());
		
		if (DBMgr.addRepeat(repeat) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to insert Repeat");
		}
		
		DBMgr.updateRepeat(mItem.getType(), mItem.getID(), repeat.getID());
    }
    
    /**
     * 반복버튼 클릭 시
     */
	protected void setRepeatBtnClickListener(int resource) {
    	((Button)findViewById(resource)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Repeat repeat = mItem.getRepeat();
				int repeatType = repeat.getType();
				Intent intent = new Intent(InputFinanceItemBaseLayout.this, RepeatLayout.class);
				
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
				
				startActivityForResult(intent, ACT_REPEAT);
			}
		});
	}
}
