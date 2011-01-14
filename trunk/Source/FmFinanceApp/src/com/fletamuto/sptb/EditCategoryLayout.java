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
import com.fletamuto.sptb.util.LogTag;


public class EditCategoryLayout  extends FmBaseActivity {  	
	
	private int mType = -1;
	private ArrayList<Category> mArrCategory;
	protected CategoryItemAdapter mAdapterCategory;
	private Button mVisibleDeleteButton;
	private boolean mHasSubCategory = false;
	private boolean mHasWithMainCategory = false;
	private String mMainCategoryName;
	private int mMainCategoryID = -1;
	private boolean mEditTextEnable = false;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtraInfo();
        
        if (mHasWithMainCategory == false) {
        	setContentView(R.layout.category_edit, true);
        }
        else {
        	setContentView(R.layout.category_edit_has_main, true);
        }
        
        setMainText();
        getCategoryItems();
        setAdapterList();
        setViewListener();
        updateView();
    }
    
    @Override
    protected void setTitleBtn() {
    	setTitle("분류 편집");
        setTitleBtnVisibility(FmMainMenuLayout.BTN_RIGTH_01,View.VISIBLE);
        setTitleBtnText(FmMainMenuLayout.BTN_RIGTH_01, getResources().getString(R.string.btn_add));
        
        setTitleButtonListener(FmMainMenuLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mHasSubCategory == true) {
					Intent intent = new Intent(EditCategoryLayout.this, EditCategoryLayout.class);
					intent.putExtra("CATEGORY_TYPE", mType);
					if (mHasSubCategory) {
						intent.putExtra("CATEGORY_HAS_WITH_MAIN", true);
					}
					
					startActivityForResult(intent, SelectCategoryBaseLayout.ACT_EDIT_CATEGORY);
				}
				else {
					makeCategory();
				}
			}
		});
        
    	super.setTitleBtn();
    }
    
    protected void setMainText() {
    	if (mHasWithMainCategory == false) return;
    	if (mMainCategoryID != -1 && mMainCategoryName != "") {
        	EditText mainCategoryName = (EditText)findViewById(R.id.ETMainCategoryName);
        	mainCategoryName.setText(mMainCategoryName);
        	
        	if (mHasSubCategory == false) {
        		mEditTextEnable = true;
        	}
        	
        }
    }
    
    protected void setViewListener() {
    	if (mHasWithMainCategory == true) {
    		
    		((Button)findViewById(R.id.BtnMainCategoryEdit)).setOnClickListener(new View.OnClickListener() {
    		final EditText mainCategoryName = (EditText)findViewById(R.id.ETMainCategoryName); 
				
				public void onClick(View v) {
					if (mMainCategoryID == -1) {
						if (createMainCategory(mainCategoryName.getText().toString()) == null) {
							Log.e(LogTag.LAYOUT, ":: Fail to create category");
							return;
						}
					}
					else {
						if (updateMainCategory(mMainCategoryID, mainCategoryName.getText().toString()) == 0) {
							Log.e(LogTag.LAYOUT, ":: Fail to update category");
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
		if (checkCategoryName(categoryName) == false || mType == -1) {
			return null;
		}
		
		Category category = new Category(categoryName);
		
		mMainCategoryID = DBMgr.addCategory(mType, category);
		category.setID(mMainCategoryID);
		
		if (mMainCategoryID == -1) {
			return null;
		}
		mMainCategoryName = name;
		
		if (mHasWithMainCategory == true) {
			makeSubCategory();
		}
		
		return category;
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
		
		int subCategoryID = DBMgr.addSubCategory(mType, mMainCategoryID, subCategoryName);
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
    	if (mHasWithMainCategory == false) {
    		return;
    	}
    	Button editCategoryBtn = ((Button)findViewById(R.id.BtnMainCategoryEdit));
    	
		if (isNewCategoryWithSub()) {
    		setTitleBtnEnabled(FmMainMenuLayout.BTN_RIGTH_01, false);
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
			setTitleBtnEnabled(FmMainMenuLayout.BTN_RIGTH_01, true);
    	}
		
		((EditText)findViewById(R.id.ETMainCategoryName)).setEnabled(mEditTextEnable);
	}
    
    private void updateAdapterCategory(Category category) {
//    	if (mAdapterCategory == null) {
//			getCategoryItems();
//	        setAdapterList();
//		}
//		else {
//			mAdapterCategory.add(category);
//			mAdapterCategory.notifyDataSetChanged();
//		}
    	
    	if (mAdapterCategory != null) {
    		mAdapterCategory.clear();
    	}
    	
    	getCategoryItems();
    	setAdapterList();
	}
    

	public boolean isNewCategoryWithSub() {
    	return (mHasWithMainCategory && mMainCategoryID == -1);
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
    	if (mHasWithMainCategory == false) {
    		mArrCategory = DBMgr.getCategory(mType);
    	}
    	else {
    		if (mMainCategoryID != -1) {
    			mArrCategory = DBMgr.getSubCategory(mType, mMainCategoryID);
    		}
    	}
    }
    
    protected void getExtraInfo() {
    	mType = getIntent().getIntExtra("CATEGORY_TYPE", -1) ;
        mHasSubCategory = getIntent().getBooleanExtra("CATEGORY_HAS_SUB", false) ;
        mHasWithMainCategory = getIntent().getBooleanExtra("CATEGORY_HAS_WITH_MAIN", false) ;
        mMainCategoryName = getIntent().getStringExtra("CATEGORY_MAIN_CATEGORY_NAME");
        mMainCategoryID = getIntent().getIntExtra("CATEGORY_MAIN_CATEGORY_ID", -1) ;
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
				if (mHasWithMainCategory == true) {
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
    
    protected void setDeleteBtnListener(View convertView, int itemID, int position) {
    	final Button btnDelete = (Button)convertView.findViewById(R.id.BtnCategoryDelete);
    	final int deletePosition = position;
    	final int dleteItemID = itemID;
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (mHasWithMainCategory) {
					if (DBMgr.deleteSubCategory(mType, dleteItemID) == 0) {
						return;
					}
				}
				else {
					if (DBMgr.deleteCategory(mType, dleteItemID) == 0) {
						return;
					}
				}
				
				mArrCategory.remove(deletePosition);
				mAdapterCategory.notifyDataSetChanged();
			}
		});
    }
    
    protected void setEditBtnListener(View convertView, Category category, int position)  {
    	final Button btnEdit = (Button)convertView.findViewById(R.id.BtnCategoryEdit);
    	final int editPosition = position;
    	final Category editCategory = category;
    	
    	btnEdit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				updateCategory(editCategory, editPosition);
			}
		});
    }
    
    public void updateCategory(Category category, int position) {
    	if (mHasSubCategory && mHasWithMainCategory == false) {
    		updateCategoryWithSubCategory(category, position);
    	}
    	else {
    		updateSingleCategory(category, position);
    	}
    	
    }
    
    private void updateCategoryWithSubCategory(Category category, int position) {
    	Intent intent = new Intent(EditCategoryLayout.this, EditCategoryLayout.class);
		intent.putExtra("CATEGORY_TYPE", mType);
		intent.putExtra("CATEGORY_HAS_SUB", true);
		intent.putExtra("CATEGORY_HAS_WITH_MAIN", true);
		intent.putExtra("CATEGORY_MAIN_CATEGORY_NAME", category.getName());
		intent.putExtra("CATEGORY_MAIN_CATEGORY_ID", category.getID());
		
		startActivityForResult(intent, SelectCategoryBaseLayout.ACT_EDIT_CATEGORY);
	}

	private void updateSingleCategory(Category category, int position) {
    	final EditText edit = new EditText(EditCategoryLayout.this);
    	final int editPosition = position;
    	final int editCategoryID = category.getID();
    	
    	edit.setText(category.getName());
		new AlertDialog.Builder(EditCategoryLayout.this)
		.setTitle("분류를 입력하세요")
		.setView(edit)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				String categoryName = edit.getText().toString();
				
				if (categoryName.length() == 0) {
					return;
				}
				
				if (mHasWithMainCategory == true) {
					if (updateSubCategory(editCategoryID, categoryName) == 0) {
						Log.e(LogTag.LAYOUT, ":: Fail update the subcategory");
						return;
					}
				}
				else {
					if (updateMainCategory(editCategoryID, categoryName) == 0) {
						Log.e(LogTag.LAYOUT, ":: Fail update the category");
						return;
					}
				}
				
				mArrCategory.set(editPosition, new Category(editCategoryID, categoryName));
				mAdapterCategory.notifyDataSetChanged();
			}

			
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
	}

	private int updateMainCategory(int id, String name) {
		return (DBMgr.updateCategory(mType, id, name));
	}
    
    private int updateSubCategory(int id, String name) {
    	return (DBMgr.updateSubCategory(mType, id, name));
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
			
			setDeleteBtnListener(convertView, item.getID(), position);
			setEditBtnListener(convertView, item, position);
			
			return convertView;
		}
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SelectCategoryBaseLayout.ACT_EDIT_CATEGORY) {
			if (resultCode == RESULT_OK) {
				int categoryID = data.getIntExtra("CATEGORY_ID", -1);
				String categoryName = data.getStringExtra("CATEGORY_NAME");
				
				if (categoryID != -1) {
					updateAdapterCategory(new Category(categoryID, categoryName));
				}
				
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
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
