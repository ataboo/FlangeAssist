package com.atasoft.helpers;
import android.nfc.*;

//Keeps the figures and tables for the Unit Converter in Boilermaker Toolbox
public class ConvDataHold
{
	public static final String[] typeStrings = {"Length", "Force", "Mass", "Volume"};
	
	//values are placeholder
	private static final String[] lengthStrings = {"Meters", "Feet", "Inches"};
	private static double[] lengthRates = {1d, 0.33d, 38d};
	private static final String[] forceStrings = {"Kilonewtons", "Pounds"};
	private static double[] forceRates = {1d, 100d};
	private static final String[] massStrings = {"Kilograms", "Pounds", "Tonnes", "Long Tons"};
	private static final double[] massRates = {1d, 2.2d, 4200d, 4400d};
	private static final String[] volumeStrings = {"Litres", "Gallons", "Cubic Feet"};
	private static final double[] volumeRates = {1d, 3.2d, 0.5d};
	
	public ConvDataHold() {
	}
	
	public String[] getUnitStrings(int typeConst) {
		String[] unitStrings;
		
		switch(typeConst) {
			case 0:
				unitStrings = lengthStrings;
				break;
			case 1:
				unitStrings = forceStrings;
				break;
			case 2:
				unitStrings = massStrings;
				break;
			case 3:
				unitStrings = volumeStrings;
				break;
				
			default:
			unitStrings = new String[]{"CVD err1"};
			break;
		}
		
		return unitStrings;
	}
	
	public double getConversion(double value, int typeInt, int fromInt, int toInt){
		double[] unitRates;
		switch(typeInt) {
			case 0:
				unitRates = lengthRates;
				break;
			case 1:
				unitRates = forceRates;
				break;
			case 2:
				unitRates = massRates;
				break;
			case 3:
				unitRates = volumeRates;
				break;

			default:
				unitRates = new double[]{0d};
				break;
		}
		//TODO: Alinear conversion
		double retDouble = value * unitRates[fromInt] * unitRates[toInt];
		
		return retDouble;
	}
}
