package com.winjune.wifiindoor.drawing.graphic.svg;

import java.io.InputStream;

import android.graphics.Bitmap;

public interface SVGImageBuilder {
	
	public static SVGImageBuilder instance = new JNISVGImageBuilder();

	public abstract Bitmap generateFromFile(InputStream inputStream);

	public abstract Bitmap generateFromFile(InputStream inputStream, int width,
			int height);

}