import java.awt.*;
        import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
        import javax.swing.event.*;

public class SellerDashboard extends JPanel implements ActionListener{
    private JLabel sellerDashLabel;
    private JLabel sellerWalletLabel;
    private JTextField sellerWallet;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel walletLabel;
    private JTextField nameText;
    private JTextField emailText;
    private JTextField phoneText;
    private JTextField walletText;
    private JLabel sellerProducts;
    private JList products;
    private JButton newBtn;
    private JButton exit;
    private Seller seller;
    private JTextField updateInput;
    private JButton updateBtn;

    public SellerDashboard(Seller seller) throws IOException {
        this.seller=seller;

        ArrayList<Product> sellersProduct=seller.getProducts(seller.getEmail());

        //construct preComponents
        String[] productsList = new String[sellersProduct.size()];

        if (productsList.length ==0){
            productsList= new String[]{"You do not have any product in store", "Add your products"};
        }else {
            for (int i = 0; i < productsList.length; i++) {
                productsList[i]= sellersProduct.get(i).toString();
            }
        }

        //construct components
        sellerDashLabel = new JLabel ("Seller Dashboard");
        sellerWalletLabel = new JLabel ("Balance:");
        sellerWallet = new JTextField ();
        nameLabel = new JLabel ("Name:");
        emailLabel = new JLabel ("Email:");
        phoneLabel = new JLabel ("Phone:");
        walletLabel = new JLabel ("Wallet:");
        nameText = new JTextField (5);
        emailText = new JTextField (5);
        phoneText = new JTextField (5);
        walletText = new JTextField (5);
        sellerProducts = new JLabel ("My Products");
        products = new JList (productsList);
        newBtn=new JButton("New Product");
        newBtn.addActionListener(this);
        exit = new JButton ("Exit");
        exit.addActionListener(this);
        updateInput=new JTextField("Enter product ID to update",5);
        updateBtn=new JButton("Update");
        updateBtn.addActionListener(this);


        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (sellerDashLabel);
        add(sellerWalletLabel);
        add(sellerWallet);
        add (nameLabel);
        add (emailLabel);
        add (phoneLabel);
        add (walletLabel);
        add (nameText);
        add (emailText);
        add (phoneText);
        add (walletText);
        add (sellerProducts);
        add (products);
        add(newBtn);
        add (exit);
        add(updateInput);
        add(updateBtn);

        //set component bounds (only needed by Absolute Positioning)
        sellerDashLabel.setBounds (10, 10, 100, 25);
        sellerWalletLabel.setBounds (450, 10, 100, 25);
        sellerWallet.setBounds (550, 10, 100, 25);
        nameLabel.setBounds (15, 50, 100, 25);
        emailLabel.setBounds (15, 80, 100, 25);
        phoneLabel.setBounds (15, 110, 100, 25);
        walletLabel.setBounds (15, 145, 100, 25);
        nameText.setBounds (70, 50, 300, 25);
        emailText.setBounds (70, 85, 300, 25);
        phoneText.setBounds (70, 115, 300, 25);
        walletText.setBounds (70, 145, 300, 25);
        sellerProducts.setBounds (410, 195, 100, 25);
        products.setBounds (20, 240, 900, 200);
//        newBtn.setBounds (350, 470, 200, 25);
        updateInput.setBounds(20, 470, 300, 25);
        updateBtn.setBounds (815, 470, 100, 25);
        newBtn.setBounds(20, 520, 200, 25);
        exit.setBounds (815, 520, 100, 25);


        // Set field values
        nameText.setText(seller.getFirstName()+ " " + seller.getLastName());
        emailText.setText(seller.getEmail());
        phoneText.setText(seller.getPhone());
        walletText.setText("$ "+ seller.getWallet());
        sellerWallet.setText("$"+ seller.getWallet());
    }


    public void open (Seller seller) throws IOException {
        JFrame frame = new JFrame ("Seller Dashboard");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new SellerDashboard(seller));
        frame.pack();
        frame.setVisible (true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newBtn) {
            ProductForm productForm=new ProductForm(seller);
            productForm.open(seller);
        }
        else if(e.getSource()==exit){
            System.exit(0);
        }
        else if(e.getSource()==updateBtn){
            try{
                int id=Integer.parseInt(updateInput.getText());
                ProductUpdateForm.open(seller,id);
            }catch (Exception exception){
                System.out.println(exception);
            }
        }
    }
}
