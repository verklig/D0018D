package boleri2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This class is an extension of the Account class and contains more specific methods for a savings account.
 * @author Eric Blohm, boleri-2
 */
public class SavingsAccount extends Account implements Serializable
{
	// Program version
	private static final long serialVersionUID = 1L;
	
	// Instance variables
    private int freeWithdrawals;
    private BigDecimal withdrawalFeeRate;
    private BigDecimal interestRate;

    // Default constructor of the class
    public SavingsAccount(int accountNum, String pNo)
    {
        super(accountNum, "Savings Account", pNo);
        
        freeWithdrawals = 1;
        withdrawalFeeRate = new BigDecimal("0.02");
        interestRate = new BigDecimal("1.2");
    }
    
    /**
     * Gets the interest rate of the savings account
     * @return the account interest rate as an object of BigDecimal
     */
    @Override
    public BigDecimal getInterestRate()
    {
    	return interestRate;
    }

    /**
     * The withdrawal method specific to the savings account
     * @return true or false
     */
    @Override
    public boolean withdraw(BigDecimal amount)
    {
        BigDecimal withdrawalAmount = amount;

        if (freeWithdrawals == 0)
        {
            BigDecimal fee = amount.multiply(withdrawalFeeRate);
            withdrawalAmount = withdrawalAmount.add(fee);
        }
    	
        if (freeWithdrawals > 0)
        {
            freeWithdrawals--;
        }
        
        if (balance.compareTo(withdrawalAmount) >= 0)
        {
            balance = balance.subtract(withdrawalAmount);
            recordTransaction(withdrawalAmount, "Withdrawal");
            
            return true;
        }
        
        return false;
    }

    /**
     * Calculates the interest and formats it
     * @return the interest as a formatted String
     */
    @Override
    public String calculateInterest()
    {
        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv", "SE"));
        percentFormat.setMaximumFractionDigits(1); // Set maximum fraction digits to 1
        String percentStr = percentFormat.format(interestRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
        
        return percentStr;
    }
}
