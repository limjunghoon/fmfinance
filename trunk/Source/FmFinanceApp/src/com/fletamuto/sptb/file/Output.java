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
			//정상적으로 파일 복사
			Log.e("DBOutput", "Successed");
			Toast.makeText(context, "데이터 베이스 백업 성공", Toast.LENGTH_SHORT).show();
		} else {
			//파일 복사 실패
			Log.e("DBOutput", "Failed");
			Toast.makeText(context, "데이터 베이스 백업 실패", Toast.LENGTH_SHORT).show();
		}
	}
}
