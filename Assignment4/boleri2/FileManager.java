package boleri2;

import java.io.*;
import java.util.List;

/**
 * This class manages the files (reads and writes them), it uses the FileBundle class to get the relevant information.
 * @author Eric Blohm, boleri-2
 */
public class FileManager
{
    /**
     * Saves the data to a file
     * @param fileName contains the file name
     * @param customers contains the list of customers
     * @param accounts contains the list of accounts
     * @param lastAccountNumber contains the last account number
     * @return true or false
     */
    public boolean saveData(String fileName, List<Customer> customers, List<Account> accounts, int lastAccountNumber)
    {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
        	outputStream.writeObject(new FileBundle(customers, accounts, lastAccountNumber));
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads the data from a file
     * @param fileName contains the file name
     * @return the FileBundle object
     */
    public FileBundle loadData(String fileName)
    {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName)))
        {
            return (FileBundle)inputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Saves the transactions to a file
     * @param fileName contains the file name
     * @param transactions contains the list of transactions
     * @return true or false
     */
    public boolean saveTransactions(String fileName, List<String> transactions)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for (String transaction : transactions)
            {
                writer.write(transaction);
                writer.newLine();
            }
            
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}