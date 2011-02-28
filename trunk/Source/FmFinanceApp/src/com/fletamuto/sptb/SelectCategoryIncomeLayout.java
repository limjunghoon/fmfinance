package com.fletamuto.sptb;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;

public class SelectCategoryIncomeLayout extends SelectCategoryBaseLayout /*InputAfterSelectCategoryLayout */{
	public static final int ACT_ADD_INCOME = MsgDef.ActRequest.ACT_ADD_INCOME;
	
	//���� category
	Category dCategory;
	
	final static int ACT_EDIT_INCOME_CATEGORY = 0;
	final static int ACT_ADD_INCOME_CATEGORY = 1;
	
	public SelectCategoryIncomeLayout() {
		setType(IncomeItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //���� ��ư ó��
        getGridRightBtn().setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setEditCategoryMode(true);
				//Sub Category ��� ���� ���� ����
				setSubCategoryMode(false);
				setCategoryAdaper();
			}
		});
    }
    
    protected void onClickCategoryButton(Category category) {
    	
    	int type = IncomeItem.TYPE;
    	
    	//�߰� ��ư�� ���� ���� �� ó��
    	if (category.getID() == -2) {
			Intent intent = new Intent(SelectCategoryIncomeLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("EDIT_TYPE", type);
			intent.putExtra("EDIT_TITLE", "�з� �߰�");
			intent.putExtra("EDIT_MODE", "INCOME_CATEGORY_ADD");

			startActivityForResult(intent, ACT_ADD_INCOME_CATEGORY);
			return;
		}
    	
		if (getEditCategoryMode()) {
			//�������� �Ѿ� ���� ȭ��
			Intent intent = new Intent(SelectCategoryIncomeLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("EDIT_TYPE", type);
			intent.putExtra("CATEGORY_ID", category.getID());
			intent.putExtra("CATEGORY_NAME", category.getName());
			intent.putExtra("CATEGORY_IMAGE_INDEX", category.getImageIndex());
			intent.putExtra("EDIT_TITLE", "�з� ����");
			intent.putExtra("EDIT_MODE", "INCOME_CATEGORY_EDIT");

			startActivityForResult(intent, ACT_EDIT_INCOME_CATEGORY);
			return;
		} else {
			Intent intent = new Intent();
			intent.putExtra("CATEGORY_ID", category.getID());
			intent.putExtra("CATEGORY_NAME", category.getName());
			setResult(RESULT_OK, intent);
			finish();
		}
	}
    
    //Back Ű ó��
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		if (getEditCategoryMode()) {
    			setEditCategoryMode(false);
    			setCategoryAdaper();
    		} else {
    			disAppearAnimation();
    			setResult(RESULT_CANCELED);
    		}
    	}
      
    	return true;
    }
    
    //���� �̹��� ������ �� ó��
    protected void onClickDeleteCategoryButton(Category category) {
    	dCategory = category;
    	
    	new AlertDialog.Builder(SelectCategoryIncomeLayout.this)
    	.setMessage("���� �Ͻðڽ��ϱ�?")
    	.setPositiveButton("����", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				if (DBMgr.deleteCategory(IncomeItem.TYPE, dCategory.getID()) == 0) {
					return;
				}
				mArrCategory.remove(dCategory);
			 
				setCategoryAdaper();				
			}
		})
		.setNegativeButton("���", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();   	
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ACT_ADD_INCOME_CATEGORY || requestCode == ACT_EDIT_INCOME_CATEGORY) {
    		
    		if (resultCode == RESULT_OK) {
    			updateAdapterCategory();			
    		}
    		return;
    	} 
		
		super.onActivityResult(requestCode, resultCode, data);
	}
/*
    @Override
	protected void setTitleBtn() {
		 setTitleBtnText(FmTitleLayout.BTN_LEFT_01, "����");
		 setTitleBtnVisibility(FmTitleLayout.BTN_LEFT_01, View.VISIBLE);   
		 
		super.setTitleBtn();
	}
	
    @Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(SelectCategoryIncomeLayout.this, InputExpenseLayout.class);
		startActivity(intent);
		
    	super.onClickLeft1TitleBtn();
    }
*/
   /*
	@Override
	protected void startInputActivity(Category category) {
    	Intent intent = null;
    	
		if (category.getExtndType() == ItemDef.ExtendIncome.SALARY) {
			intent = new Intent(SelectCategoryIncomeLayout.this, InputIncomeSelaryLayout.class);
		}
		else {
			intent = new Intent(SelectCategoryIncomeLayout.this, InputIncomeLayout.class);
		}
		
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_INCOME);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_INCOME) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	*/
}
