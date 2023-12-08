package boleri2;

import java.io.Serializable;
import java.util.List;

public class DataBundle implements Serializable
{
	// Program version
	private static final long serialVersionUID = 1L;
	
	private List<Customer> customers;
    private List<Account> accounts;
    private int lastAccountNumber;

    public DataBundle(List<Customer> customers, List<Account> accounts, int lastAccountNumber)
    {
        this.customers = customers;
        this.accounts = accounts;
        this.lastAccountNumber = lastAccountNumber;
    }

    public List<Customer> getCustomers()
    {
        return customers;
    }

    public List<Account> getAccounts()
    {
        return accounts;
    }

    public int getLastAccountNumber()
    {
        return lastAccountNumber;
    }
}
