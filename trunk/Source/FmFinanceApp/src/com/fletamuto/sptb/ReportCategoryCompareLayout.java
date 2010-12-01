package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class ReportCategoryCompareLayout extends FmBaseActivity {  	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_categoyr_compare, true);
       
        setBtnClickListener();
    }

	private void setBtnClickListener() {
		findViewById(R.id.BtnCompearExpense).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ReportCategoryCompareLayout.this, SelectCategoryExpenseLayout.class);
				intent.putExtra(MsgDef.ExtraNames.SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY, false);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_CATEGORY);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_CATEGORY) {
			if (resultCode == RESULT_OK) {
				int id = data.getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
				String name = data.getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME); 
				
				Intent intent = new Intent(ReportCategoryCompareLayout.this, ReportCategoryCompareBaseLayout.class);
				intent.putExtra(MsgDef.ExtraNames.SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY, false);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_CATEGORY);
				
				//"CATEGORY_ID", mMainCategory.getID());
				//intent.putExtra("CATEGORY_NAME", mMainCategory.getName());
				//setResult(RESULT_OK, intent);
    			//finish();
    			return;
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}