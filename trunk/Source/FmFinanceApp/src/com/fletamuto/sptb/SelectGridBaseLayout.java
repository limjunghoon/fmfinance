package com.fletamuto.sptb;

import android.os.Bundle;

public abstract class SelectGridBaseLayout extends FmBaseActivity {
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.select_grid_base, true);
        getData();
        setAdaper();
    }
	
	/**
	 * 그리드에 표시할 데이타를 가져온다.
	 */
	public abstract void getData();
	
	/**
	 * 데이타와 그리드 어뎁터를 연결한다.
	 */
	public abstract void setAdaper();
}
