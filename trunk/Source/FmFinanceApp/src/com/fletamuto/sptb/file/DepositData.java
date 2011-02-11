package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class DepositData {	//예금	-	위와 형태와 사용 방법은 동일
	public static final String STRING_NAME = "예금";
	public static final String STRING_TITLE = "제목";
	public static final String STRING_AMOUNT = "금액";
	public static final String STRING_DEPO_DATE = "예금일";
	public static final String STRING_EXPI_DATE = "만기일";
	public static final String STRING_ACCOUNT = "계좌";
	public static final String STRING_INTEREST = "이율";
	public static final String STRING_MEMO = "메모";
	
	public String title;
	public long amount;
	public Date depoDate;
	public Date expiDate;
	public String account;
	public float interest;
	public String memo;
	
	public static ArrayList<DepositData> getDepositDatas() {	// FIXME 객체를 얻어오는 부분
		ArrayList<DepositData> depositDatas = new ArrayList<DepositData>();
		
		for(int i = 0; i < 10; i++) {
			DepositData depositData = new DepositData();	//출력 내용 확인용 임시 데이터 만들기
			
			depositData.title = "국민은행 통장" + i;
			depositData.amount = 123456789;
			depositData.depoDate = new Date();
			depositData.expiDate = new Date();
			depositData.account = "123-45-6789" + i;
			depositData.interest = 2.8f;
			depositData.memo = "";
			depositDatas.add(depositData);
		}
		
		return depositDatas;
	}
}