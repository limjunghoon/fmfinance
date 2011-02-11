package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class RealEstateData {	//부동산
	public static final String STRING_NAME = "부동산";
	public static final String STRING_DATE = "날짜";
	public static final String STRING_TITLE = "제목";
	public static final String STRING_AMOUNT = "금액";
	public static final String STRING_MEMO = "메모";
	
	public Date date;
	public String title;
	public long amount;
	public String memo;
	
	public static ArrayList<RealEstateData> getRealEstateDatas() {	// FIXME 객체를 얻어오는 부분
		ArrayList<RealEstateData> realEstateDatas = new ArrayList<RealEstateData>();
		
		for(int i = 0; i < 10; i++) {	//출력 확인용 임시 데이터 만들기
			RealEstateData realEstateData = new RealEstateData();
			
			realEstateData.date = new Date();
			realEstateData.title = "테스트" + i;
			realEstateData.amount = 1000000000;
			realEstateData.memo = "";
			realEstateDatas.add(realEstateData);
		}
		
		return realEstateDatas;
	}
}