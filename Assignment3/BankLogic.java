package boleri2;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This class handles the bank logic, it can create and edit both customers and accounts and stores them as lists.
 * @author Eric Blohm, boleri-2
 */
public class BankLogic
{
	// Lists and account number counter
    private List<Customer> customers;
    private List<Account> accounts;
    private static int lastAccountNumber = 1000;

    // Default constructor of the class
    public BankLogic()
    {
    	// Creates new objects of the lists when called
        customers = new ArrayList<>();
        accounts = new ArrayList<>();
    }
    
    /**
     * Gets the transactions within a specific account
     * @param pNo contains the personal number of the customer
     * @param accountNum contains the account number
     * @return the list of transactions as a list
     */
    public List<String> getTransactions(String pNo, int accountNum)
    {
        Customer customer = findCustomer(pNo);

        if (customer != null)
        {
            Account account = findAccount(accountNum);

            if (account != null && customer.hasAccount(accountNum, accounts))
            {
                List<Transaction> transactions = account.getTransactions();

                if (transactions.isEmpty())
                {
                    return new ArrayList<>();
                }

                List<String> formattedTransactions = new ArrayList<>();

                for (Transaction transaction : transactions)
                {
                    String formattedTransaction = account.getFormattedTransaction(transaction);
                    formattedTransactions.add(formattedTransaction);
                }

                return formattedTransactions;
            }
        }

        return null; // Account or customer not found
    }

    /**
     * Gets the all customers as a list of strings
     * @return the list of all customers
     */
    public List<String> getAllCustomers()
    {
        List<String> customerList = new ArrayList<>();
        
        for (Customer customer : customers)
        {
            customerList.add(customer.toString());
        }
        
        return customerList;
    }
    
    /**
     * Gets a specific customer as a list of strings
     * @param pNo contains the personal number of the customer
     * @return the list of the specific customer
     */
    public List<String> getCustomer(String pNo)
    {
        for (Customer customer : customers)
        {
        	String existingPNo = customer.getPersonalNumber();
        	
            if (existingPNo.equals(pNo))
            {
                return customer.getCustomerInfo(accounts);
            }
        }
        
        return null; // Customer not found
    }
    
    /**
     * Gets the account for the specific customer as a string
     * @param pNo contains the personal number of the customer
     * @param accountNum contains the account number
     * @return the account info as a string
     */
    public String getAccount(String pNo, int accountNum)
    {
        for (Customer customer : customers)
        {
        	String existingPNo = customer.getPersonalNumber();
        	
            if (existingPNo.equals(pNo))
            {
                return customer.getAccountInfo(accountNum, accounts);
            }
        }
        
        return null; // Customer not found or account doesn't belong to the customer
    }
    
    /**
     * Gets the account balance to be displayed on the GUI
     * @param accountNum contains the account number
     * @return the account balance
     */
    public BigDecimal getAccountBalance(int accountNum)
    {
        Account account = findAccount(accountNum);

        if (account != null)
        {
            return account.getBalance();
        }

        return null; // Account not found
    }
    
    /**
     * Gets the customers account numbers as Strings for validation in the GUI class
     * @param pNo contains the personal number of the customer
     * @return the account numbers for a specific customer as a String
     */
    public List<String> getCustomerAccountNumbers(String pNo)
    {
        for (Customer customer : customers)
        {
            String existingPNo = customer.getPersonalNumber();

            if (existingPNo.equals(pNo))
            {
                List<String> accountNumbers = new ArrayList<>();
                
                for (Account account : accounts)
                {
                    if (account.getCustomerPersonalNumber().equals(pNo))
                    {
                        accountNumbers.add(String.valueOf(account.getAccountNumber()));
                    }
                }
                return accountNumbers;
            }
        }

        return null; // Customer not found
    }

    /**
     * Creates a customer with the information given as parameters
     * @param firstName contains the first name of the customer
     * @param surName contains the surname of the customer
     * @param pNo contains the personal number of the customer
     * @return true or false
     */
    public boolean createCustomer(String firstName, String surName, String pNo)
    {
        for (Customer customer : customers)
        {
        	String existingPNo = customer.getPersonalNumber();
        	
            if (existingPNo.equals(pNo))
            {
                return false; // Customer with the same personal number already exists
            }
        }

        Customer newCustomer = new Customer(firstName, surName, pNo);
        customers.add(newCustomer);
        
        return true;
    }

    /**
     * Changes the specific customer's name with the given parameters
     * @param firstName contains the first name of the customer
     * @param surName contains the surname of the customer
     * @param pNo contains the personal number of the customer
     * @return true or false
     */
    public boolean changeCustomerName(String firstName, String surName, String pNo)
    {
        for (Customer customer : customers)
        {
        	String existingPNo = customer.getPersonalNumber();
        	
            if (existingPNo.equals(pNo))
            {
            	if (!firstName.isEmpty() || !surName.isEmpty())
            	{
                	if (!firstName.isEmpty())
                	{
                		customer.setFirstName(firstName);
                	}
                	if (!surName.isEmpty())
                	{
                        customer.setSurName(surName);
                	}
                	
                    return true;
            	}
            }
        }
        
        return false; // Customer not found or both name fields are empty
    }

    /**
     * Creates a savings account for the specific customer
     * @param pNo contains the personal number of the customer
     * @return the account number as an integer, or -1 if not found
     */
    public int createSavingsAccount(String pNo)
    {
        for (Customer customer : customers)
        {
        	String existingPNo = customer.getPersonalNumber();
        	
            if (existingPNo.equals(pNo))
            {
            	int accountNumber = getNextAccountNumber();
                Account newAccount = new SavingsAccount(accountNumber, pNo);
                accounts.add(newAccount);
                
                return accountNumber;
            }
        }
        
        return -1; // Customer not found
    }
    
