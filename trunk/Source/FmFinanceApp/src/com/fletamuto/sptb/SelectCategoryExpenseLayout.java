package com.fletamuto.sptb;



import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class SelectCategoryExpenseLayout extends SelectCategoryBaseLayout {

	private Category mMainCategory;

	
	//Sub Category 를 잠시 저장할 ArrayList
	ArrayList<Category> subCategory = null;
	//현재 currentMainCagegoryID
	int currentMainCagegoryID = -1;

	
	public SelectCategoryExpenseLayout() {
		setType(ExpenseItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //main category Add 버튼 추가 를 위한 
    	Category addButton = new Category(-2, "+");
        mArrCategory.add(addButton);
        
        //편집 버튼 처리
        getGridRightBtn().setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setEditCategoryMode(true);	
				setCategoryAdaper();
			}
		});
    }
    
    protected void onClickCategoryButton(Category category) {
    	
    	//sub category Add 버튼 추가 를 위한
    	Category addButton = new Category(-3, "+");
    	
    	//선택된 메인 분류 ID
    	int mainCagegoryID = -1;
    	//선택된 메인 분류 Type
    	int type = -1;
    	//Adapter에 Sub 분류가 시작 될 위치 값 
    	int subCategoryStartPos = 0;
    	
    	if (isSelectSubCategory()) {
/*
    		Intent intent = new Intent(SelectCategoryExpenseLayout.this, SelectSubCategoryLayout.class);
        	intent.putExtra("MAIN_CATEGORY_ID", category.getID());
        	intent.putExtra("MAIN_CATEGORY_NAME", category.getName());
        	intent.putExtra("ITEM_TYPE", ExpenseItem.TYPE);
        	startActivityForResult(intent, ACT_SUB_CATEGORY);
*/

    		mainCagegoryID = category.getID();
    		type = ExpenseItem.TYPE;
    		
    		if (type == -1 || mainCagegoryID == -1) {
    			Log.e(LogTag.LAYOUT, "== invaild category type");
    			return;
    		}
    		
    		//Main Category 자기 자신을 눌렀을 때 다시 사라지게 하는 부분
    		if (currentMainCagegoryID == category.getID() && getSubCategoryCount() > 0 && subCategory.contains(category) == false) {
    			mArrCategory.removeAll(subCategory);
    			setSubCategoryCount(-1);
    			setSubCategoryStartPosition(-1);
    			currentMainCagegoryID = -1;
    			subCategory = null;
    			setSubCategoryMode(false);
    			setCategoryAdaper();
    			return;
    		}    		
    		
    		//SubCategory 떠 있는 상황
    		if (getSubCategoryCount() > 0) {
    			//SubCategory 를 선택 했을 때
    			if (subCategory.contains(category)) {
    				if (category.getID() == -3) {
    					//Sub Category Add 버튼 눌렸을 때 처리
    					Toast.makeText(this, "Sub Category 추가 버튼 눌렸다", Toast.LENGTH_SHORT).show();
    					return;
    				} else {
    					if (getEditCategoryMode()) {
    						//============여기에 편집 화면으로 넘어가는 것 구현 해야 함 (Sub 항목 부분)
    						onEditButtonClick();
    						return;
    					} else {
    						Intent intent = new Intent();
    						intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
    						intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
    						intent.putExtra("SUB_CATEGORY_ID", category.getID());
    						intent.putExtra("SUB_CATEGORY_NAME", category.getName());
    						setResult(RESULT_OK, intent);
    						setSubCategoryMode(false);
        				
    						finish();
    						return;
    					}
    				}
    				
    			}
    			mArrCategory.removeAll(subCategory);    			
    		}
    		
    		//Main Category Add 버튼 눌렸을 때 처리
    		if (category.getID() == -2) {
    			Toast.makeText(this, "Main Category 추가 버튼 눌렸다", Toast.LENGTH_SHORT).show();
				return;
    		}
    		mMainCategory = category;
    		currentMainCagegoryID = mainCagegoryID;
    		
    		//DB 에서 Sub Category 가져오는 오는 부분
    		subCategory = DBMgr.getSubCategory(type, mainCagegoryID);
    		//subCategory 마지막에 add 버튼 추가
    		subCategory.add(addButton);
    		setSubCategoryCount(subCategory.size());    		
    		
    		//Sub Category 추가 후 남는 Grid 칸에 빈공간 넣기 위한 부분
    		if (getSubCategoryCount() % 4 != 0) {
    			for (int i=0; i < 4 - (getSubCategoryCount() % 4); i++) {
    				subCategory.add(null);
    			}
    			setSubCategoryCount(subCategory.size());
    		}
    		
    		//Sub Category 위치 계산 하는 부분
    		subCategoryStartPos = ((mainCagegoryID-1)/4+1) * 4;
    		if (subCategoryStartPos >= mArrCategory.size()) {
    			subCategoryStartPos = subCategoryStartPos - 4;
    		}    		
    		setSubCategoryStartPosition(subCategoryStartPos);    		
    		
    		//Adapter 의 계산된 위치에  Sub category 추가 하는 부분 
    		mArrCategory.addAll(getSubCategoryStartPosition(), subCategory);
    		setSubCategoryMode(true);
    		setCategoryAdaper();
    	}
    	else {
    		super.onClickCategoryButton(category);
    	}
	}
    
    protected void onClickDeleteCategoryButton(Category category) {
    	
    	new AlertDialog.Builder(SelectCategoryExpenseLayout.this)
    	.setMessage("삭제 하시겠습니까?")
    	.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//여기에 삭제 루틴 구현 해야함 
				testToast();				
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();   	
    }
    
    void testToast() {
    	Toast.makeText(this, "Delete 버튼이 눌렸다", Toast.LENGTH_SHORT).show();
    }
  //Back 키 처리
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		if (getEditCategoryMode()) {
    			setEditCategoryMode(false);
    			setCategoryAdaper();
    		} else if (getSubCategoryCount() > 0) {
    			mArrCategory.removeAll(subCategory);
    			setSubCategoryCount(-1);
    			setSubCategoryStartPosition(-1);
    			currentMainCagegoryID = -1;
    			subCategory = null;
    			setSubCategoryMode(false);
    			setCategoryAdaper();
    		} else {
    			disAppearAnimation();
    			setResult(RESULT_CANCELED);
    		}
    	}
      
    	return true;
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SUB_CATEGORY) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
				intent.putExtra("SUB_CATEGORY_ID", data.getIntExtra("SUB_CATEGORY_ID", -1));
				intent.putExtra("SUB_CATEGORY_NAME", data.getStringExtra("SUB_CATEGORY_NAME"));
				setResult(RESULT_OK, intent);
    			finish();
    			return;
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
