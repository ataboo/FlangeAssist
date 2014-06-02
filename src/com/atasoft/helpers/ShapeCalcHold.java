package com.atasoft.helpers;

import java.util.*;

public class ShapeCalcHold
 {
	public static final String[] SHAPE_TYPES = new String[]{
		"Cylinder", "Sphere", "Box", "Rectangle", "Circle"};
	
	public class ShapeObject{
		int fieldCount;
		String[] labels;
		int type;
		public ShapeObject(int type){
			this.type = type;
			switch(type){
				case 0:  //Cylinder
					this.labels = new String[]{"Radius (r):", "Height (h):"};
					break;
				case 1:  //Sphere
					this.labels = new String[]{"Radius (r):"};
					break;
				case 2:  //Box
					this.labels = new String[]{"Length (l):", "Width (w):", "Height (h)"};
					break;
				case 3:  //Rectangle
					this.labels = new String[]{"Length (l):", "Width (w):"};
					break;
				default: //Circle
					this.labels = new String[]{"Radius (r):"};
					break;
			}
			this.fieldCount = labels.length;
		}
	}
		
	public ShapeCalcHold(){
		setupValues();
	}
	
	public String[] getLabelStrings(String shapeType){
		return shapeHash.get(shapeType).labels;
	}
	
	public double[] getValues(String typeName, double[] vals){
		int thisType = shapeHash.get(typeName).type;
		double[] retArr = new double[2]; //Volume, Surface Area
		switch(thisType){
			case 0:  //Cylinder (r, h)
				retArr = solveCylinder(vals[0], vals[1]);
				break;
			case 1:  //Sphere (r)
				retArr = solveSphere(vals[0]);
				break;
			case 2:  //Box (l, w, h)
				retArr = solveBox(vals[0], vals[1], vals[2]);
				break;
			case 3:  //Rectangle (l, w)
				retArr = solveRect(vals[0], vals[1]);
				break;
			default: //Circle (r)
				retArr= solveCirc(vals[0]);
				break;
		}
		
		//3d: volume, surface area. 2d: perimeter, surface area.
		return retArr;
	}
	
	private double[] solveCylinder(double radius, double height){
		double[] retArr = new double[2];
		retArr[0] = Math.PI * radius * radius * height;
		retArr[1] = (Math.PI * radius * radius * 2) + (2 * Math.PI * radius * height);
		return retArr;
	}
	
	private double[] solveSphere(double radius){
		double[] retArr = new double[2];
		retArr[0] = 4 * Math.PI * Math.pow(radius, 3) / 3;
		retArr[1] = 4 * Math.PI * radius * radius;
		return retArr;
	}
	
	private double[] solveBox(double length, double width, double height){
		double[] retArr = new double[2];
		retArr[0] = length * width * height;
		retArr[1] = 2 * (length * width + width * height + length * height);
		return retArr;
	}
	
	private double[] solveRect(double length, double width){
		double[] retArr = new double[2];
		retArr[0] = length * width;
		retArr[1] = 2 * length + 2 * width; //perimeter
		return retArr;
	}
	
	private double[] solveCirc(double radius){
		double[] retArr = new double[2];
		retArr[0] = Math.PI * radius * radius;
		retArr[1] = 2 * Math.PI * radius * radius; //perimeter
		return retArr;
	}
	
	HashMap<String, ShapeObject> shapeHash;
	private void setupValues(){
		this.shapeHash = new HashMap<String, ShapeObject>(SHAPE_TYPES.length);
		for(int i=0; i<SHAPE_TYPES.length; i++){
			shapeHash.put(SHAPE_TYPES[i], new ShapeObject(i));
		}
	}
	
	public boolean isThis2D(String checkName){
		if(checkName == SHAPE_TYPES[3] || checkName == SHAPE_TYPES[4]){
			return true;
		}
		return false;
	}
	
	public static double roundDouble(double val, int decimals) {
		double factor = Math.pow(10, decimals);
		val = (Math.round(val * factor)) / factor;
		return val;
	}
	
}
