/**
 * 테스트용 액티비티 클래스
 * 직접 호출 하려면 Manifest에 추가해야 함
 */
package com.fletamuto.sptb.file;

import android.app.Activity;
import android.os.Bundle;

public class POITest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		new POIOutPut().onCreate(this);
	}
}
