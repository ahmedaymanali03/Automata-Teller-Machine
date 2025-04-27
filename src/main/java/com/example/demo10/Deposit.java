package com.example.demo10;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class Deposit {

    @FXML
    private TextField amountField;
    @FXML
    private Button depositButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button ejectCardButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Label balanceLabel;

    public void initialize() {
        depositButton.setOnAction(event -> handleDeposit());
        returnButton.setOnAction(event -> handleReturn());
        ejectCardButton.setOnAction(event -> handleEjectCard());
        
        // Update balance label with current balance
        if (HelloController.atm.currentCard != null) {
            updateBalanceLabel();
        }
        
        // Update status label using the global static variable
        statusLabel.setText("Current state: " + ATM.currentStatusText);
    }
    
    private void updateBalanceLabel() {
        double balance = HelloController.atm.currentCard.getBalance();
        balanceLabel.setText(String.format("Current balance: $%.2f", balance));
    }

    private void handleDeposit() {
        String amountText = amountField.getText();
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Please enter a positive amount.");
                return;
            }

            // Process the deposit through the ATM state machine
            HelloController.atm.process("deposit:" + amount);
            
            showAlert("Deposited: $" + amount);
            
            // Automatically return to transaction page after successful deposit
            try {
                // Ensure we reset to transaction state before navigating
                HelloController.atm.resetToTransactionState();
                
                Stage stage = (Stage) depositButton.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transactionpage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Failed to return to transaction page.");
                updateBalanceLabel(); // Update balance if we can't return
                amountField.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid amount. Please enter a valid number.");
        }
    }

    private void handleReturn() {
        try {
            // Reset to transaction state before navigating
            HelloController.atm.resetToTransactionState();
            
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