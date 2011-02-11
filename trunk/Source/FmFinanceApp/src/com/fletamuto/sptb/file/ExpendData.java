package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class ExpendData {
	//�⵵/��/��	����/����	����	�ݾ�	����	�޸�	�±�	�ݺ�
	public static final String STRING_DATE = "�⵵/��/��";
	public static final String STRING_EXPEND = "����/����";
	public static final String STRING_TYPE = "����";
	public static final String STRING_COAST = "�ݾ�";
	public static final String STRING_PAY = "����";
	public static final String STRING_MEMO = "�޸�";
	public static final String STRING_TAG = "�±�";
	public static final String STRING_REPEAT = "�ݺ�";
	
	public Date date;
	public String expend;
	public String type;
	public long coast;
	public String pay;
	public String memo;
	public String tag;
	public String repeat; 
}

class ExpendDatas {	//���Ƿ� ���� ������ - ���� ȭ�鿡 �ѷ����Ը� �ϴ� ���� ����
	public ArrayList<ExpendData> expendDatas;
	
	public ExpendDatas() {
		ArrayList<ExpendData> expendDatas = new ArrayList<ExpendData>();	// FIXME ��ü�� ������ �κ�
		ExpendData expendData = new ExpendData();
		expendData.date = new Date();
		expendData.expend = "����";
		expendData.type = "�޿�";
		expendData.coast = 2000000;
		expendData.pay = "";
		expendData.memo = "";
		expendData.tag = "�޿�";
		expendData.repeat = "�ſ� 8�� �ݺ�";
		for(int i = 0; i < 10; i++) {
			expendDatas.add(expendData);
		}
		this.expendDatas = expendDatas;
	}
}