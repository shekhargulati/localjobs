package com.localjobs.service;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class CoordinateFinder {

	/**
	 * Returns a double array of which first is longitude and second is latitude
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public double[] coordinates(String location) throws Exception {
		if(StringUtils.isBlank(location)){
			return new double[]{};
		}
		
		location = location.trim().replace(" ", "%20");
		Document document = Jsoup.connect(
				"http://www.findlatitudeandlongitude.com/?loc=" + location)
				.get();

		String text = document.text();

		if (StringUtils.isBlank(text)) {
			return new double[] {};
		}

		int indexOf = StringUtils.indexOf(text,
				"Selected Location (Approximate)");
		if (indexOf == -1) {
			return new double[] {};
		}
		String subString = StringUtils.substring(text, 0, indexOf);
		int lastLatitudeIndex = subString.lastIndexOf("Latitude");
		String latitudeLongitudeString = StringUtils.substring(subString, lastLatitudeIndex);
		String[] latitudeLongitudeArray = latitudeLongitudeString.split("\\s");
		double[] coordinates = new double[2];
		int i = 0;
		for (String part : latitudeLongitudeArray) {
			String coordinate = part.substring(0, part.length() - 1).split(":")[1];
			coordinates[i] = Double.parseDouble(coordinate);
			i++;
		}
		ArrayUtils.reverse(coordinates);
		return coordinates;
	}
	
}
