import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import constants.TradeAccountType;
import constants.Transaction;
import pojo.CashAccount;
import pojo.MarginAccount;
import repository.TradeAccountRepository;
import service.TradeAccountService;
import service.CashAccountService;
import service.MarginAccountService;

public class Main {

        static Path[] paths = new Path[] {Paths.get("data/accounts.txt"), Paths.get("data/transactions.txt")};


    static TradeAccountRepository tradeAccountRepository = new TradeAccountRepository();
    static CashAccountService cashAccountService = new CashAccountService(tradeAccountRepository);
    static MarginAccountService marginAccountService = new MarginAccountService(tradeAccountRepository);



public static void loadTradeAccounts() {

    try {
        Files.lines(paths[0])
            .map(line -> line.split("\\s+"))
            .forEach(parts -> {
            String accountType = parts[0];
            String accountNumber = parts[1];
            BigDecimal balance = new BigDecimal(parts[2]);
        if (accountType.equals(TradeAccountType.CASH.toString())) {
            CashAccount cashAccount = new CashAccount(accountNumber, balance);
            cashAccountService.createTradeAccount(cashAccount);
        }
        else if (accountType.equals(TradeAccountType.MARGIN.toString())) {
            MarginAccount marginAccount = new MarginAccount(accountNumber, balance);
            marginAccountService.createTradeAccount(marginAccount);
        }
    
    });
}
    catch (IOException e) {
        e.printStackTrace();
    }
}

public static void applyTransactions() {
    try {
        Files.lines(paths[1])
            .map(line -> line.split("\\s+"))
            .forEach(parts -> {
                String accountType = parts[0];
                String accountNumber = parts[1];
                String transaction = parts[2];
                BigDecimal amount = new BigDecimal(parts[3]);
                if (accountType.equals(TradeAccountType.CASH.toString())) {
                if (transaction.equals(Transaction.DEPOSIT.toString())) {
                    cashAccountService.deposit(accountNumber, amount);
                }
                else if (transaction.equals(Transaction.WITHDRAWAL.toString())) {
                    cashAccountService.withdraw(accountNumber, amount);
                }
            }
            else if (accountType.equals(TradeAccountType.MARGIN.toString())) {
                if (transaction.equals(Transaction.DEPOSIT.toString())) {
                    marginAccountService.deposit(accountNumber, amount);
                }
                else if (transaction.equals(Transaction.WITHDRAWAL.toString())) {
                    marginAccountService.withdraw(accountNumber, amount);
                } }
            
            });
    }
    catch (IOException e) {
        e.printStackTrace();
    }

} 


public static void finalTest() throws IOException {
    System.out.println("Account A1234B Cash Balance: " + cashAccountService.retrieveTradeAccount("A1234B").getCashBalance());
    System.out.println("Account E3456F Cash Balance: " + cashAccountService.retrieveTradeAccount("E3456F").getCashBalance());
    System.out.println("Account I5678J Cash Balance: " + cashAccountService.retrieveTradeAccount("I5678J").getCashBalance());
    System.out.println("Account C2345D Margin: " + marginAccountService.retrieveTradeAccount("C2345D").getMargin());
    System.out.println("Account G4567H Margin: " + marginAccountService.retrieveTradeAccount("G4567H").getMargin());
}

 public static void main(String[] args)  {
    try {
        loadTradeAccounts();
        applyTransactions();
        finalTest();
    } catch (IOException exception) {
        System.out.println(exception.getMessage());
    }


}







































}












    

