package boleri2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This class contains the account information, methods to deposit, withdraw and calculate interest, as well as formatted toString methods.
 * @author Eric Blohm, boleri-2
 */
public class Account
{
	// Instance variables
    private int accountNum;
    private String accountType;
    private String pNo;
    private BigDecimal balance;
    private BigDecimal interestRate;

    // Default constructor of the class
    public Account(int accountNum, String accountType, String pNo)
    {
        this.accountNum = accountNum;
        this.accountType = accountType;
        this.pNo = pNo;
        
        // BigDecimal class used to contain exact values
        balance = new BigDecimal("0.0");
        interestRate = new BigDecimal("1.2");
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
     * Gets the account interest rate
     * @return the account interest rate as an object of BigDecimal
     */
    public BigDecimal getInterestRate()
    {
        return interestRate;
    }

    /**
     * Gets the customer personal number
     * @return the customer personal number as a string
     */
    public String getCustomerPersonalNumber()
    {
        return pNo;
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
        }
    }

    /**
     * Withdraws from the account
     * @param amount contains the amount to withdraw
     */
    public void withdraw(BigDecimal amount)
    {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0)
        {
        	balance = balance.subtract(amount);
        }
    }

    /**
     * Calculates the interest rate of the account
     * @param totalBalanceOfAllAccounts contains the balance of all the accounts
     * @return the interest rate of the account as an object of BigDecimal
     */
    public BigDecimal calculateInterest(BigDecimal totalBalanceOfAllAccounts)
    {
        if (totalBalanceOfAllAccounts.compareTo(BigDecimal.ZERO) == 0)
        {
            return BigDecimal.ZERO; // Return zero interest if totalBalanceOfAllAccounts is zero
        }

        BigDecimal interest = balance.divide(totalBalanceOfAllAccounts, 2, RoundingMode.HALF_UP)
                                   .multiply(interestRate.divide(new BigDecimal("100")))
                                   .setScale(2, RoundingMode.HALF_UP); // Round to 2 decimals

        return interest;
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

        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv", "SE"));
        percentFormat.setMaximumFractionDigits(1); // Set maximum fraction digits to 1
        String percentStr = percentFormat.format(interestRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
        // Not sure why 3 is needed instead of 1

        return accountNum + " " + balanceStr + " " + accountType + " " + percentStr;
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
