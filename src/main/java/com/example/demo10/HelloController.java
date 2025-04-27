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

public class HelloController {

    @FXML
    private TextField cardField;
    @FXML
    private Button enterButton;
    @FXML
    private Label statusLabel;

    static Card[] cards = {
            new Card("9876", "4321", 1000.0),
            new Card("5432", "8765", 2000.0),
            new Card("6789", "3456", 500.0),
            new Card("4321", "6543", 1500.0),
            new Card("8765", "9876", 3000.0),
            new Card("2345", "6789", 2500.0),
            new Card("1357", "2468", 4000.0),
            new Card("2468", "1357", 3500.0),
            new Card("3579", "5791", 4500.0),
            new Card("4680", "3579", 6000.0),
            new Card("5791", "4680", 7000.0)
    };

    static ATM atm = new ATM(cards);

    public void initialize() {
        enterButton.setOnAction(event -> handleEnter());

        // Reset ATM to initial state when returning to login page
        atm.resetToInitialState();

        // Update the status label using global static variable
        updateStatusLabel();
    }

    private void updateStatusLabel() {
        if (statusLabel != null) {
            statusLabel.setText("Current state: " + ATM.currentStatusText);
        }
    }

    private void handleEnter() {
        String cardNumber = cardField.getText();

        if (!atm.cardMap.containsKey(cardNumber)) {
            showAlert("Card not recognized.");
            return;
        }

        // Card exists, process the card insertion
        atm.process("insertCard:" + cardNumber);
        updateStatusLabel();
        
        // Navigate to PIN entry screen
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pin-entry.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            
            // Pass the card number to the PIN entry controller
            PinEntryController pinController = fxmlLoader.getController();
            pinController.setCardNumber(cardNumber);
            
            Stage stage = (Stage) enterButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load PIN entry page.");
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