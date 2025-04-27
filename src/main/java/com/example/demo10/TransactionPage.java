package com.example.demo10;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionPage{

    @FXML
    private Button withdrawButton;
    @FXML
    private Button depositButton;
    @FXML
    private Button checkBalanceButton;
    @FXML
    private Button removeCardButton;
    @FXML
    private Label statusLabel;

    public void initialize() {
        withdrawButton.setOnAction(event -> {
            try {
                handleWithdraw();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        depositButton.setOnAction(event -> {
            try {
                handleDeposit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        checkBalanceButton.setOnAction(event -> {
            try {
                handleCheckBalance();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        removeCardButton.setOnAction(event -> {
            try {
                handleRemoveCard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        
        // Update status label using global static variable and refresh
        ATM.currentStatusText = "Choose transaction"; // Make sure it's correct when returning to transaction page
        statusLabel.setText("Current state: " + ATM.currentStatusText);
    }

    private void handleWithdraw() throws IOException {
        ATM atm = HelloController.atm;
        atm.process("3"); // This already updates the ATM.currentStatusText in the process method
        Stage stage = (Stage) withdrawButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("withdraw.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private void handleDeposit() throws IOException {
        ATM atm = HelloController.atm;
        atm.process("1"); // This already updates the ATM.currentStatusText in the process method
        showAlert("Deposit selected.");
        Stage stage = (Stage) depositButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("deposit.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private void handleCheckBalance() throws IOException {
        ATM atm = HelloController.atm;
        atm.process("2"); // This already updates the ATM.currentStatusText in the process method
        showAlert("Check balance selected.");
        Stage stage = (Stage) checkBalanceButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("checkbalance.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private void handleRemoveCard() throws IOException {
        ATM atm = HelloController.atm;
        atm.process("ejectCard"); // This already updates the ATM.currentStatusText in the process method
        showAlert("Card removed.");
        Stage stage = (Stage) removeCardButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ATM Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}