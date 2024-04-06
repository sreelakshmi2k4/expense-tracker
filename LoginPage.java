import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties; // Add this import statement
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginPage extends JFrame implements ActionListener {
    // Components
    private JLabel usernameLabel, passwordLabel, messageLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Properties credentials;
    String username ;
    public LoginPage() {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set Nimbus look and feel
        Dimension screenSizeLogin = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSizeLogin.width, screenSizeLogin.height);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        loadCredentials();//load the credentials file here
        // Initialize components
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        messageLabel = new JLabel();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        // Set preferred sizes for components
        usernameLabel.setPreferredSize(new Dimension(80, 30));
        passwordLabel.setPreferredSize(new Dimension(80, 30));
        usernameField.setPreferredSize(new Dimension(150, 30));
        passwordField.setPreferredSize(new Dimension(150, 30));
        loginButton.setPreferredSize(new Dimension(100, 30));
        messageLabel.setPreferredSize(new Dimension(230, 30));

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        gbc.gridy = 3;
        add(messageLabel, gbc);

        // Add action listener to login button
        loginButton.addActionListener(this);

        pack(); // Pack the components to ensure they are displayed properly
    }
    private void loadCredentials() {
        credentials = new Properties();
        try {
            credentials.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
         username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Check if the entered username and password match any entry in the properties file
        if (credentials.getProperty(username) != null && credentials.getProperty(username).equals(password)) {
            messageLabel.setText("Login Successful");
            // Open welcome page or perform further actions
            openWelcomePage();
        } else {
            messageLabel.setText("Invalid Username or Password");
        }
    }
    private void openWelcomePage() {
        WelcomePage welcomePage = new WelcomePage(username);
        welcomePage.setVisible(true);
        dispose(); // Close login page after successful login
    }

    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
    }
}


public class WelcomePage extends JFrame {
    private JTextField itemNameField, expenseAmountField;
    private JButton addExpenseButton;
    private String username;

    public WelcomePage(String username) {
        this.username = username;
        setTitle("Welcome Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create components
        JLabel itemNameLabel = new JLabel("Item Name:");
        JLabel expenseAmountLabel = new JLabel("Expense Amount (INR):");
        itemNameField = new JTextField();
        expenseAmountField = new JTextField();
        addExpenseButton = new JButton("Add Expense");

        

        // Set layout
        setLayout(new GridLayout(4, 2));

        // Add components to frame
        add(itemNameLabel);
        add(itemNameField);
        add(expenseAmountLabel);
        add(expenseAmountField);
        add(addExpenseButton);

        // Add action listener to addExpenseButton
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                String expenseAmount = expenseAmountField.getText();

                // Perform expense addition logic here
                // For demonstration, just printing the entered values
                System.out.println("Item Name: " + itemName);
                System.out.println("Expense Amount: " + expenseAmount + " INR");
              

                // Append expense details to CSV file
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String dateTime = dateFormat.format(date);
                appendExpenseToCSV(username, itemName, expenseAmount, dateTime);
            }
        });
 
    
       
    }
    private void appendExpenseToCSV(String username, String itemName, String expenseAmount, String dateTime) {
        try (FileWriter writer = new FileWriter("expenses.csv", true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "," + itemName + "," + expenseAmount + "," + dateTime);
            JOptionPane.showMessageDialog(this, "Expense added successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error occurred while adding expense.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}