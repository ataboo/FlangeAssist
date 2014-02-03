package com.atasoft.helpers;

import com.atasoft.flangeassist.R;

import android.content.Context;
import java.io.*;
import android.util.*;

public class TaxManager {
	//Tax Years
	public static final int TY_2013 = 0;
	public static final int TY_2014 = 1;
	
	//Provinces
	public static final int PROV_BC = 0;
	public static final int PROV_AB = 1;
	public static final int PROV_SK = 2;
	public static final int PROV_MB = 3;
	public static final int PROV_ON = 4;
	public static final int PROV_QC = 5;
	public static final int PROV_NB = 6;
	public static final int PROV_NS = 7;
	public static final int PROV_PE = 8;
	public static final int PROV_NL = 9;
	
	public TaxManager() {
		Log.d("tax manager","started");
		setupTaxStats();
	}
	
	//Used as a container for tables for each type of tax
	public class TaxStats{  
		public double[][] rates;
		public double[][] brackets;
		public double[][] constK;
		public double[] taxCred;
		public double[][] taxReduction;
	}
	
	public TaxManager.TaxStats fedStats;
	public TaxManager.TaxStats bcStats;
	public TaxManager.TaxStats abStats;
	public TaxManager.TaxStats onStats;
	public double[][] cppEi;
	private void setupTaxStats(){
		this.cppEi = new double[][]{
			{0.0495, 3500, 0.0188},
			{0.0495, 3500, 0.0188}
		};
		
		this.fedStats = new TaxManager.TaxStats();
		fedStats.brackets = new double[][]{
			{0,43561,87123,135054},
			{0,43953,87907,136370}};
		fedStats.rates = new double[][]{
			{0.15, 0.22, 0.26, 0.29},
			{0.15, 0.22, 0.26, 0.29}};
		fedStats.constK = new double[][]{
			{0,3049,6534,10586},
			{0,3077,6593,10681}};
		fedStats.taxCred = new double[]{2310.35, 2340.63};
		
		this.bcStats = new TaxManager.TaxStats();
		bcStats.brackets = new double[][]{
			{0, 37568, 75138, 86268, 104754.01, 150000},
			{0, 37606, 75213, 86354, 104858, 150000}};
		bcStats.rates = new double[][]{
			{0.0506, 0.0770, 0.1050, 0.1229, 0.1470, 0.1680},
			{0.0506, 0.0770, 0.1050, 0.1229, 0.1470, 0.1680}};
		bcStats.constK = new double[][]{
			{0, 992, 3096, 4640, 7164, 10322},
			{0, 993, 3099, 4644, 7172, 10322}};
		bcStats.taxCred = new double[]{684.28, 668.33};
		bcStats.taxReduction = new double[][]{
			{18181, 409, 0.032},  //under 18181 gets 409 over gets 409 - difference * %3.2
			{18200, 409, 0.032}
		};
		
		//Simplicity is key
		this.abStats = new TaxManager.TaxStats();
		abStats.rates = new double[][]{{0.10},{0.10}};
		abStats.taxCred = new double[]{2084.03, 2112.62};
		
	}
	
	//Returns [fed, prov, cpp, ei]
	public double[] getTaxes(double gross, int year, int province) {
		double provTax = 0;
		double anGross = gross * 52;
		double fedTax = getFedTax(anGross, year);
		switch(province){
			case PROV_BC:
				provTax = getBCTax(anGross, year);
				break;
			case PROV_AB:
				provTax = getABTax(anGross, year);
				break;
			/*case PROV_ON:
				provTax = getONTax(anGross, year);
				break;*/
		}
		
		double[] cppEi = getCppEi(anGross, year);
		provTax = (provTax > 0) ? provTax:0;
		fedTax = (fedTax > 0) ? fedTax:0;
		
		return new double[]{fedTax/52, provTax/52, cppEi[0]/52, cppEi[1]/52};
	}
	
	private double[] getCppEi(double anGross, int year){
		//[cpp rate, exemption, ei rate]
		double cppRate = cppEi[year][0];
		double cppExempt = cppEi[year][1];
		double eiRate = cppEi[year][2];
		
		double cppRet = (anGross - cppExempt) * cppRate;
		double eiRet = anGross * eiRate;
		cppRet = (cppRet > 0) ? cppRet:0;
		return new double[]{cppRet, eiRet};
	}
	
	private double getFedTax(double anGross, int year){
		double[] bracket = fedStats.brackets[year];
		int taxIndex = (anGross<bracket[1]) ? 0:
			(anGross<bracket[2] ? 1 :
			(anGross<bracket[3] ? 2 : 3));
		double rate = fedStats.rates[year][taxIndex];
		double constK = fedStats.constK[year][taxIndex];
		double fedTax = anGross * rate -
			constK - 
			fedStats.taxCred[year];
		return fedTax;
	}
	
	private double getBCTax(double anGross, int year){
		double[] bracket = bcStats.brackets[year];
		int taxIndex = (anGross<bracket[1]) ? 0:
			(anGross<bracket[2] ? 1 :
			(anGross<bracket[3] ? 2 : 3));
		double rate = bcStats.rates[year][taxIndex];  //Rate and constant will share same index
		double constK = bcStats.constK[year][taxIndex];	
		double taxTotal = rate * anGross - 
			constK -
			bcStats.taxCred[year] -
			taxReduction(anGross, year);	
		return taxTotal;
	}
	
	private double getABTax(double anGross, int year){
		double rate = abStats.rates[year][0];
		double taxCred = abStats.taxCred[year];
		return rate * anGross - taxCred;
	}
	
	//So far only BC uses this style of reduction.  Might have to add provincial specific later
	private double taxReduction(double anGross, int year){  
		//switch case for provinces later?
		double[] redTable = bcStats.taxReduction[year];	//[bracket, credit, drop rate]
		double diff = anGross - redTable[0];
		double taxRed = (diff < 0) ? redTable[1] : redTable[1] - redTable[2] * diff;
		if(taxRed < 0) taxRed = 0;
		return taxRed;
	}
}
