import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel implements ActionListener {
    private JTextField fNameInput;
    private JLabel jcomp2;
    private JLabel fNameLabel;
    private JTextField lNameInput;
    private JLabel lNameLabel;
    private JTextField emailInput;
    private JLabel emailLabel;
    private JTextField phoneInput;
    private JLabel phoneLabel;
    private JLabel userTypeLabel;
    private JComboBox userCombo;
    private JTextField passwordInput;
    private JLabel passwordLabel;
    private JButton registerBtn;
    private JButton loginBtn;
    private JTextField res;

    public RegisterPanel() {
        //construct preComponents
        String[] userComboItems = {"Buyer", "Seller", "Admin"};

        //construct components
        fNameInput = new JTextField(5);
        jcomp2 = new JLabel("Register");
        fNameLabel = new JLabel("First Name");
        lNameInput = new JTextField(5);
        lNameLabel = new JLabel("Last Name");
        emailInput = new JTextField(5);
        emailLabel = new JLabel("Email");
        phoneInput = new JTextField(5);
        phoneLabel = new JLabel("Phone");
        userTypeLabel = new JLabel("User Type");
        userCombo = new JComboBox(userComboItems);
        passwordInput = new JPasswordField();
        passwordLabel = new JLabel("Password");
        registerBtn = new JButton("Register");
        registerBtn.addActionListener(this);

        loginBtn = new JButton("Login");
        loginBtn.addActionListener(this);


        res = new JTextField(" ");

        //adjust size and set layout
        setPreferredSize(new Dimension(944, 601));
        setLayout(null);

        //add components
        add(fNameInput);
        add(jcomp2);
        add(fNameLabel);
        add(lNameInput);
        add(lNameLabel);
        add(emailInput);
        add(emailLabel);
        add(phoneInput);
        add(phoneLabel);
        add(userTypeLabel);
        add(userCombo);
        add(passwordInput);
        add(passwordLabel);
        add(registerBtn);
        add(loginBtn);
//        add(res);

        //set component bounds (only needed by Absolute Positioning)
        fNameInput.setBounds(285, 105, 300, 25);
        jcomp2.setBounds(395, 45, 100, 25);
        fNameLabel.setBounds(155, 105, 100, 25);
        lNameInput.setBounds(285, 140, 300, 25);
        lNameLabel.setBounds(155, 145, 100, 25);
        emailInput.setBounds(285, 185, 300, 25);
        emailLabel.setBounds(155, 185, 100, 25);
        phoneInput.setBounds(285, 225, 300, 25);
        phoneLabel.setBounds(155, 225, 100, 25);
        userTypeLabel.setBounds(155, 260, 100, 25);
        userCombo.setBounds(285, 265, 300, 25);
        passwordInput.setBounds(285, 300, 300, 25);
        passwordLabel.setBounds(155, 300, 100, 25);
        registerBtn.setBounds(285, 340, 100, 25);
        loginBtn.setBounds(480, 340, 100, 25);
        res.setBounds(285, 400, 300, 25);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerBtn) {
            String firstName=fNameInput.getText();
            String lastName=lNameInput.getText();
            String email=emailInput.getText();
            String phone=phoneInput.getText();
            String type= (String) userCombo.getSelectedItem();
            String password=passwordInput.getText();

            User user=null;
            try {
                User.register(firstName,lastName,email,password,phone,type);
                add(res);
                res.setText("Registration Successful");

                // Open the login page
                LoginPanel loginPanel=new LoginPanel();
                loginPanel.open();
                this.setVisible(false);
            }catch (Exception exception) {
                System.out.println("Something went wrong" + e);
                add(res);
                res.setText("Registration Failed");
            }
        }
        else if(e.getSource() == loginBtn){
            LoginPanel loginPanel=new LoginPanel();
//            this.setVisible(false);
            this.setVisible(false);
            loginPanel.open();
        }
    }


    public static void open() {
        JFrame frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new RegisterPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
