/**
 * 정보를 얻어와서 파일로 출력하는 부분
 * 파일 경로에 패키지 이름을 얻어와야 하기 때문에 Context 객체를 넘겨 주어야 함 - 직접 문자열로 입력하도록 수정하면 없애도 무관
 * 
 * 각 출력 부분은 별도의 메소드로 분리해야 옳지만 테스트로 별도로 분리는 안되어 있는 상태
 * 
 * 다른 클래스에서 호출 할 경우에는 POITest 액티비티 클래스를 거치지 않고 이 클래스만 사용하면 됨
 * 기능이 완성되면 처리하는데 많은 비용과 시간이 소모되므로 스레드 처리로 변경해야 함
 */

package com.fletamuto.sptb.file;

import android.content.Context;


public class POIOutPut {
	static final String XLS_OUTPUT_DB_DIRECTORY = "/backup/";
	private Context context;
	
	public POIOutPut(Context context) {
		super();
		this.context = context;
	}

//	@SuppressWarnings("static-access")
//    public void onCreate(Context context) {
//        // TODO : 엑셀 파일을 만들기 위해서 워크북을 생성
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        
//        /**
//         * 수입-지출 시트
//         */
//        HSSFSheet hssfSheet = workbook.createSheet("수입-지출");
//        
//        ArrayList<ExpendData> expendDatas = new ExpendDatas().expendDatas;
//        
//        HSSFRow hssfRow = hssfSheet.createRow(0);
//        hssfRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_DATE);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_EXPEND);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_TYPE);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_COAST);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_PAY);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_MEMO);
//        hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_TAG);
//        hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(0).STRING_REPEAT);
//        
//        for(int i = 0; i < expendDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(i).date);
//            hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(i).expend);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(i).type);
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(expendDatas.get(i).coast);
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(i).pay);
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(i).memo);
//            hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(i).pay);
//            hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(expendDatas.get(i).repeat);
//        }
//        
//        hssfRow = hssfSheet.createRow(expendDatas.size()+1);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_FORMULA).setCellFormula("SUM(D2:D6)");
//        
//        
//        /**
//         * 셀 스타일 정의 부분
//         */
//        CellStyle rootCategoryCellStyle = workbook.createCellStyle();
//        rootCategoryCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        rootCategoryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        //rootCategoryCellStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
//        
//        CellStyle categoryCellStyle = workbook.createCellStyle();
//        categoryCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        categoryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        //categoryCellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
//        
//        CellStyle colunmNameStyle = workbook.createCellStyle();
//        colunmNameStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        colunmNameStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        //colunmNameStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
//        
//        
//        /**
//         * 자산 시트
//         */
//        hssfSheet = workbook.createSheet("자산");
//
//        AssetsData assetsData = new AssetsData();	//데이터를 얻어오기 위한 객체 - 자산관련 데이터를 전부 접근
//        
//        //보통예금
//        ArrayList<SavingsData> savingsDatas = assetsData.savingsData.getSavingsDatas();
//        
//        hssfRow = hssfSheet.createRow(0);
//        hssfRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue(assetsData.STRING_ASSETS);
//        hssfRow.getCell(0).setCellStyle(rootCategoryCellStyle);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(0).STRING_FINANCIAL);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(0).STRING_ACCOUNT_NUMBER);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(0).STRING_TYPE);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(0).STRING_BALANCE);
//        for(int i = 2; i <= 5; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < savingsDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        	hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(i).financial);
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(i).accountNumber);
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsDatas.get(i).type);
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(savingsDatas.get(i).balance);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-savingsDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//        
//        //예금
//        ArrayList<DepositData> depositDatas = assetsData.depositDatas.getDepositDatas();
//        
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_TITLE);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_AMOUNT);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_DEPO_DATE);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_EXPI_DATE);
//        hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_ACCOUNT);
//        hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_INTEREST);
//        hssfRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(0).STRING_MEMO);
//        for(int i = 2; i <= 8; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < depositDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(i).title);
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(depositDatas.get(i).amount);
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(i).depoDate.toLocaleString());
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(i).expiDate.toLocaleString());
//            hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(i).account);
//            hssfRow.createCell(7, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(depositDatas.get(i).interest);
//            hssfRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(depositDatas.get(i).memo);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-depositDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//        
//        //적금
//        ArrayList<SavingsPlanData> savingsPlanDatas = assetsData.savingsPlanDatas.getSavingsPlanDatas();
//        
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_TITLE);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_AMOUNT);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_DEPO_DATE);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_EXPI_DATE);
//        hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_ACCOUNT);
//        hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_INTEREST);
//        hssfRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(0).STRING_MEMO);
//        for(int i = 2; i <= 8; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < savingsPlanDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(i).title);
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(savingsPlanDatas.get(i).amount);
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(i).depoDate.toLocaleString());
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(i).expiDate.toLocaleString());
//            hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(i).account);
//            hssfRow.createCell(7, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(savingsPlanDatas.get(i).interest);
//            hssfRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(savingsPlanDatas.get(i).memo);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-savingsPlanDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//        
//        //주식
//        ArrayList<StockData> stockDatas = assetsData.stockDatas.getStockDatas();
//        
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).STRING_TICKER);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).STRING_DATE);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).STRING_QUANTITY);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).STRING_PRICE);
//        hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).STRING_DEALER);
//        hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).STRING_MEMO);
//        for(int i = 2; i <= 7; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < stockDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).ticker);
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).date.toLocaleString());
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(stockDatas.get(0).quantity);
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(stockDatas.get(0).price);
//            hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).dealer);
//            hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(stockDatas.get(0).memo);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-stockDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//        
//        //펀드
//        ArrayList<FundsData> fundsDatas = assetsData.fundsDatas.getFundsDatas();
//        
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_BRAND);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_OPENING);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_MATURITY);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_PRICE);
//        hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_TYPE);
//        hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_DEALER);
//        hssfRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_MEMO);
//        hssfRow.createCell(9, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).STRING_REPEAT);
//        for(int i = 2; i <= 9; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < fundsDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).brand);
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).opening.toLocaleString());
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).maturity.toLocaleString());
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(fundsDatas.get(0).price);
//            hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).type);
//            hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).dealer);
//            hssfRow.createCell(8, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).memo);
//            hssfRow.createCell(9, HSSFCell.CELL_TYPE_STRING).setCellValue(fundsDatas.get(0).repeat);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-fundsDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//
//        //보험
//        ArrayList<InsuranceData> insuranceDatas = assetsData.insuranceDatas.getInsuranceDatas();
//        
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).STRING_TITLE);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).STRING_OPENING);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).STRING_MATURITY);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).STRING_AMOUNT);
//        hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).STRING_INSURANCE);
//        hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).STRING_MEMO);
//        for(int i = 2; i <= 7; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < insuranceDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).title);
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).opening.toLocaleString());
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).maturity.toLocaleString());
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(insuranceDatas.get(0).amount);
//            hssfRow.createCell(6, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).insurance);
//            hssfRow.createCell(7, HSSFCell.CELL_TYPE_STRING).setCellValue(insuranceDatas.get(0).memo);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-insuranceDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//        
//        //부동산
//        ArrayList<RealEstateData> realEstateDatas = assetsData.realEstateDatas.getRealEstateDatas();
//        
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).STRING_DATE);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).STRING_TITLE);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).STRING_AMOUNT);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).STRING_MEMO);
//        for(int i = 2; i <= 5; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < realEstateDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).date.toLocaleString());
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).title);
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(realEstateDatas.get(0).amount);
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(realEstateDatas.get(0).memo);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-realEstateDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//        
//        //기타 
//        ArrayList<OtherData> otherDatas = assetsData.otherDatas.getOtherDatas();
//        
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).STRING_NAME);
//        hssfRow.getCell(1).setCellStyle(categoryCellStyle);
//        hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).STRING_DATE);
//        hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).STRING_TITLE);
//        hssfRow.createCell(4, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).STRING_AMOUNT);
//        hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).STRING_MEMO);
//        for(int i = 2; i <= 5; i++)
//        	hssfRow.getCell(i).setCellStyle(colunmNameStyle);
//        
//        for(int i = 0; i < otherDatas.size(); i++) {
//        	hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
//            hssfRow.createCell(2, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).date.toLocaleString());
//            hssfRow.createCell(3, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).title);
//            hssfRow.createCell(4, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(otherDatas.get(0).amount);
//            hssfRow.createCell(5, HSSFCell.CELL_TYPE_STRING).setCellValue(otherDatas.get(0).memo);
//        }
//        hssfSheet.addMergedRegion(new CellRangeAddress(hssfSheet.getLastRowNum()-otherDatas.size(), hssfSheet.getLastRowNum(), 1, 1));	//셀 병합
//        
//        hssfSheet.addMergedRegion(new CellRangeAddress(0, hssfSheet.getLastRowNum(), 0, 0));	//셀 병합
//
//        
//        //최하단 시간 표시
//        hssfRow = hssfSheet.createRow(hssfSheet.getLastRowNum()+2);
//        hssfRow.createCell(0, HSSFCell.CELL_TYPE_STRING).setCellValue("작성일");
//        hssfRow.createCell(1, HSSFCell.CELL_TYPE_STRING).setCellValue(new Date().toLocaleString());
//        
//        
//        
//        
//        
//        
//        
//        //만들어진 개체를 파일로 출력하는 부분
//        File directory = new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName());
//        if(!directory.isDirectory())
//        	directory.mkdir();
//        String path =  directory.getPath() + "/backup_" + (new Date().getYear()+1900+"-") + (new Date().getMonth()+1+"-") + (new Date().getDate()) + ".xls";
//        Toast.makeText(context.getApplicationContext(), path, Toast.LENGTH_LONG).show();
//        File file = new File(path);
//        FileOutputStream out;
//		try {
//			out = new FileOutputStream(file);
//			
//			workbook.write(out);
//			out.close();
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
}