import java.awt.*;
        import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
        import javax.swing.event.*;

public class AdminDashboard extends JPanel implements ActionListener {
    private JLabel jcomp1;
    private JLabel sellerLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JTextField nameText;
    private JTextField emailText;
    private JTextField phoneText;
    private JList allSellers;
    private JLabel buyersLabel;
    private JList allBuyers;
    private JLabel adminAction;
//    private JComboBox selectSellerCombo;
    private JTextField removeID;
    private JButton deleteBtn;
    private JButton exitBtn;
    private JTextField res;
    private User admin;

    ArrayList<Seller> sellers;
    ArrayList<Buyer> buyers;
    ArrayList<Product> products;

    ArrayList<User> users;

    public AdminDashboard(Admin admin) throws IOException {
        //construct preComponents
        sellers=admin.getSellers();
        buyers=admin.getBuyers();
        products= Store.retrieve();

        String[] sellersList = new String[sellers.size()+1];
        String[] buyersList = new String[buyers.size()+1];

        String[] selectSellerComboItems = new String[sellers.size()];


        if (sellers.size()==0){
            sellersList[0]="No sellers in the database";
        }
        else {
            sellersList[0]="Firstname" + padString("Lastname",20) + padString("Email",20) + padString("Phone",20);
            for (int i = 0; i < sellers.size(); i++) {
                sellersList[i+1]= sellers.get(i).toString();
                selectSellerComboItems[i]= sellers.get(i).toString();
            }
        }

        if (buyers.size()==0){
            buyersList[0]="No buyers in the database";
        }
        else {
            buyersList[0]="Firstname" + padString("Lastname",20) + padString("Email",20) + padString("Phone",20);
            for (int i = 0; i < buyers.size(); i++) {
                buyersList[i+1]= buyers.get(i).toString();
            }
        }


        this.admin=admin;

        //construct components
        jcomp1 = new JLabel ("Admin Dashboard");
        sellerLabel = new JLabel ("Sellers");
        nameLabel = new JLabel ("Name:");
        emailLabel = new JLabel ("Email:");
        phoneLabel = new JLabel ("Phone:");
        nameText = new JTextField ();
        emailText = new JTextField ();
        phoneText = new JTextField ();
        allSellers = new JList (sellersList);
        buyersLabel = new JLabel ("Buyers");
        allBuyers = new JList (buyersList);
        adminAction = new JLabel ("Select user by email to Remove");
//        selectSellerCombo = new JComboBox (selectSellerComboItems);
        removeID=new JTextField();
        res = new JTextField ();
        deleteBtn = new JButton ("Delete");
        deleteBtn.addActionListener(this);
        exitBtn = new JButton ("Exit");
        exitBtn.addActionListener(this);

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (jcomp1);
        add (sellerLabel);
        add (nameLabel);
        add (emailLabel);
        add (phoneLabel);
        add (nameText);
        add (emailText);
        add (phoneText);
        add (allSellers);
        add (buyersLabel);
        add (allBuyers);
        add (adminAction);
//        add (selectSellerCombo);
        add(removeID);
        add (deleteBtn);
        add (exitBtn);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (5, 5, 200, 25);
        sellerLabel.setBounds (205, 180, 100, 25);
        nameLabel.setBounds (5, 45, 100, 25);
        emailLabel.setBounds (5, 75, 100, 25);
        phoneLabel.setBounds (5, 110, 100, 25);
        nameText.setBounds (65, 45, 300, 25);
        emailText.setBounds (65, 75, 300, 25);
        phoneText.setBounds (65, 110, 300, 25);
        allSellers.setBounds (45, 220, 400, 250);
        buyersLabel.setBounds (660, 185, 100, 25);
        allBuyers.setBounds (510, 220, 400, 250);
        adminAction.setBounds (45, 485, 200, 25);
//        selectSellerCombo.setBounds (45, 515, 700, 25);
        removeID.setBounds (45, 515, 700, 25);
        deleteBtn.setBounds (810, 515, 100, 25);
        exitBtn.setBounds (810, 560, 100, 25);
        res.setBounds(45, 550, 700, 25);

        // Set field values
        nameText.setText(admin.getFirstName()+ " " + admin.getLastName());
        emailText.setText(admin.getEmail());
        phoneText.setText(admin.getPhone());

    }


    public void open (Admin admin) throws IOException {
        JFrame frame = new JFrame ("Admin Dashboard");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new AdminDashboard(admin));
        frame.pack();
        frame.setVisible (true);
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteBtn) {
            String email= removeID.getText().trim();

            try{
                for (Seller user :
                        sellers) {
                    if (Objects.equals(user.getEmail(), email)){
                        try {
                            User.delete(email);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        add(res);
                        res.setText("Seller removed from the database");
                        System.out.println(sellers.size());
                    }
                }
            }catch (Exception exception){
                System.out.println(exception);
            }
            try{
                for (Buyer user :
                        buyers) {
                    if (Objects.equals(user.getEmail(), email)){
                        try {
                            User.delete(email);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        add(res);
                        res.setText("Buyer removed from the database");
                        System.out.println(sellers.size());
                    }
                }
            }catch (Exception exception){
                System.out.println(exception);
            }
        }
        else if(e.getSource()==exitBtn){
            System.exit(0);
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
