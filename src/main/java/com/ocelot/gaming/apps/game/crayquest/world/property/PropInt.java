package com.ocelot.gaming.apps.game.crayquest.world.property;

public class PropInt extends Prop {

	private int value = 0;
	private int minValue = 0;
	private int maxValue = 0;

	public int getValue() {
		return value;
	}
	
	public int getMinValue() {
		return minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
}