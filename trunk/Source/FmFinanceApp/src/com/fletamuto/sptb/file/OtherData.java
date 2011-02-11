package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class OtherData {	//기타
	public static final String STRING_NAME = "기타";
	public static final String STRING_DATE = "날짜";
	public static final String STRING_TITLE = "제목";
	public static final String STRING_AMOUNT = "금액";
	public static final String STRING_MEMO = "메모";
	
	public Date date;
	public String title;
	public long amount;
	public String memo;
	
	public static ArrayList<OtherData> getOtherDatas() {	// FIXME 객체를 얻어오는 부분
		ArrayList<OtherData> otherDatas = new ArrayList<OtherData>();
		
		for(int i = 0; i < 10; i++) {	//출력 확인용 임시 데이터 만들기
			OtherData otherData = new OtherData();
			
			otherData.date = new Date();
			otherData.title = "테스트" + i;
			otherData.amount = 1000000000;
			otherData.memo = "";
			otherDatas.add(otherData);
		}
		
		return otherDatas;
	}
}