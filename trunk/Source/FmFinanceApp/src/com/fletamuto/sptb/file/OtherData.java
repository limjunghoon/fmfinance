package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class OtherData {	//��Ÿ
	public static final String STRING_NAME = "��Ÿ";
	public static final String STRING_DATE = "��¥";
	public static final String STRING_TITLE = "����";
	public static final String STRING_AMOUNT = "�ݾ�";
	public static final String STRING_MEMO = "�޸�";
	
	public Date date;
	public String title;
	public long amount;
	public String memo;
	
	public static ArrayList<OtherData> getOtherDatas() {	// FIXME ��ü�� ������ �κ�
		ArrayList<OtherData> otherDatas = new ArrayList<OtherData>();
		
		for(int i = 0; i < 10; i++) {	//��� Ȯ�ο� �ӽ� ������ �����
			OtherData otherData = new OtherData();
			
			otherData.date = new Date();
			otherData.title = "�׽�Ʈ" + i;
			otherData.amount = 1000000000;
			otherData.memo = "";
			otherDatas.add(otherData);
		}
		
		return otherDatas;
	}
}