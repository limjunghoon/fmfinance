package com.fletamuto.common.control.fmgraph;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BarGraph extends View {
	
	private int currentAreaWidth = getMeasuredWidth(); //그래프가 그려질 영역의 Width
	private int currentAreaHeight = getMeasuredHeight(); //그래프가 그려질 영역의 Height
	
	private int barGraphMargin = 20;
		
//	private int[] axisPositions = null; //그릴 축 
	private ArrayList<Integer> axisPositions = new ArrayList<Integer>(); 
	private int standardAxis = Constants.BAR_AXIS_X_BOTTOM; //그래프가 시작할 기준이 되는 축
	
	private int bargroupAndBargroupGap = Constants.BAR_DEFAULT_BARGROUP_AND_BARGROUP_GAP; //Bar 그룹과 bar 그룹의 간격
	private int barAndBarGapInBargroup = Constants.BAR_DEFAULT_BAR_AND_BAR_GAP_IN_BAR_GROUP; //bar 그룹 안의 bar 간격
	private int barWidth = Constants.BAR_DEFAULT_BAR_WIDTH; //bar 의 폭
	
//	private String[] standardAxisTitles = null; //그래프가 놓여질 축의 타이틀 값
	private ArrayList<String> standardAxisTitles = new ArrayList<String>();
	private int standardAxisTitlesCount;
	
	private int gradationCount = Constants.BAR_DEFAULT_GRADATION_COUNT; //눈금 갯수
	private int gradationMode = Constants.BAR_GRADATION_MODE_NONE; //눈금 모드
	
//	private String[] gradationTitles = null; //눈금 타이틀
	private ArrayList<String> gradationTitles = new ArrayList<String>();
	
	private int barGroupMemberCount = Constants.BAR_DEFAULT_GROUP_MEMBER_COUNT; //bar 그룹의 멤버 갯수
	private int barGraphGroupCount; //바 그래프 그룹 갯수
	
	private int axisThickness = Constants.BAR_DEFAULT_AXIS_THICKNESS; //축의 두께
	private int gradationThickness = Constants.BAR_DEFAULT_GRADATION_THICKNESS; //눈금 두께
	
	private int backgroundColor = Color.BLACK;
	private int axisColor = Color.WHITE;
	private int gradationColor = Color.WHITE;
	
//	private long[] graphItemValues = null; //그래프로 그려질 항목들에 대한 값
	private ArrayList<Long> graphItemValues = new ArrayList<Long>();
	
	private long mexGraphItemValue; //그래프로 그려질 항목의 최고 값
	
//	private String[] barTitles = null; //bar 에 표시 될 타이틀 값들
	private ArrayList<String> barTitles = new ArrayList<String>();
	
	private int[] defaultGraphColors = new int[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY, Color.CYAN,
		Color.GREEN, Color.MAGENTA, Color.DKGRAY, Color.WHITE, Color.LTGRAY, Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY, Color.CYAN,
		Color.GREEN, Color.MAGENTA, Color.DKGRAY, Color.WHITE, Color.LTGRAY, Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY, Color.CYAN,
		Color.GREEN, Color.MAGENTA, Color.DKGRAY, Color.WHITE, Color.LTGRAY};
	
	private int axisXLength; //X축들의 길이
	private int axisYLength; //Y축들의 길이
	
	private Paint[] barPaint;
	private RectF[] bar;
	
		
	/* =============================== 생성자 =================================*/
	public BarGraph (Context context) {
		super(context);		
	}
	
	public BarGraph (Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BarGraph (Context context, AttributeSet attrs, int defaultStyle) {
		super (context, attrs, defaultStyle);
	}
	
	/* =============================== 사용자 위젲 관련  =================================*/
	protected void onMeasure (int wMeasureSpec, int hMeasureSpec) {
		int measuredHeight = measureHeight(hMeasureSpec);
		int measuredWidth = measureWidth(wMeasureSpec);
		
		setMeasuredDimension(measuredWidth, measuredHeight);
	}
	
	private int measureHeight (int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		int result = 0;


			result = specSize;

		return result;
	}
	
	private int measureWidth (int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		int result = 0;


			result = specSize;

		
		return result;
	}	
	
	/* =============================== SET Method =================================*/
	public void setAxisPositions (ArrayList<Integer> axis_Positions) {
		
		if (axisPositions.isEmpty() == false) {
			axisPositions.clear();
		}
		
		for (int i=0; i<axis_Positions.size(); i++) {
			axisPositions.add(axis_Positions.get(i));
		}		
		
	}
	public void setStandardAxis (int standard_Axis) {
		standardAxis = standard_Axis;
	}
	
	public void setbarGraphMargin (int bar_Graph_Margin) {
		barGraphMargin = bar_Graph_Margin;
	}

	public void setbargroupAndBargroupGap  (int bargroup_And_Bargroup_Gap) {
		bargroupAndBargroupGap = bargroup_And_Bargroup_Gap;
	}
	public void setbarAndBarGapInBargroup  (int bar_And_Bar_Gap_In_Bargroup) {
		barAndBarGapInBargroup = bar_And_Bar_Gap_In_Bargroup;
	}
	public void setBarWidth  (int bar_Width) {
		barWidth = bar_Width;
	}
	public void setStandardAxisTitles  (ArrayList<String> standard_Axis_Titles ) {
		if (standardAxisTitles.isEmpty() == false) {
			standardAxisTitles.clear();
		}

		standardAxisTitlesCount = standard_Axis_Titles.size();
		for (int i=0; i<standard_Axis_Titles.size(); i++) {
			standardAxisTitles.add(standard_Axis_Titles.get(i));
		}
		
	}
	public void setGradationCount (int gradation_Count) {
		gradationCount =gradation_Count;
	}
	public void setGradationMode (int gradation_Mode) {
		gradationMode =gradation_Mode;
	}
	public void setGradationTitles  (ArrayList<String> gradation_Titles ) {
		if (gradationTitles.isEmpty() == false) {
			gradationTitles.clear();
		}

		gradationCount = gradation_Titles.size();
		for (int i=0; i<gradation_Titles.size(); i++) {
			gradationTitles.add(gradation_Titles.get(i));
		}
		
	}
	public void setBarGroupMemberCount (int bar_Group_Member_Count) {
		barGroupMemberCount = bar_Group_Member_Count;
	}
	public void setBarGraphGroupCount (int graph_Item_Values_Length, int bar_Group_Member_Count) {
		barGraphGroupCount = (graph_Item_Values_Length%bar_Group_Member_Count > 0) 
		? ((graph_Item_Values_Length/bar_Group_Member_Count)+1) : (graph_Item_Values_Length/bar_Group_Member_Count);
	}
	public void setAxisThickness (int axis_Thickness) {
		axisThickness = axis_Thickness;
	}
	public void setGradationThickness (int gradation_Thickness) {
		gradationThickness = gradation_Thickness;
	}
	public void setBackgroundColor (int background_Color) {
		backgroundColor = background_Color;
	}
	public void setAxisColor (int axis_Color) {
		axisColor = axis_Color;
	}
	public void setGradationColor (int gradation_Color) {
		gradationColor =gradation_Color;
	}
	public void setGraphItemValues  (ArrayList<Long> graph_Item_Values ) {
		if (graphItemValues.isEmpty() == false) {
			graphItemValues.clear();
		}
		
		for (int i=0; i<graph_Item_Values.size(); i++) {
			
			graphItemValues.add(graph_Item_Values.get(i));
		}
		
		long temp = 0;
		long negativeTemp = 0;
		for (int i=0; i<graphItemValues.size(); i++) {
			if (i == 0) {
				if (graphItemValues.get(i) >= 0) {
					temp = graphItemValues.get(i);
				} else {
					negativeTemp = graphItemValues.get(i);
				}
			} else {
				if (graphItemValues.get(i) >= 0) {
					temp = (temp > graphItemValues.get(i)) ? temp : graphItemValues.get(i);
				} else {
					negativeTemp = (negativeTemp < graphItemValues.get(i)) ? negativeTemp : graphItemValues.get(i);
				}
			}			
		}
		
		if (negativeTemp == 0) {
			mexGraphItemValue = temp;
		} else {
			mexGraphItemValue = (temp > (negativeTemp * (-1))) ? temp : negativeTemp * (-1);
		}
		
		if (mexGraphItemValue == 0) {
			mexGraphItemValue = 1;
		}
	}
	public void setBarTitles  (ArrayList<String> bar_Titles ) {
		if (barTitles.isEmpty() == false) {
			barTitles.clear();
		}

		for (int i=0; i<bar_Titles.size(); i++) {
			barTitles.add(bar_Titles.get(i));
		}
	}
	
	public void setDefaultGraphColors  (int[] default_Graph_Colors ) {
		defaultGraphColors = null;
		defaultGraphColors = new int[default_Graph_Colors.length];
		for (int i=0; i<default_Graph_Colors.length; i++) {
			defaultGraphColors[i] = default_Graph_Colors[i];
		}
	}

	/* =============================== GET Method =================================*/
	public ArrayList<Integer> getAxisPositions () {
		return 	axisPositions;
	}
	public int getStandardAxis () {
		return standardAxis;
	}
	public int getbarGraphMargin () {
		return barGraphMargin;
	}
	public int getbargroupAndBargroupGap  () {
		return bargroupAndBargroupGap;
	}
	public int getbarAndBarGapInBargroup  () {
		return barAndBarGapInBargroup;
	}
	public int getBarWidth  () {
		return barWidth;
	}
	public ArrayList<String> getStandardAxisTitles  () {
		return standardAxisTitles;

	}
	public int getStandardAxisTitlesCount () {
		return standardAxisTitlesCount;
	}
	public int getGradationCount () {
		return gradationCount;
	}
	public int getGradationMode () {
		return gradationMode;
	}
	public ArrayList<String> getGradationTitles  () {
		return gradationTitles;
	}
	public ArrayList<Long> getGraphItemValues () {
		return graphItemValues;	
	}
	public long getMexGraphItemValue () {
		return mexGraphItemValue;	
	}
	public ArrayList<String> getBarTitles () {
		return barTitles;

	}
	public int getBarGraphGroupCount () {
		return barGraphGroupCount;
	}
	
	public int getBarGraphWidth() {
		if ((standardAxis == Constants.BAR_AXIS_Y_LEFT || standardAxis == Constants.BAR_AXIS_Y_RIGHT || standardAxis == Constants.BAR_AXIS_Y_CENTER)) {
			return 0;
		}
		if (barGroupMemberCount == 1) {
			return (bargroupAndBargroupGap * (graphItemValues.size() + 1)) + (barWidth * graphItemValues.size()) + barGraphMargin*2;
		} else {
			return (bargroupAndBargroupGap * (barGraphGroupCount + 1)) + 
				(((barWidth * barGroupMemberCount) + ((barGroupMemberCount - 1) * barAndBarGapInBargroup)))*barGraphGroupCount + barGraphMargin*2;
		}	
	}
	public int getBarGraphHeight() {
		if ((standardAxis == Constants.BAR_AXIS_X_BOTTOM || standardAxis == Constants.BAR_AXIS_X_TOP || standardAxis == Constants.BAR_AXIS_X_CENTER)) {
			return 0;
		}
		if (barGroupMemberCount == 1) {
			return (bargroupAndBargroupGap * (graphItemValues.size() + 1)) + (barWidth * graphItemValues.size()) + barGraphMargin*2;
		} else {
			return (bargroupAndBargroupGap * (barGraphGroupCount + 1)) + 
			(((barWidth * barGroupMemberCount) + ((barGroupMemberCount - 1) * barAndBarGapInBargroup)))*barGraphGroupCount + + barGraphMargin*2;
		}
	}
	/* =============================== Methods =================================*/
	
	public void makeUserTypeGraph (ArrayList<Integer> axis_Positions, int standard_Axis, ArrayList<Long> graph_Item_Values, int bar_Group_Member_Count, 
			ArrayList<String> standard_Axis_Titles) {
		
		if (axis_Positions.size() < 1) {
			return;
		}
		
		if (graph_Item_Values.size() < 1) {
			return;
		}

		setAxisPositions(axis_Positions);
		
		setStandardAxis(standard_Axis);

		setGraphItemValues (graph_Item_Values);
		
		setBarGroupMemberCount(bar_Group_Member_Count);

		setStandardAxisTitles(standard_Axis_Titles);

		setBarGraphGroupCount(graphItemValues.size(), barGroupMemberCount);
		
	}

	/* =============================== 그래프 그리기 =================================*/
	@Override
	public void onDraw(Canvas canvas) {

		if (graphItemValues.isEmpty() == true) {
			return;
		}
		if (barAndBarGapInBargroup < 1) {
			return;
		}
		canvas.drawColor(backgroundColor);

		//그래프 축 그리기
		Paint[] axisPaint = new Paint[axisPositions.size()];
		RectF[] axisLine = new RectF[axisPositions.size()]; 
		
		if (standardAxis == Constants.BAR_AXIS_X_BOTTOM || standardAxis == Constants.BAR_AXIS_X_TOP || standardAxis == Constants.BAR_AXIS_X_CENTER) {
			axisXLength = getBarGraphWidth() - barGraphMargin*2;

		} else {
			axisYLength = getBarGraphHeight() - barGraphMargin*2;
		}
		if (axisXLength < getMeasuredWidth()) {
			axisXLength = getMeasuredWidth() - barGraphMargin*2;
		}
		if (axisYLength < getMeasuredHeight()) {
			axisYLength = getMeasuredHeight() - barGraphMargin*2;
		}

		for(int i=0; i<axisPositions.size(); i++) {
			if (axisPositions.get(i) == Constants.BAR_AXIS_X_BOTTOM) {
				axisLine[i] = new RectF(barGraphMargin, axisYLength + barGraphMargin , axisXLength + barGraphMargin, axisYLength + barGraphMargin - axisThickness);
			} else if (axisPositions.get(i) == Constants.BAR_AXIS_X_TOP) {
				axisLine[i] = new RectF(barGraphMargin, barGraphMargin, axisXLength + barGraphMargin, barGraphMargin + axisThickness);
			} else if (axisPositions.get(i) == Constants.BAR_AXIS_X_CENTER) {
				axisLine[i] = new RectF(barGraphMargin, getMeasuredHeight()/2, axisXLength + barGraphMargin, (getMeasuredHeight()/2) + axisThickness);
			} else if (axisPositions.get(i) == Constants.BAR_AXIS_Y_LEFT) {
				axisLine[i] = new RectF(barGraphMargin, barGraphMargin, barGraphMargin + axisThickness, axisYLength + barGraphMargin);
			} else if (axisPositions.get(i) == Constants.BAR_AXIS_Y_RIGHT) {
				axisLine[i] = new RectF(axisXLength + barGraphMargin, barGraphMargin , axisXLength + barGraphMargin - axisThickness, axisYLength + barGraphMargin);
			} else if (axisPositions.get(i) == Constants.BAR_AXIS_Y_CENTER) {
				axisLine[i] = new RectF(getMeasuredWidth()/2, barGraphMargin, (getMeasuredWidth()/2) + axisThickness, axisYLength + barGraphMargin);
			}
			axisPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			axisPaint[i].setColor(axisColor); 
			axisPaint[i].setStyle(Paint.Style.FILL);
			
			canvas.drawRect(axisLine[i], axisPaint[i]);
		}
		
		//눈금 그리기
		if (gradationCount > 0 && gradationMode != Constants.BAR_GRADATION_MODE_NONE) {
			
			int gradationCountTmp = gradationCount;
			if (standardAxis == Constants.BAR_AXIS_X_CENTER || standardAxis == Constants.BAR_AXIS_Y_CENTER) {
				gradationCountTmp = gradationCount * 2;
			}
			
			int gradationXLength = axisXLength;
			int gradationYLength = axisYLength;
			
		
			Paint[] gradationPaint = new Paint[gradationCountTmp];
			RectF[] gradationLine = new RectF[gradationCountTmp];
						
			int gradationTmp =0;			
			
			for (int i=0; i<gradationCountTmp; i++) {
				
				if (standardAxis == Constants.BAR_AXIS_X_BOTTOM) {
					gradationTmp = (getMeasuredHeight()/(gradationCountTmp + 1)) * (i+1);
					gradationLine[i] = new RectF(0, getMeasuredHeight()-gradationTmp , gradationXLength, getMeasuredHeight()-gradationTmp - gradationThickness);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
					
				} else if (standardAxis == Constants.BAR_AXIS_X_TOP) {
					gradationTmp = (getMeasuredHeight()/(gradationCountTmp + 1)) * (i+1);
					gradationLine[i] = new RectF(0, gradationTmp, gradationXLength, gradationTmp + gradationThickness);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
					
				} else if (standardAxis == Constants.BAR_AXIS_X_CENTER) {
					gradationTmp = (getMeasuredHeight()/(gradationCountTmp + 2)) * ((i/2)+1);
					gradationLine[i] = new RectF(0, (getMeasuredHeight()/2) - gradationTmp , gradationXLength, ((getMeasuredHeight()/2) - gradationTmp) + gradationThickness);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
					i++;
					gradationLine[i] = new RectF(0, (getMeasuredHeight()/2) + gradationTmp , gradationXLength, ((getMeasuredHeight()/2) + gradationTmp) + gradationThickness);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
					
				} else if (standardAxis == Constants.BAR_AXIS_Y_LEFT) {
					gradationTmp = (getMeasuredWidth()/(gradationCountTmp + 1)) * (i+1);
					gradationLine[i] = new RectF(gradationTmp, 0, gradationTmp + gradationThickness, gradationYLength);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
					
				} else if (standardAxis == Constants.BAR_AXIS_Y_RIGHT) {
					gradationTmp = (getMeasuredWidth()/(gradationCountTmp + 1)) * (i+1);
					gradationLine[i] = new RectF(getMeasuredWidth() - gradationTmp, 0, (getMeasuredWidth()-gradationTmp) - gradationThickness, gradationYLength);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
					
				} else if (standardAxis == Constants.BAR_AXIS_Y_CENTER) {
					gradationTmp = (getMeasuredWidth()/(gradationCountTmp + 2)) * ((i/2)+1);
					gradationLine[i] = new RectF((getMeasuredWidth()/2) - gradationTmp, 0, ((getMeasuredWidth()/2) - gradationTmp) + gradationThickness, gradationYLength);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
					i++;
					gradationLine[i] = new RectF((getMeasuredWidth()/2) + gradationTmp, 0, ((getMeasuredWidth()/2) + gradationTmp) + gradationThickness, gradationYLength);
					gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
					gradationPaint[i].setColor(gradationColor); 
					gradationPaint[i].setStyle(Paint.Style.FILL);
					canvas.drawRect(gradationLine[i], gradationPaint[i]);
				}			
			}
		}
		
		//그래프 그리기
		barPaint = new Paint[graphItemValues.size()];
		bar = new RectF[graphItemValues.size()];
		
		int tempX = bargroupAndBargroupGap + barGraphMargin;
		int tempY = bargroupAndBargroupGap;
		
		for (int i=0; i<graphItemValues.size(); i++) {
			
			if (standardAxis == Constants.BAR_AXIS_X_BOTTOM) {
				bar[i] = new RectF( axisThickness + tempX, (axisYLength + barGraphMargin) - (((axisYLength) * graphItemValues.get(i))) / (mexGraphItemValue), 
						(axisThickness + tempX) + barWidth, axisYLength + barGraphMargin - axisThickness);
				if (((i+1)%barGroupMemberCount == 0)) {
					tempX = tempX + barWidth + bargroupAndBargroupGap;
				} else {
					tempX = tempX + barWidth + barAndBarGapInBargroup;
				}

			} else if (standardAxis == Constants.BAR_AXIS_X_TOP) {
				bar[i] = new RectF( axisThickness + tempX, barGraphMargin + axisThickness, (axisThickness + tempX) + barWidth, 
						(((axisYLength + barGraphMargin) * graphItemValues.get(i)) / mexGraphItemValue));
				tempX = tempX + barWidth + bargroupAndBargroupGap;
			} else if (standardAxis == Constants.BAR_AXIS_X_CENTER) {
				if (graphItemValues.get(i) >= 0) {
					bar[i] = new RectF( axisThickness + tempX, (axisYLength/2 + barGraphMargin) - (((axisYLength/2) * graphItemValues.get(i)) / mexGraphItemValue - axisThickness), 
							(axisThickness + tempX) + barWidth, (getMeasuredHeight()/2));
				} else {
					bar[i] = new RectF( axisThickness + tempX, (axisYLength/2 + barGraphMargin) + axisThickness, (axisThickness + tempX) + barWidth, 
							(axisYLength/2 + barGraphMargin) - (((axisYLength/2) * graphItemValues.get(i)) / mexGraphItemValue));
				}				
				tempX = tempX + barWidth + bargroupAndBargroupGap;
			} else if (standardAxis == Constants.BAR_AXIS_Y_LEFT) {
				bar[i] = new RectF( barGraphMargin + axisThickness, axisYLength + barGraphMargin  - axisThickness - tempY, 
						axisXLength + barGraphMargin - ((axisXLength * graphItemValues.get(i)) / mexGraphItemValue), (axisYLength + barGraphMargin - axisThickness - tempY) - barWidth);
				tempY = tempY + barWidth + bargroupAndBargroupGap;
			} else if (standardAxis == Constants.BAR_AXIS_Y_RIGHT) {
				bar[i] = new RectF( axisXLength + barGraphMargin- (axisXLength + barGraphMargin - ((axisXLength * graphItemValues.get(i)) / mexGraphItemValue)), axisYLength + barGraphMargin - axisThickness - tempY, 
						axisXLength + barGraphMargin - axisThickness, (axisYLength + barGraphMargin - axisThickness - tempY) - barWidth);
				tempY = tempY + barWidth + bargroupAndBargroupGap;
			} else if (standardAxis == Constants.BAR_AXIS_Y_CENTER) {
				if (graphItemValues.get(i) >= 0) {
					bar[i] = new RectF( getMeasuredWidth()/2 + axisThickness, axisYLength + barGraphMargin - axisThickness - tempY, 
							(getMeasuredWidth()/2) + (((axisXLength/2) * graphItemValues.get(i)) / mexGraphItemValue), (axisYLength + barGraphMargin - axisThickness - tempY) - barWidth);
				} else {
					bar[i] = new RectF( (getMeasuredWidth()/2)- ((axisXLength/2) + (((axisXLength/2) * graphItemValues.get(i)) / mexGraphItemValue)), axisYLength + barGraphMargin - axisThickness - tempY, 
							(getMeasuredWidth()/2), (axisYLength + barGraphMargin - axisThickness - tempY) - barWidth);
				}
				tempY = tempY + barWidth + bargroupAndBargroupGap;
			}

			
			barPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			barPaint[i].setColor(defaultGraphColors[i]); 
			barPaint[i].setStyle(Paint.Style.FILL);
			
			canvas.drawRect(bar[i], barPaint[i]);
		}
		
		//타이틀 그리기
		Paint[] titlePaint = new Paint[standardAxisTitles.size()];
		
		int titleXTemp;
		if (barGroupMemberCount == 1) {
			titleXTemp = bargroupAndBargroupGap + barWidth/2 + barGraphMargin;
		} else {
			titleXTemp = bargroupAndBargroupGap + ((barWidth*barGroupMemberCount + (barGroupMemberCount-1) * barAndBarGapInBargroup))/2 + barGraphMargin;
		}
		
		
		for (int i=0; i<standardAxisTitles.size(); i++) {
			titlePaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			titlePaint[i].setColor(Color.WHITE);
			canvas.drawText(standardAxisTitles.get(i), titleXTemp, getMeasuredHeight()-5, titlePaint[i]);
			if (barGroupMemberCount == 1) {
				titleXTemp = titleXTemp + bargroupAndBargroupGap + barWidth ;
			} else {
				titleXTemp = titleXTemp + bargroupAndBargroupGap + ((barWidth*barGroupMemberCount + (barGroupMemberCount-1) * barAndBarGapInBargroup));
			}
		}		
	}
	
	
	/* =============================== 터치된 항목 찾기 =================================*/

	public int FindTouchItemID (int touchX, int touchY) {
		
		for (int i=0; i<bar.length; i++) {
			if (bar[i].contains(touchX, touchY)) {
				return i;
			}
		}
		return -1;
	}
	
	
	

}
