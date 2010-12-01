package com.fletamuto.common.control.fmgraph;

import java.util.ArrayList;
import java.math.*;

import com.fletamuto.common.control.fmgraph.Constants;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;


public class LineGraph extends View {
	
	private int currentAreaWidth = getMeasuredWidth(); //그래프가 그려질 영역의 Width
	private int currentAreaHeight = getMeasuredHeight(); //그래프가 그려질 영역의 Height
	
	private int lineGraphMargin = Constants.LINE_DEFAULT_MARGIN;
		
	private ArrayList<Integer> axisPositions = new ArrayList<Integer>(); //그릴 축
	private int standardAxis = Constants.BAR_AXIS_X_BOTTOM; //그래프가 시작할 기준이 되는 축
	
	private int pointAndPointGap = Constants.LINE_DEFAULT_POINT_AND_POINT_GAP; //점과 점 사이 거리
	private int lineThickness = Constants.LINE_DEFAULT_LINE_THICKNESS; // line 두께
	
	private ArrayList<String> standardAxisTitles = new ArrayList<String>(); //그래프가 놓여질 축의 타이틀 값
	private int standardAxisTitlesCount;
	
	private int gradationCount = Constants.LINE_DEFAULT_GRADATION_COUNT; //눈금 갯수
//	private int gradationMode = Constants.LINE_GRADATION_MODE_NONE; //눈금 모드
	
	private ArrayList<String> gradationTitles = new ArrayList<String>(); //눈금 타이틀
	
	private int axisThickness = Constants.LINE_DEFAULT_AXIS_THICKNESS; //축의 두께
	private int gradationThickness = Constants.LINE_DEFAULT_GRADATION_THICKNESS; //눈금 두께
	
	private int backgroundColor = Color.BLACK;
	private int axisColor = Color.WHITE;
	private int gradationColor = Color.WHITE;
	
	private ArrayList<Long> graphItemValues = new ArrayList<Long>(); //그래프로 그려질 항목들에 대한 값
	private ArrayList<Long> graphItemValues2 = new ArrayList<Long>();
	private ArrayList<Long> graphItemValues3 = new ArrayList<Long>();
	
	private long mexGraphItemValue; //그래프로 그려질 항목의 최고 값
	private boolean haveNegativeInGraphItemValue = false; //항목 값 중 음수 값이 있는지 
	
	private ArrayList<String> pointTitles = new ArrayList<String>(); //point 에 표시 될 타이틀 값들
	
	private int[] defaultGraphColors = new int[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY, Color.CYAN,
		Color.GREEN, Color.MAGENTA, Color.DKGRAY, Color.WHITE, Color.LTGRAY, Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY, Color.CYAN,
		Color.GREEN, Color.MAGENTA, Color.DKGRAY, Color.WHITE, Color.LTGRAY, Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY, Color.CYAN,
		Color.GREEN, Color.MAGENTA, Color.DKGRAY, Color.WHITE, Color.LTGRAY};
	
	private int axisXLength; //X축들의 길이
	private int axisYLength; //Y축들의 길이
	
	private long gradationMax; //눈금 최고 값
	private long gradationMin = 0; //눈금 최저 값
	
	private int pointSize = Constants.LINE_DEFAULT_POINT_SIZE; //점 크기 설정
	
	//그래프 그리기 위한 사각형과 페인트 객첵
	private Paint[] pointPaint;
	private RectF[] point;
	
	private Paint[] pointPaint2;
	private RectF[] point2;
	
	private Paint[] pointPaint3;
	private RectF[] point3;
	
	private RectF[] pointArea; //터치 영역
	
		
	/* =============================== 생성자 =================================*/
	public LineGraph (Context context) {
		super(context);		
	}
	
