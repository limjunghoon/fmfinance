package com.fletamuto.sptb.file;

/**
 * 자산 탭에서 관리되는 데이터들을 전부 얻어오기 위한 객체
 */
public class AssetsData {
	public static final String STRING_ASSETS = "자산";
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