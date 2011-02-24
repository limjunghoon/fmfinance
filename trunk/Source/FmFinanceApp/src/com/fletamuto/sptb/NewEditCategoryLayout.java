package com.fletamuto.sptb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.ConstantImagesArray;



public class NewEditCategoryLayout  extends Activity {  	
	
	private int mType = -1;
	
	//현재 모드 상수
	final static int MAIN_CATEGORY_ADD_MODE = 0;
	final static int MAIN_CATEGORY_EDIT_MODE = 1;
	final static int SUB_CATEGORY_ADD_MODE = 2;
	final static int SUB_CATEGORY_EDIT_MODE = 3;
	final static int INCOME_CATEGORY_ADD_MODE = 4;
	final static int INCOME_CATEGORY_EDIT_MODE = 5;
	
	//선택된 이미지 위치
	private int beforeImagePosition = -1;
	//현재 모드
	private int editMode;
	
	//Editor
	EditText categoryName;

	//이미지 아답타
	ImageAdapter Adapter;
	
	private String mMainCategoryName;
	private int mMainCategoryID = -1;
	private String mCategoryName;
	private int mCategoryID = -1;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        getExtraInfo();
        Intent intent = getIntent();
        
        mType = intent.getIntExtra("CATEGORY_EDIT_TYPE", -1);
        
        if (intent.getStringExtra("CATEGORY_EDIT_MODE").contentEquals("MAIN_ADD")) {
        	editMode = MAIN_CATEGORY_ADD_MODE;
        } else if (intent.getStringExtra("CATEGORY_EDIT_MODE").contentEquals("MAIN_EDIT")) {
        	editMode = MAIN_CATEGORY_EDIT_MODE;
        } else if (intent.getStringExtra("CATEGORY_EDIT_MODE").contentEquals("SUB_ADD")) {
        	editMode = SUB_CATEGORY_ADD_MODE;
        } else if (intent.getStringExtra("CATEGORY_EDIT_MODE").contentEquals("SUB_EDIT")) {
        	editMode = SUB_CATEGORY_EDIT_MODE;
        } else if (intent.getStringExtra("CATEGORY_EDIT_MODE").contentEquals("INCOME_ADD")) {
        	editMode = INCOME_CATEGORY_ADD_MODE;
        } else if (intent.getStringExtra("CATEGORY_EDIT_MODE").contentEquals("INCOME_EDIT")) {
        	editMode = INCOME_CATEGORY_EDIT_MODE;
        } else {
        	editMode = -1;
        }
        Log.d("jp test (New)", " editMode="+editMode);
        
       	setContentView(R.layout.category_new_edit);
       	
       	TextView title = (TextView) findViewById (R.id.EditCategoryTitle);
       	title.setText(intent.getStringExtra("CATEGORY_EDIT_TITLE"));
       	
       	categoryName = (EditText) findViewById (R.id.EditCategoryEditor);
       	
       	if (editMode == MAIN_CATEGORY_EDIT_MODE || editMode == SUB_CATEGORY_EDIT_MODE || editMode == INCOME_CATEGORY_EDIT_MODE) {
        	beforeImagePosition = intent.getIntExtra("CATEGORY_IMAGE_INDEX", -1);
        	categoryName.setText(intent.getStringExtra("CATEGORY_NAME"));   
        	mCategoryName = intent.getStringExtra("CATEGORY_NAME");
            mCategoryID = intent.getIntExtra("CATEGORY_ID", -1);
        } else {
        	beforeImagePosition = -1;
        	categoryName.setHint("분류명을 입력하세요");
        }
       	
       	if (editMode == SUB_CATEGORY_ADD_MODE || editMode == SUB_CATEGORY_EDIT_MODE) {
       		mMainCategoryName = intent.getStringExtra("MAIN_CATEGORY_NAME");
       		mMainCategoryID = intent.getIntExtra("MAIN_CATEGORY_ID", -1);
       	} else {
       		mMainCategoryName = null;
       		mMainCategoryID = -1;
       	}
       	
       	GridView grid = (GridView) findViewById (R.id.EditCategoryGrid);
       	Adapter = new ImageAdapter(this, R.layout.edit_category_grid);
       	grid.setAdapter(Adapter);
       	
