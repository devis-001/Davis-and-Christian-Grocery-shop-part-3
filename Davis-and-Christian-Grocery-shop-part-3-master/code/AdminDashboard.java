import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminDashboard extends JPanel implements ActionListener {
    private static final JFrame currentFrame = new JFrame("Admin Dashboard");
    private final JLabel jcomp1;
    private final JLabel sellerLabel;
    private final JLabel nameLabel;
    private final JLabel emailLabel;
    private final JLabel phoneLabel;
    private final JTextField nameText;
    private final JTextField emailText;
    private final JTextField phoneText;
    private final JList allSellers;
    private final JLabel buyersLabel;
    private final JList allBuyers;
    private final JButton deleteBtn;
    private final JButton exitBtn;
    private final JButton reports;
    private final JTextField res;
    ArrayList<Seller> sellers;
    ArrayList<Buyer> buyers;
    ArrayList<Product> products;
    ArrayList<User> users;
    private User admin;


    public AdminDashboard(Admin admin) throws IOException {
        //construct preComponents
        sellers = admin.getSellers();
        buyers = admin.getBuyers();
        products = Store.retrieve();

        String[] sellersList = new String[sellers.size() + 1];
        String[] buyersList = new String[buyers.size() + 1];

        generateSellersList(sellersList);

        generateBuyersList(buyersList);


        this.admin = admin;

        //construct components
        jcomp1 = new JLabel("Admin Dashboard");
        sellerLabel = new JLabel("Sellers");
        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone:");
        nameText = new JTextField();
        emailText = new JTextField();
        phoneText = new JTextField();
        allSellers = new JList(sellersList);
        buyersLabel = new JLabel("Buyers");
        allBuyers = new JList(buyersList);
        res = new JTextField();
        reports = new JButton("View Reports");
        reports.setFocusable(false);
        reports.addActionListener((e) -> {
            Reports.open(admin, currentFrame);
        });
        deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener((e) -> {
            String userRole = "";
            Object selectedSeller = allSellers.getSelectedValue();
            Object selectedBuyer = allBuyers.getSelectedValue();
            if (selectedSeller == null && selectedBuyer == null)
                JOptionPane.showMessageDialog(null, "Please select a seller or a buyer to delete", "User Management", JOptionPane.ERROR_MESSAGE);
            else {
                String[] selectedValueString;
                if (selectedBuyer != null) {
                    selectedValueString = selectedBuyer.toString().split(" ");
                    userRole = "Buyer";
                } else {
                    selectedValueString = selectedSeller.toString().split(" ");
                    userRole = "Seller";
                }

                String email = "";
                for (String string :
                        selectedValueString) {
                    if (string.contains("@"))
                        email = string;
                }

                int deleteConfirmed = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (deleteConfirmed == 0) {

                    try {
                        User.delete(email);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    buyers = admin.getBuyers();
                    sellers = admin.getSellers();
                    String[] sellersListB = new String[sellers.size() + 1];
                    String[] buyersListB = new String[buyers.size() + 1];
                    generateBuyersList(buyersListB);
                    generateSellersList(sellersListB);
                    allSellers.setListData(sellersListB);
                    allBuyers.setListData(buyersListB);

                    JOptionPane.showMessageDialog(null, "user successfully deleted", "User Delete", JOptionPane.INFORMATION_MESSAGE);

                    // store deleted user
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter("deleted-users.txt", true));
                        out.write(String.format("\n%s%10s%30s%30s", LocalDate.now(), userRole, email, admin.getEmail()));
                        out.close();

                        // update reports
                        if (Reports.deletedUsers != null)
                            Reports.deletedUsers.setListData(Reports.getDeletedUsersDetails());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        exitBtn = new JButton("Exit");
        exitBtn.addActionListener((e) -> {
            LoginPanel.open(currentFrame);
        });

        //adjust size and set layout
        setPreferredSize(new Dimension(944, 601));
        setLayout(null);

        //add components
        add(jcomp1);
        add(sellerLabel);
        add(nameLabel);
        add(emailLabel);
        add(phoneLabel);
        add(nameText);
        add(emailText);
        add(phoneText);
        add(allSellers);
        add(buyersLabel);
        add(allBuyers);
        add(reports);
        add(deleteBtn);
        add(exitBtn);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds(5, 5, 200, 25);
        sellerLabel.setBounds(205, 180, 100, 25);
        nameLabel.setBounds(5, 45, 100, 25);
        emailLabel.setBounds(5, 75, 100, 25);
        phoneLabel.setBounds(5, 110, 100, 25);
        nameText.setBounds(65, 45, 300, 25);
        nameText.setEditable(false);
        emailText.setBounds(65, 75, 300, 25);
        emailText.setEditable(false);
        phoneText.setBounds(65, 110, 300, 25);
        phoneText.setEditable(false);
        allSellers.setBounds(45, 220, 400, 250);
        buyersLabel.setBounds(660, 185, 100, 25);
        allBuyers.setBounds(510, 220, 400, 250);
        reports.setBounds(400, 45, 300, 25);
        reports.setBackground(Color.CYAN);
        reports.setForeground(Color.BLACK);
        reports.setFocusable(false);
        deleteBtn.setBounds(810, 515, 100, 25);
        exitBtn.setBounds(810, 560, 100, 25);
        res.setBounds(45, 550, 700, 25);

        // Set field values
        nameText.setText(admin.getFirstName() + " " + admin.getLastName());
        emailText.setText(admin.getEmail());
        phoneText.setText(admin.getPhone());

    }

    public static void open(Admin admin, JFrame prevFrame) throws IOException {
        prevFrame.dispose();
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.getContentPane().add(new AdminDashboard(admin));
        currentFrame.pack();
        currentFrame.setVisible(true);
    }

    private void generateBuyersList(String[] buyersList) {
        if (buyers.size() == 0) {
            buyersList[0] = "No buyers in the database";
        } else {
            buyersList[0] = "Firstname" + padString("Lastname", 20) + padString("Email", 20) + padString("Phone", 20);
            for (int i = 0; i < buyers.size(); i++) {
                buyersList[i + 1] = buyers.get(i).toString();
            }
        }
    }

    private void generateSellersList(String[] sellersList) {
        String[] selectSellerComboItems = new String[sellers.size()];


        if (sellers.size() == 0) {
            sellersList[0] = "No sellers in the database";
        } else {
            sellersList[0] = "Firstname" + padString("Lastname", 20) + padString("Email", 20) + padString("Phone", 20);
            for (int i = 0; i < sellers.size(); i++) {
                sellersList[i + 1] = sellers.get(i).toString();
                selectSellerComboItems[i] = sellers.get(i).toString();
            }
        }
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
