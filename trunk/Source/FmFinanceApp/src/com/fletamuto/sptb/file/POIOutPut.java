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

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("unused")
public class POIOutPut {
	static final String XLS_OUTPUT_DB_DIRECTORY = "/backup/";
	private Context mContext;
	
	HSSFWorkbook mWorkbook;
	HSSFSheet mHssfSheet;
	HSSFRow mHssfRow;
	
	CellStyle defaultStyle;
	
	public POIOutPut(Context context) {
		super();
		this.mContext = context;
	}
	
	protected boolean poiOutput() {
		try {
			makeWorkbook();
			makeSheet("테스트용 출력");
			return makeExpenseDataSheet();
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean makeExpenseDataSheet() {
		makeHead(new String[]{ExpenseData.STRING_DATE,
								ExpenseData.STRING_CATEGORY,
								ExpenseData.STRING_AMOUNT,
								ExpenseData.STRING_PAY_METHOD,
								ExpenseData.STRING_PAY_MONNEY_BALANCE,
								ExpenseData.STRING_PAY_ACCOUNT,
								ExpenseData.STRING_PAY_ACCOUNT_BALANCE,
								ExpenseData.STRING_PAY_BALANCE,
								ExpenseData.STRING_MEMO,
								ExpenseData.STRING_TAG,
								ExpenseData.STRING_REPEAT});
		ArrayList<ExpenseData> expenseDatas =  new GetExpenseDatas().getExpenseDatas();
		for(int i = 0, size = expenseDatas.size(); i < size; i++) {
			makeRow();
			makeCells(expenseDatas.get(i));
		}
		
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
	 * 복수의 셀을 만든다
	 */
	private boolean makeCells(ExpenseData data) {
		try {
			makeCell(0, data.getDate());
			makeCell(1, data.getCategory());
			makeCell(2, data.getAmount());
			makeCell(3, data.getPayMethod());
			makeCell(4, data.getMonneyBalance());
			makeCell(5, data.getAccount());
			makeCell(6, data.getAccountBalance());
			makeCell(7, data.getBalance());
			makeCell(8, data.getMemo());
			makeCell(9, data.getTag());
			makeCell(10, data.getRepeat());
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
		String path =  directory.getPath() + "/backup_" + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(date) + ".xls";
		File file = new File(path);
		FileOutputStream out = new FileOutputStream(file);
		mWorkbook.write(out);
		out.close();
		
		return file.isFile();
	}
}