package com.fletamuto.sptb.file;

import java.util.ArrayList;

public class SavingsData {	//���뿹��
	public static final String STRING_NAME = "���뿹��";
	public static final String STRING_FINANCIAL = "�������";
	public static final String STRING_ACCOUNT_NUMBER = "���¹�ȣ";
	public static final String STRING_TYPE = "����";
	public static final String STRING_BALANCE = "�ܾ�";
	
	public String financial;
	public String accountNumber;
	public String type;
	public long balance;
	
	public static ArrayList<SavingsData> getSavingsDatas() {		// FIXME ��ü�� ������ �κ�	- �����δ� ���� ������ �޼ҵ������� ������ ���� �־ �׽�Ʈ
		ArrayList<SavingsData> savingsDatas = new ArrayList<SavingsData>();
		
		SavingsData savingsData = new SavingsData();
		savingsData.financial = "��������";
		savingsData.accountNumber = "1111";
		savingsData.type = "����";
		savingsData.balance = 1111;
		savingsDatas.add(savingsData);
		
		savingsData = new SavingsData();
		savingsData.financial = "��������";
		savingsData.accountNumber = "2222";
		savingsData.type = "����";
		savingsData.balance = 2222;
		savingsDatas.add(savingsData);
		
		savingsData = new SavingsData();
		savingsData.financial = "�츮����";
		savingsData.accountNumber = "3333";
		savingsData.type = "����";
		savingsData.balance = 3333;
		savingsDatas.add(savingsData);
		
		return savingsDatas;
	}
}