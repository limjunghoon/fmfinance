package com.fletamuto.sptb;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fletamuto.common.control.InputAmountDialog;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class InputBaseLayout extends Activity {
	
	protected FinanceItem mItem;
	protected InputMode mInputMode = InputMode.ADD_MODE;
	
	protected final static int ACT_AMOUNT = 0;
	protected final static int ACT_CATEGORY = 1;
	
	protected enum InputMode{ADD_MODE, EDIT_MODE};
	
	protected abstract void updateDate();
	protected abstract void updateCategory(int id, String name);
	protected abstract void saveItem();
	protected abstract void updateItem();
	protected abstract void createItemInstance();
	protected abstract boolean getItemInstance(int id);
	protected abstract void onCategoryClick();
	protected abstract void updateChildView();
	
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
        
        Initialize();
    }
	
	public FinanceItem getItem() {
		return mItem;
	}
	
	private void Initialize() {
		int id  = getIntent().getIntExtra("EDIT_ITEM_ID", -1);
        if (id != -1) {
        	mInputMode = InputMode.EDIT_MODE;
        	if (getItemInstance(id) == false) {
        		Log.e(LogTag.LAYOUT, "== not found item");
        		createItemInstance();
        	}
        }
        else {
        	createItemInstance();
        }
	}
    
    protected void updateBtnDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mItem.getDateString());
    }
    
    protected void updateBtnAmountText(int btnID) {
    	((Button)findViewById(btnID)).setText(String.format("%,d¿ø", mItem.getAmount()));
    }
    
    protected void updateEditMemoText(int editID) {
    	((EditText)findViewById(editID)).setText(mItem.getMemo());
    }
    
    protected void updateEditTitleText(int editID) {
    	((EditText)findViewById(editID)).setText(mItem.getTitle());
    }
    
    protected void updateBtnCategoryText(int btnID) {
    	String categoryText = getResources().getString(R.string.input_select_category);
    	if (mItem.getCategory() != null) {
    		categoryText = mItem.getCategory().getName();
    	}
    	((Button)findViewById(btnID)).setText(categoryText);
    }
    
    protected void updateAmount(Long amount) {
    	mItem.setAmount(amount);
    }
    
    protected void SetDateBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				new DatePickerDialog(InputBaseLayout.this, dateDlg, 
						mItem.getCreateDate().get(Calendar.YEAR),
						mItem.getCreateDate().get(Calendar.MONTH), 
						mItem.getCreateDate().get(Calendar.DAY_OF_MONTH)).show(); 				
			}
		 });
    }
    
    protected void SetAmountBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				Intent intent = new Intent(InputBaseLayout.this, InputAmountDialog.class);
				startActivityForResult(intent, ACT_AMOUNT);
			}
		 });
    }
    
    protected void SetSaveBtnClickListener(int btnID) {
    	Button btnIncomeDate = (Button)findViewById(btnID);
		 btnIncomeDate.setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				updateItem();
				
				if (checkInputData() == true) {
					saveItem();		
					finish();
		    	}
			}
		 });
    }
    
    protected void saveNewItem(Class<?> cls) {
    	if (DBMgr.getInstance().addFinanceItem(mItem) == false) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mItem.getId());
    		return;
    	}
    	
    	Intent intent = new Intent(InputBaseLayout.this, cls);
		startActivity(intent);
    }
    
    protected void saveUpdateItem() {
    	if (DBMgr.getInstance().updateFinanceItem(mItem) == false) {
    		Log.e(LogTag.LAYOUT, "== UPDATE fail to the save item : " + mItem.getId());
    		return;
    	}
		
		Intent intent = new Intent();
		intent.putExtra("EDIT_ITEM_ID", mItem.getId());
		setResult(RESULT_OK, intent);
		finish();
    }
    
    protected void SetCategoryClickListener(int btnID) {
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
    	if (mItem.getCategory() == null) {
    		displayAlertMessage(getResources().getString(R.string.input_warning_msg_not_category));
    		return false;
    	}
    	
    	if (mItem.getAmount() == 0L) {
    		displayAlertMessage(getResources().getString(R.string.input_warning_msg_not_amount));
    		return false;
    	}
    	
    	return true;
    }
    
    public void displayAlertMessage(String msg) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(InputBaseLayout.this);
    	alert.setMessage(msg);
    	alert.setPositiveButton("´Ý±â", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	alert.show();
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
    			updateCategory(data.getIntExtra("CATEGORY_ID", 0), data.getStringExtra("CATEGORY_NAME"));
    		}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
