package com.fletamuto.sptb.file;

import java.util.ArrayList;

/**
 * 자산 탭에서 관리되는 데이터들을 전부 얻어오기 위한 객체
 */
public class AssetsData {
	public static final String STRING_ASSETS = "자산";
	private ArrayList<AccountData> savingsDatas;
	private ArrayList<DepositData> depositDatas;
	private ArrayList<SavingsData> savingsPlanDatas;
	private ArrayList<StockData> stockDatas;
	private ArrayList<FundsData> fundsDatas;
	private ArrayList<InsuranceData> insuranceDatas;
	private ArrayList<RealEstateData> realEstateDatas;
	private ArrayList<OtherData> otherDatas;
	
	public AssetsData() {
		this.savingsDatas = new AccountData().getAccountDatas();
//		this.depositDatas = new DepositData();
//		this.savingsPlanDatas = new SavingsPlanData();
//		this.stockDatas = new StockData();
//		this.fundsDatas = new FundsData();
//		this.insuranceDatas = new InsuranceData();
//		this.realEstateDatas = new RealEstateData();
//		this.otherDatas = new OtherData();
	}
}