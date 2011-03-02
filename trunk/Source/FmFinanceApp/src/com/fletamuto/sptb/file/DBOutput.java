package com.fletamuto.sptb.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

public class DBOutput {
	static final String DB_OUTPUT_PACKAGE_PATH = Environment.getExternalStorageDirectory() + "/com.fletamuto.sptb/";
	static final String DB_OUTPUT_DB_DIRECTORY = "/db/";
	static final String DB_FILE_NAME = "fmfinance.db";
	private Context context;
	
	public DBOutput(Context context) {
		super();
		this.context = context;
	}
	
	protected boolean dbOutput() {
		File file = context.getDatabasePath(DB_FILE_NAME);
		int size = (int)file.length();
		
		try {
			byte[] buffer = new byte[size];
			InputStream inputStream = new FileInputStream(file);
			inputStream.read(buffer);
			inputStream.close();
			
			if(!packageMkDir())
				return false;
			File outputDBDirectory = new File(DB_OUTPUT_PACKAGE_PATH + DB_OUTPUT_DB_DIRECTORY);
			if(!outputDBDirectory.isDirectory())
				outputDBDirectory.mkdir();
			File outputFile = new File(DB_OUTPUT_PACKAGE_PATH + DB_OUTPUT_DB_DIRECTORY + DB_FILE_NAME);
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(buffer);
			outputStream.flush();
			outputStream.close();
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * SD카드에 파일을 저장할 때 반복해서 사용되는 패키지 디렉토리 생성 메소드 
	 * @return
	 * <p><b>true</b><br/>패키지 디렉토리가 정상적으로 생성 됨</p>
	 * <p><b>false</b><br/>패키지 디렉토리가 정상적으로 생성되지 않음</p>
	 */
	public boolean packageMkDir() {
		File outputPackageDirectory = new File(DB_OUTPUT_PACKAGE_PATH);
		if(!outputPackageDirectory.isDirectory())
			outputPackageDirectory.mkdir();
		return outputPackageDirectory.isDirectory();
	}
}
