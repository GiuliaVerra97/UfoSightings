package it.polito.tdp.ufo.model;

import java.time.Year;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model model=new Model();
		Year anno=Year.of(1954);		//prendo un anno
		model.creaGrafo(anno);
		
	
		
	}

}
