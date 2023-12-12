package boleri2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * This class handles and creates the GUI that contains the necessary fields and methods to make a user friendly program.
 * @author Eric Blohm, boleri-2
 */
public class GUI extends JFrame
{
	// Program version
	private static final long serialVersionUID = 1L;
	
	// I chose to initialize everything at instance level so that I
	// can break everything out to their own methods
	private BankLogic bankLogic;
	private FileManager fileManager;
    private DefaultListModel<String> customerListModel;
    private JList<String> customerList;
    private JComboBox<String> accountComboBox;
    private JLabel balanceLabel;
    private JButton createCustomerButton;
    private JButton changeNameButton;
    private JButton createSavingsAccButton;
    private JButton createCreditAccButton;
    private JButton withdrawButton;
    private JButton depositButton;
    private JButton closeAccountButton;
    private JButton deleteCustomerButton;
    private JButton clearSelectionButton;
    
    // The main method that gets executed by the compiler
    public static void main(String[] args)
    {
    	// Setting default language to English
    	Locale.setDefault(Locale.ENGLISH);
    	
    	// Using lambda expressions for a more concise syntax and better readability
    	// This also goes for creating events for buttons and such
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }

    // Default constructor of the class
    public GUI()
    {
        bankLogic = new BankLogic();
        fileManager = new FileManager();
        customerListModel = new DefaultListModel<>();
        customerList = new JList<>(customerListModel);
        accountComboBox = new JComboBox<>();
        balanceLabel = new JLabel("");
        
        initializeComponents();
    }

    /**
     * Initializes the components of the GUI
     */
    private void initializeComponents()
    {
    	// Trying to set the UI to look like the systems UI, prints the error and uses default UI otherwise
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        
        // Set custom colors for the UIManager
        UIManager.put("Button.background", new Color(220, 220, 220, 255));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Label.foreground", Color.BLACK);
        
        // Button to add customer
        createCustomerButton = new JButton("Create New Customer");
        createCustomerButton.addActionListener(e ->
        {
        	createNewCustomer();
        });
        
        // Button to change customer name
        changeNameButton = new JButton("Change Customer Name");
        changeNameButton.addActionListener(e ->
        {
        	changeCustomerName();
        });
        
        // Button to create a savings account
        createSavingsAccButton = new JButton("Create Savings Account");
        createSavingsAccButton.addActionListener(e ->
        {
        	createSavingsAccount();
        });
        
        // Button to create a credit account
        createCreditAccButton = new JButton("Create Credit Account");
        createCreditAccButton.addActionListener(e ->
        {
        	createCreditAccount();
        });
        
        // Button to withdraw from an account
        withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e ->
        {
        	withdraw();
        });
        
        // Button to deposit to an account
        depositButton = new JButton("Deposit");
        depositButton.addActionListener(e ->
        {
        	deposit();
        });
        
        // Button to delete account
        closeAccountButton = new JButton("Close Account");
        closeAccountButton.addActionListener(e ->
        {
        	closeAccount();
        });
        
        // Button to delete customer
        deleteCustomerButton = new JButton("Delete Customer");
        deleteCustomerButton.addActionListener(e ->
        {
        	deleteCustomer();
        });
        
        // Button to clear selection
        clearSelectionButton = new JButton("Clear Selection");
        clearSelectionButton.addActionListener(e ->
        {
            customerList.clearSelection();
            accountComboBox.setSelectedIndex(-1);
            accountComboBox.removeAllItems();
            balanceLabel.setText("");
            
            resetAllButtons();
        });
        
        // Event to update the account list when a customer is chosen
        customerList.addListSelectionListener(e ->
        {
        	if (customerList.getSelectedIndex() != -1)
        	{
                changeNameButton.setEnabled(true);
                createSavingsAccButton.setEnabled(true);
                createCreditAccButton.setEnabled(true);
                deleteCustomerButton.setEnabled(true);
                clearSelectionButton.setEnabled(true);
        		
            	updateAccountList();
            	updateAccountButtons();
        	}
        });
        
        // Event to update the balance label when an account is chosen
        accountComboBox.addActionListener(e ->
        {
            String selectedAccount = (String) accountComboBox.getSelectedItem();
            if (selectedAccount != null)
            {
            	updateBalanceLabel();
            }
        });
        
        // Creating the menu bar
        createMenuBar();
        
        // Setting the icon
        setIcon();
        
