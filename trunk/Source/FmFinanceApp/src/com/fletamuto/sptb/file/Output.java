package com.fletamuto.sptb.file;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Output {
	private POIOutPut poiOutPut;
	private DBOutput dbOutput;
	private Context context;

	public Output(Context context) {
		super();
		this.poiOutPut = new POIOutPut(context);
		this.dbOutput = new DBOutput(context);
		this.context = context;
	}
	
	public void savePOI() {
		
	}
	
	public void saveDB() {
		if(dbOutput.dbOutput()) {
			//���������� ���� ����
			Log.e("DBOutput", "Successed");
			Toast.makeText(context, "������ ���̽� ��� ����", Toast.LENGTH_SHORT).show();
		} else {
			//���� ���� ����
			Log.e("DBOutput", "Failed");
			Toast.makeText(context, "������ ���̽� ��� ����", Toast.LENGTH_SHORT).show();
		}
	}
}
