package com.fletamuto.sptb;

import com.fletamuto.sptb.util.LogTag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public abstract class InputBaseLayout extends FmBaseActivity {
	
	protected InputMode mInputMode = InputMode.ADD_MODE;
	
	protected final static int ACT_AMOUNT = MsgDef.ActRequest.ACT_AMOUNT;
	protected final static int ACT_CATEGORY = MsgDef.ActRequest.ACT_CATEGORY;
	
	protected enum InputMode{ADD_MODE, EDIT_MODE};
	
	/**
	 * 입력한 아이템을 저장한다.
	 */
	protected abstract void saveItem();
	
	/**
	 * 입력된 상태로 아이템을 갱신한다.
	 */
	protected abstract void updateItem();
	
	/**
	 * 입력할 아이템을 생성한다.
	 */
	protected abstract void createItemInstance();
	
	/**
	 * DB로 부터 아아템을 가져온다.
	 * @param id에 가져올 아이템 아이디 
	 * @return 실패시 null
	 */
	protected abstract boolean getItemInstance(int id);
	
	/**
	 * 자신뷰의 상태(정보)를 갱신한다.
	 */
	protected abstract void updateChildView();
	
	/**
	 * 입력된 아이템의 유효성을 확인한다.
	 * @return 성공여부
	 */
    public abstract boolean checkInputData();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        initialize();
    }
	
	/**
	 * 초기화
	 */
	protected void initialize() {
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
    
	/**
	 * 저장버튼 클릭시 리스너 설정
	 * @param btnID 저장버튼 아이디
	 */
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
    
    /**
     * 알림메시지
     * @param msg 알림문구
     */
    public void displayAlertMessage(String msg) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(InputBaseLayout.this);
    	alert.setMessage(msg);
    	alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	alert.show();
    }
}
