/**
 * �׽�Ʈ�� ��Ƽ��Ƽ Ŭ����
 * ���� ȣ�� �Ϸ��� Manifest�� �߰��ؾ� ��
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
