package com.atasoft.helpers;
import java.util.*;

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
	private HashMap<String, Double> lengthHash;
	private HashMap<String, Double> forceHash;
	private HashMap<String, Double> massHash;
	private HashMap<String, Double> volumeHash;
	private HashMap<String, HashMap> typesHash;
	public ConvDataHold() {
		this.typesHash = new HashMap<String, HashMap>(typeStrings.length);
		this.lengthHash = makeDoubHash(lengthStrings, lengthRates);
	    typesHash.put(typeStrings[0], lengthHash);
		this.forceHash = makeDoubHash(forceStrings, forceRates);
		typesHash.put(typeStrings[1], forceHash);
		this.massHash = makeDoubHash(massStrings, massRates);
		typesHash.put(typeStrings[2], massHash);
		this.volumeHash = makeDoubHash(volumeStrings, volumeRates);
		typesHash.put(typeStrings[3], volumeHash);
	}
	
	private HashMap<String, Double> makeDoubHash(String[] keys, double[] doubs){
		HashMap<String, Double> retHash = new HashMap<String, Double>(keys.length);
		for(int i = 0; i<keys.length; i++){
			retHash.put(keys[i], doubs[i]);
		}
		return retHash;
	}
	
	private HashMap<String, Double> makeStringHash(String[] keys, String[] strings){
		HashMap<String, Double> retHash = new HashMap<String, String[]>(strings.length);
		for(int i = 0; i<keys.length; i++){
			retHash.put(keys[i], doubs[i]);
		}
		return retHash;
	}
	
	public String[] getUnitStrings(String type) {
		String[] unitStrings = typesHash.get(type);
		
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
