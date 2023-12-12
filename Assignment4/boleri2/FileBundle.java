package boleri2;

import java.io.Serializable;
import java.util.List;

/**
 * This class bundles the necessary lists and variables that are needed when saving the program state.
 * @author Eric Blohm, boleri-2
 */
public class FileBundle implements Serializable
{
	// Program version
	private static final long serialVersionUID = 1L;
	
	// Lists and account number counter
	private List<Customer> customers;
    private List<Account> accounts;
    private int lastAccountNumber;

    // Default constructor of the class
    public FileBundle(List<Customer> customers, List<Account> accounts, int lastAccountNumber)
    {
        this.customers = customers;
        this.accounts = accounts;
        this.lastAccountNumber = lastAccountNumber;
    }
    
    /**
     * Gets the list of customers
     * @return the list of customers
     */
    public List<Customer> getCustomers()
    {
        return customers;
    }

    /**
     * Gets the list of accounts
     * @return the list of accounts
     */
    public List<Account> getAccounts()
    {
        return accounts;
    }

    /**
     * Gets the last account number
     * @return the last account number
     */
    public int getLastAccountNumber()
    {
        return lastAccountNumber;
    }
}