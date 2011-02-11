package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class SavingsPlanData {	//적금
	public static final String STRING_NAME = "적금";
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
	
	public static ArrayList<SavingsPlanData> getSavingsPlanDatas() {	// FIXME 객체를 얻어오는 부분
		ArrayList<SavingsPlanData> savingsPlanDatas = new ArrayList<SavingsPlanData>();
		
		for(int i = 0; i < 10; i++) {
			SavingsPlanData savingsPlanData = new SavingsPlanData();	//출력 내용 확인용 임시 데이터 만들기
			
			savingsPlanData.title = "국민은행 적금 통장" + i;
			savingsPlanData.amount = 123456789;
			savingsPlanData.depoDate = new Date();
			savingsPlanData.expiDate = new Date();
			savingsPlanData.account = "123-45-6789" + i;
			savingsPlanData.interest = 3.8f;
			savingsPlanData.memo = "";
			savingsPlanDatas.add(savingsPlanData);
		}
		
		return savingsPlanDatas;
	}
}