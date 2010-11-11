package com.fletamuto.common.control.fmgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PieGraph extends View {
	
	private int centerPointX = getMeasuredWidth()/2; //원점 X
	private int centerPointY = getMeasuredHeight()/2; //원점 Y
	private int radius = 0; //반지름
	private int bgcolor = Color.BLACK; //background color
	private int graphPadding = Constants.PIE_DEFAULT_GRAPH_PADDING; //그래프 여백
	private int boundaryLine = Constants.PIE_DEFAULT_GRAPH_BOUNDARY_THICKNESS; //경계 라인 두께
	private int[] defaultGraphColors = new int[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY, Color.CYAN,
			Color.GREEN, Color.MAGENTA, Color.DKGRAY, Color.TRANSPARENT, Color.LTGRAY};
	
	private PieGraphItem[] pieGraphItem; //아이템 항목
	
	//생성자
	public PieGraph (Context context) {
		super(context);		
	}
	
	public PieGraph (Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PieGraph (Context context, AttributeSet attrs, int defaultStyle) {
		super (context, attrs, defaultStyle);
	}
	
	//사용자 위젯 생성을 위해
	protected void onMeasure (int wMeasureSpec, int hMeasureSpec) {
		int measuredHeight = measureHeight(hMeasureSpec);
		int measuredWidth = measureWidth(wMeasureSpec);
		
		setMeasuredDimension(measuredWidth, measuredHeight);
	}
	
	private int measureHeight (int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		int result = 0;

		if(specMode == MeasureSpec.UNSPECIFIED) {
			result = 200;
		} else {
			result = specSize;
		}
		return result;
	}
	
	private int measureWidth (int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		int result = 0;

		if(specMode == MeasureSpec.UNSPECIFIED) {
			result = 200;
		} else {
			result = specSize;
		}
		
		return result;
	}
	
	//아이템 값 설정
	public void setItemValues (long[] item_Values) {
		pieGraphItem = new PieGraphItem[item_Values.length];
		
		long sum = 0;
		
		for (int i=0; i<item_Values.length; i++) {
			pieGraphItem[i] = new PieGraphItem();
			pieGraphItem[i].itemValue = item_Values[i];
			sum += pieGraphItem[i].itemValue;	
			
			pieGraphItem[i].itemColor = defaultGraphColors[i%10];
			pieGraphItem[i].itemIDX = i;
		}
		
		for (int i=0; i<pieGraphItem.length; i++) {
			pieGraphItem[i].itemAngle = (float)(pieGraphItem[i].itemValue * 360) / sum;
		}		
	}
	
	public void setItemValuesAndColors (long[] item_Values, int[] item_Colors) {
		pieGraphItem = new PieGraphItem[item_Values.length];
		
		long sum = 0;
		
		for (int i=0; i<item_Values.length; i++) {
			pieGraphItem[i] = new PieGraphItem();
			pieGraphItem[i].itemValue = item_Values[i];
			sum += pieGraphItem[i].itemValue;	
			
			pieGraphItem[i].itemColor = item_Colors[i];
			pieGraphItem[i].itemIDX = i;
		}
		
		for (int i=0; i<pieGraphItem.length; i++) {
			pieGraphItem[i].itemAngle = (float)(pieGraphItem[i].itemValue * 360) / sum;
		}		
	}
/*	
	public void setItemDgrees (float[] id) {
		
		int sum = 0;
		graphItem = new GraphItem[id.length];
		
		for (int i=0; i<id.length; i++) {
			graphItem[i] = new GraphItem();
			graphItem[i].itemDegree = id[i];
			sum += graphItem[i].itemDegree;
						
			graphItem[i].itemValue = (long)graphItem[i].itemDegree;
			
			graphItem[i].itemColor = defaultGraphColors[i];
			
			if (sum > 360) {
				sum -= graphItem[i].itemDegree;
				graphItem[i].itemDegree = 360 - sum;
				graphItem[i].itemValue = (long)graphItem[i].itemDegree;
				sum += graphItem[i].itemDegree;
				break;
			}
		}
		
		if (sum < 360) {
			graphItem[id.length].itemDegree = 360 - sum;
			graphItem[id.length].itemValue = (long)graphItem[id.length].itemDegree;
			graphItem[id.length].itemColor = defaultGraphColors[id.length];
		}
	}
*/
	public void setCenterPoint (int centerPoint_X, int centerPoint_Y) {
		centerPointX = centerPoint_X;
		centerPointY = centerPoint_Y;
	}
	
	public void setRadius (int pie_Radius) {
		radius = pie_Radius;
	}
	
	public void setBgColor (int background_Color) {
		bgcolor = background_Color;
	}
	
	public void setGraphPadding (int graph_Padding) {
		graphPadding = graph_Padding;
	}
	
	public void setBoundaryLine (int boundary_Line) {
		boundaryLine = boundary_Line;
	}
	
	
	public void changeItemValue (int itemIndex, long item_Value) {
		pieGraphItem[itemIndex].itemValue = item_Value;
	}
	
	public void changeItemColor (int itemIndex, int item_Color) {
		pieGraphItem[itemIndex].itemColor = item_Color;
	}
	
	public void changeItemValueAndColor (int itemIndex, long item_Value, int item_Color) {
		pieGraphItem[itemIndex].itemValue = item_Value;
		pieGraphItem[itemIndex].itemColor = item_Color;		
	}
	
	
	
	public long[] getItemValues() {
		long[] itemValues = new long[pieGraphItem.length];
		
		for(int i=0; i<pieGraphItem.length; i++) {
			itemValues[i] = pieGraphItem[i].itemValue;
		}
		return itemValues;
	}
	
	public float[] getItemDgrees() {
		float[] itemAngles = new float[pieGraphItem.length];
		
		for(int i=0; i<pieGraphItem.length; i++) {
			itemAngles[i] = pieGraphItem[i].getItemAngle();
		}
		return itemAngles;
	}
	
	public int[] getItemColors() {
		int[] item_Colors = new int[pieGraphItem.length];
		
		for(int i=0; i<pieGraphItem.length; i++) {
			item_Colors[i] = pieGraphItem[i].itemColor;
		}
		return item_Colors;
	}
	
	public long getItemValue (int itemIDX) {
		return pieGraphItem[itemIDX].itemValue;
	}
	
	public float getItemDegree (int itemIDX) {
		return pieGraphItem[itemIDX].getItemAngle();
	}
	
	public int getItemColor (int itemIDX) {
		return pieGraphItem[itemIDX].getItemColor();
	}

	public int getCenterPointX () {
		return centerPointX;
	}
	
	public int getCenterPointY () {
		return centerPointY;
	}
	
	public int getRadius () {
		return radius;
	}
	
	public int getBgColor () {
		return bgcolor;
	}
	
	public int getGraphPadding() {
		return graphPadding;
	}
	
	public int getBoundaryLine() {
		return boundaryLine;
	}
	
	
	
	@Override
	public void onDraw(Canvas canvas) {
		float startDegree = 0;
		
		if (pieGraphItem == null) {
			return;
		}
		
		if (centerPointX == 0 || centerPointY == 0) {
			centerPointX = getMeasuredWidth()/2;
			centerPointY = getMeasuredHeight()/2;
		}
		
		if (radius == 0) {
			radius = Math.min(centerPointX, centerPointY) - graphPadding - boundaryLine;
		}
		
		canvas.drawColor(bgcolor);
		
		Paint[] piePaint = new Paint[pieGraphItem.length];
		RectF r = new RectF(centerPointX - radius, centerPointY - radius, centerPointX + radius, centerPointY + radius);
		for (int i=0; i<pieGraphItem.length; i++) {
			piePaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			piePaint[i].setStrokeWidth(boundaryLine);
			piePaint[i].setColor(Color.WHITE);		
			piePaint[i].setStyle(Paint.Style.STROKE);
			canvas.drawArc(r, startDegree -90, pieGraphItem[i].itemAngle, true, piePaint[i]);
			piePaint[i].setColor(pieGraphItem[i].itemColor);
			piePaint[i].setStyle(Paint.Style.FILL);
			canvas.drawArc(r, startDegree -90, pieGraphItem[i].itemAngle, true, piePaint[i]);
			startDegree += pieGraphItem[i].itemAngle;
		}			
	}
	
	public int FindTouchItemID (int touchX, int touchY) {
		int sumDegree = 0;
		Double degreeAngle, radianAngle, pieX, pieY;

		degreeAngle = 180 - Math.atan2(touchX - centerPointX, touchY - centerPointY)*(180/Math.PI);
        radianAngle = degreeAngle *((2 * Math.PI)/360);
        pieX = centerPointX + (Math.sin(radianAngle) * (radius));
        pieY = centerPointY - (Math.cos(radianAngle) * (radius));
        
		for (int i=0; i<pieGraphItem.length; i++) {
			sumDegree += pieGraphItem[i].itemAngle;
			Log.d("jptest10", "[" + i+ "]sumDegree = " + sumDegree);
			if (sumDegree > degreeAngle) {
				if (touchX > centerPointX && touchY > centerPointY) {
					if (touchX < pieX && touchY < pieY) {
						return i;
					}					
				} else if (touchX > centerPointX && touchY < centerPointY) {
					if (touchX < pieX && touchY > pieY) {
						return i;
					}					
				} else if (touchX < centerPointX && touchY > centerPointY){
					if (touchX > pieX && touchY < pieY) {
						return i;
					}					
				} else if (touchX < centerPointX && touchY < centerPointY) {
					if (touchX > pieX && touchY > pieY) {
						return i;
					}					
				} else if (touchX == centerPointX && touchY > centerPointY) {
					if (touchY < (centerPointY + (radius + boundaryLine))) {
						return i;
					}					
				} else if (touchX == centerPointX && touchY < centerPointY) {
					if (touchY > (centerPointY - (radius + boundaryLine))) {
						return i;
					}					
				} else if (touchX > centerPointX && touchY == centerPointY) {
					if (touchX < (centerPointX + (radius + boundaryLine))) {
						return i;
					}					
				} else if (touchX < centerPointX && touchY == centerPointY) {
					if (touchX > (centerPointX - (radius + boundaryLine))) {
						return i;
					}					
				}
				
			}
		}
		return -1;
	}

}
