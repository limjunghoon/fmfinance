package com.fletamuto.sptb;



import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class SelectCategoryExpenseLayout extends SelectCategoryBaseLayout {

	private Category mMainCategory;
	
	final static int ACT_EDIT_CATEGORY = 0;
	final static int ACT_ADD_CATEGORY = 1;

	
	//Sub Category 를 잠시 저장할 ArrayList
	ArrayList<Category> subCategory = null;
	//현재 currentMainCagegoryID
	int currentMainCagegoryID = -1;
	//지울 category ID
	Category dCategory = null;

	
	public SelectCategoryExpenseLayout() {
		setType(ExpenseItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
    			/*
    			mArrCategory.removeAll(subCategory);
    			setSubCategoryCount(-1);
    			setSubCategoryStartPosition(-1);
    			currentMainCagegoryID = -1;
    			subCategory = null;
    			setSubCategoryMode(false);
    			setCategoryAdaper();
    			return;
    			*/
    			Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
				intent.putExtra("SUB_CATEGORY_ID", /*category.getID()*/-1);
				intent.putExtra("SUB_CATEGORY_NAME", /*category.getName()*/"");
				setResult(RESULT_OK, intent);
				setSubCategoryMode(false);
			
				finish();
				return;
    		}    		
    		
    		//SubCategory 떠 있는 상황
    		if (getSubCategoryCount() > 0) {
    			//SubCategory 를 선택 했을 때
    			if (subCategory.contains(category)) {
    				if (category.getID() == -3) {
    					//Sub Category Add 버튼 눌렸을 때 처리
    	    			Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    	    			intent.putExtra("EDIT_TYPE", type);
    	    			intent.putExtra("MAIN_CATEGORY_ID", mMainCategory.getID());
						intent.putExtra("MAIN_CATEGORY_NAME", mMainCategory.getName());
    					intent.putExtra("EDIT_TITLE", "분류 추가");
    					intent.putExtra("EDIT_MODE", "SUB_CATEGORY_ADD");

    					startActivityForResult(intent, ACT_ADD_CATEGORY);
    					return;
    				} else {
    					if (getEditCategoryMode()) {
    						//============여기에 편집 화면으로 넘어가는 것 구현 해야 함 (Sub 항목 부분)
    						Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    						intent.putExtra("EDIT_TYPE", type);
    						intent.putExtra("CATEGORY_ID", category.getID());
    						intent.putExtra("CATEGORY_NAME", category.getName());
    						intent.putExtra("MAIN_CATEGORY_ID", mMainCategory.getID());
    						intent.putExtra("MAIN_CATEGORY_NAME", mMainCategory.getName());
    						intent.putExtra("CATEGORY_IMAGE_INDEX", category.getImageIndex());
    						intent.putExtra("EDIT_TITLE", "분류 편집");
    						intent.putExtra("EDIT_MODE", "SUB_CATEGORY_EDIT");

    						startActivityForResult(intent, ACT_EDIT_CATEGORY);
    						
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
    			Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    			intent.putExtra("EDIT_TYPE", type);
				intent.putExtra("EDIT_TITLE", "분류 추가");
				intent.putExtra("EDIT_MODE", "MAIN_CATEGORY_ADD");

				startActivityForResult(intent, ACT_ADD_CATEGORY);
				return;
    		}
    		
    		//Main Category 편집 화면으로 넘어 가는 부분
    		if (getEditCategoryMode() && getSubCategoryMode() == false) {
    			//============여기에 편집 화면으로 넘어가는 것 구현 해야 함 (Main 항목 부분)
    			Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    			intent.putExtra("EDIT_TYPE", type);
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
				intent.putExtra("CATEGORY_IMAGE_INDEX", category.getImageIndex());
				intent.putExtra("EDIT_TITLE", "분류 편집");
				intent.putExtra("EDIT_MODE", "MAIN_CATEGORY_EDIT");

				startActivityForResult(intent, ACT_EDIT_CATEGORY);
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
    		subCategoryStartPos = ((/*mainCagegoryID*/mArrCategory.indexOf(mMainCategory))/4+1) * 4;
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
    	
    	dCategory = category;
    	    	
    	new AlertDialog.Builder(SelectCategoryExpenseLayout.this)
    	.setMessage("삭제 하시겠습니까?")
    	.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				if (getSubCategoryMode()) {
					if (DBMgr.deleteSubCategory(ExpenseItem.TYPE, dCategory.getID()) == 0) {
						return;
					}
					
					mArrCategory.removeAll(subCategory);
					
					//Adapter에 Sub 분류가 시작 될 위치 값 
			    	int subCategoryStartPos = 0;
					//sub category Add 버튼 추가 를 위한
			    	Category addButton = new Category(-3, "+");
					//DB 에서 Sub Category 가져오는 오는 부분
		    		subCategory = DBMgr.getSubCategory(ExpenseItem.TYPE, currentMainCagegoryID);
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
		    		subCategoryStartPos = ((/*currentMainCagegoryID*/mArrCategory.indexOf(mMainCategory))/4+1) * 4;
		    		if (subCategoryStartPos >= mArrCategory.size()) {
		    			subCategoryStartPos = subCategoryStartPos - 4;
		    		}    		
		    		setSubCategoryStartPosition(subCategoryStartPos);    		
		    		
		    		//Adapter 의 계산된 위치에  Sub category 추가 하는 부분 
		    		mArrCategory.addAll(getSubCategoryStartPosition(), subCategory);
		    		setSubCategoryMode(true);					
					
				}
				else {
					if (DBMgr.deleteCategory(ExpenseItem.TYPE, dCategory.getID()) == 0) {
						return;
					}
					mArrCategory.remove(dCategory);
				}				 
				
				setCategoryAdaper();				
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();   	
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
    	} else if (requestCode == ACT_EDIT_CATEGORY || requestCode == ACT_ADD_CATEGORY) {
    		if (resultCode == RESULT_OK) {

    			if (getSubCategoryMode()) {

    				mArrCategory.removeAll(subCategory);
					
					//Adapter에 Sub 분류가 시작 될 위치 값 
			    	int subCategoryStartPos = 0;
					//sub category Add 버튼 추가 를 위한
			    	Category addButton = new Category(-3, "+");
					//DB 에서 Sub Category 가져오는 오는 부분
		    		subCategory = DBMgr.getSubCategory(ExpenseItem.TYPE, currentMainCagegoryID);
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
		    		subCategoryStartPos = ((/*currentMainCagegoryID*/mArrCategory.indexOf(mMainCategory))/4+1) * 4;
		    		if (subCategoryStartPos >= mArrCategory.size()) {
		    			subCategoryStartPos = subCategoryStartPos - 4;
		    		}    		
		    		setSubCategoryStartPosition(subCategoryStartPos);    		
		    		
		    		//Adapter 의 계산된 위치에  Sub category 추가 하는 부분 
		    		mArrCategory.addAll(getSubCategoryStartPosition(), subCategory);
		    		setSubCategoryMode(true);
		    		
		    		setCategoryAdaper();
    			} else {

    				updateAdapterCategory();
    			}   			
    		}
    		return;
    	} 
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
