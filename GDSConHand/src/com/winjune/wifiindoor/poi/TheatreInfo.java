package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

public class TheatreInfo extends PlaceOfInterest{
	
	private ArrayList<MovieInfo> movies;
	
	public TheatreInfo(){
		super();
		this.label = "巨幕影院";		
		movies = new ArrayList<MovieInfo>();
	}

	public ArrayList<MovieInfo> getMovies() {				
		return movies;
	}
}
