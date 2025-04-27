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

public class Withdraw {

    @FXML
    private TextField amountField;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button ejectCardButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Label balanceLabel;

    public void initialize() {
        withdrawButton.setOnAction(event -> handleWithdraw());
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
        balanceLabel.setText(String.format("Available balance: $%.2f", balance));
    }

    private void handleWithdraw() {
        String amountText = amountField.getText();
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Please enter a positive amount.");
                return;
            }

            if (HelloController.atm.currentCard.withdraw(amount)) {
                HelloController.atm.process("withdraw:" + amount);
                showAlert("Withdrew: $" + amount);
                updateBalanceLabel();
                
                // Automatically eject card after successful withdrawal
                showAlert("Transaction complete. Card is being ejected.");
                handleEjectCard();
            } else {
                showAlert("Insufficient funds. Your balance is $" + 
                          HelloController.atm.currentCard.getBalance());
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid amount. Please enter a valid number.");
        }
    }

    private void handleReturn() {
        try {
            // Reset the state to CHOOSE_TRANSACTION before returning
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
