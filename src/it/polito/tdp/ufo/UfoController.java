/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.javafx.collections.SetAdapterChange;

import it.polito.tdp.ufo.db.AnnoCount;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<AnnoCount> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    private Button btnSequenza;
    

    //successori, precedenti
    @FXML
    void handleAnalizza(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	String stato=boxStato.getValue();
    	if(stato==null) {
    		txtResult.appendText("\nSeleziona stato");
    		return;
    	}
    	
    	List<String> predecessori=this.model.getPredecessori(stato);
    	List<String> successori=this.model.getSuccessori(stato);
    	List<String> raggiungibili=this.model.getRaggiungibili(stato);
    	txtResult.clear();
    	
    	txtResult.appendText("Predecessori:\n");

    	for(String s: predecessori) {
    		txtResult.appendText(s+" ");
    	}
    	
    	
    	txtResult.appendText("\nSuccessori:\n");
    	for(String s: successori) {
    		txtResult.appendText(s+" ");
    	}
    	
    	
    	txtResult.appendText("\nSi possono raggiungere");
    	for(String s: raggiungibili) {
    		txtResult.appendText(s+" ");
    	}
    			
    	
    	btnSequenza.setDisable(false);
    	
    }

    //crea grafo
    @FXML
    void handleAvvistamenti(ActionEvent event) {

    	txtResult.clear();
    	
    	AnnoCount anno=boxAnno.getValue();
    	if(anno==null) {
    		txtResult.appendText("Errore");
    	}else {
    		this.model.creaGrafo(anno.getYear());
    		txtResult.appendText("\nGrafo creato");
    		txtResult.appendText("\nVertici: "+model.getNVertici()+"\n Archi "+model.getNArchi());
    	}
    	
    	this.boxStato.getItems().addAll(model.getStati());
    	
    	btnSequenza.setDisable(false);
    	
    	
    }

    
    //ricorsione
    @FXML
    void handleSequenza(ActionEvent event) {

    	txtResult.clear();
    	
    	String stato=boxStato.getValue();
    	if(stato==null) {
    		txtResult.appendText("\nSeleziona stato");
    		return;
    	}
    	
    	List<String> percorso=model.getPercorsoOttimo(stato);
    	
    	for(String s: percorso) {
    		txtResult.appendText(s+"\n");
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }
    
    
    
    public void setModel(Model model) {
    	this.model=model;
    	this.boxAnno.getItems().addAll(model.getAnni());

    }
    
    
    
}
