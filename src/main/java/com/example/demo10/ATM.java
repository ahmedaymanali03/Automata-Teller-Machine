package com.example.demo10;

import java.util.HashMap;
import java.util.Map;

public class ATM {
    private enum State {
        WAITING_FOR_CARD, ENTER_PIN, CHOOSE_TRANSACTION, DEPOSIT, CHECK_BALANCE, WITHDRAW, DISPLAY_BALANCE, UPDATE_BALANCE, EJECT_CARD
    }

    private State currentState = State.WAITING_FOR_CARD;
    private boolean cardInserted = false;
    private boolean correctPin = false;
    Map<String, Card> cardMap = new HashMap<>();
    static Card currentCard = null;
    
    // Global static variable for status display across all FXML controllers
    public static String currentStatusText = "Waiting for card";

    public ATM(Card[] cards) {
        for (Card card : cards) {
            cardMap.put(card.getCardNumber(), card);
        }
    }
    
    /**
     * Update and return the current status text for display in the UI
     */
    public static String getCurrentStatusText() {
        return currentStatusText;
    }
    
    /**
     * Gets the current state of the ATM as a string
     * @return The name of the current state
     */
    public String getCurrentState() {
        return currentState.name();
    }
    
    /**
     * Resets the ATM to its initial state (WAITING_FOR_CARD)
     */
    public void resetToInitialState() {
        currentState = State.WAITING_FOR_CARD;
        cardInserted = false;
        correctPin = false;
        currentCard = null;
        currentStatusText = "Waiting for card";
    }
    
    /**
     * Resets the ATM to the CHOOSE_TRANSACTION state
     * This is needed when returning to the transaction page from other screens
     */
    public void resetToTransactionState() {
        if (currentCard != null && cardInserted && correctPin) {
            currentState = State.CHOOSE_TRANSACTION;
            currentStatusText = "Choose transaction";
            System.out.println("Returned to transaction selection.");
        }
    }

    public void process(String input) {
        switch (currentState) {
            case WAITING_FOR_CARD:
                if (input.startsWith("insertCard:")) {
                    String cardNumber = input.substring(11);
                    if (cardMap.containsKey(cardNumber)) {
                        currentCard = cardMap.get(cardNumber);
                        cardInserted = true;
                        currentState = State.ENTER_PIN;
                        currentStatusText = "Enter PIN";
                        System.out.println("Card inserted. Enter PIN.");
                    } else {
                        System.out.println("Card not recognized.");
                    }
                }
                break;
            case ENTER_PIN:
                if (input.startsWith("pin:")) {
                    correctPin = input.substring(4).equals(currentCard.getPin());
                    currentState = correctPin ? State.CHOOSE_TRANSACTION : State.ENTER_PIN;
                    currentStatusText = correctPin ? "Choose transaction" : "Incorrect PIN. Try again";
                    System.out.println(correctPin ? "PIN correct. Choose transaction." : "Incorrect PIN. Try again.");
                }
                break;
            case CHOOSE_TRANSACTION:
                switch (input) {
                    case "1":
                        currentState = State.DEPOSIT;
                        currentStatusText = "Deposit";
                        System.out.println("Deposit selected.");
                        break;
                    case "2":
                        currentState = State.CHECK_BALANCE;
                        currentStatusText = "Check Balance";
                        System.out.println("Check balance selected.");
                        break;
                    case "3":
                        currentState = State.WITHDRAW;
                        currentStatusText = "Withdraw";
                        System.out.println("Withdraw selected.");
                        break;
                    case "ejectCard":
                        currentState = State.EJECT_CARD;
                        currentStatusText = "Card ejected";
                        System.out.println("Card ejected.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                break;
            case DEPOSIT:
                if (input.startsWith("deposit:")) {
                    double amount = Double.parseDouble(input.substring(8));
                    currentCard.deposit(amount);
                    currentState = State.UPDATE_BALANCE;
                    currentStatusText = "Balance updated";
                    System.out.println("Deposited: $" + amount);
                }
                break;
            case CHECK_BALANCE:
                currentState = State.DISPLAY_BALANCE;
                currentStatusText = "Displaying balance";
                System.out.println("Current balance: $" + currentCard.getBalance());
                break;
            case WITHDRAW:
                if (input.startsWith("withdraw:")) {
                    double amount = Double.parseDouble(input.substring(9));
                    if (currentCard.withdraw(amount)) {
                        currentState = State.UPDATE_BALANCE;
                        currentStatusText = "Balance updated";
                        System.out.println("Withdrew: $" + amount);
                    } else {
                        System.out.println("Insufficient funds.");
                        currentStatusText = "Insufficient funds";
                        currentState = State.CHOOSE_TRANSACTION;
                    }
                }
                break;
            case DISPLAY_BALANCE:
                currentState = State.CHOOSE_TRANSACTION;
                currentStatusText = "Choose transaction";
                break;
            case UPDATE_BALANCE:
                currentState = State.CHOOSE_TRANSACTION;
                currentStatusText = "Choose transaction";
                System.out.println("Balance updated.");
                break;
            case EJECT_CARD:
                cardInserted = false;
                currentState = State.WAITING_FOR_CARD;
                currentStatusText = "Waiting for card";
                currentCard = null;
                break;
        }
    }
}