       	Button saveBtn = (Button) findViewById (R.id.BtnEditCategorySave);
       	saveBtn.setOnClickListener(saveBtnClickLintener);
    }

    class ImageAdapter extends BaseAdapter {
    	
    	private Context mContext;
    	int mResource;
    	LayoutInflater mInflater;
    	
    	public ImageAdapter (Context c, int resource) {		
    		this.mResource = resource;
    		mContext = c;
    		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}
    	
    	public int getCount() {
    		if (mType == ExpenseItem.TYPE) {
    			return 150;
    		} else if (mType == IncomeItem.TYPE) {
    			return 50;
    		}
    		return 50;
    	}
    	
    	public Object getItem (int position) {
    		if (mType == ExpenseItem.TYPE) {
    			return ConstantImagesArray.CATEGORY_IMAGES[position];
    		} else if (mType == IncomeItem.TYPE) {
    			return ConstantImagesArray.INCOME_CATEGORY_IMAGES[position];
    		}
    		return null;
    	}
    	
    	public long getItemId (int position) {
    		return position;
    	}
    	
    	public View getView (int position, View convertView, ViewGroup parent) {
    		
    		if (convertView == null) {
    			convertView = mInflater.inflate(mResource, parent, false);
    		}
    		
    		FrameLayout fLayout = (FrameLayout) convertView.findViewById (R.id.EditCategoryGridFL);
    		ImageView categoryImage = (ImageView) convertView.findViewById(R.id.CategoryItemImage);
    		ImageView categoryCheck = (ImageView) convertView.findViewById(R.id.CategoryCheckImage);
    		
    		if (mType == ExpenseItem.TYPE) {
    			categoryImage.setImageResource(ConstantImagesArray.CATEGORY_IMAGES[position]);
    		} else if (mType == IncomeItem.TYPE) {
    			categoryImage.setImageResource(ConstantImagesArray.INCOME_CATEGORY_IMAGES[position]);
    		} else {
    			
    		}
    		categoryCheck.setImageResource(R.drawable.openarrow);
    		
    		if (position == (beforeImagePosition-1)) {
    			categoryCheck.setVisibility(ImageView.VISIBLE);
    		} else {
    			categoryCheck.setVisibility(ImageView.INVISIBLE);
    		}
    		
    		categoryImage.setOnClickListener(imageViewClickLintener);
    		categoryImage.setTag(position+1);
    		return convertView;
    		
    	}
    }
    
    //이미지를 선택 했을 때 처리
    public View.OnClickListener imageViewClickLintener = new View.OnClickListener() {
		
		public void onClick(View v) {
			int selectedImagePosition = (Integer)v.getTag();
			
			beforeImagePosition = selectedImagePosition;
			Adapter.notifyDataSetInvalidated();
		}
	};
	
	//완료 버튼 선택 했을 때 처리
	public View.OnClickListener saveBtnClickLintener = new View.OnClickListener() {
		
		public void onClick(View v) {
			String name = categoryName.getText().toString();
			int imagePosition = beforeImagePosition;
			Category category;
			
			if (editMode == MAIN_CATEGORY_ADD_MODE || editMode == INCOME_CATEGORY_ADD_MODE) {
				category = createMainCategory(name, imagePosition);
				if (category == null) {
					Log.e(LogTag.LAYOUT, ":: Fail make the category");
					return;
				}
	        } else if (editMode == MAIN_CATEGORY_EDIT_MODE || editMode == INCOME_CATEGORY_EDIT_MODE) {
	        	if (updateMainCategory(mCategoryID, name, imagePosition) == 0) {
					Log.e(LogTag.LAYOUT, ":: Fail to update category");
					return;
				}
	        } else if (editMode == SUB_CATEGORY_ADD_MODE) {
	        	category = createSubCategory(name, imagePosition);
	        	if (category == null) {
					Log.e(LogTag.LAYOUT, ":: Fail make the category");
					return;
				}
	        } else if (editMode == SUB_CATEGORY_EDIT_MODE) {
	        	if (updateSubCategory(mCategoryID, name, imagePosition) == 0) {
					Log.e(LogTag.LAYOUT, ":: Fail to update category");
					return;
				}
	        } else {
	        	
	        }

			setResult(RESULT_OK);
			finish();
		}
	};
	
	private int updateMainCategory(int id, String name, int imagePosition) {
		return (DBMgr.updateCategory(mType, id, name, imagePosition));
	}
    
    private int updateSubCategory(int id, String name, int imagePosition) {
    	return (DBMgr.updateSubCategory(mType, id, name, imagePosition));
	}
    
	private Category createMainCategory(String categoryName, int imgPosition) {
    	String name = categoryName;
    	int imagePosition = imgPosition;
    	
		if (checkCategoryName(categoryName) == false || mType == -1 || checkCategoryImage(imagePosition) == false) {
			return null;
		}
		
		Category category = new Category(categoryName);
		category.setImageIndex(imagePosition);
		
		mMainCategoryID = DBMgr.addCategory(mType, category);
		category.setID(mMainCategoryID);
		
		if (mMainCategoryID == -1) {
			return null;
		}
		mMainCategoryName = name;
		
		return category;
	}
    
	private Category createSubCategory(String subCategoryName, int imgPosition) {
		if (mMainCategoryID == -1 || mType == -1) {
			Log.e(LogTag.LAYOUT, ":: Don't make sub category");
			return null;
		}
		
		if (checkCategoryName(subCategoryName) == false || checkCategoryImage(imgPosition) == false) {
			return null;
		}
		
		int subCategoryID = DBMgr.addSubCategory(mType, mMainCategoryID, subCategoryName, imgPosition);
		if (subCategoryID == -1) {
			return null;
		}
		
		return new Category(subCategoryID, subCategoryName);
	}
    
    private boolean checkCategoryName(String name) {
		if (name.length() == 0) {
			displayAlertMessage("이름이 입력되지 않았습니다.");
			return false;
		}
		return true;
	}
    
    private boolean checkCategoryImage(int imageIndex) {
		if (imageIndex < 1) {
			displayAlertMessage("이미지가 선택 되지 않았습니다.");
			return false;
		}
		return true;
	}
    
    public void displayAlertMessage(String msg) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(NewEditCategoryLayout.this);
    	alert.setMessage(msg);
    	alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	alert.show();
    }
}

