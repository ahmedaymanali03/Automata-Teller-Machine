package com.example.demo10;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class Checkbalance {

    @FXML
    private Button returnButton;
    @FXML
    private Button ejectCardButton;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label statusLabel;

    public void initialize() {
        returnButton.setOnAction(event -> handleReturn());
        ejectCardButton.setOnAction(event -> handleEjectCard());
        
        // Update balance label with current balance
        if (HelloController.atm.currentCard != null) {
            updateBalanceLabel();
        }
        
        // Update status label using global static variable
        statusLabel.setText("Current state: " + ATM.currentStatusText);
    }
    
    private void updateBalanceLabel() {
        double balance = HelloController.atm.currentCard.getBalance();
        balanceLabel.setText(String.format("Your current balance: $%.2f", balance));
    }

    private void handleReturn() {
        try {
            // Reset the state to CHOOSE_TRANSACTION before returning
            HelloController.atm.resetToTransactionState();
            
            // Update status text to reflect the new state
            ATM.currentStatusText = "Transaction state reset";
            
            Stage stage = (Stage) returnButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transactionpage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to return to transaction page.");
        }
    }
    
    private void handleEjectCard() {
        HelloController.atm.process("ejectCard");
        showAlert("Card ejected.");
        try {
            Stage stage = (Stage) ejectCardButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to eject card.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ATM Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
