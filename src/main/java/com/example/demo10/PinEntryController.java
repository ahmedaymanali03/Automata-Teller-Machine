package com.example.demo10;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PinEntryController {

    @FXML
    private PasswordField pinField;
    @FXML
    private Button enterPinButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label cardNumberLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label attemptsLabel;
    
    private String cardNumber;
    private static Map<String, Integer> pinAttempts = new HashMap<>();
    
    public void initialize() {
        enterPinButton.setOnAction(event -> handlePinEnter());
        cancelButton.setOnAction(event -> handleCancel());
        
        // Update status label using global static variable
        statusLabel.setText("Current state: " + ATM.currentStatusText);
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        cardNumberLabel.setText("Card: " + maskCardNumber(cardNumber));
        
        // Initialize attempts for this card if not already done
        if (!pinAttempts.containsKey(cardNumber)) {
            pinAttempts.put(cardNumber, 0);
        }
        
        updateAttemptsLabel();
    }
    
    private String maskCardNumber(String cardNumber) {
        // Show only last 4 digits if card number is longer
        if (cardNumber.length() > 4) {
            return "XXXX-XXXX-XXXX-" + cardNumber.substring(cardNumber.length() - 4);
        } else {
            return cardNumber;
        }
    }
    
    private void updateAttemptsLabel() {
        int attemptsRemaining = 3 - pinAttempts.getOrDefault(cardNumber, 0);
        attemptsLabel.setText("Attempts remaining: " + attemptsRemaining);
    }
    
    private void handlePinEnter() {
        String pin = pinField.getText();
        
        if (pinAttempts.getOrDefault(cardNumber, 0) >= 3) {
            showAlert("Card is blocked due to 3 incorrect PIN attempts.");
            handleCancel();
            return;
        }
        
        if (!HelloController.atm.cardMap.get(cardNumber).getPin().equals(pin)) {
            pinAttempts.put(cardNumber, pinAttempts.getOrDefault(cardNumber, 0) + 1);
            updateAttemptsLabel();
            
            if (pinAttempts.get(cardNumber) >= 3) {
                showAlert("Card is blocked due to 3 incorrect PIN attempts.");
                handleCancel();
            } else {
                showAlert("Incorrect PIN.");
                pinField.clear();
            }
            return;
        }
        
        // Reset attempts on successful login
        pinAttempts.put(cardNumber, 0);
        HelloController.atm.currentCard = HelloController.atm.cardMap.get(cardNumber);
        HelloController.atm.process("pin:" + pin);
        
        // Change scene to transaction page
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transactionpage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) enterPinButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load transaction page.");
        }
    }
    
    private void handleCancel() {
        try {
            HelloController.atm.resetToInitialState();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to return to card entry screen.");
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