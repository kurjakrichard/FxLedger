/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sire.fxledger;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author sire
 */
public class FxLedger extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxLedgerFXML.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(com.sire.fxledger.FxLedger.class.getResource("Style.css").toExternalForm());
        stage.setTitle("FxLedger");
        //stage.getIcons().add(new Image("8tracks-icon.png"));
        stage.setScene(scene);
        stage.show(); 
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
