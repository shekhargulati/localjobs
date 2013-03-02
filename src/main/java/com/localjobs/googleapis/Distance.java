package com.localjobs.googleapis;

public class Distance {
	public String text;
	public String value;

	@Override
	public String toString() {
		return "Distance [text=" + text + ", value=" + value + "]";
	}
	
	public String getText() {
		return text;
	}
	
	public String getValue() {
		return value;
	}

}
