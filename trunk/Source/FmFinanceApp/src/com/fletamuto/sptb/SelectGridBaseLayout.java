package com.fletamuto.sptb;

import android.os.Bundle;
import android.view.View;

import com.fletamuto.common.control.BaseSlidingActivity;

//public abstract class SelectGridBaseLayout extends FmBaseActivity {
public abstract class SelectGridBaseLayout extends BaseSlidingActivity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
//        setContentView(R.layout.select_grid_base, true);
        setContentView(R.layout.select_grid_base);
        
        setSlidingView(findViewById(R.id.GridBase));
        appearAnimation();
        
        getData();
        setAdaper();
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
	
	
	/**
	 * 에디트 버튼 클릭시
	 */
	protected abstract void onEditButtonClick();

	/**
	 * 그리드에 표시할 데이타를 가져온다.
	 */
	public abstract void getData();
	
	/**
	 * 데이타와 그리드 어뎁터를 연결한다.
	 */
	public abstract void setAdaper();
	
	protected abstract void clearAdapter();
}
