import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuyerDashboard extends JPanel implements ActionListener {
    public static JTextField walletText;
    private static final JFrame currentJFrame = new JFrame("Buyer Dashboard");
    private final JLabel dashboardLabel;
    private final JLabel nameLabel;
    private final JLabel emailLabel;
    private final JLabel phoneLabel;
    private final JLabel wallet;
    private final JTextField namesText;
    private final JTextField emailText;
    private final JTextField phoneText;
    private final JList jcomp10;
    private final JLabel historyLabel;
    private final JButton exitBtn;
    private final JButton catalogBtn;
    private final JButton refreshBtn;
    private final JTextField res;
    private final JButton addFund;
    private Buyer buyer;


    public BuyerDashboard(Buyer buyer) throws IOException {
        //construct preComponents
        ArrayList<Product> recommendedProducts = buyer.recommendedProducts();

        String[] recommended = new String[recommendedProducts.size()];


        this.buyer = buyer;

        if (recommendedProducts.size() == 0) {
            recommended[0] = "There are no products in store";
        } else {
            recommended[0] = "ID" + "\t" + padString("Name", 40) + "\t" + padString("Price", 20) + "\t" + padString("Seller Email", 30) + "\t" + padString("Quantity", 40);
            for (int i = 0; i < recommendedProducts.size() - 1; i++) {
                recommended[i + 1] = recommendedProducts.get(i).toString();
            }
        }

        //construct components
        dashboardLabel = new JLabel("Dashboard");


        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone:");
        wallet = new JLabel("Wallet Balance:");
        namesText = new JTextField(5);
        namesText.setEditable(false);
        emailText = new JTextField(5);
        emailText.setEditable(false);
        phoneText = new JTextField(5);
        phoneText.setEditable(false);
        walletText = new JTextField(5);
        refreshBtn = new JButton("refresh");
        refreshBtn.setFocusable(false);
        refreshBtn.setBackground(Color.LIGHT_GRAY);
        refreshBtn.addActionListener((e) -> {
            try {
                List<String> userFromDb = Files.readAllLines(Paths.get("usersDB.txt")).stream().filter(user -> user.contains(buyer.getEmail())).collect(Collectors.toList());
                if (userFromDb.size() > 0) {
                    String balance = userFromDb.get(0).split(",")[5];
                    walletText.setText("$ " + balance);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        addFund = new JButton("Add Funds");
        addFund.setFocusable(false);
        addFund.addActionListener((e) -> {
            String enter_amount = JOptionPane.showInputDialog("Enter amount");
            buyer.setWallet(buyer.getWallet() + Double.parseDouble(enter_amount));

            try {
                List<String> userByEmail = Files.readAllLines(Paths.get("usersDB.txt")).stream().filter(user -> user.contains(buyer.getEmail())).collect(Collectors.toList());

                if (userByEmail.size() > 0) {
                    String prevAmount = userByEmail.get(0).split(",")[5];
                    User.updateWalletBalance(buyer.getEmail(), Double.parseDouble(prevAmount) + Double.parseDouble(enter_amount));
                    walletText.setText("$ " + buyer.getWallet());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        jcomp10 = new JList(recommended);
        historyLabel = new JLabel("Available in Store");
        exitBtn = new JButton("Exit");
        exitBtn.setFocusable(false);
        exitBtn.addActionListener((e) -> {
            LoginPanel.open(currentJFrame);
        });
        catalogBtn = new JButton("Catalogue");
        res = new JTextField(" ");


        //adjust size and set layout
        setPreferredSize(new Dimension(944, 601));
        setLayout(null);

        //add components
        add(dashboardLabel);

        add(nameLabel);
        add(emailLabel);
        add(phoneLabel);
        add(wallet);
        add(namesText);
        add(emailText);
        add(phoneText);
        add(walletText);
        add(addFund);
        add(jcomp10);
        add(historyLabel);
        add(exitBtn);
        add(catalogBtn);
        add(refreshBtn);

        //set component bounds (only needed by Absolute Positioning)
        dashboardLabel.setBounds(10, 5, 100, 25);
        dashboardLabel.setBounds(10, 5, 100, 25);
        nameLabel.setBounds(65, 40, 50, 25);
        emailLabel.setBounds(65, 70, 50, 25);
        phoneLabel.setBounds(60, 100, 100, 25);
        wallet.setBounds(10, 140, 100, 25);
        addFund.setBounds(135, 163, 200, 25);
        refreshBtn.setBounds(335, 163,100,25);
        namesText.setBounds(135, 40, 300, 25);
        emailText.setBounds(135, 70, 300, 25);
        phoneText.setBounds(135, 100, 300, 25);
        walletText.setBounds(135, 140, 300, 25);
        jcomp10.setBounds(20, 245, 900, 200);
        historyLabel.setBounds(410, 200, 100, 25);
        exitBtn.setBounds(815, 490, 100, 25);
        catalogBtn.setBounds(20, 480, 150, 25);
        catalogBtn.addActionListener(this);
        res.setBounds(660, 60, 250, 25);

        // Set field values
        namesText.setText(buyer.getFirstName() + " " + buyer.getLastName());
        namesText.setEditable(false);
        emailText.setText(buyer.getEmail());
        phoneText.setText(buyer.getPhone());
        walletText.setText("$ " + getWalletBalance(buyer.getEmail()));
        walletText.setEditable(false);

    }


    public static void open(Buyer buyer, JFrame previousFrame) throws IOException {
        if (previousFrame != null)
            previousFrame.dispose();
        currentJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentJFrame.getContentPane().add(new BuyerDashboard(buyer));
        currentJFrame.pack();
        currentJFrame.setVisible(true);
    }

    private static String getWalletBalance(String buyerEmail) throws IOException {
        String walletBalance = "";
        List<String> userById = Files.readAllLines(Paths.get("usersDB.txt")).stream().filter(user -> user.contains(buyerEmail)).collect(Collectors.toList());
        if (userById.size() > 0)
            walletBalance = userById.get(0).split(",")[5];
        return walletBalance;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == catalogBtn) {
            try {
                CataloguePanel cataloguePanel = new CataloguePanel(buyer);
                CataloguePanel.open(buyer, currentJFrame);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Helper method
    private String padString(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(' ');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
