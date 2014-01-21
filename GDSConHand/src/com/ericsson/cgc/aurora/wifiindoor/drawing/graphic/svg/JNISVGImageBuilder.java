package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.svg;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.widget.ImageView.ScaleType;

import com.libsvg.SvgDrawable;

public class JNISVGImageBuilder implements SVGImageBuilder {

	static {
		System.loadLibrary("svgandroid");
	}

	@Override
	public Bitmap generateFromFile(InputStream inputStream) {
		SvgDrawable svg = new SvgDrawable(null, inputStream);
		svg.setScaleType(ScaleType.CENTER);
		svg.onBoundsChange(new Rect(0, 0 , svg.getIntrinsicWidth(), svg.getIntrinsicHeight()));
		
		return svg.getBitmap();
	}

	@Override
	public Bitmap generateFromFile(InputStream inputStream, int width,
			int height) {
		SvgDrawable svg = new SvgDrawable(null, inputStream);
		svg.setScaleType(ScaleType.CENTER);
		svg.onBoundsChange(new Rect(0, 0 , width, height));
		
		return svg.getBitmap();
	}

}

