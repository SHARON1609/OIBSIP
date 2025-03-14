import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Represents a user
class User {
    private String userId;
    private String userPin;
    private Account account;

    public User(String userId, String userPin, double initialBalance) {
        this.userId = userId;
        this.userPin = userPin;
        this.account = new Account(initialBalance);
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public Account getAccount() {
        return account;
    }
}

// Represents a transaction
class Transaction {
    private String type;
    private double amount;
    private String timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    @Override
    public String toString() {
        return type + ": $" + amount + " at " + timestamp;
    }
}

// Manages the user's account
class Account {
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(double initialBalance) {
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount));
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance!");
        } else {
            balance -= amount;
            transactionHistory.add(new Transaction("Withdraw", amount));
        }
    }

    public void transfer(Account targetAccount, double amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance!");
        } else {
            balance -= amount;
            targetAccount.deposit(amount);
            transactionHistory.add(new Transaction("Transfer to " + targetAccount.hashCode(), amount));
        }
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History:");
        for (Transaction transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

// Handles ATM operations
class ATMService {
    private User currentUser;

    public ATMService(User user) {
        this.currentUser = user;
    }

    public void showMenu() {
        System.out.println("Welcome, " + currentUser.getUserId());
        System.out.println("1. Transactions History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    public void performOperation(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                currentUser.getAccount().showTransactionHistory();
                break;
            case 2:
                System.out.print("Enter amount to withdraw: ");
                double withdrawAmount = scanner.nextDouble();
                currentUser.getAccount().withdraw(withdrawAmount);
                break;
            case 3:
                System.out.print("Enter amount to deposit: ");
                double depositAmount = scanner.nextDouble();
                currentUser.getAccount().deposit(depositAmount);
                break;
            case 4:
                System.out.print("Enter target user ID: ");
                String targetUserId = scanner.next();
                System.out.print("Enter amount to transfer: ");
                double transferAmount = scanner.nextDouble();
                // In a real system, you'd fetch the target user from a database
                User targetUser = new User(targetUserId, "0000", 0); // Dummy target user
                currentUser.getAccount().transfer(targetUser.getAccount(), transferAmount);
                break;
            case 5:
                System.out.println("Thank you for using the ATM. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

// Main ATM class
public class ATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Dummy user for demonstration
        User user = new User("12345", "6789", 1000.0);

        System.out.print("Enter User ID: ");
        String userId = scanner.next();
        System.out.print("Enter PIN: ");
        String userPin = scanner.next();

        if (userId.equals(user.getUserId()) && userPin.equals(user.getUserPin())) {
            ATMService atmService = new ATMService(user);
            while (true) {
                atmService.showMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                atmService.performOperation(choice, scanner);
            }
        } else {
            System.out.println("Invalid User ID or PIN. Exiting...");
        }
    }
}
