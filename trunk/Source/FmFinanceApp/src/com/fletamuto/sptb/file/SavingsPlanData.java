package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class SavingsPlanData {	//����
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
	
	public static ArrayList<SavingsPlanData> getSavingsPlanDatas() {	// FIXME ��ü�� ������ �κ�
		ArrayList<SavingsPlanData> savingsPlanDatas = new ArrayList<SavingsPlanData>();
		
		for(int i = 0; i < 10; i++) {
			SavingsPlanData savingsPlanData = new SavingsPlanData();	//��� ���� Ȯ�ο� �ӽ� ������ �����
			
			savingsPlanData.title = "�������� ���� ����" + i;
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