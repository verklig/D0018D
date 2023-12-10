package boleri2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class keeps track of the transactions and is stored as a list in the Account class.
 * @author Eric Blohm, boleri-2
 */
public class Transaction implements Serializable
{
	// Program version
	private static final long serialVersionUID = 1L;
	
	// Instance variables
    private Date date;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
    private String transactionType;

    // Default constructor of the class
    public Transaction(Date date, BigDecimal amount, BigDecimal balanceAfterTransaction, String transactionType)
    {
        this.date = date;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.transactionType = transactionType;
    }

    /**
     * Gets the formatted date time of the transaction
     * @return the date as a formatted String
     */
    public String getFormattedDateTime()
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String strDate = sdf.format(date);
    	
        return strDate;
    }

    /**
     * Gets the amount of the transaction
     * @return the transaction amount as an object of BigDecimal
     */
    public BigDecimal getAmount()
    {
    	if (transactionType.equals("Withdrawal"))
    	{
    		return amount.negate();
    	}
    	else if (transactionType.equals("Deposit"))
    	{
    		return amount;
    	}
    	
    	return null;
    }

    /**
     * Gets the balance after the transaction
     * @return the balance after the transaction as an object of BigDecimal
     */
    public BigDecimal getBalanceAfterTransaction()
    {
        return balanceAfterTransaction;
    }
}