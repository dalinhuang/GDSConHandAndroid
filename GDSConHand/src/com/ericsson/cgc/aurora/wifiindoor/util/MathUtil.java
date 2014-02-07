package com.ericsson.cgc.aurora.wifiindoor.util;

public class MathUtil {
	// 两个坐标系平行的矩形相交 = X轴投影相交 & Y轴投影相交
	// Rect(left1, top1, right1, bottom1), Rect(left2, top2, right2, bottom2) 
	public static boolean hasCrossArea(float left1, float top1, float right1, float bottom1, float left2, float top2, float right2, float bottom2) {
		return (isShadowCrossed(left1, right1, left2, right2) && isShadowCrossed(top1, bottom1, top2, bottom2));
	}
		
	// 判断X或Y轴投影是否相交, assume a < b, line (a1, b1), line (a2, b2)
	private static boolean isShadowCrossed(float a1, float b1, float a2, float b2) {
		return (isPointInnerLine(a1, a2, b2) || isPointInnerLine(b1, a2, b2) || isPointInnerLine(a2, a1, b1) || isPointInnerLine(b2, a1, b1));
	}
	
	// 判断X或Y轴投影点在另一条投影线上, C inner line(a, b)
	private static boolean isPointInnerLine(float c, float a, float b) {
		return (c >= a && c <= b);
	}
}
