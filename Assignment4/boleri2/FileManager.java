package boleri2;

import java.io.*;
import java.util.List;

public class FileManager
{
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