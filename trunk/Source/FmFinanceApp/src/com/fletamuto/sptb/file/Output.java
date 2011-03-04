package com.fletamuto.sptb.file;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Output {
	private POIOutPut poiOutPut;
	private DBOutput dbOutput;
	private Context mContext;
	
	private ProgressDialog mProgressDialog;

	public Output(Context context) {
		super();
		this.poiOutPut = new POIOutPut(context);
		this.dbOutput = new DBOutput(context);
		this.mContext = context;
	}
	
	public void savePOI() {
		showProgress();
		if(poiOutPut.poiOutput()) {
			//정상적으로 파일 복사
			Log.e("POIOutput", "Successed");
			Toast.makeText(mContext, "XLS 파일 출력 성공", Toast.LENGTH_SHORT).show();
		} else {
			//파일 복사 실패
			Log.e("POIOutput", "Failed");
			Toast.makeText(mContext, "XLS 파일 출력 실패", Toast.LENGTH_SHORT).show();
		} 
		hideProgress();
	}
	
	public void saveDB() {
		if(dbOutput.dbOutput()) {
			//정상적으로 파일 복사
			Log.e("DBOutput", "Successed");
			Toast.makeText(mContext, "데이터 베이스 백업 성공", Toast.LENGTH_SHORT).show();
		} else {
			//파일 복사 실패
			Log.e("DBOutput", "Failed");
			Toast.makeText(mContext, "데이터 베이스 백업 실패", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 저장 프로그래스바 보이기
	 */
	private void showProgress() {
		mProgressDialog = ProgressDialog.show(mContext, "파일 저장", "파일을 저장 하는 중입니다", true);
	}
	/**
	 * 저장 프로그래스바 감추기
	 */
	private void hideProgress() {
		mProgressDialog.dismiss();
	}
}
