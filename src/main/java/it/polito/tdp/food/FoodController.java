/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;
	private boolean entrato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Grafo creato!\n");
    	try {
			int por = Integer.parseInt(txtPorzioni.getText());
			entrato = true;
			model.creaGrafo(por);
			txtResult.appendText("# VERTICI: " + model.getNVertici() + "\n# ARCHI: " + model.getNArchi());
			boxFood.getItems().addAll(model.getVertici());
		} catch (NumberFormatException e) {
			throw e;
		}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	if(entrato) {
    		txtResult.clear();
        	txtResult.appendText("Analisi calorie...\n");
        	if(boxFood.getValue() != null) {
        		List<Adiacenza> adiacenze = model.calorieCongiunte(boxFood.getValue());
        		
        		if(adiacenze.size() > 0) {
        			int num = 0;
        			if(adiacenze.size() < 5) {
        				num = adiacenze.size();
        			}
        			else {
        				num = 5;
        			}
 
        			String string = "";
        			
        			for (int i=0; i<num; i++) {
        				string += adiacenze.get(i).getF2().getDisplay_name() + " - " + adiacenze.get(i).getPeso() + "\n";
        			}
        			
        			txtResult.appendText(string);
        		}
        		else {
        			txtResult.appendText("Non ci sono archi per il food selezionato");
        		}
        	}
    	}
    	else {
    		txtResult.appendText("Creare prima il grafo!\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	if(entrato) {
    		txtResult.clear();
        	txtResult.appendText("Simulazione...\n");
        	
    		try {
    			int k = Integer.parseInt(txtK.getText());
    			txtResult.appendText(model.getDatiSimulazione(k, boxFood.getValue()));
    		} catch (NumberFormatException e) {
    			throw e;
    		}
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
