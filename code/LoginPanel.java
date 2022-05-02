import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPanel extends JPanel implements ActionListener {
    private JLabel loginLegend;
    private JTextField loginEmailInput;
    private JTextField loginEmailPassword;
    private JButton loginBtn;
    private JButton registerBtn;
    private JLabel emailLabel;
    private JLabel passwordLable;
    private JTextField res;

    public LoginPanel() {
        //construct components
        loginLegend = new JLabel ("Login");
        loginEmailInput = new JTextField (5);
        loginEmailPassword = new JTextField (5);
        loginBtn = new JButton ("Login");
        registerBtn = new JButton ("Don't have account");
        emailLabel = new JLabel ("Email");
        passwordLable = new JLabel ("Password");
        loginBtn.addActionListener(this);
        registerBtn.addActionListener(this);
        res = new JTextField(" ");


        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (loginLegend);
        add (loginEmailInput);
        add (loginEmailPassword);
        add (loginBtn);
        add(registerBtn);
        add (emailLabel);
        add (passwordLable);

        //set component bounds (only needed by Absolute Positioning)
        loginLegend.setBounds (445, 60, 100, 25);
        loginEmailInput.setBounds (315, 135, 300, 25);
        loginEmailPassword.setBounds (315, 190, 300, 25);
        loginBtn.setBounds (315, 240, 100, 25);
        registerBtn.setBounds (460, 240, 150, 25);
        emailLabel.setBounds (195, 135, 100, 25);
        passwordLable.setBounds (195, 190, 100, 25);
        res.setBounds(315, 300, 300, 25);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String email=loginEmailInput.getText();
            String password=loginEmailPassword.getText();

            User user;
            user=User.login(email,password);

            if (user==null){
                add(res);
                res.setText("User not found!! Create a new account");
            }
            else {

                if (user instanceof Admin){
                    AdminDashboard adminDashboard= null;
                    try {
                        adminDashboard = new AdminDashboard((Admin) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        adminDashboard.open((Admin) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else if(user instanceof Seller){
                    SellerDashboard sellerDashboard= null;
                    try {
                        sellerDashboard = new SellerDashboard((Seller) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        sellerDashboard.open((Seller) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else if (user instanceof Buyer){
                    BuyerDashboard buyerDashboard= null;
                    try {
                        buyerDashboard = new BuyerDashboard((Buyer) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        buyerDashboard.open((Buyer) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    CataloguePanel cataloguePanel= null;
                    try {
                        cataloguePanel = new CataloguePanel(user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        cataloguePanel.open(user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        else if (e.getSource()==registerBtn){
            RegisterPanel.open();
        }

    }

    public void open () {
        JFrame frame = new JFrame ("Login");
        frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add (new LoginPanel());
        frame.pack();
        frame.setVisible (true);
    }

}