        // Changing properties of the GUI
        setTitle("Bank GUI");
        setSize(800, 440);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        
        // Creating a container for the JScrollPane
        JPanel scrollPaneContainer = new JPanel();
        scrollPaneContainer.setLayout(null);
        scrollPaneContainer.setBounds(10, 10, 575, 200);

        // Adding the JScrollPane to the container
        JScrollPane scrollPane = new JScrollPane(customerList);
        scrollPane.setBounds(0, 0, 575, 200);
        scrollPaneContainer.add(scrollPane);
        
        // Setting the absolute positions of the elements on the GUI
        accountComboBox.setBounds(595, 10, 180, 30);
        balanceLabel.setBounds(595, 180, 180, 30);
        createCustomerButton.setBounds(10, 220, 180, 30);
        changeNameButton.setBounds(10, 260, 180, 30);
        createSavingsAccButton.setBounds(10, 300, 180, 30);
        createCreditAccButton.setBounds(10, 340, 180, 30);
        withdrawButton.setBounds(200, 220, 180, 30);
        depositButton.setBounds(200, 260, 180, 30);
        closeAccountButton.setBounds(200, 300, 180, 30);
        deleteCustomerButton.setBounds(200, 340, 180, 30);
        clearSelectionButton.setBounds(463, 220, 120, 30);
        
        // Setting buttons to be disabled as default except creating a customer
        createCustomerButton.setEnabled(true);
        changeNameButton.setEnabled(false);
        createSavingsAccButton.setEnabled(false);
        createCreditAccButton.setEnabled(false);
        withdrawButton.setEnabled(false);
        depositButton.setEnabled(false);
        closeAccountButton.setEnabled(false);
        deleteCustomerButton.setEnabled(false);
        clearSelectionButton.setEnabled(false);
        accountComboBox.setEnabled(false);

