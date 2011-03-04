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
			//���������� ���� ����
			Log.e("POIOutput", "Successed");
			Toast.makeText(mContext, "XLS ���� ��� ����", Toast.LENGTH_SHORT).show();
		} else {
			//���� ���� ����
			Log.e("POIOutput", "Failed");
			Toast.makeText(mContext, "XLS ���� ��� ����", Toast.LENGTH_SHORT).show();
		} 
		hideProgress();
	}
	
	public void saveDB() {
		if(dbOutput.dbOutput()) {
			//���������� ���� ����
			Log.e("DBOutput", "Successed");
			Toast.makeText(mContext, "������ ���̽� ��� ����", Toast.LENGTH_SHORT).show();
		} else {
			//���� ���� ����
			Log.e("DBOutput", "Failed");
			Toast.makeText(mContext, "������ ���̽� ��� ����", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * ���� ���α׷����� ���̱�
	 */
	private void showProgress() {
		mProgressDialog = ProgressDialog.show(mContext, "���� ����", "������ ���� �ϴ� ���Դϴ�", true);
	}
	/**
	 * ���� ���α׷����� ���߱�
	 */
	private void hideProgress() {
		mProgressDialog.dismiss();
	}
}
