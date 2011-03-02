package com.fletamuto.sptb.file;

import java.util.Locale;

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
		if(poiOutPut.poiOutput()) {
			//���������� ���� ����
			Log.e("POIOutput", "Successed");
			Toast.makeText(context, "XLS ���� ��� ����", Toast.LENGTH_SHORT).show();
		} else {
			//���� ���� ����
			Log.e("POIOutput", "Failed");
			Toast.makeText(context, "XLS ���� ��� ����", Toast.LENGTH_SHORT).show();
		}
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
