package com.fletamuto.sptb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.common.control.BaseSlidingActivity;

//public abstract class SelectGridBaseLayout extends FmBaseActivity {
public abstract class SelectGridBaseLayout extends BaseSlidingActivity {
	
	//GridView Title Text
	private TextView gridTitle;
	
	//GridView Right Edit Button
	private  Button gridRightBtn;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        
//        setContentView(R.layout.select_grid_base, true);
        setContentView(R.layout.select_grid_base);
                
        setSlidingView(findViewById(R.id.GridBase));
        appearAnimation();
        
        getData();
        setAdaper();         
        
        gridTitle = (TextView) findViewById(R.id.selectGridBaseTitleText);
        gridTitle.setText(this.getTitle());
        
        gridRightBtn = (Button) findViewById (R.id.BtnGridBaseEdit);
    }
	
	public void setGridRightBtn (Button btn) {
		gridRightBtn = btn;
	}
	public Button getGridRightBtn() {
		return gridRightBtn;
	}
	
/*
	protected void setTitleBtn() {
        setEditButtonListener();
        setTitle(getResources().getString(R.string.btn_category_select));
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
        
		super.setTitleBtn();
	}
	
	public void setEditButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onEditButtonClick();
			}
		});
	}
*/
	
	protected void updateAdapter() {
		
		clearAdapter();
        getData();
        setAdaper();
	}
	
	public void setTitle(CharSequence title) {
		gridTitle.setText(title);
    };
	
	/**
	 * ����Ʈ ��ư Ŭ����
	 */
	protected abstract void onEditButtonClick();

	/**
	 * �׸��忡 ǥ���� ����Ÿ�� �����´�.
	 */
	public abstract void getData();
	
	/**
	 * ����Ÿ�� �׸��� ��͸� �����Ѵ�.
	 */
	public abstract void setAdaper();
	
	protected abstract void clearAdapter();
}
