package com.fletamuto.sptb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public abstract class InputBaseLayout extends FmBaseActivity {
	
	protected InputMode mInputMode = InputMode.ADD_MODE;
	
	protected final static int ACT_AMOUNT = 0;
	protected final static int ACT_CATEGORY = 1;
	
	protected enum InputMode{ADD_MODE, EDIT_MODE};
	
	protected abstract void saveItem();
	protected abstract void updateItem();
	protected abstract void createItemInstance();
	protected abstract boolean getItemInstance(int id);
	protected abstract void updateChildView();
    public abstract boolean checkInputData();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initialize();
    }
	
	private void initialize() {
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
    
    protected void setSaveBtnClickListener(int btnID) {
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
}
