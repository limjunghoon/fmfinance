package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class InsuranceData {	//보험
	public static final String STRING_NAME = "보험";
	public static final String STRING_TITLE = "제목";
	public static final String STRING_OPENING = "개설일";
	public static final String STRING_MATURITY = "만기일";
	public static final String STRING_AMOUNT = "금액";
	public static final String STRING_INSURANCE = "보험사";
	public static final String STRING_MEMO = "메모";
	
	public String title;
	public Date opening;
	public Date maturity;
	public long amount;
	public String insurance;
	public String memo;
	
	public static ArrayList<InsuranceData> getInsuranceDatas() {	// FIXME 객체를 얻어오는 부분
		ArrayList<InsuranceData> insuranceDatas = new ArrayList<InsuranceData>();
		
		for(int i = 0; i < 10; i++) {	//출력 확인용 임시 데이터 만들기
			InsuranceData insuranceData = new InsuranceData();
			
			insuranceData.title = "테스트 상품" + i;
			insuranceData.opening = new Date();
			insuranceData.maturity = new Date();
			insuranceData.amount = 10000000;
			insuranceData.insurance = "테스트 보험사" + i;
			insuranceData.memo = "";
			insuranceDatas.add(insuranceData);
		}
		
		return insuranceDatas;
	}
}