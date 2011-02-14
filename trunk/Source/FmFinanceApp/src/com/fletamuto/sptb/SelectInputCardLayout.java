package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fletamuto.common.control.BaseSlidingActivity;
import com.fletamuto.sptb.util.LogTag;

public class SelectInputCardLayout extends BaseSlidingActivity/*Activity*/ {
	public static final int ACT_ADD_CARD = MsgDef.ActRequest.ACT_ADD_CARD;
	private final ChangeActivity changeActivity = new ChangeActivity();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
 //       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.input_select_card);
        setBtnClickListener();
 
    }
    
    private void setBtnClickListener() {
		setChangeActivityBtnClickListener(R.id.BtnInputSelectCreditCard);
		setChangeActivityBtnClickListener(R.id.BtnInputSelectCheckCard);
		setChangeActivityBtnClickListener(R.id.BtnInputSelectPrepaidCard);
	}
    
    protected void setChangeActivityBtnClickListener(int id) {
    	((Button)findViewById(id)).setOnClickListener(changeActivity);
    }
    
    public class ChangeActivity implements View.OnClickListener {
    	
    	/** 버튼 클릭 시 */
		public void onClick(View arg0) {
			Class<?> changeClass = null;
			int id = arg0.getId();
			
			if (id == R.id.BtnInputSelectCreditCard) changeClass = InputCreditCardLayout.class;
			else if (id == R.id.BtnInputSelectCheckCard) changeClass = InputCheckCardLayout.class;
			else if (id == R.id.BtnInputSelectPrepaidCard) changeClass = InputPrepaidCardLayout.class;
			else {
				Log.e(LogTag.LAYOUT, "== unregistered event hander ");
				return;
			}
			
	    	Intent intent = new Intent(SelectInputCardLayout.this, changeClass);
	    	startActivityForResult(intent, ACT_ADD_CARD);
		}
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_CARD) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.CARD_ID, data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1));
				setResult(RESULT_OK, intent);
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}