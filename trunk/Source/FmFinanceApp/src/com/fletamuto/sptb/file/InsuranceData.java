package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class InsuranceData {	//����
	public static final String STRING_NAME = "����";
	public static final String STRING_TITLE = "����";
	public static final String STRING_OPENING = "������";
	public static final String STRING_MATURITY = "������";
	public static final String STRING_AMOUNT = "�ݾ�";
	public static final String STRING_INSURANCE = "�����";
	public static final String STRING_MEMO = "�޸�";
	
	public String title;
	public Date opening;
	public Date maturity;
	public long amount;
	public String insurance;
	public String memo;
	
	public static ArrayList<InsuranceData> getInsuranceDatas() {	// FIXME ��ü�� ������ �κ�
		ArrayList<InsuranceData> insuranceDatas = new ArrayList<InsuranceData>();
		
		for(int i = 0; i < 10; i++) {	//��� Ȯ�ο� �ӽ� ������ �����
			InsuranceData insuranceData = new InsuranceData();
			
			insuranceData.title = "�׽�Ʈ ��ǰ" + i;
			insuranceData.opening = new Date();
			insuranceData.maturity = new Date();
			insuranceData.amount = 10000000;
			insuranceData.insurance = "�׽�Ʈ �����" + i;
			insuranceData.memo = "";
			insuranceDatas.add(insuranceData);
		}
		
		return insuranceDatas;
	}
}