package com.atasoft.helpers;
import java.util.*;

//Keeps the figures and tables for the Unit Converter in Boilermaker Toolbox
public class ConvDataHold
{
	public class ConversionType{  
		public String name;
		public String[] unitNames;
		public HashMap<String, Double> conversions;
		public ConversionType(String name, String[] units, double[] values){
			this.name = name;
			this.unitNames = units;
			this.conversions = new HashMap<String, Double>(units.length);
			for(int i=0; i < units.length; i++){
				this.conversions.put(units[i], values[i]);
			}
		}
	}
	
	public static final String[] typeStrings = {"Length", "Force", "Mass", "Volume"};
	private HashMap<String, ConversionType> typeHash;
	//values are placeholder
	private static final String[] lengthStrings = {"Meters", "Feet", "Inches"};
	private static double[] lengthRates = {1d, 0.33d, 38d};
	private static final String[] forceStrings = {"Kilonewtons", "Pounds"};
	private static double[] forceRates = {1d, 100d};
	private static final String[] massStrings = {"Kilograms", "Pounds", "Tonnes", "Long Tons"};
	private static double[] massRates = {1d, 2.2d, 4200d, 4400d};
	private static final String[] volumeStrings = {"Litres", "Gallons", "Cubic Feet"};
	private static double[] volumeRates = {1d, 3.2d, 0.5d};
	
	public ConvDataHold() {
		this.typeHash = new HashMap<String, ConversionType>(typeStrings.length);
		ConversionType newType = new ConversionType(typeStrings[0], lengthStrings, lengthRates);
		typeHash.put(newType.name, newType);
		newType = new ConversionType(typeStrings[1], forceStrings, forceRates);
		typeHash.put(newType.name, newType);
		newType = new ConversionType(typeStrings[2], massStrings, massRates);
		typeHash.put(newType.name, newType);
		newType = new ConversionType(typeStrings[3], volumeStrings, volumeRates);
		typeHash.put(newType.name, newType);
	}
	
	public String[] getUnitNames(String typeKey){
		String[] retString = typeHash.get(typeKey).unitNames;
		return retString;
	}
	
	public double convertValue(double inVal, String type, String unit1, String unit2){
		ConversionType unitType = typeHash.get(type);
		double factorOrig = unitType.conversions.get(unit1);
		double factorConv = unitType.conversions.get(unit2);
		return (inVal * factorOrig / factorConv);
	}
}
