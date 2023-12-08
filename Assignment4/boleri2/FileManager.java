package boleri2;

import java.io.*;
import java.util.List;

public class FileManager
{
    public boolean saveData(String fileName, List<Customer> customers, List<Account> accounts, int lastAccountNumber)
    {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
        	outputStream.writeObject(new DataBundle(customers, accounts, lastAccountNumber));
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public DataBundle loadData(String fileName)
    {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName)))
        {
            return (DataBundle)inputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public void saveTransactions(String fileName, Account account)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for (Transaction transaction : account.getTransactions())
            {
                writer.write(transaction.toString());
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}