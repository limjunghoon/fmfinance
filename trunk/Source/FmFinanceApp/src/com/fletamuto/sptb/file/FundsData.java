package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class FundsData {	//�ݵ�
	public static final String STRING_NAME = "�ݵ�";
	public static final String STRING_BRAND = "��ǰ��";
	public static final String STRING_OPENING = "������";
	public static final String STRING_MATURITY = "������";
	public static final String STRING_PRICE = "���԰���";
	public static final String STRING_TYPE = "����";
	public static final String STRING_DEALER = "�Ǹ�ó";
	public static final String STRING_MEMO = "�޸�";
	public static final String STRING_REPEAT = "�ݺ�";
	
	public String brand;
	public Date opening;
	public Date maturity;
	public long price;
	public String type;
	public String dealer;
	public String memo;
	public String repeat;
	
	public static ArrayList<FundsData> getFundsDatas() {	// FIXME ��ü�� ������ �κ�
		ArrayList<FundsData> fundsDatas = new ArrayList<FundsData>();
		
		for(int i = 0; i < 10; i++) {	//��� Ȯ�ο� �ӽ� ������ �����
			FundsData fundsData = new FundsData();
			
			fundsData.brand = "�׽�Ʈ �ݵ�" + i;
			fundsData.opening = new Date();
			fundsData.maturity = new Date();
			fundsData.price = 1000;
			fundsData.type = "�׽�Ʈ" + i;
			fundsData.dealer = "�׽�Ʈ" + i;
			fundsData.memo = "";
			fundsData.repeat = "�Ŵ� " + i + "��";
			fundsDatas.add(fundsData);
		}
		
		return fundsDatas;
	}
}