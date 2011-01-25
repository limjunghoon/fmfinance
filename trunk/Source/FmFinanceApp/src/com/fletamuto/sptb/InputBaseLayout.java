package com.fletamuto.sptb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public abstract class InputBaseLayout extends FmBaseActivity {
	
	protected InputMode mInputMode = InputMode.ADD_MODE;
	
	protected final static int ACT_AMOUNT = MsgDef.ActRequest.ACT_AMOUNT;
	protected final static int ACT_CATEGORY = MsgDef.ActRequest.ACT_CATEGORY;
	
	protected enum InputMode{ADD_MODE, EDIT_MODE, STATE_CHANGE_MODE};
	
	/**
	 * �޷����� ��¥ �Է��� ����
	 */
	protected MonthlyCalendar monthlyCalendar;
	
	/**
	 * �Է��� �������� �����Ѵ�.
	 */
	protected abstract void saveItem();
	
	/**
	 * �Էµ� ���·� �������� �����Ѵ�.
	 */
	protected abstract void updateItem();
	
	
	
	/**
	 * �Է��� �������� �����Ѵ�.
	 */
	protected abstract void createItemInstance();
	
	/**
	 * DB�� ���� �ƾ����� �����´�.
	 * @param id�� ������ ������ ���̵� 
	 * @return ���н� null
	 */
	protected abstract boolean getItemInstance(int id);
	
	/**
	 * �ڽź��� ����(����)�� �����Ѵ�.
	 */
	protected abstract void updateChildView();
	
	/**
	 * ��ư�� Ŭ���Ǿ��� ��� ���� ����
	 */
	protected abstract void setBtnClickListener();
	
	/**
	 * �Էµ� �������� ��ȿ���� Ȯ���Ѵ�.
	 * @return ��������
	 */
    public abstract boolean checkInputData();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
	
	/**
	 * �ʱ�ȭ
	 */
	protected void initialize() {
		setMenuVisible(View.GONE);
		
		int id  = getIntent().getIntExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, -1);
        if (id != -1) {
        	if (getIntent().getBooleanExtra(MsgDef.ExtraNames.INPUT_CHANGE_MODE, false)) {
        		mInputMode = InputMode.STATE_CHANGE_MODE;
        	}
        	else {
        		mInputMode = InputMode.EDIT_MODE;
        	}
        	
        	if (getItemInstance(id) == false) {
        		Log.e(LogTag.LAYOUT, "== not found item");
        		createItemInstance();
        	}
        }
        else {
        	createItemInstance();
        }
        
        setBtnClickListener();
        
        super.initialize();
	}
    
	/**
	 * �����ư Ŭ���� ������ ����
	 * @param btnID �����ư ���̵�
	 */
    protected void setSaveBtnClickListener(int btnID) {
    	findViewById(btnID).setOnClickListener(new Button.OnClickListener() {
		
			public void onClick(View v) {
				updateItem();
				
				if (checkInputData() == true) {
					saveItem();		
					finish();
		    	}
			}
		 });
    }
    
    /**
     * �˸��޽���
     * @param msg �˸�����
     */
    public void displayAlertMessage(String msg) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(InputBaseLayout.this);
    	alert.setMessage(msg);
    	alert.setPositiveButton("�ݱ�", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	alert.show();
    }
    
    
    public void setDeleteBtnListener(int btnId) {
    	findViewById(btnId).setOnClickListener(new Button.OnClickListener() {
    		
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(InputBaseLayout.this);
		    	alert.setMessage("�����Ͻðڽ��ϱ�?");
		    	alert.setPositiveButton("����", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						deleteItem();
					}
				});
		    	alert.setNegativeButton("���", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
		    	alert.show();
			}
		 });
    }
    

    /**
	 * ������ ����
	 */
	protected void deleteItem() {
		finish();
	}
    

}
