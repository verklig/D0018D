package boleri2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class contains the account information, methods to deposit, withdraw and calculate interest, as well as formatted toString methods.
 * @author Eric Blohm, boleri-2
 */
public abstract class Account implements Serializable
{
	// Program version
	private static final long serialVersionUID = 1L;
	
	// Instance variables and list of transactions
    private int accountNum;
    private String accountType;
    private String pNo;
    protected BigDecimal balance;
    protected List<Transaction> transactions;

    // Default constructor of the class
    public Account(int accountNum, String accountType, String pNo)
    {
        this.accountNum = accountNum;
        this.accountType = accountType;
        this.pNo = pNo;
        
        // BigDecimal class used to contain exact values
        balance = new BigDecimal("0.0");
        transactions = new ArrayList<>();
    }
    
    /**
     * Gets the account number
     * @return the account number as an integer
     */
    public int getAccountNumber()
    {
        return accountNum;
    }

    /**
     * Gets the account type
     * @return the account type as a string
     */
    public String getAccountType()
    {
        return accountType;
    }

    /**
     * Gets the account balance
     * @return the account balance as an object of BigDecimal
     */
    public BigDecimal getBalance()
    {
        return balance;
    }

    /**
     * An abstract method to get the interest rate depending on the account type
     * @return the account interest rate as an object of BigDecimal
     */
    public abstract BigDecimal getInterestRate();

    /**
     * Gets the customer personal number
     * @return the customer personal number as a string
     */
    public String getCustomerPersonalNumber()
    {
        return pNo;
    }
    
    /**
     * Gets the list of transactions
     * @return the list of transactions as a list
     */
    public List<Transaction> getTransactions()
    {
        return transactions;
    }
    
    /**
     * Gets the formatted transactions
     * @param transaction contains one transaction at a time that is formatted
     * @return the formatted transaction as a String
     */
    public String getFormattedTransaction(Transaction transaction)
    {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
        String amount = currencyFormatter.format(transaction.getAmount());
        String balanceAfter = currencyFormatter.format(transaction.getBalanceAfterTransaction());
        String formattedTransaction = transaction.getFormattedDateTime() +
                " " + amount + " Saldo: " + balanceAfter;

        return formattedTransaction;
    }

    /**
     * Deposits to the account
     * @param amount contains the amount to deposit
     */
    public void deposit(BigDecimal amount)
    {
        if (amount.compareTo(BigDecimal.ZERO) > 0)
        {
        	balance = balance.add(amount);
        	recordTransaction(amount, "Deposit");
        }
    }

    /**
     * An abstract method to withdraw from an account depending on the account type
     * @param amount contains the amount to withdraw
     * @return true or false
     */
    public abstract boolean withdraw(BigDecimal amount);
    
    /**
     * An abstract method to calculate the interest rate depending on the account type
     * @return the interest rate as a formatted String
     */
    public abstract String calculateInterest();

    /**
     * Calculates the interest rate of all the accounts of a customer
     * @param totalBalanceOfAllAccounts contains the balance of all the accounts
     * @return the interest rate of the accounts as an object of BigDecimal
     */
    public BigDecimal calculateInterestAll(BigDecimal totalBalanceOfAllAccounts)
    {
        if (totalBalanceOfAllAccounts.compareTo(BigDecimal.ZERO) == 0)
        {
            return BigDecimal.ZERO; // Return zero interest if totalBalanceOfAllAccounts is zero
        }

        BigDecimal interest = balance.divide(totalBalanceOfAllAccounts, 2, RoundingMode.HALF_UP)
                                   .multiply(getInterestRate().divide(new BigDecimal("100")))
                                   .setScale(2, RoundingMode.HALF_UP); // Round to 2 decimals

        return interest;
    }

    /**
     * Records the transactions when they happen and stores them in a list
     * @param amount contains the amount in the transaction
     * @param transactionType contains the transaction type (deposit or withdrawal) to define if amount is positive or negative
     */
    public void recordTransaction(BigDecimal amount, String transactionType)
    {
        Transaction transaction = new Transaction(new Date(), amount, balance, transactionType);
        transactions.add(transaction);
    }

    /**
     * An edited toString method formatted to contain the account details
     * @param interest contains the interest rate of the accounts
     * @return the formatted account details as a string
     */
    public String toStringWithInterest(BigDecimal interest)
    {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
        String balanceStr = currencyFormatter.format(balance);

        return accountNum + " " + balanceStr + " " + accountType + " " + calculateInterest();
    }
    
    /**
     * An edited toString method formatted to contain the account details with sum of interest instead of percentage
     * @param interest contains the interest rate of the accounts
     * @return the formatted account details as a string
     */
    public String toStringWithInterestSum(BigDecimal interest)
    {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
        String balanceStr = currencyFormatter.format(balance);
        String formattedInterestSum = currencyFormatter.format(interest);

        return accountNum + " " + balanceStr + " " + accountType + " " + formattedInterestSum;
    }
}
