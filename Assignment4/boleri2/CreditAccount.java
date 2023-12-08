package boleri2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This class is an extension of the Account class and contains more specific methods for a credit account.
 * @author Eric Blohm, boleri-2
 */
public class CreditAccount extends Account implements Serializable
{
	// Program version
	private static final long serialVersionUID = 1L;
	
	// Instance variables
    private BigDecimal creditLimit;
    private BigDecimal debtInterestRate;
    private BigDecimal normalInterestRate;

    // Default constructor of the class
    public CreditAccount(int accountNum, String pNo)
    {
        super(accountNum, "Credit Account", pNo);
        
        creditLimit = new BigDecimal("5000.0");
        debtInterestRate = new BigDecimal("7.0");
        normalInterestRate = new BigDecimal("0.5");
    }
    
    /**
     * Gets the interest rate depending on if the account is in debt or not
     * @return the account interest rate as an object of BigDecimal
     */
    @Override
    public BigDecimal getInterestRate()
    {
    	if (balance.compareTo(BigDecimal.ZERO) < 0) 
    	{
    		return debtInterestRate;
    	}
    	else
    	{
    		return normalInterestRate;
    	}
    }

    /**
     * The withdrawal method specific to the credit account
     * @return true or false
     */
    @Override
    public boolean withdraw(BigDecimal amount)
    {
        BigDecimal remainingBalance = balance.subtract(amount);
        
        if (remainingBalance.abs().compareTo(creditLimit) <= 0)
        {
            balance = remainingBalance;
            recordTransaction(amount, "Withdrawal");
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Calculates the interest and formats it depending on if the account is in debt or not
     * @return the interest as a formatted String
     */
    @Override
    public String calculateInterest()
    {
        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv", "SE"));
        percentFormat.setMaximumFractionDigits(1); // Set maximum fraction digits to 1
        
        if (balance.compareTo(BigDecimal.ZERO) < 0)
        {
            return percentFormat.format(debtInterestRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
        }
        else
        {
            return percentFormat.format(normalInterestRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
        }
    }
}