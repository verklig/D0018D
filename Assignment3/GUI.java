package boleri2;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

// TODO:
// Fix withdrawal from credit account
// Fix when deleting a customer with an account the buttons doesn't reset
// Add Credit and Savings to account list
// Comment

// Why does the list of customers work?

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private BankLogic bankLogic;
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
    
    
    public static void main(String[] args)
    {
    	Locale.setDefault(Locale.ENGLISH);
    	
    	// Using lambda expressions for a more concise syntax and better readability
    	// This also goes for creating events for buttons and such
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }

    public GUI()
    {
        bankLogic = new BankLogic();
        customerListModel = new DefaultListModel<>();
        customerList = new JList<>(customerListModel);
        accountComboBox = new JComboBox<>();
        balanceLabel = new JLabel("");
        initializeComponents();
    }

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
        	if (customerListModel.getSize() != 0)
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
        
        // Event to update the balance label when an account is clicked
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
        
        // Setting buttons to be disabled as default
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
    
    private void canceledOperationMessage()
    {
    	JOptionPane.showMessageDialog(this, "Operation was canceled.", "Alert", JOptionPane.WARNING_MESSAGE);
    }
    
    private void noCustomerSelectedMessage()
    {
    	JOptionPane.showMessageDialog(this, "No Customer selected.", "Alert", JOptionPane.WARNING_MESSAGE);
    }
    
    private void noCustomerOrAccountSelectedMessage()
    {
    	JOptionPane.showMessageDialog(this, "No Customer/Account selected.", "Alert", JOptionPane.WARNING_MESSAGE);
    }
    
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
    
    private void updateAccountButtons()
    {
    	boolean comboBoxEmpty = accountComboBox.getItemCount() == 0;
    	
        withdrawButton.setEnabled(!comboBoxEmpty);
        depositButton.setEnabled(!comboBoxEmpty);
        closeAccountButton.setEnabled(!comboBoxEmpty);
    	accountComboBox.setEnabled(!comboBoxEmpty);
    }
    
    private static boolean isNumeric(String str)
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
    
    private void updateAccountList()
    {
        String selectedCustomer = customerList.getSelectedValue();

        if (selectedCustomer != null)
        {
            String[] parts = selectedCustomer.split(" ");
            String pNo = parts[0];

            List<String> accounts = bankLogic.getCustomerAccountNumbers(pNo);

            accountComboBox.removeAllItems();
            
            for (String account : accounts)
            {
            	accountComboBox.addItem(account);
            }
            
            updateBalanceLabel();
        }
    }
    
    private void updateCustomerList()
    {
        List<String> customers = bankLogic.getAllCustomers();
        
        customerListModel.clear();

        for (String customer : customers)
        {
            customerListModel.addElement(customer);
        }
    }
    
    private void updateBalanceLabel()
    {
        String selectedAccount = (String) accountComboBox.getSelectedItem();

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
    
    private void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        
        fileMenu.addSeparator();
        
        JMenuItem loadMenuItem = new JMenuItem("Load");
        fileMenu.add(loadMenuItem);
        
        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        
        fileMenu.addSeparator();
        
        JMenuItem saveTransactionsMenuItem = new JMenuItem("Save Transactions");
        fileMenu.add(saveTransactionsMenuItem);

        exitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
    }
    
    private void createNewCustomer()
    {
        String firstName = JOptionPane.showInputDialog(this, "Enter First Name:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (firstName == null)
        {
        	canceledOperationMessage();
            return;
        }
        
        String surName = JOptionPane.showInputDialog(this, "Enter Surname:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (surName == null)
        {
        	canceledOperationMessage();
            return;
        }
        
        String pNo = JOptionPane.showInputDialog(this, "Enter Personal Number:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (pNo == null)
        {
        	canceledOperationMessage();
            return;
        }

        if (firstName.matches("[a-zA-Z]+") && surName.matches("[a-zA-Z]+") && isNumeric(pNo))
        {
            boolean success = bankLogic.createCustomer(firstName, surName, pNo);

            if (success)
            {
                customerList.clearSelection();
                accountComboBox.setSelectedIndex(-1);
                accountComboBox.removeAllItems();
                balanceLabel.setText("");
            	
            	updateCustomerList();
                
                JOptionPane.showMessageDialog(this, "Customer created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Failed to create Customer. Check input or Customer already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
        	JOptionPane.showMessageDialog(this, "Failed to create Customer. Names can only consist of characters and Personal Number can only consist of numbers.",
        																													   "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
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
        	canceledOperationMessage();
            return;
        }
        
        String newSurName = JOptionPane.showInputDialog(this, "Enter New Surname:", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if (newSurName == null)
        {
        	canceledOperationMessage();
            return;
        }
        
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];
        
        if (newFirstName.matches("[a-zA-Z]+") && newSurName.matches("[a-zA-Z]+"))
        {
            boolean success = bankLogic.changeCustomerName(newFirstName, newSurName, pNo);
            
            if (success)
            {
            	resetAllButtons();
            	updateCustomerList();
            	
                JOptionPane.showMessageDialog(this, "Customer Name changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Failed to change Customer Name. Check input or Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
        	JOptionPane.showMessageDialog(this, "Failed to change Customer Name. Names can only consist of characters.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createSavingsAccount()
    {
    	String selectedCustomer = customerList.getSelectedValue();
        
        if (selectedCustomer == null)
        {
        	noCustomerSelectedMessage();
            return;
        }
        
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to create a new Savings Account for the selected Customer?",
        																						 "Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	canceledOperationMessage();
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
    
    private void createCreditAccount()
    {
    	String selectedCustomer = customerList.getSelectedValue();
        
        if (selectedCustomer == null)
        {
        	noCustomerSelectedMessage();
            return;
        }
        
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to create a new Credit Account for the selected Customer?",
        																						"Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	canceledOperationMessage();
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
    
    private void withdraw()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	String selectedAccount = (String)accountComboBox.getSelectedItem();
        
        if (selectedCustomer == null || selectedAccount == null)
        {
        	noCustomerOrAccountSelectedMessage();
            return;
        }
        
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];
        
        try
        {
            int accountNum = Integer.parseInt(selectedAccount);
            String amountStr = JOptionPane.showInputDialog(this, "Enter Amount To Withdraw:", "Input", JOptionPane.PLAIN_MESSAGE);
            
            if (amountStr == null)
            {
                canceledOperationMessage();
                return;
            }
            
            int amount = Integer.parseInt(amountStr);

            boolean success = bankLogic.withdraw(pNo, accountNum, amount);

            if (success)
            {
            	updateBalanceLabel();
            	
                JOptionPane.showMessageDialog(this, "Withdrawal of \"" + amount + "\" from the Account (" + accountNum +
                        							   ") was successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
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
    
    private void deposit()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	String selectedAccount = (String)accountComboBox.getSelectedItem();
        
        if (selectedCustomer == null || selectedAccount == null)
        {
            noCustomerOrAccountSelectedMessage();
            return;
        }
        
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];
        
        try
        {
            int accountNum = Integer.parseInt(selectedAccount);
            String amountStr = JOptionPane.showInputDialog(this, "Enter Amount To Deposit:", "Input", JOptionPane.PLAIN_MESSAGE);
            
            if (amountStr == null)
            {
                canceledOperationMessage();
                return;
            }
            
            int amount = Integer.parseInt(amountStr);

            boolean success = bankLogic.deposit(pNo, accountNum, amount);
            
            if (success)
            {
            	updateBalanceLabel();
            	
                JOptionPane.showMessageDialog(this, "Deposit of \"" + amount + "\" to the Account (" + accountNum +
                								  ") was successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
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
    
    private void closeAccount()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	String selectedAccount = (String)accountComboBox.getSelectedItem();
        
        if (selectedCustomer == null || selectedAccount == null)
        {
        	noCustomerOrAccountSelectedMessage();
            return;
        }
        
        int accountNum = Integer.parseInt(selectedAccount);
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to close the selected Account (" + accountNum + ")?",
																						   "Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	canceledOperationMessage();
        	return;
        }
        
        String[] parts = selectedCustomer.split(" ");
        String pNo = parts[0];
        
        String accountInfo = bankLogic.closeAccount(pNo, accountNum);
        
        updateAccountList();
        updateAccountButtons();
        updateBalanceLabel();
        
        JOptionPane.showMessageDialog(this, "Account closed successfully.\n\n" + accountInfo, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteCustomer()
    {
    	String selectedCustomer = customerList.getSelectedValue();
    	
        if (selectedCustomer == null)
        {
        	noCustomerOrAccountSelectedMessage();
            return;
        }
        
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected Customer?",
																	   "Confirmation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION)
        {
        	canceledOperationMessage();
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
}

