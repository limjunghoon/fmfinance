package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;

/**
 * ������ �Է� �Ǵ� �����ϴ� ȭ���� �����Ѵ�.
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputIncomeLayout extends InputFinanceItemBaseLayout {
	private IncomeItem mIncomeItem;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_income, true);
        
        updateChildView();
        
        //�޷��� �̿��� ��¥ �Է��� ����
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputIncome);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
        setDateBtnClickListener(R.id.BtnIncomeDate); 
        setAmountBtnClickListener(R.id.BtnIncomeAmount);
        setRepeatBtnClickListener(R.id.BtnIncomeRepeat);
        setTitle(mIncomeItem.getCategory().getName());
    }
  
    protected void initialize() {
    	super.initialize();
    	int categoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1) ;
        String categoryName = getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME);
        
        updateCategory(categoryID, categoryName);
	}
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnIncomeDate);
    }
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		if (saveNewItem(null) == true) {
    			saveRepeat();
    		}
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    }
    
    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnIncomeAmount);
	}

	@Override
	protected void createItemInstance() {
		mIncomeItem = new IncomeItem();
		setItem(mIncomeItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mIncomeItem = (IncomeItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mIncomeItem == null) return false;
		setItem(mIncomeItem);
		return true;
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputIncomeLayout.this, SelectCategoryIncomeLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		mIncomeItem.setCategory(id, name);
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnAmountText(R.id.BtnIncomeAmount);
		updateEditMemoText(R.id.ETIncomeMemo);
		updateRepeatText(R.id.BtnIncomeRepeat);
	}

	@Override
	protected void updateItem() {
    	String memo = ((TextView)findViewById(R.id.ETIncomeMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
	
	protected void updateRepeat(int type, int value) {
		
		if (type == Repeat.MONTHLY) {
			mIncomeItem.setRepeatMonthly(value);
		}
		else if (type == Repeat.WEEKLY) {
			mIncomeItem.setRepeatWeekly(value);
		}
		else {
			
		}
		updateRepeatText(R.id.BtnIncomeRepeat);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_REPEAT) {
			if (resultCode == RESULT_OK) {
				int repeatType = data.getIntExtra(MsgDef.ExtraNames.RPEAT_TYPE, -1);
				
				if (repeatType == Repeat.MONTHLY) {
					int daily = data.getIntExtra(MsgDef.ExtraNames.RPEAT_DAILY, -1);
					if (daily == -1) return;
					
					updateRepeat(Repeat.MONTHLY, daily);
				}
				else if (repeatType == Repeat.WEEKLY) {
					int weekly = data.getIntExtra(MsgDef.ExtraNames.RPEAT_WEEKLY, -1);
					if (weekly == -1) return;
					
					updateRepeat(Repeat.WEEKLY, weekly);
				}
				else {
					return;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
    
}
