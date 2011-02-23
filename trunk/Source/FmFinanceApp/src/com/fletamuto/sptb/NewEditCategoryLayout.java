package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.view.FmBaseLayout;


public class NewEditCategoryLayout  extends Activity {  	
	
	private int mType = -1;
	
	//현재 모드 상수
	final static int MAIN_CATEGORY_ADD_MODE = 0;
	final static int MAIN_CATEGORY_EDIT_MODE = 1;
	final static int SUB_CATEGORY_ADD_MODE = 2;
	final static int SUB_CATEGORY_EDIT_MODE = 3;
	
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
        } else {
        	editMode = -1;
        }
        
        
       	setContentView(R.layout.category_new_edit);
       	
       	TextView title = (TextView) findViewById (R.id.EditCategoryTitle);
       	title.setText(intent.getStringExtra("CATEGORY_EDIT_TITLE"));
       	
       	categoryName = (EditText) findViewById (R.id.EditCategoryEditor);
       	
       	if (editMode == MAIN_CATEGORY_EDIT_MODE || editMode == SUB_CATEGORY_EDIT_MODE) {
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
    	
    	int[] CATEGORY_IMAGES = {R.drawable.category_001, R.drawable.category_002, R.drawable.category_003,
    		R.drawable.category_004, R.drawable.category_005, R.drawable.category_006, R.drawable.category_007, R.drawable.category_008,
    		R.drawable.category_009, R.drawable.category_010, R.drawable.category_011, R.drawable.category_012, R.drawable.category_013,
    		R.drawable.category_014, R.drawable.category_015, R.drawable.category_016, R.drawable.category_017, R.drawable.category_018,
    		R.drawable.category_019, R.drawable.category_020, R.drawable.category_021, R.drawable.category_022, R.drawable.category_023,
    		R.drawable.category_024, R.drawable.category_025, R.drawable.category_026, R.drawable.category_027, R.drawable.category_028,
    		R.drawable.category_029, R.drawable.category_030, R.drawable.category_031, R.drawable.category_032, R.drawable.category_033,
    		R.drawable.category_034, R.drawable.category_035, R.drawable.category_036, R.drawable.category_037, R.drawable.category_038,
    		R.drawable.category_039, R.drawable.category_040, R.drawable.category_041, R.drawable.category_042, R.drawable.category_043,
    		R.drawable.category_044, R.drawable.category_045, R.drawable.category_046, R.drawable.category_047, R.drawable.category_048,
    		R.drawable.category_049, R.drawable.category_050, R.drawable.category_051, R.drawable.category_052, R.drawable.category_053,
    		R.drawable.category_054, R.drawable.category_055, R.drawable.category_056, R.drawable.category_057, R.drawable.category_058,
    		R.drawable.category_059, R.drawable.category_060, R.drawable.category_061, R.drawable.category_062, R.drawable.category_063,
    		R.drawable.category_064, R.drawable.category_065, R.drawable.category_066, R.drawable.category_067, R.drawable.category_068,
    		R.drawable.category_069, R.drawable.category_070, R.drawable.category_071, R.drawable.category_072, R.drawable.category_073,
    		R.drawable.category_074, R.drawable.category_075, R.drawable.category_076, R.drawable.category_077, R.drawable.category_078,
    		R.drawable.category_079, R.drawable.category_080, R.drawable.category_081, R.drawable.category_082, R.drawable.category_083,
    		R.drawable.category_084, R.drawable.category_085, R.drawable.category_086, R.drawable.category_087, R.drawable.category_088,
    		R.drawable.category_089, R.drawable.category_090, R.drawable.category_091, R.drawable.category_092, R.drawable.category_093,
    		R.drawable.category_094, R.drawable.category_095, R.drawable.category_096, R.drawable.category_097, R.drawable.category_098,
    		R.drawable.category_099, R.drawable.category_100, R.drawable.category_101, R.drawable.category_102, R.drawable.category_103,
    		R.drawable.category_104, R.drawable.category_105, R.drawable.category_106, R.drawable.category_107, R.drawable.category_108,
    		R.drawable.category_109, R.drawable.category_110, R.drawable.category_111, R.drawable.category_112, R.drawable.category_113,
    		R.drawable.category_114, R.drawable.category_115, R.drawable.category_116, R.drawable.category_117, R.drawable.category_118,
    		R.drawable.category_119, R.drawable.category_120, R.drawable.category_121, R.drawable.category_122, R.drawable.category_123,
    		R.drawable.category_124, R.drawable.category_125, R.drawable.category_126, R.drawable.category_127, R.drawable.category_128,
    		R.drawable.category_129, R.drawable.category_130, R.drawable.category_131, R.drawable.category_132, R.drawable.category_133,
    		R.drawable.category_134, R.drawable.category_135, R.drawable.category_136, R.drawable.category_137, R.drawable.category_138,
    		R.drawable.category_139, R.drawable.category_140, R.drawable.category_141, R.drawable.category_142, R.drawable.category_143,
    		R.drawable.category_144, R.drawable.category_145, R.drawable.category_146, R.drawable.category_147, R.drawable.category_148,
    		R.drawable.category_149, R.drawable.category_150};
    	
    	public ImageAdapter (Context c, int resource) {		
    		this.mResource = resource;
    		mContext = c;
    		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}
    	
    	public int getCount() {
    		return 150;
    	}
    	
    	public Object getItem (int position) {
    		return CATEGORY_IMAGES[position];
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
    		
    		categoryImage.setImageResource(CATEGORY_IMAGES[position]);
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
			
			if (editMode == MAIN_CATEGORY_ADD_MODE) {
				category = createMainCategory(name, imagePosition);
				if (category == null) {
					Log.e(LogTag.LAYOUT, ":: Fail make the category");
					return;
				}
	        } else if (editMode == MAIN_CATEGORY_EDIT_MODE) {
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

