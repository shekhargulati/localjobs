package com.localjobs.googleapis;

public class Duration {
	public String text;

	public String value;

	@Override
	public String toString() {
		return "Duration [text=" + text + ", value=" + value + "]";
	}

	public String getText() {
		return text;
	}
	
	public String getValue() {
		return value;
	}
}