    /**
     * Creates a credit account for the specific customer
     * @param pNo contains the personal number of the customer
     * @return the account number as an integer, or -1 if not found
     */
    public int createCreditAccount(String pNo)
    {				
        for (Customer customer : customers)
        {
        	String existingPNo = customer.getPersonalNumber();
        	
            if (existingPNo.equals(pNo))
            {
            	int accountNumber = getNextAccountNumber();
                Account newAccount = new CreditAccount(accountNumber, pNo);
                accounts.add(newAccount);
                
                return accountNumber;
            }
        }
        
        return -1; // Customer not found
    }

    /**
     * Deposits to a specific account that a customer owns
     * @param pNo contains the personal number of the customer
     * @param accountNum contains the account number
     * @param amount contains the amount to deposit
     * @return true or false
     */
    public boolean deposit(String pNo, int accountNum, int amount)
    {
        if (amount <= 0)
        {
            return false; // Invalid deposit amount
        }

        Customer customer = findCustomer(pNo);
        
        if (customer != null)
        {
            Account account = findAccount(accountNum);
            
            if (account != null && customer.hasAccount(accountNum, accounts))
            {
                BigDecimal depositAmount = BigDecimal.valueOf(amount);
                account.deposit(depositAmount);
                
                return true;
            }
        }
        
        return false; // Customer not found, account doesn't belong to the customer, or account not found
    }

    /**
     * Withdraws to a specific account that a customer owns
     * @param pNo contains the personal number of the customer
     * @param accountNum contains the account number
     * @param amount contains the amount to withdraw
     * @return true or false
     */
    public boolean withdraw(String pNo, int accountNum, int amount)
    {
        if (amount <= 0)
        {
            return false; // Invalid withdrawal amount
        }

        Customer customer = findCustomer(pNo);
        
        if (customer != null)
        {
            Account account = findAccount(accountNum);

            if (account != null && customer.hasAccount(accountNum, accounts))
            {
                BigDecimal withdrawalAmount = BigDecimal.valueOf(amount);
                
                return account.withdraw(withdrawalAmount);
            }
        }
        
        return false; // Customer not found, or account doesn't belong to the customer
    }

    /**
     * Closes an account that a customer owns
     * @param pNo contains the personal number of the customer
     * @param accountNum contains the account number
     * @return the closed account info as a formatted string
     */
    public String closeAccount(String pNo, int accountNum)
    {
        Customer customer = findCustomer(pNo);
        
        if (customer != null)
        {
            Account account = findAccount(accountNum);

            if (account != null && customer.hasAccount(accountNum, accounts))
            {
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
                String formattedBalance = currencyFormatter.format(account.getBalance());
                
                BigDecimal interest = (account.getBalance().multiply(account.getInterestRate())).divide(BigDecimal.valueOf(100));
                String formattedInterestSum = currencyFormatter.format(interest);
                
                accounts.remove(account);
                return account.getAccountNumber() + " " + formattedBalance + " " + account.getAccountType() + " " + formattedInterestSum;
            }
        }
        
        return null; // Customer not found, account doesn't belong to the customer, or account not found
    }

    /**
     * Deletes a customer
     * @param pNo contains the personal number of the customer
     * @return the deleted customer and closed account info as a formatted string
     */
    public List<String> deleteCustomer(String pNo)
    {
        for (Customer customer : customers)
        {
            String existingPNo = customer.getPersonalNumber();
            
            if (existingPNo.equals(pNo))
            {
                List<String> deletedCustomerInfo = new ArrayList<>();
                deletedCustomerInfo.add(customer.toString());

                List<Account> customerAccounts = new ArrayList<>(accounts);
                BigDecimal totalBalanceOfAllAccounts = BigDecimal.ZERO;
                
                for (Account account : customerAccounts)
                {
                	String accountPNo = account.getCustomerPersonalNumber();
                	
                    if (accountPNo.equals(pNo))
                    {
                        BigDecimal balance = account.getBalance();
                        BigDecimal interest = (balance.multiply(account.getInterestRate())).divide(BigDecimal.valueOf(100));
                        totalBalanceOfAllAccounts = totalBalanceOfAllAccounts.add(balance);
                        
                        deletedCustomerInfo.add(account.toStringWithInterestSum(interest));
                        accounts.remove(account);
                    }
                }

                customers.remove(customer);
                return deletedCustomerInfo;
            }
        }
        
        return null; // Customer not found
    }

    /**
     * A helper method to find a specific account
     * @param accountNum contains the account number
     * @return either the account object or null
     */
    private Account findAccount(int accountNum)
    {
        for (Account account : accounts)
        {
            if (account.getAccountNumber() == accountNum)
            {
                return account;
            }
        }
        
        return null; // Account not found
    }
    
    /**
     * A helper method to find a specific customer
     * @param pNo contains the personal number of the customer
     * @return either the customer object or null
     */
    private Customer findCustomer(String pNo)
    {
        for (Customer customer : customers)
        {
        	String existingPNo = customer.getPersonalNumber();
            if (existingPNo.equals(pNo))
            {
                return customer;
            }
        }
        
        return null; // Customer not found
    }

    /**
     * A helper method to change to the next account number
     * @return the next account number in order
     */
    private int getNextAccountNumber()
    {
        lastAccountNumber++;
        return lastAccountNumber;
    }
}