	public LineGraph (Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public LineGraph (Context context, AttributeSet attrs, int defaultStyle) {
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
	
	public void setLineGraphMargin (int line_Graph_Margin) {
		lineGraphMargin = line_Graph_Margin;
	}

	public void setPointAndPointGap  (int point_And_Point_Gap) {
		pointAndPointGap = point_And_Point_Gap;
	}
	
	public void setBarWidth  (int line_Thickness) {
		lineThickness = line_Thickness;
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

	public void setGradationTitles  (ArrayList<String> gradation_Titles ) {
		if (gradationTitles.isEmpty() == false) {
			gradationTitles.clear();
		}

		gradationCount = gradation_Titles.size();
		for (int i=0; i<gradation_Titles.size(); i++) {
			gradationTitles.add(gradation_Titles.get(i));
		}
		
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
	public void setGraphItemValues  (ArrayList<Long> graph_Item_Values, ArrayList<Long> graph_Item_Values_2, ArrayList<Long> graph_Item_Values_3 ) {
		if (graphItemValues.isEmpty() == false) {
			graphItemValues.clear();
		}
		if (graphItemValues2.isEmpty() == false) {
			graphItemValues2.clear();
		}
		if (graphItemValues3.isEmpty() == false) {
			graphItemValues3.clear();
		}
		if (graph_Item_Values != null) {
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
			
			long maxGraphItemValueTemp;
			if (negativeTemp == 0) {
				maxGraphItemValueTemp = temp;
			} else {
				haveNegativeInGraphItemValue = true;
				maxGraphItemValueTemp = (temp > (negativeTemp * (-1))) ? temp : negativeTemp * (-1);
			}
			mexGraphItemValue = maxGraphItemValueTemp;
		}
		
		if (graph_Item_Values_2 != null) {
			for (int i=0; i<graph_Item_Values_2.size(); i++) {
				
				graphItemValues2.add(graph_Item_Values_2.get(i));
			}
			
			long temp = 0;
			long negativeTemp = 0;
			for (int i=0; i<graphItemValues2.size(); i++) {
				if (i == 0) {
					if (graphItemValues2.get(i) >= 0) {
						temp = graphItemValues2.get(i);
					} else {
						negativeTemp = graphItemValues2.get(i);
					}
				} else {
					if (graphItemValues2.get(i) >= 0) {
						temp = (temp > graphItemValues2.get(i)) ? temp : graphItemValues2.get(i);
					} else {
						negativeTemp = (negativeTemp < graphItemValues2.get(i)) ? negativeTemp : graphItemValues2.get(i);
					}
				}			
			}
			long maxGraphItemValueTemp;
			if (negativeTemp == 0) {
				maxGraphItemValueTemp = temp;
			} else {
				haveNegativeInGraphItemValue = true;
				maxGraphItemValueTemp = (temp > (negativeTemp * (-1))) ? temp : negativeTemp * (-1);
			}
			mexGraphItemValue = (mexGraphItemValue > maxGraphItemValueTemp) ? mexGraphItemValue : maxGraphItemValueTemp;
		}
		
		if (graph_Item_Values_3 != null) {
			for (int i=0; i<graph_Item_Values_3.size(); i++) {
				
				graphItemValues3.add(graph_Item_Values_3.get(i));
			}
			
			long temp = 0;
			long negativeTemp = 0;
			for (int i=0; i<graphItemValues3.size(); i++) {
				if (i == 0) {
					if (graphItemValues3.get(i) >= 0) {
						temp = graphItemValues3.get(i);
					} else {
						negativeTemp = graphItemValues3.get(i);
					}
				} else {
					if (graphItemValues3.get(i) >= 0) {
						temp = (temp > graphItemValues3.get(i)) ? temp : graphItemValues3.get(i);
					} else {
						negativeTemp = (negativeTemp < graphItemValues3.get(i)) ? negativeTemp : graphItemValues3.get(i);
					}
				}			
			}
			
			long maxGraphItemValueTemp;
			if (negativeTemp == 0) {
				maxGraphItemValueTemp = temp;
			} else {
				haveNegativeInGraphItemValue = true;
				maxGraphItemValueTemp = (temp > (negativeTemp * (-1))) ? temp : negativeTemp * (-1);
			}
			mexGraphItemValue = (mexGraphItemValue > maxGraphItemValueTemp) ? mexGraphItemValue : maxGraphItemValueTemp;
		}		
		if (mexGraphItemValue == 0) {
			mexGraphItemValue = 10;
		}
	}
	public void setPointTitles  (ArrayList<String> point_Titles ) {
		if (pointTitles.isEmpty() == false) {
			pointTitles.clear();
		}

		for (int i=0; i<point_Titles.size(); i++) {
			pointTitles.add(point_Titles.get(i));
		}
	}
	
	public void setDefaultGraphColors  (int[] default_Graph_Colors ) {
		defaultGraphColors = null;
		defaultGraphColors = new int[default_Graph_Colors.length];
		for (int i=0; i<default_Graph_Colors.length; i++) {
			defaultGraphColors[i] = default_Graph_Colors[i];
		}
	}
	
	public void setPointSize (int point_Size) {
		pointSize = point_Size;
	}

	/* =============================== GET Method =================================*/
	public ArrayList<Integer> getAxisPositions () {
		return 	axisPositions;
	}
	public int getStandardAxis () {
		return standardAxis;
	}
	public int getLineGraphMargin () {
		return lineGraphMargin;
	}
	public int getPointAndPointGap  () {
		return pointAndPointGap;
	}

	public int getLineThickness  () {
		return lineThickness;
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

	public ArrayList<String> getGradationTitles  () {
		return gradationTitles;
	}
	public ArrayList<Long> getGraphItemValues () {
		return graphItemValues;	
	}
	public long getMexGraphItemValue () {
		return mexGraphItemValue;	
	}
	public ArrayList<String> getPointTitles () {
		return pointTitles;
	}	
	public int getPointSize () {
		return pointSize;
	}
	public int getLineGraphWidth() {
		return ((graphItemValues.size() + 1) * pointAndPointGap) + lineGraphMargin*2;
	}
	public int getLineGraphHeight() {
		return getMeasuredHeight();
	}
	
	/* =============================== Methods =================================*/
	
	public void makeUserTypeGraph (ArrayList<Long> graph_Item_Values, ArrayList<Long> graph_Item_Values_2, 
			ArrayList<Long> graph_Item_Values_3, ArrayList<String> standard_Axis_Titles) {
		 
		if (graph_Item_Values.size() < 1) {
			return;
		}
		
		setGraphItemValues (graph_Item_Values, graph_Item_Values_2, graph_Item_Values_3);
		
		setStandardAxisTitles(standard_Axis_Titles);

	}

	/* =============================== 그래프 그리기 =================================*/
	@Override
	public void onDraw(Canvas canvas) {

		if (graphItemValues.isEmpty() == true) {
			return;
		}

		canvas.drawColor(backgroundColor);
		
		//그래프 축 그리기
		Paint[] axisPaint = new Paint[2];
		RectF[] axisLine = new RectF[2]; 
		

		axisXLength = ((graphItemValues.size()+1) * pointAndPointGap);
		axisXLength = (axisXLength > ((graphItemValues2.size()+1) * pointAndPointGap)) ? axisXLength : ((graphItemValues2.size()+1) * pointAndPointGap);
		axisXLength = (axisXLength > ((graphItemValues3.size()+1) * pointAndPointGap)) ? axisXLength : ((graphItemValues3.size()+1) * pointAndPointGap);
			

		if (axisXLength < getMeasuredWidth() - lineGraphMargin*2) {
			axisXLength = getMeasuredWidth() - lineGraphMargin*2;
		}

		axisYLength = getMeasuredHeight() - lineGraphMargin*2;
		
		int maxGraphItemValuesCount;
		
		maxGraphItemValuesCount = graphItemValues.size();
		maxGraphItemValuesCount = (maxGraphItemValuesCount > graphItemValues2.size()) ? maxGraphItemValuesCount : graphItemValues2.size();
		maxGraphItemValuesCount = (maxGraphItemValuesCount > graphItemValues3.size()) ? maxGraphItemValuesCount : graphItemValues3.size();
		
		pointArea = new RectF[maxGraphItemValuesCount];
		
		for (int i=0; i<maxGraphItemValuesCount; i++) {
			pointArea[i] = new RectF(lineGraphMargin+pointAndPointGap*(i+1)-pointAndPointGap/2, lineGraphMargin,
					lineGraphMargin+pointAndPointGap*(i+1)+pointAndPointGap/2, axisYLength + lineGraphMargin);
		}
		
		for(int i=0;i<2;i++) {
			if (i == 0) {
				axisLine[i] = new RectF(lineGraphMargin, axisYLength + lineGraphMargin , axisXLength + lineGraphMargin, axisYLength + lineGraphMargin - axisThickness);
			} else if (i == 1) {
				axisLine[i] = new RectF(lineGraphMargin, lineGraphMargin, lineGraphMargin + axisThickness, axisYLength + lineGraphMargin);
			}
		
			axisPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			axisPaint[i].setColor(axisColor); 
			axisPaint[i].setStyle(Paint.Style.FILL);
			
			canvas.drawRect(axisLine[i], axisPaint[i]);
		}

		//눈금 그리기
		int zeroCount = 1;
		
		while (mexGraphItemValue/(long)Math.pow(10,zeroCount) != 0 ) {			
			zeroCount++;			
		}
		zeroCount = zeroCount - 1;

		if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) > 8 ) {
			gradationCount = 10;	
			gradationMax = 10 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		} else if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) > 6 && mexGraphItemValue/((int)Math.pow(10,zeroCount)) <= 8) {
			gradationCount = 8;
			gradationMax = 8 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		} else if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) > 5 && mexGraphItemValue/((int)Math.pow(10,zeroCount)) <= 6) {
			gradationCount = 12;
			gradationMax = 6 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		} else if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) > 4 && mexGraphItemValue/((int)Math.pow(10,zeroCount)) <= 5) {
			gradationCount = 10;
			gradationMax = 5 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		} else if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) > 3 && mexGraphItemValue/((int)Math.pow(10,zeroCount)) <= 4) {
			gradationCount = 8;
			gradationMax = 4 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		} else if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) > 2 && mexGraphItemValue/((int)Math.pow(10,zeroCount)) <= 3) {
			gradationCount = 12;
			gradationMax = 3 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		} else if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) > 1 && mexGraphItemValue/((int)Math.pow(10,zeroCount)) <= 2) {
			gradationCount = 8;
			gradationMax = 2 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		} else if (mexGraphItemValue/((long)Math.pow(10,zeroCount)) == 1 && mexGraphItemValue%((long)Math.pow(10,zeroCount)) == 0) {
			gradationCount = 10;
			gradationMax = 1 * (long)Math.pow(10,zeroCount);
			if (haveNegativeInGraphItemValue == true) {				
				gradationMin = gradationMax *(-1);
			} 
		}

		int gradationXLength = axisXLength;
		int gradationYLength = axisYLength;
			
		
		Paint[] gradationPaint = new Paint[gradationCount];
		RectF[] gradationLine = new RectF[gradationCount];
					
		int gradationTmp =0;			
		
		for (int i=0; i<gradationCount; i++) {
			gradationTmp = (gradationYLength/(gradationCount)) * i;
			gradationLine[i] = new RectF(lineGraphMargin, gradationTmp + lineGraphMargin , gradationXLength + lineGraphMargin, gradationTmp + lineGraphMargin - gradationThickness);
			gradationPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			gradationPaint[i].setColor(gradationColor); 
			gradationPaint[i].setStyle(Paint.Style.FILL);
			canvas.drawRect(gradationLine[i], gradationPaint[i]);					
			
		}

		//X축 타이틀 그리기
		Paint[] titleXPaint = new Paint[standardAxisTitles.size()];
		
		for (int i=0; i<standardAxisTitles.size(); i++) {
			titleXPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			titleXPaint[i].setColor(Color.WHITE);
			canvas.drawText(standardAxisTitles.get(i), (pointAndPointGap*(i+1)) + lineGraphMargin - pointSize, axisYLength + (lineGraphMargin*2) -5, titleXPaint[i]);
		}	
		
		//Y축 타이틀 그리기
		Paint[] titleYPaint = new Paint[gradationCount];
		
		for (int i=0; i<gradationCount; i++) {
			titleYPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			titleYPaint[i].setColor(Color.WHITE);
			canvas.drawText(String.valueOf(gradationMax - ((gradationMax - gradationMin)/gradationCount) * i), 2, 
					((gradationYLength/(gradationCount)) * i) + lineGraphMargin + pointSize,titleYPaint[i]);
		}	

		
		//그래프 그리기
		pointPaint = new Paint[graphItemValues.size()];
		point = new RectF[graphItemValues.size()];		

		for (int i=0; i<graphItemValues.size(); i++) {
			
			if (haveNegativeInGraphItemValue == true) {
				point[i] = new RectF( lineGraphMargin + (pointAndPointGap*(i+1)) - pointSize, gradationYLength - (((graphItemValues.get(i) + gradationMax) * gradationYLength) /(gradationMax - gradationMin)) + lineGraphMargin - gradationThickness - pointSize, 
						lineGraphMargin + (pointAndPointGap*(i+1)) + pointSize, gradationYLength -(((graphItemValues.get(i) + gradationMax) * gradationYLength) /(gradationMax - gradationMin)) + lineGraphMargin - gradationThickness + pointSize);
			} else {
				point[i] = new RectF( lineGraphMargin + (pointAndPointGap*(i+1)) - pointSize, gradationYLength - ((graphItemValues.get(i) * gradationYLength) / gradationMax) + lineGraphMargin - gradationThickness - pointSize, 
						lineGraphMargin + (pointAndPointGap*(i+1)) + pointSize, gradationYLength - ((graphItemValues.get(i) * gradationYLength) / gradationMax) + lineGraphMargin - gradationThickness + pointSize);
			}

			pointPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			pointPaint[i].setColor(defaultGraphColors[0]); 
			pointPaint[i].setStyle(Paint.Style.FILL);
			
			canvas.drawRect(point[i], pointPaint[i]);
			
			if (i > 0) {
				canvas.drawLine(point[i-1].centerX(), point[i-1].centerY(), point[i].centerX(), point[i].centerY(), pointPaint[i]);
			}
		}
		
		
		if (graphItemValues2.isEmpty() == false) {
			pointPaint2 = new Paint[graphItemValues2.size()];
			point2 = new RectF[graphItemValues2.size()];		

			for (int i=0; i<graphItemValues2.size(); i++) {
							
				if (haveNegativeInGraphItemValue == true) {
					point2[i] = new RectF( lineGraphMargin + (pointAndPointGap*(i+1)) - pointSize, gradationYLength - (((graphItemValues2.get(i) + gradationMax) * gradationYLength) /(gradationMax - gradationMin)) + lineGraphMargin - gradationThickness - pointSize, 
							lineGraphMargin + (pointAndPointGap*(i+1)) + pointSize, gradationYLength -(((graphItemValues2.get(i) + gradationMax) * gradationYLength) /(gradationMax - gradationMin)) + lineGraphMargin - gradationThickness + pointSize);
				} else {
					point2[i] = new RectF( lineGraphMargin + (pointAndPointGap*(i+1)) - pointSize, gradationYLength - ((graphItemValues2.get(i) * gradationYLength) / gradationMax) + lineGraphMargin - gradationThickness - pointSize, 
							lineGraphMargin + (pointAndPointGap*(i+1)) + pointSize, gradationYLength - ((graphItemValues2.get(i) * gradationYLength) / gradationMax) + lineGraphMargin - gradationThickness + pointSize);
				}
				
				pointPaint2[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
				pointPaint2[i].setColor(defaultGraphColors[1]); 
				pointPaint2[i].setStyle(Paint.Style.FILL);
				
				canvas.drawRect(point2[i], pointPaint2[i]);
				
				if (i > 0) {
					canvas.drawLine(point2[i-1].centerX(), point2[i-1].centerY(), point2[i].centerX(), point2[i].centerY(), pointPaint2[i]);
				}
			}
		}
		
		if (graphItemValues3.isEmpty() == false) {
			pointPaint3 = new Paint[graphItemValues3.size()];
			point3 = new RectF[graphItemValues3.size()];		

			for (int i=0; i<graphItemValues3.size(); i++) {
							
				if (haveNegativeInGraphItemValue == true) {
					point3[i] = new RectF( lineGraphMargin + (pointAndPointGap*(i+1)) - pointSize, gradationYLength - (((graphItemValues3.get(i) + gradationMax) * gradationYLength) /(gradationMax - gradationMin)) + lineGraphMargin - gradationThickness - pointSize, 
							lineGraphMargin + (pointAndPointGap*(i+1)) + pointSize, gradationYLength -(((graphItemValues3.get(i) + gradationMax) * gradationYLength) /(gradationMax - gradationMin)) + lineGraphMargin - gradationThickness + pointSize);
				} else {
					point3[i] = new RectF( lineGraphMargin + (pointAndPointGap*(i+1)) - pointSize, gradationYLength - ((graphItemValues3.get(i) * gradationYLength) / gradationMax) + lineGraphMargin - gradationThickness - pointSize, 
							lineGraphMargin + (pointAndPointGap*(i+1)) + pointSize, gradationYLength - ((graphItemValues3.get(i) * gradationYLength) / gradationMax) + lineGraphMargin - gradationThickness + pointSize);
				}
				
				pointPaint3[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
				pointPaint3[i].setColor(defaultGraphColors[2]); 
				pointPaint3[i].setStyle(Paint.Style.FILL);
				
				canvas.drawRect(point3[i], pointPaint3[i]);
				
				if (i > 0) {
					canvas.drawLine(point3[i-1].centerX(), point3[i-1].centerY(), point3[i].centerX(), point3[i].centerY(), pointPaint3[i]);
				}
			}
		}
		
		
		
	}
	
	
	/* =============================== 터치된 항목 찾기 =================================*/

	
	public int FindTouchItemID (int touchX, int touchY) {
		
		for (int i=0; i<pointArea.length; i++) {

			if (pointArea[i].contains(touchX, touchY)) {
				return i;
			}
		}

		return -1;
	}
	
	
	
	

}
