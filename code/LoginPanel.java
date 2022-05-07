import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPanel extends JPanel implements ActionListener {
    private static final JFrame currentFrame = new JFrame("Login");
    private final JLabel loginLegend;
    private final JTextField loginEmailInput;
    private final JTextField loginEmailPassword;
    private final JButton loginBtn;
    private final JButton registerBtn;
    private final JLabel emailLabel;
    private final JLabel passwordLable;
    private final JTextField res;

    public LoginPanel() {
        //construct components
        loginLegend = new JLabel("Login");
        loginEmailInput = new JTextField(5);
        loginEmailPassword = new JTextField(5);
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Don't have account");
        emailLabel = new JLabel("Email");
        passwordLable = new JLabel("Password");
        loginBtn.addActionListener(this);
        registerBtn.addActionListener(this);
        res = new JTextField(" ");


        //adjust size and set layout
        setPreferredSize(new Dimension(944, 601));
        setLayout(null);

        //add components
        add(loginLegend);
        add(loginEmailInput);
        add(loginEmailPassword);
        add(loginBtn);
        add(registerBtn);
        add(emailLabel);
        add(passwordLable);

        //set component bounds (only needed by Absolute Positioning)
        loginLegend.setBounds(445, 60, 100, 25);
        loginEmailInput.setBounds(315, 135, 300, 25);
        loginEmailPassword.setBounds(315, 190, 300, 25);
        loginBtn.setBounds(315, 240, 100, 25);
        registerBtn.setBounds(460, 240, 150, 25);
        emailLabel.setBounds(195, 135, 100, 25);
        passwordLable.setBounds(195, 190, 100, 25);
        res.setBounds(315, 300, 300, 25);
        this.setVisible(true);
    }

    public static void open(JFrame prevFrame) {
        if (prevFrame != null)
            prevFrame.dispose();
        currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        currentFrame.getContentPane().add(new LoginPanel());
        currentFrame.pack();
        currentFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String email = loginEmailInput.getText();
            String password = loginEmailPassword.getText();

            User user;
            user = User.login(email, password);

            if (user == null) {
                JOptionPane.showMessageDialog(null,"Wrong username and password", "Login", JOptionPane.ERROR_MESSAGE);
            } else {
                loginEmailInput.setText("");
                loginEmailPassword.setText("");

                if (user instanceof Admin) {
                    AdminDashboard adminDashboard = null;
                    try {
                        currentFrame.dispose();
                        adminDashboard = new AdminDashboard((Admin) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        currentFrame.dispose();
                        AdminDashboard.open((Admin) user, currentFrame);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (user instanceof Seller) {
                    SellerDashboard sellerDashboard = null;
                    try {
                        currentFrame.dispose();
                        sellerDashboard = new SellerDashboard((Seller) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        currentFrame.dispose();
                        SellerDashboard.open((Seller) user, currentFrame);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (user instanceof Buyer) {
                    BuyerDashboard buyerDashboard = null;
                    try {
                        buyerDashboard = new BuyerDashboard((Buyer) user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        assert buyerDashboard != null;
                        BuyerDashboard.open((Buyer) user, currentFrame);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    CataloguePanel cataloguePanel = null;
                    try {
                        currentFrame.dispose();
                        cataloguePanel = new CataloguePanel(user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        CataloguePanel.open(user, currentFrame);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (e.getSource() == registerBtn) {
            RegisterPanel.open(currentFrame);
        }

    }

}
