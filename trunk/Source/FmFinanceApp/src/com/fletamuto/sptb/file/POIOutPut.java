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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("unused")
public class POIOutPut {
	static final String XLS_OUTPUT_DB_DIRECTORY = "/backup/";
	private Context mContext;
	
	private HSSFWorkbook mWorkbook;
	private HSSFSheet mHssfSheet;
	private HSSFRow mHssfRow;
	
	CellStyle defaultStyle;
	
	public POIOutPut(Context context) {
		super();
		this.mContext = context;
	}
	
	protected boolean poiOutput() {
		try {
			makeWorkbook();
			
			makeIncomeExpenseDataSheet();	//수입-지출
			makeAssetsSheet();	//자산
			
			try {
				if(savePOI()) {
					Log.e("POI Export", "Successed");
					return true;
				} else {
					Log.e("POI Export", "Failed");
					return false;
				}
			} catch (Exception e) {
				Log.e("POI Export", "Error");
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	private boolean makeAssetsSheet() {
		try {
			makeSheet("자산");
			makeAccountDataSheet();	//자산-보통예금
			makeRows(4);
			makeDepositDataSheet();	//자산-예금
			makeRows(4);
			makeSavingsDataSheet();	//자산-적금
			makeRows(4);
			makeStockDataSheet();	//자산-주식
			makeRows(4);
			makeFundDataSheet();	//자산-펀드
			makeRows(4);
			makeInsuranceDataSheet();	//자산-보험
			makeRows(4);
			makeRealEstateDataSheet();	//자산-부동산
			makeRows(4);
			makeOtherDataSheet();	//자산-기타
			makeRows(4);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private boolean makeIncomeExpenseDataSheet() {
		makeSheet(IncomeExpenseData.STRING_NAME);
		makeHead(new String[]{IncomeExpenseData.STRING_DATE,
								IncomeExpenseData.STRING_TYPE,
								IncomeExpenseData.STRING_CATEGORY,
								IncomeExpenseData.STRING_AMOUNT,
								IncomeExpenseData.STRING_PAY_METHOD,
								IncomeExpenseData.STRING_PAY_ACCOUNT,
								IncomeExpenseData.STRING_MEMO,
								IncomeExpenseData.STRING_TAG,
								IncomeExpenseData.STRING_REPEAT});
		ArrayList<IncomeExpenseData> expenseDatas =  new IncomeExpenseData().getIncomeExpenseDatas();
		for(int i = 0, size = expenseDatas.size(); i < size; i++) {
			makeRow();
			makeCells(expenseDatas.get(i));
		}
		
		return !expenseDatas.isEmpty();
	}
	private boolean makeAccountDataSheet() {
		//makeSheet("자산-보통예금");
		makeHead(new String[]{AccountData.STRING_NAME});
		makeRow();
		makeHead(new String[]{AccountData.STRING_FINANCIAL,
								AccountData.STRING_ACCOUNT_NUMBER,
								AccountData.STRING_TYPE,
								AccountData.STRING_BALANCE});
		ArrayList<AccountData> accountDatas =  new AccountData().getAccountDatas();
		for(int i = 0, size = accountDatas.size(); i < size; i++) {
			makeRow();
			makeCells(accountDatas.get(i));
		}
		
		return !accountDatas.isEmpty();
	}
	private boolean makeDepositDataSheet() {
		//makeSheet("자산-예금 ");
		makeHead(new String[]{DepositData.STRING_NAME});
		makeRow();
		makeHead(new String[]{DepositData.STRING_TITLE,
								DepositData.STRING_FINANCIAL,
								DepositData.STRING_ACCOUNT,
								DepositData.STRING_EXPECTAMOUNT,
								DepositData.STRING_TOTALAMOUNT,
								DepositData.STRING_INTEREST,
								DepositData.STRING_DEPO_DATE,
								DepositData.STRING_EXPI_DATE,
								DepositData.STRING_MEMO});
		ArrayList<DepositData> depositDatas =  new DepositData().getDepositDatas();
		for(int i = 0, size = depositDatas.size(); i < size; i++) {
			makeRow();
			makeCells(depositDatas.get(i));
		}
		
		return !depositDatas.isEmpty();
	}
	private boolean makeSavingsDataSheet() {
		//makeSheet("자산-적금 ");
		makeHead(new String[]{SavingsData.STRING_NAME});
		makeRow();
		makeHead(new String[]{SavingsData.STRING_TITLE,
								SavingsData.STRING_FINANCIAL,
								SavingsData.STRING_ACCOUNT,
								SavingsData.STRING_AMOUNT,
								SavingsData.STRING_TOTALAMOUNT,
								SavingsData.STRING_INTEREST,
								SavingsData.STRING_DEPO_DATE,
								SavingsData.STRING_EXPI_DATE,
								SavingsData.STRING_MEMO});
		ArrayList<SavingsData> savingsDatas =  new SavingsData().getSavingsDatas();
		for(int i = 0, size = savingsDatas.size(); i < size; i++) {
			makeRow();
			makeCells(savingsDatas.get(i));
		}
		
		return !savingsDatas.isEmpty();
	}
	private boolean makeStockDataSheet() {
		//makeSheet("주식");
		makeHead(new String[]{StockData.STRING_NAME});
		makeRow();
		makeHead(new String[]{StockData.STRING_TICKER,
								StockData.STRING_DATE,
								StockData.STRING_QUANTITY,
								StockData.STRING_PRICE,
								StockData.STRING_DEALER,
								StockData.STRING_MEMO});
		ArrayList<StockData> stockDatas =  new StockData().getStockDatas();
		for(int i = 0, size = stockDatas.size(); i < size; i++) {
			makeRow();
			makeCells(stockDatas.get(i));
		}
		
		return !stockDatas.isEmpty();
	}
	private boolean makeFundDataSheet() {
		//makeSheet("펀드");
		makeHead(new String[]{FundData.STRING_NAME});
		makeRow();
		makeHead(new String[]{FundData.STRING_BRAND,
								FundData.STRING_OPENING,
								FundData.STRING_MATURITY,
								FundData.STRING_PRICE,
								FundData.STRING_TYPE,
								FundData.STRING_DEALER,
								FundData.STRING_MEMO});
		ArrayList<FundData> fundDatas =  new FundData().getFundDatas();
		for(int i = 0, size = fundDatas.size(); i < size; i++) {
			makeRow();
			makeCells(fundDatas.get(i));
		}
		
		return !fundDatas.isEmpty();
	}
	private boolean makeInsuranceDataSheet() {
		//makeSheet("보험");
		makeHead(new String[]{InsuranceData.STRING_NAME});
		makeRow();
		makeHead(new String[]{InsuranceData.STRING_TITLE,
								InsuranceData.STRING_OPENING,
								InsuranceData.STRING_MATURITY,
								InsuranceData.STRING_AMOUNT,
								InsuranceData.STRING_INSURANCE,
								InsuranceData.STRING_MEMO});
		ArrayList<InsuranceData> insuranceDatas =  new InsuranceData().getInsuranceDatas();
		for(int i = 0, size = insuranceDatas.size(); i < size; i++) {
			makeRow();
			makeCells(insuranceDatas.get(i));
		}
		
		return !insuranceDatas.isEmpty();
	}
	private boolean makeRealEstateDataSheet() {
		//makeSheet("부동산");
		makeHead(new String[]{RealEstateData.STRING_NAME});
		makeRow();
		makeHead(new String[]{RealEstateData.STRING_TITLE,
								RealEstateData.STRING_DATE,
								RealEstateData.STRING_AMOUNT,
								RealEstateData.STRING_SCALE,
								RealEstateData.STRING_MEMO});
		ArrayList<RealEstateData> realEstateDatas =  new RealEstateData().getRealEstateDatas();
		for(int i = 0, size = realEstateDatas.size(); i < size; i++) {
			makeRow();
			makeCells(realEstateDatas.get(i));
		}
		
		return !realEstateDatas.isEmpty();
	}
	private boolean makeOtherDataSheet() {
		//makeSheet("기타");
		makeHead(new String[]{OtherData.STRING_NAME});
		makeRow();
		makeHead(new String[]{OtherData.STRING_TITLE,
				OtherData.STRING_DATE,
				OtherData.STRING_AMOUNT,
				OtherData.STRING_MEMO});
		ArrayList<OtherData> otherDatas =  new OtherData().getOtherDatas();
		for(int i = 0, size = otherDatas.size(); i < size; i++) {
			makeRow();
			makeCells(otherDatas.get(i));
		}
		
		return !otherDatas.isEmpty();
	}

	/**
	 * 워크북을 만든다
	 * @return
	 * <p><b>true</b><br/>워크북이 만들어짐</p>
	 * <p><b>false</b><br/>워크북이 만들어지지 않음</p>
	 */
	private boolean makeWorkbook() {
		mWorkbook = new HSSFWorkbook();
		defaultStyle = mWorkbook.createCellStyle();
		defaultStyle.setAlignment(CellStyle.ALIGN_LEFT);
		return (mWorkbook != null);
	}
	/**
	 * 시트를 만든다.<br/>
	 * 시트를 만듦과 동시에 0번 행을 자동으로 만드므로 첫 번째 행은 만들지 않고 시작해도 됨
	 * @return
	 * <p><b>true</b><br/>시트가 만들어짐</p>
	 * <p><b>false</b><br/>시트가 만들어지지 않음</p>
	 */
	private boolean makeSheet(String title) {
		if(mWorkbook == null)
			return false;
		else {
			mHssfSheet = mWorkbook.createSheet(title);
			mHssfRow = mHssfSheet.createRow(0);
			return (mHssfSheet != null);
		}
	}
	/**
	 * 행을 만든다
	 * @return
	 * <p><b>true</b><br/>행이 만들어짐</p>
	 * <p><b>false</b><br/>행이 만들어지지 않음</p>
	 */
	private boolean makeRow() {
		if(mWorkbook == null || mHssfSheet == null)
			return false;
		else {
			mHssfRow = mHssfSheet.createRow(mHssfSheet.getLastRowNum()+1);
			return (mHssfRow != null);
		}
	}
	/**
	 * 복수의 행을 만든다
	 * @param
	 * @return
	 * <p><b>true</b><br/>행이 만들어짐</p>
	 * <p><b>false</b><br/>행이 만들어지지 않음</p>
	 */
	private boolean makeRows(int rows) {
		if(mWorkbook == null || mHssfSheet == null)
			return false;
		else {
			mHssfRow = mHssfSheet.createRow(mHssfSheet.getLastRowNum() + rows);
			return (mHssfRow != null);
		}
	}
	
	/**
	 * 수입/지출 복수의 셀을 만든다
	 */
	private boolean makeCells(IncomeExpenseData data) {
		try {
			makeCell(0, data.getDate());
			makeCell(1, (data.getType())?"수입":"지출");
			makeCell(2, data.getCategory());
			makeCell(3, data.getAmount());
			makeCell(4, data.getPayMethod());
			makeCell(5, data.getAccount());
			makeCell(6, data.getMemo());
			makeCell(7, data.getTag());
			makeCell(8, data.getRepeat());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 보통예금의 복수의 셀을 만든다
	 */
	private boolean makeCells(AccountData data) {
		try {
			makeCell(0, data.getFinancial());
			makeCell(1, data.getAccountNumber());
			makeCell(2, data.getType());
			makeCell(3, data.getBalance());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 예금의 복수의 셀을 만든다
	 */
	private boolean makeCells(DepositData data) {
		try {
			makeCell(0, data.getTitle());
			makeCell(1, data.getFinance());
			makeCell(2, data.getAccount());
			makeCell(3, data.getExpectAmount());
			makeCell(4, data.getTotalAmount());
			makeCell(5, data.getInterest());
			makeCell(6, data.getDepoDate());
			makeCell(7, data.getExpiDate());
			makeCell(8, data.getMemo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 적금의 복수의 셀을 만든다
	 */
	private boolean makeCells(SavingsData data) {
		try {
			makeCell(0, data.getTitle());
			makeCell(1, data.getFinance());
			makeCell(2, data.getAccount());
			makeCell(3, data.getAmount());
			makeCell(4, data.getTotalAmount());
			makeCell(5, data.getInterest());
			makeCell(6, data.getDepoDate());
			makeCell(7, data.getExpiDate());
			makeCell(8, data.getMemo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 주식의 복수의 셀을 만든다
	 */
	private boolean makeCells(StockData data) {
		try {
			makeCell(0, data.getTicker());
			makeCell(1, data.getDate());
			makeCell(2, data.getQuantity());
			makeCell(3, data.getPrice());
			makeCell(4, data.getDealer());
			makeCell(5, data.getMemo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 펀드의 복수의 셀을 만든다
	 */
	private boolean makeCells(FundData data) {
		try {
			makeCell(0, data.getBrand());
			makeCell(1, data.getOpening());
			makeCell(2, data.getMaturity());
			makeCell(3, data.getPrice());
			makeCell(4, data.getType());
			makeCell(5, data.getDealer());
			makeCell(5, data.getMemo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 보험의 복수의 셀을 만든다
	 */
	private boolean makeCells(InsuranceData data) {
		try {
			makeCell(0, data.getTitle());
			makeCell(1, data.getOpening());
			makeCell(2, data.getMaturity());
			makeCell(3, data.getAmount());
			makeCell(4, data.getInsurance());
			makeCell(5, data.getMemo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 부동산의 복수의 셀을 만든다
	 */
	private boolean makeCells(RealEstateData data) {
		try {
			makeCell(0, data.getTitle());
			makeCell(1, data.getDate());
			makeCell(2, data.getAmount());
			makeCell(3, data.getScale());
			makeCell(4, data.getMemo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 기타의 복수 셀을 만든다
	 */
	private boolean makeCells(OtherData data) {
		try {
			makeCell(0, data.getTitle());
			makeCell(1, data.getDate());
			makeCell(2, data.getAmount());
			makeCell(3, data.getMemo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 표의 항목을 만든다
	 */
	private boolean makeHead(String[] heads) {
		try {
			for(int i = 0, size = heads.length; i < size; i++)
				makeCell(i, heads[i]);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean makeCell(int cellIndex, String value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, String value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, double value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, double value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, boolean value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, boolean value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_BOOLEAN).setCellValue(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, Date value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, Date value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(value.toLocaleString());
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, Calendar value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, Calendar value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(value.getTime().toLocaleString());
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeBlankCell(int cellIndex) {
		return makeBlankCell(cellIndex, defaultStyle);
	}
	private boolean makeBlankCell(int cellIndex, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_BLANK).setCellValue("");
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeErrorCell(int cellIndex) {
		return makeErrorCell(cellIndex, defaultStyle);
	}
	private boolean makeErrorCell(int cellIndex, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_ERROR).setCellErrorValue((byte)0);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeFormulaCell(int cellIndex, String value) {
		return makeFormulaCell(cellIndex, value, defaultStyle);
	}
	private boolean makeFormulaCell(int cellIndex, String value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA).setCellFormula(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}

	/**
	 * 만들어진 워크북을 파일로 출력
	 * @return
	 * <b>boolean</b> 파일 만들기가 성공적으로 이루어 졌는지에 대한 결과. true인 경우 성공.
	 * @throws Exception
	 */
	protected boolean savePOI() throws Exception {
		if(!(new DBOutput(mContext)).packageMkDir())
			return false;
		File directory = new File(DBOutput.DB_OUTPUT_PACKAGE_PATH + XLS_OUTPUT_DB_DIRECTORY);
		if(!directory.isDirectory())
			directory.mkdir();
		Date date = new Date();
		String path =  directory.getPath() + "/backup_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(date) + ".xls";
		File file = new File(path);
		FileOutputStream out = new FileOutputStream(file);
		mWorkbook.write(out);
		out.close();
		
		return file.isFile();
	}
}