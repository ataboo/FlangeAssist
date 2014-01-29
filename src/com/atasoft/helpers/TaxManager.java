package com.atasoft.helpers;

import com.atasoft.flangeassist.R;

import android.content.Context;

public class TaxManager {
	//Tax Years
	public static final int TY_2013 = 0;
	public static final int TY_2014 = 1;
	
	public static final int PROV_AB = 0;
	public static final int PROV_ON = 1;
	
	Context context;
	
	
	public TaxManager(Context context, int taxYear) {
		this.context = context;
		setupTaxStats();
	}
	
	private static class TaxStats{
		public double[][] rates;
		public double[][] brackets;
		public double[][] constK;
		public double[] taxCred;
		
		public TaxStats(){}
	}
	
	public TaxManager.TaxStats fedStats;
	public TaxManager.TaxStats abStats;
	private void setupTaxStats(){
		this.fedStats = new TaxManager.TaxStats();
		fedStats.brackets = new double[][]{
			{0,43561,87123,135054},
			{0,43953,87907,136370}};
		fedStats.rates = new double[][]{
			{0.15,0.22,0.26,0.29}};
		fedStats.constK = new double[][]{
			{0,3049,6534,10586},
			{0,3077,6593,10681}};
		fedStats.taxCred = new double[]{
			2310.35, 2340.63};
		
		this.abStats = new TaxManager.TaxStats();
		abStats.rates = new double[][]{{0.10}};
		abStats.taxCred = new double[]{
		    2084.03, 2112.62};
		//TODO: other provinces
	}
	
	public double[] getTaxes(double gross, int year, int province) {
		double provTax = 0;
		double anGross = gross * 52;
		double fedTax = getFedTax(anGross, year);
		switch(province){
			case PROV_AB:
				provTax = getABTax(anGross, year);
				break;
			/*case PROV_ON:
				provTax = getONTax(anGross, year);
				break;*/
		}
		
		return new double[]{fedTax/52, provTax/52};
	}
	
	private double getFedTax(double anGross, int year){
		
		return 0;
	}
	
	private double getABTax(double anGross, int year){
		double rate = abStats.rates[0][0];
		double taxCred = abStats.taxCred[year];
		return rate * anGross - taxCred;
	}
	
}

/*
private double[] taxCalc(double gross, double grossNoVac, TaxYear taxYear, boolean addCPP){
	double anGross = gross * 52;
	double bracket[] = {0,0,0,0};
	double diff[] = {0,0,0};
	double rate[] = {0, 0, 0, 0};
	double fedConst[] = {0,0,0,0};
	double fedTax = 0;
	double provTax = 0;
	String brackStr[];
	String rateStr[];
	String fedCStr[];
	double fedTaxCred;
	double provTaxCred;
	double provTaxCredNoEI;
	
	switch (taxYear) {
		case AB_2013:
			brackStr = getResources().getStringArray(R.array.tax_brackets);
			rateStr = getResources().getStringArray(R.array.tax_rates);
			fedCStr = getResources().getStringArray(R.array.fed_const);
			fedTaxCred = Double.parseDouble(getString(R.string.fed_taxcred));
			provTaxCred = Double.parseDouble(getString(R.string.ab_taxcred));
			provTaxCredNoEI = Double.parseDouble(getString(R.string.ab_taxcred_noEI));
			break;
		default:
			brackStr = getResources().getStringArray(R.array.tax_brackets_2014);
			rateStr = getResources().getStringArray(R.array.tax_rates_2014);
			fedCStr = getResources().getStringArray(R.array.fed_const_2014);
			fedTaxCred = Double.parseDouble(getString(R.string.fed_taxcred_2014));
			provTaxCred = Double.parseDouble(getString(R.string.ab_taxcred_2014));	
			provTaxCredNoEI = Double.parseDouble(getString(R.string.ab_taxcred_noEI_2014));  //gotta change to 2014 eventually
			break;
	}
	
	double duesRate = Double.parseDouble(getString(R.string.dues_rate));
	double cppRate = Double.parseDouble(getString(R.string.cpp_rate));
	double eiRate = Double.parseDouble(getString(R.string.ei_rate));

	for(int i=0; i<4; i++){
		bracket[i] = Double.parseDouble(brackStr[i]);
		rate[i] = Double.parseDouble(rateStr[i]);
		fedConst[i] = Double.parseDouble(fedCStr[i]);

		if(i < 3){
			diff[i] = bracket[i + 1] - bracket [i];
		}
	}

	if(anGross < bracket[1]) {
		fedTax = anGross * rate[0] - fedConst[0] - fedTaxCred;
	}
	if(anGross < bracket[2]) {
		fedTax = anGross * rate[1] - fedConst[1] - fedTaxCred;
	}
	if(anGross < bracket[3]) {
		fedTax = anGross * rate[2] - fedConst[2] - fedTaxCred;	
	}
	if(anGross >= bracket[3]) {
		fedTax = anGross * rate[3] - fedConst[3] - fedTaxCred;
	}

	provTax = (anGross * 0.1) - provTaxCred;
	double dues = grossNoVac * duesRate;
	double cppRet = (anGross - 3500) / 52 * cppRate;
	double eiRet = gross * eiRate;
	if (cppRet < 0) cppRet = 0;
	if(!addCPP) {
		cppRet = 0;
		eiRet = 0;
		provTax += (provTaxCredNoEI);  // Seems like it only takes it off the provTaxCred with EI paid
	}
	
	if(provTax < 0){provTax = 0;}
	if(fedTax < 0){fedTax = 0;}
	
	return new double[] {
		fedTax / 52, 
		provTax / 52, 
		dues, 
		cppRet, 
		eiRet
		};
}
*/
