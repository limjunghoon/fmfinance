package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class RealEstateData {	//�ε���
	public static final String STRING_NAME = "�ε���";
	public static final String STRING_DATE = "��¥";
	public static final String STRING_TITLE = "����";
	public static final String STRING_AMOUNT = "�ݾ�";
	public static final String STRING_MEMO = "�޸�";
	
	public Date date;
	public String title;
	public long amount;
	public String memo;
	
	public static ArrayList<RealEstateData> getRealEstateDatas() {	// FIXME ��ü�� ������ �κ�
		ArrayList<RealEstateData> realEstateDatas = new ArrayList<RealEstateData>();
		
		for(int i = 0; i < 10; i++) {	//��� Ȯ�ο� �ӽ� ������ �����
			RealEstateData realEstateData = new RealEstateData();
			
			realEstateData.date = new Date();
			realEstateData.title = "�׽�Ʈ" + i;
			realEstateData.amount = 1000000000;
			realEstateData.memo = "";
			realEstateDatas.add(realEstateData);
		}
		
		return realEstateDatas;
	}
}