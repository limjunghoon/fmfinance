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
	 * �׸��忡 ǥ���� ����Ÿ�� �����´�.
	 */
	public abstract void getData();
	
	/**
	 * ����Ÿ�� �׸��� ��͸� �����Ѵ�.
	 */
	public abstract void setAdaper();
}
