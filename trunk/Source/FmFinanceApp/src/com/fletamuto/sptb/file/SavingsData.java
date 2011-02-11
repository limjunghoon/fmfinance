package com.fletamuto.sptb.file;

import java.util.ArrayList;

public class SavingsData {	//보통예금
	public static final String STRING_NAME = "보통예금";
	public static final String STRING_FINANCIAL = "금융기관";
	public static final String STRING_ACCOUNT_NUMBER = "계좌번호";
	public static final String STRING_TYPE = "종류";
	public static final String STRING_BALANCE = "잔액";
	
	public String financial;
	public String accountNumber;
	public String type;
	public long balance;
	
	public static ArrayList<SavingsData> getSavingsDatas() {		// FIXME 객체를 얻어오는 부분	- 실제로는 값을 얻어오는 메소드이지만 임의의 값만 넣어서 테스트
		ArrayList<SavingsData> savingsDatas = new ArrayList<SavingsData>();
		
		SavingsData savingsData = new SavingsData();
		savingsData.financial = "국민은행";
		savingsData.accountNumber = "1111";
		savingsData.type = "예금";
		savingsData.balance = 1111;
		savingsDatas.add(savingsData);
		
		savingsData = new SavingsData();
		savingsData.financial = "신한은행";
		savingsData.accountNumber = "2222";
		savingsData.type = "예금";
		savingsData.balance = 2222;
		savingsDatas.add(savingsData);
		
		savingsData = new SavingsData();
		savingsData.financial = "우리은행";
		savingsData.accountNumber = "3333";
		savingsData.type = "예금";
		savingsData.balance = 3333;
		savingsDatas.add(savingsData);
		
		return savingsDatas;
	}
}