        // Adding elements to the GUI
        add(scrollPaneContainer);
        add(accountComboBox);
        add(balanceLabel);
        add(createCustomerButton);
        add(changeNameButton);
        add(createSavingsAccButton);
        add(createCreditAccButton);
        add(withdrawButton);
        add(depositButton);
        add(closeAccountButton);
        add(deleteCustomerButton);
        add(clearSelectionButton);
    }
    
    /**
     * Simple method to reuse the no customer selected message
     */
    private void noCustomerSelectedMessage()
    {
    	JOptionPane.showMessageDialog(this, "No Customer selected.", "Alert", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Simple method to reuse the no customer/account selected message
     */
    private void noCustomerOrAccountSelectedMessage()
    {
    	JOptionPane.showMessageDialog(this, "No Customer/Account selected.", "Alert", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Resets all the buttons to their default state
     */
    private void resetAllButtons()
    {
    	createCustomerButton.setEnabled(true);
        changeNameButton.setEnabled(false);
        createSavingsAccButton.setEnabled(false);
        createCreditAccButton.setEnabled(false);
        withdrawButton.setEnabled(false);
        depositButton.setEnabled(false);
        closeAccountButton.setEnabled(false);
        deleteCustomerButton.setEnabled(false);
        clearSelectionButton.setEnabled(false);
        accountComboBox.setEnabled(false);
    }
    
    /**
     * Updates the buttons associated with an account
     */
    private void updateAccountButtons()
    {
    	boolean comboBoxEmpty = accountComboBox.getItemCount() == 0;
    	
        withdrawButton.setEnabled(!comboBoxEmpty);
        depositButton.setEnabled(!comboBoxEmpty);
        closeAccountButton.setEnabled(!comboBoxEmpty);
    	accountComboBox.setEnabled(!comboBoxEmpty);
    }
    
    /**
     * Updates the account list by removing the items and adding them again if they exist
     */
    private void updateAccountList()
    {
        String selectedCustomer = customerList.getSelectedValue();

        if (selectedCustomer != null)
        {
            String[] parts = selectedCustomer.split(" ");
            String pNo = parts[0];

            List<String> accountNumList = bankLogic.getCustomerAccountNumbers(pNo);

            accountComboBox.removeAllItems();
            
            for (String accountNumStr : accountNumList)
            {
                String[] accountNumParts = accountNumStr.split(" ");
                int accountNum = Integer.parseInt(accountNumParts[0]);
                
            	String accountInfo = bankLogic.getAccount(pNo, accountNum);
            	
                String[] accountInfoParts = accountInfo.split(" ");
                String accountType = accountInfoParts[2];
            	
            	accountComboBox.addItem(accountNumStr + " " + accountType);
            }
            
            updateBalanceLabel();
        }
    }
    
    /**
     * Updates the customer list by removing the items and adding them again if they exist
     */
    private void updateCustomerList()
    {
        List<String> customers = bankLogic.getAllCustomers();
        
        customerListModel.clear();

        for (String customer : customers)
        {
            customerListModel.addElement(customer);
        }
    }
    
    /**
     * Updates the balance label by setting the text to the balance if an account is selected
     */
    private void updateBalanceLabel()
    {
        String selectedAccount = (String)accountComboBox.getSelectedItem();

        if (selectedAccount != null)
        {
            String[] parts = selectedAccount.split(" ");
            int accountNum = Integer.parseInt(parts[0]);

            BigDecimal balance = bankLogic.getAccountBalance(accountNum);

            if (balance != null)
            {
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));
                String formattedBalance = currencyFormatter.format(balance);

                balanceLabel.setText("Balance: " + formattedBalance);
            }
            else
            {
                balanceLabel.setText("");
            }
        }
        else
        {
            balanceLabel.setText("");
        }
    }
    
    /**
     * Sets the icon of the GUI
     */
    private void setIcon()
    {
        try
        {
        	InputStream iconStream = getClass().getClassLoader().getResourceAsStream("boleri2_files/icon.png");
            BufferedImage icon = ImageIO.read(iconStream);
            setIconImage(icon);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates the menu bar with exit, save, load and save transactions functions
     */
    private void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        
        fileMenu.addSeparator();
        
        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        
        JMenuItem loadMenuItem = new JMenuItem("Load");
        fileMenu.add(loadMenuItem);
        
        fileMenu.addSeparator();
        
        JMenuItem saveTransactionsMenuItem = new JMenuItem("Save Transactions");
        fileMenu.add(saveTransactionsMenuItem);

        exitMenuItem.addActionListener(e ->
        {
            System.exit(0);
        });
        
        saveMenuItem.addActionListener(e ->
        {
        	saveData();
        });
        
        loadMenuItem.addActionListener(e ->
        {
        	loadData();
        });
        
        saveTransactionsMenuItem.addActionListener(e ->
        {
        	saveTransactions();
        });
    }
    
    /**
     * Creates a new customer with the inputs of the user
     */
    private void createNewCustomer()
    {
        String firstName = JOptionPane.showInputDialog(this, "Enter First Name:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (firstName == null)
        {
            return;
        }
        
        String surName = JOptionPane.showInputDialog(this, "Enter Surname:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (surName == null)
        {
            return;
        }
        
        String pNo = JOptionPane.showInputDialog(this, "Enter Personal Number:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (pNo == null)
        {
            return;
        }

        if (firstName.matches("[a-zA-ZåäöÅÄÖ]+") && surName.matches("[a-zA-ZåäöÅÄÖ]+") && isNumeric(pNo))
        {
            boolean success = bankLogic.createCustomer(firstName, surName, pNo);

            if (success)
            {
                customerList.clearSelection();
                accountComboBox.removeAllItems();
            	
            	resetAllButtons();
            	updateCustomerList();
                updateBalanceLabel();
                
                JOptionPane.showMessageDialog(this, "Customer created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Failed to create Customer. Check input or Customer already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
        	JOptionPane.showMessageDialog(this, "Failed to create Customer. Names can only consist of letters and Personal Number can only consist of numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Changes the name of a customer with the inputs of the user
     */
    private void changeCustomerName()
    {
    	String selectedCustomer = customerList.getSelectedValue();
        
        if (selectedCustomer == null)
        {
        	noCustomerSelectedMessage();
            return;
        }
        
        String newFirstName = JOptionPane.showInputDialog(this, "Enter New First Name:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (newFirstName == null)
        {
            return;
        }
        
        String newSurName = JOptionPane.showInputDialog(this, "Enter New Surname:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (newSurName == null)
        {
            return;
        }
        
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];
        
        if (newFirstName.matches("[a-zA-ZåäöÅÄÖ]+") && newSurName.matches("[a-zA-ZåäöÅÄÖ]+"))
        {
            boolean success = bankLogic.changeCustomerName(newFirstName, newSurName, pNo);
            
            if (success)
            {
                customerList.clearSelection();
                accountComboBox.removeAllItems();
            	
            	resetAllButtons();
            	updateCustomerList();
                updateBalanceLabel();
            	
                JOptionPane.showMessageDialog(this, "Customer Name changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Failed to change Customer Name. Check input or Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
        	JOptionPane.showMessageDialog(this, "Failed to change Customer Name. Names can only consist of letters.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Creates a savings account if a customer is selected
     */
    private void createSavingsAccount()
    {
    	String selectedCustomer = customerList.getSelectedValue();
        
        if (selectedCustomer == null)
        {
        	noCustomerSelectedMessage();
            return;
        }
        
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to create a new Savings Account for the selected Customer?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	return;
        }
        
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];

        int accountNum = bankLogic.createSavingsAccount(pNo);
        
        if (accountNum > 1000)
        {
        	updateAccountList();
        	updateAccountButtons();
        	
            JOptionPane.showMessageDialog(this, "Account (" + accountNum + ") created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Failed to create Account. Check input or Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Creates a credit account if a customer is selected
     */
    private void createCreditAccount()
    {
    	String selectedCustomer = customerList.getSelectedValue();
        
        if (selectedCustomer == null)
        {
        	noCustomerSelectedMessage();
            return;
        }
        
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to create a new Credit Account for the selected Customer?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	return;
        }
        
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];

        int accountNum = bankLogic.createCreditAccount(pNo);
        
        if (accountNum > 1000)
        {
        	updateAccountList();
        	updateAccountButtons();
        	
            JOptionPane.showMessageDialog(this, "Account (" + accountNum + ") created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Failed to create Account. Check input or Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Withdraws from the selected account
     */
    private void withdraw()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	String selectedAccount = (String)accountComboBox.getSelectedItem();
        
        if (selectedCustomer == null || selectedAccount == null)
        {
        	noCustomerOrAccountSelectedMessage();
            return;
        }
        
        String[] customerParts = selectedCustomer.split(" ");
        String pNo = customerParts[0];
        
        try
        {
            String[] accountParts = selectedAccount.split(" ");
            String accountNumStr = accountParts[0];
            
            int accountNum = Integer.parseInt(accountNumStr);
            String amountStr = JOptionPane.showInputDialog(this, "Enter Amount To Withdraw:", "Input", JOptionPane.PLAIN_MESSAGE);
            
            if (amountStr == null)
            {
                return;
            }
            
            int amount = Integer.parseInt(amountStr);

            boolean success = bankLogic.withdraw(pNo, accountNum, amount);

            if (success)
            {
            	updateBalanceLabel();
            	
                JOptionPane.showMessageDialog(this, "Withdrawal of \"" + amount + "\" from the Account (" + accountNum + ") was successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Failed to Withdraw. Check input or Customer/Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for the Amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Deposits to the selected account
     */
    private void deposit()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	String selectedAccount = (String)accountComboBox.getSelectedItem();
        
        if (selectedCustomer == null || selectedAccount == null)
        {
            noCustomerOrAccountSelectedMessage();
            return;
        }
        
        String[] customerParts = selectedCustomer.split(" ");
        String pNo = customerParts[0];
        
        try
        {
            String[] accountParts = selectedAccount.split(" ");
            String accountNumStr = accountParts[0];
            
            int accountNum = Integer.parseInt(accountNumStr);
            
            String amountStr = JOptionPane.showInputDialog(this, "Enter Amount To Deposit:", "Input", JOptionPane.PLAIN_MESSAGE);
            
            if (amountStr == null)
            {
                return;
            }
            
            int amount = Integer.parseInt(amountStr);

            boolean success = bankLogic.deposit(pNo, accountNum, amount);
            
            if (success)
            {
            	updateBalanceLabel();
            	
                JOptionPane.showMessageDialog(this, "Deposit of \"" + amount + "\" to the Account (" + accountNum + ") was successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Failed to Withdraw. Check input or Customer/Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for the Amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Closes the selected account
     */
    private void closeAccount()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	String selectedAccount = (String)accountComboBox.getSelectedItem();
        
        if (selectedCustomer == null || selectedAccount == null)
        {
        	noCustomerOrAccountSelectedMessage();
            return;
        }
        
        String[] accountParts = selectedAccount.split(" ");
        String accountNumStr = accountParts[0];
        
        int accountNum = Integer.parseInt(accountNumStr);
        
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to close the selected Account (" + accountNum + ")?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	return;
        }
        
        String[] customerParts = selectedCustomer.split(" ");
        String pNo = customerParts[0];
        
        String accountInfo = bankLogic.closeAccount(pNo, accountNum);
        
        updateAccountList();
        updateAccountButtons();
        updateBalanceLabel();
        
        JOptionPane.showMessageDialog(this, "Account closed successfully.\n\n" + accountInfo, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Deletes the selected customer and closes associated accounts
     */
    private void deleteCustomer()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	
        if (selectedCustomer == null)
        {
        	noCustomerOrAccountSelectedMessage();
            return;
        }
        
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected Customer?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	return;
        }
    	
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];
    	
    	List<String> customerInfo = bankLogic.deleteCustomer(pNo);
    	
        customerList.clearSelection();
        accountComboBox.removeAllItems();
    	
    	resetAllButtons();
    	updateCustomerList();
        updateBalanceLabel();
        
        JOptionPane.showMessageDialog(this, "Customer deleted successfully.\n\n" + customerInfo, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Saves the entire bank program state
     */
    private void saveData()
    {
        String currentDirectory = System.getProperty("user.dir");
        String relativePath = currentDirectory + File.separator + "src" + File.separator + "boleri2_files";
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(relativePath));
        
        JPanel accessoryPanel = new JPanel();
        accessoryPanel.setLayout(new BorderLayout());
        
        ImageIcon icon = new ImageIcon("boleri2_files/icon.png");
        JLabel iconLabel = new JLabel(icon);
        accessoryPanel.add(iconLabel, BorderLayout.CENTER);
        
        fileChooser.setAccessory(accessoryPanel);
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            
            if (selectedFile.exists())
            {
            	int answer = JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to overwrite it?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION)
                {
                    return;
                }
            }
            
            if (bankLogic.saveDataToFile(fileManager, selectedFile.getAbsolutePath()))
            {
            	JOptionPane.showMessageDialog(this, "Data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Error saving data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Loads the entire bank program state
     */
    private void loadData()
    {
        String currentDirectory = System.getProperty("user.dir");
        String relativePath = currentDirectory + File.separator + "src" + File.separator + "boleri2_files";
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(relativePath));
        
        JPanel accessoryPanel = new JPanel();
        accessoryPanel.setLayout(new BorderLayout());
        
        ImageIcon icon = new ImageIcon("boleri2_files/icon.png");
        JLabel iconLabel = new JLabel(icon);
        accessoryPanel.add(iconLabel, BorderLayout.CENTER);
        
        fileChooser.setAccessory(accessoryPanel);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            
        	int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to load the data? Any unsaved progress will be lost.", "Confirm", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION)
            {
                return;
            }
            
            if (bankLogic.loadDataFromFile(fileManager, selectedFile.getAbsolutePath()))
            {
                customerList.clearSelection();
                accountComboBox.setSelectedIndex(-1);
                accountComboBox.removeAllItems();
                balanceLabel.setText("");
                
                updateCustomerList();
                resetAllButtons();
                
                JOptionPane.showMessageDialog(this, "Data loaded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Error loading data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Saves the account transactions of the selected account
     */
    private void saveTransactions()
    {
        String selectedAccount = (String)accountComboBox.getSelectedItem();
        String selectedCustomer = customerList.getSelectedValue();

        if (selectedAccount != null && selectedCustomer != null)
        {
            String[] accountParts = selectedAccount.split(" ");
            int accountNum = Integer.parseInt(accountParts[0]);
            
            String fileName = accountNum + "-transactions.txt";
            
            String[] customerParts = selectedCustomer.split(" ");
            String pNo = customerParts[0];
            
            String currentDirectory = System.getProperty("user.dir");
            String relativePath = currentDirectory + File.separator + "src" + File.separator + "boleri2_files";
            
            JFileChooser fileChooser = new JFileChooser(new File(relativePath));
            fileChooser.setSelectedFile(new File(fileName));
            
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
            fileChooser.setFileFilter(filter);
            
            JPanel accessoryPanel = new JPanel();
            accessoryPanel.setLayout(new BorderLayout());
            
            ImageIcon icon = new ImageIcon("boleri2_files/icon.png");
            JLabel iconLabel = new JLabel(icon);
            accessoryPanel.add(iconLabel, BorderLayout.CENTER);
            
            fileChooser.setAccessory(accessoryPanel);
            
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooser.getSelectedFile();
                
                if (selectedFile.exists())
                {
                	int answer = JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to overwrite it?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (answer != JOptionPane.YES_OPTION)
                    {
                        return;
                    }
                }
                
                if (!selectedFile.getAbsolutePath().toLowerCase().endsWith(".txt"))
                {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                }
                
                if (bankLogic.saveTransactionsToFile(fileManager, accountNum, pNo, selectedFile.getAbsolutePath()))
                {
                	JOptionPane.showMessageDialog(this, "Data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Error saving data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
        {
        	noCustomerOrAccountSelectedMessage();
        }
    }
    
    /**
     * Helper method to check if a string only has numbers in it
     * @param str contains the String that is supposed to consist of numbers only
     * @return true or false
     */
    private boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}