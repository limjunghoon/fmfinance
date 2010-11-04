package com.fletamuto.sptb;

import android.os.Bundle;
import android.view.View;

public abstract class SelectGridBaseLayout extends FmBaseActivity {
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.select_grid_base, true);
        getData();
        setAdaper();
    }
	
	@Override
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
	
	protected void updateAdapter() {
		
		clearAdapter();
        getData();
        setAdaper();
	}
	
	
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
