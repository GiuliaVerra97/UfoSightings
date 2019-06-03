/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
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
    

    @FXML
    void handleAnalizza(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	AnnoCount anno=boxAnno.getValue();
    	if(anno==null) {
    		txtResult.appendText("Errore");
    	}else {
    		this.model.creaGrafo(anno.getYear());
    		txtResult.appendText("Grafo creato");
    		txtResult.appendText("Vertici: "+model.getNVertici()+"\n Archi "+model.getNArchi());
    	}
    	
    	this.boxStato.getItems().addAll(model.getStati());
    	
    	btnSequenza.setDisable(false);
    	
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {

    }

    @FXML
    void handleSequenza(ActionEvent event) {

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
