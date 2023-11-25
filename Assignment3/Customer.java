package boleri2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the customer information, methods to get customer and account info and a formatted toString method.
 * @author Eric Blohm, boleri-2
 */
public class Customer
{
	// Instance variables
    private String firstName;
    private String surName;
    private String pNo;
    
    // Default constructor of the class
    public Customer(String firstName, String surName, String pNo)
    {
        this.firstName = firstName;
        this.surName = surName;
        this.pNo = pNo;
    }
    
    /**
     * Sets the first name of the customer
     * @param firstName contains the first name of the customer
     */
    public void setFirstName(String firstName)
    {
    	this.firstName = firstName;
    }
    
    /**
     * Sets the surname of the customer
     * @param surName contains the surname of the customer
     */
    public void setSurName(String surName)
    {
    	this.surName = surName;
    }
    
    /**
     * Gets the personal number of the customer
     * @return a string with the personal number
     */
    public String getPersonalNumber()
    {
    	return pNo;
    }
    
    /**
     * Gets the customer information
     * @param allAccounts contains all the accounts associated with the customer
     * @return a list of the customer info as strings
     */
    public List<String> getCustomerInfo(List<Account> allAccounts)
    {
        List<String> customerInfo = new ArrayList<>();
        customerInfo.add(pNo + " " + firstName + " " + surName);
        
        BigDecimal totalBalanceOfAllAccounts = BigDecimal.ZERO; // Sets the total balance to 0 before iteration
        
        for (Account account : allAccounts)
        {
        	String existingPNo = account.getCustomerPersonalNumber();
            if (existingPNo.equals(pNo))
            {
                totalBalanceOfAllAccounts = totalBalanceOfAllAccounts.add(account.getBalance());
                BigDecimal interest = account.calculateInterestAll(totalBalanceOfAllAccounts);
                customerInfo.add(account.toStringWithInterest(interest));
            }
        }

        return customerInfo;
    }

    /**
     * Gets the account information
     * @param accountNum contains the account number
     * @param allAccounts contains all the accounts associated with the customer
     * @return a string with the account info
     */
    public String getAccountInfo(int accountNum, List<Account> allAccounts)
    {
    	BigDecimal totalBalanceOfAllAccounts = BigDecimal.ZERO; // Sets the total balance to 0 before iteration
    	
        for (Account account : allAccounts)
        {
        	String existingPNo = account.getCustomerPersonalNumber();
            if (account.getAccountNumber() == accountNum && existingPNo.equals(pNo))
            {
                totalBalanceOfAllAccounts = totalBalanceOfAllAccounts.add(account.getBalance());
                BigDecimal interest = account.calculateInterestAll(totalBalanceOfAllAccounts);
                
                return account.toStringWithInterest(interest);
            }
        }
        
        return null; // Account not found or account doesn't belong to the customer
    }
    
    /**
     * Checks if the customer has an account or not
     * @param accountNum contains the account number
     * @param accounts contains a list with the accounts
     * @return true or false
     */
    public boolean hasAccount(int accountNum, List<Account> accounts)
    {
        for (Account account : accounts)
        {
        	String existingPNo = account.getCustomerPersonalNumber();
            if (account.getAccountNumber() == accountNum && existingPNo.equals(pNo))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Overrides the default toString method to contain a formatted string
     * @return a formatted string
     */
    @Override
    public String toString()
    {
        return pNo + " " + firstName + " " + surName;
    }
}
