package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.db.DBMgr;

public class EditCategoryLayout  extends FmBaseActivity {  	
	private int mType = -1;
	private ArrayList<Category> mArrCategory;
	protected CategoryItemAdapter mAdapterCategory;
	private Button mVisibleDeleteButton;
	private boolean mHasSubCategory = false;
	private boolean mHasMainCategory = false;
	private String mMainCategoryName;
	private long mMainCategoryID = -1;
	private boolean mEditTextEnable = false;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtraInfo();
        
        if (mHasMainCategory == false) {
        	setContentView(R.layout.category_edit, true);
        }
        else {
        	setContentView(R.layout.category_edit_has_main, true);
        }
        
        initTitle();
        getCategoryItems();
        setAdapterList();
        setViewListener();
        updateView();
    }
    
    protected void setViewListener() {
    	if (mHasMainCategory == true) {
    		((Button)findViewById(R.id.BtnMainCategoryEdit)).setOnClickListener(new View.OnClickListener() {
    		final EditText mainCategoryName = (EditText)findViewById(R.id.ETMainCategoryName); 
				
				public void onClick(View v) {
					if (mMainCategoryID == -1) {
						Category newCategory = createMainCategory(mainCategoryName.getText().toString());
						if (newCategory == null) {
							return;
						}
					}
					
					updateView();
				}
			});
    	}
    }
    
    private Category createMainCategory(String categoryName) {
    	String name = categoryName;
		if (checkCategoryName(name) == false) {
			return null;
		}
		
		if (mType == -1 ) return null;
		mMainCategoryID = DBMgr.getInstance().addCategory(mType, name);
		
		if (mMainCategoryID == -1) {
			return null;
		}
		mMainCategoryName = name;
		
		if (mHasMainCategory == true) {
			makeSubCategory();
		}
		
		return new Category(mMainCategoryID, mMainCategoryName);
	}
    
    private void makeSubCategory() {
    	makeCategory();
	}

	private Category createSubCategory(String subCategoryName) {
		if (mMainCategoryID == -1 || mType == -1) {
			Log.e(LogTag.LAYOUT, ":: Don't make sub category");
			return null;
		}
		
		if (checkCategoryName(subCategoryName) == false) {
			return null;
		}
		
		long subCategoryID = DBMgr.getInstance().addSubCategory(mType, mMainCategoryID, subCategoryName);
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
    
    public void displayAlertMessage(String msg) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(EditCategoryLayout.this);
    	alert.setMessage(msg);
    	alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	alert.show();
    }
    
    
    public void updateView() {
    	updateMainCategoryView();
    	
    }
    
    private void updateMainCategoryView() {
    	if (mHasMainCategory == false) {
    		return;
    	}
    	Button editCategoryBtn = ((Button)findViewById(R.id.BtnMainCategoryEdit));
    	
		if (isNewCategoryWithSub()) {
    		setTitleBtnEnabled(FmTitleLayout.BTN_RIGTH_01, false);
    		editCategoryBtn.setText("완료");
    		mEditTextEnable = true;
    	}
    	else {
			if (mEditTextEnable) {
				editCategoryBtn.setText("수정");
    			mEditTextEnable = false;
			}
			else {
				editCategoryBtn.setText("완료");
	    		mEditTextEnable = true;
			}
			setTitleBtnEnabled(FmTitleLayout.BTN_RIGTH_01, true);
    	}
		
		((EditText)findViewById(R.id.ETMainCategoryName)).setEnabled(mEditTextEnable);
	}
    
    private void updateAdapterCategory(Category category) {
    	if (mAdapterCategory == null) {
			getCategoryItems();
	        setAdapterList();
		}
		else {
			mAdapterCategory.add(category);
			mAdapterCategory.notifyDataSetChanged();
		}
	}
    

	public boolean isNewCategoryWithSub() {
    	return (mHasMainCategory && mMainCategoryID == -1);
    }
 
    
    protected void setAdapterList() {
    	if (mArrCategory == null) return;
        
    	final ListView listCategory = (ListView)findViewById(R.id.ListViewEditCategory);
    	mAdapterCategory = new CategoryItemAdapter(this, R.layout.category_edit_item, mArrCategory);
    	listCategory.setAdapter(mAdapterCategory);
    	
    	listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
    	
    	listCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				if (mVisibleDeleteButton != null) {
					mVisibleDeleteButton.setVisibility(View.INVISIBLE);
				}
			}
		});
    	
    	listCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false) {
					if (mVisibleDeleteButton != null) {
						mVisibleDeleteButton.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
    }
    
    protected void getCategoryItems() {
    	if (mHasMainCategory == false) {
    		mArrCategory = DBMgr.getInstance().getCategory(mType);
    	}
    	else {
    		if (mMainCategoryID != -1) {
    			mArrCategory = DBMgr.getInstance().getSubCategory(mType, mMainCategoryID);
    		}
    	}
    }
    
    protected void getExtraInfo() {
    	mType = getIntent().getIntExtra("CATEGORY_TYPE", -1) ;
        mHasSubCategory = getIntent().getBooleanExtra("CATEGORY_HAS_SUB", false) ;
        mHasMainCategory = getIntent().getBooleanExtra("CATEGORY_HAS_MAIN", false) ;
        mMainCategoryName = getIntent().getStringExtra("CATEGORY_MAIN_CATEGORY_NAME");
        mMainCategoryID = getIntent().getIntExtra("CATEGORY_MAIN_CATEGOR_ID", -1) ;
    }
    
    protected void initTitle() {
    	setTitle("분류 편집");
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01,View.VISIBLE);
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, getResources().getString(R.string.btn_add));
        setTitleButtonListener();
        
        setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mHasSubCategory == true) {
					Intent intent = new Intent(EditCategoryLayout.this, EditCategoryLayout.class);
					intent.putExtra("CATEGORY_TYPE", mType);
					if (mHasSubCategory) {
						intent.putExtra("CATEGORY_HAS_MAIN", true);
					}
					
					startActivityForResult(intent, SelectCategoryBaseLayout.ACT_EDIT_CATEGORY);
				}
				else {
					makeCategory();
				}
			}
		});
    }
    
    private void makeCategory() {
    	final EditText edit = new EditText(EditCategoryLayout.this);
		new AlertDialog.Builder(EditCategoryLayout.this)
		.setTitle("분류를 입력하세요")
		.setView(edit)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				String categoryName = edit.getText().toString();
				Category category = null;
				if (mHasMainCategory == true) {
					category = createSubCategory(categoryName);
					if (category == null) {
						Log.e(LogTag.LAYOUT, ":: Fail make the subcategory");
						return;
					}
				}
				else {
					category = createMainCategory(categoryName);
					if (category == null) {
						Log.e(LogTag.LAYOUT, ":: Fail make the category");
						return;
					}
				}
				updateAdapterCategory(category);
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
	}
    
    private void setVisibleDeleteButton(Button button) {
		if (mVisibleDeleteButton != null) {
			mVisibleDeleteButton.setVisibility(View.INVISIBLE);
		}
		
		mVisibleDeleteButton = button;
		mVisibleDeleteButton.setVisibility(View.VISIBLE);
	}
    
    protected void setDeleteBtnListener(View convertView, long itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnCategoryDelete);
//		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
//		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    
    public class CategoryItemAdapter extends ArrayAdapter<Category> {
    	int mResource;
    	LayoutInflater mInflater;

		public CategoryItemAdapter(Context context, int resource,
				 List<Category> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Category item = (Category)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			TextView categoryName = (TextView)convertView.findViewById(R.id.TVCategoryItem);
			categoryName.setText(item.getName());
			
			setDeleteBtnListener(convertView, item.getId(), position);
			
			return convertView;
		}
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SelectCategoryBaseLayout.ACT_EDIT_CATEGORY) {
			if (resultCode == RESULT_OK) {
				long categoryID = data.getLongExtra("CATEGORY_ID", -1);
				String categoryName = data.getStringExtra("CATEGORY_NAME");
				
				if (categoryID != -1) {
					updateAdapterCategory(new Category(categoryID, categoryName));
				}
				
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    @Override
    public void onBackPressed() {
    	
    	super.onBackPressed();
    }
    
    @Override
    public void finish() {
    	Intent intent = new Intent();
		
		intent.putExtra("CATEGORY_ID", mMainCategoryID);
		intent.putExtra("CATEGORY_NAME", mMainCategoryName);
		
    	setResult(RESULT_OK, intent);
    	super.finish();
    }
}
