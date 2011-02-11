package com.fletamuto.sptb.file;

/**
 * �ڻ� �ǿ��� �����Ǵ� �����͵��� ���� ������ ���� ��ü
 */
public class AssetsData {
	public static final String STRING_ASSETS = "�ڻ�";
	SavingsData savingsData;
	DepositData depositDatas;
	SavingsPlanData savingsPlanDatas;
	StockData stockDatas;
	FundsData fundsDatas;
	InsuranceData insuranceDatas;
	RealEstateData realEstateDatas;
	OtherData otherDatas;
	
	public AssetsData() {
		this.savingsData = new SavingsData();
		this.depositDatas = new DepositData();
		this.savingsPlanDatas = new SavingsPlanData();
		this.stockDatas = new StockData();
		this.fundsDatas = new FundsData();
		this.insuranceDatas = new InsuranceData();
		this.realEstateDatas = new RealEstateData();
		this.otherDatas = new OtherData();
	}
}