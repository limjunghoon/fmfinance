package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class DepositData {	//����	-	���� ���¿� ��� ����� ����
	public static final String STRING_NAME = "����";
	public static final String STRING_TITLE = "����";
	public static final String STRING_AMOUNT = "�ݾ�";
	public static final String STRING_DEPO_DATE = "������";
	public static final String STRING_EXPI_DATE = "������";
	public static final String STRING_ACCOUNT = "����";
	public static final String STRING_INTEREST = "����";
	public static final String STRING_MEMO = "�޸�";
	
	public String title;
	public long amount;
	public Date depoDate;
	public Date expiDate;
	public String account;
	public float interest;
	public String memo;
	
	public static ArrayList<DepositData> getDepositDatas() {	// FIXME ��ü�� ������ �κ�
		ArrayList<DepositData> depositDatas = new ArrayList<DepositData>();
		
		for(int i = 0; i < 10; i++) {
			DepositData depositData = new DepositData();	//��� ���� Ȯ�ο� �ӽ� ������ �����
			
			depositData.title = "�������� ����" + i;
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