package com.fletamuto.sptb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * 그리드로 선택하는 아이템 편집창의 기본 클래스
 * @author yongbban
 * @version 1.0.0.1
 */
public abstract class EditSelectItemBaseLayout  extends FmBaseActivity {  	
	
	
	private Button mVisibleDeleteButton;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        setContentView(R.layout.edit_select_base, true);
        
        getData();
        setAdaper();
    }
    
    @Override
    protected void setTitleBtn() {
        setAddButtonListener();
        setTitle(getResources().getString(R.string.tag_edit_select));
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        setTitleBtnText(FmTitleLayout.BTN_RIGTH_01, getResources().getString(R.string.btn_add));
        
    	super.setTitleBtn();
    }

	private void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onAddButtonClick();
			}
		});
	}
	

	protected void setDeleteBtnListener(View convertView, final int deleteItemID, final int deletePosition) {
    	final Button btnDelete = (Button)convertView.findViewById(R.id.BtnEditSelectDelete);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onDeleteButtonClick(deleteItemID, deletePosition);
			}
		});
    }
    
    protected void setEditBtnListener(View convertView, final int itemID, final String itemName, final int position)  {
    	final Button btnEdit = (Button)convertView.findViewById(R.id.BtnEditSelectEdit);
    	
    	btnEdit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onEditButtonClick(itemID, itemName, position);
//				updateCategory(editCategory, position);
			}
		});
    }
    
    protected void setListViewListener(ListView listEditSelectItem) {
    	listEditSelectItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setVisibleDeleteButton((Button)view.findViewById(R.id.BtnEditSelectDelete));
				
			}
		});
    	
    	listEditSelectItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				setVisibleDeleteButton((Button)view.findViewById(R.id.BtnEditSelectDelete));
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				if (mVisibleDeleteButton != null) {
					mVisibleDeleteButton.setVisibility(View.INVISIBLE);
				}
			}
		});
    	
    	listEditSelectItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false) {
					if (mVisibleDeleteButton != null) {
						mVisibleDeleteButton.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
    }
    
	private void setVisibleDeleteButton(Button button) {
		if (mVisibleDeleteButton != null) {
			mVisibleDeleteButton.setVisibility(View.INVISIBLE);
		}
		
		mVisibleDeleteButton = button;
		mVisibleDeleteButton.setVisibility(View.VISIBLE);
	}
	
	protected void makeItem() {
		final EditText edit = new EditText(EditSelectItemBaseLayout.this);
		new AlertDialog.Builder(EditSelectItemBaseLayout.this)
		.setTitle("분류를 입력하세요")
		.setView(edit)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				String tagName = edit.getText().toString();
				
				if (tagName.length() == 0) {
					return;
				}
				
				createItem(tagName);
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
	}
	
	private void editItem(final int itemID, final String itemName, final int position) {
		
    	final EditText edit = new EditText(EditSelectItemBaseLayout.this);    	
    	edit.setText(itemName);
    	
		new AlertDialog.Builder(EditSelectItemBaseLayout.this)
		.setTitle("분류를 입력하세요")
		.setView(edit)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				String tagName = edit.getText().toString();
				
				if (tagName.length() == 0) {
					return;
				}
				
				updateItem(itemID, tagName, position);
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();

	}
	
	@Override
    public void finish() {
    	Intent intent = new Intent();
    	setResult(RESULT_OK, intent);
    	super.finish();
    }
	

	protected void onAddButtonClick() {
		makeItem();
	}
	
	protected void onEditButtonClick(int itemID, String itemName, final int position) {
		editItem(itemID, itemName, position);
	}
	
	protected abstract void createItem(String name);

	protected abstract void updateItem(int editTagID, String itemName, int editPosition);
	
	protected abstract void onDeleteButtonClick(int deleteItemID, int deletePosition);
	
	/**
	 * 표시할 데이타를 가져온다.
	 */
	public abstract void getData();
	
	/**
	 * 데이타와 어뎁터를 연결한다.
	 */
	public abstract void setAdaper();
	
}
