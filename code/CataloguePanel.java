import java.awt.*;
        import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
        import javax.swing.event.*;

public class CataloguePanel extends JPanel implements ActionListener{
    private JLabel catalogLegend;
    private JList productList;
    private JLabel addToCart;
    private JComboBox comboProduct;
    private JButton addToCartBtn;
    private JButton checkOut;
    private JButton profileBtn;
    private JButton exit;
    private JTextField res;

    private User user;

    ArrayList<Product> products;

    Cart cart=null;

    public CataloguePanel(User user) throws IOException {
        this.user=user;
        products= Store.retrieve();
        cart=new Cart((Buyer) user);
        //construct preComponents
        String[] productListItems = new String[products.size()];
        String[] comboProducts = new String[products.size()];

        for (int i = 0; i < productListItems.length; i++) {
            productListItems[i]=products.get(i).toString();
            comboProducts[i]=products.get(i).getName();
        }


        //construct components
        catalogLegend = new JLabel ("Product Catalogue");
        productList = new JList (productListItems);
        addToCart = new JLabel ("Pick Products by their number");
        comboProduct = new JComboBox (comboProducts);
        addToCartBtn = new JButton ("Add to Cart");
        addToCartBtn.addActionListener(this);
        checkOut = new JButton ("Checkout");
        checkOut.addActionListener(this);
        profileBtn = new JButton ("Dashboard");
        profileBtn.addActionListener(this);
        exit = new JButton ("Exit");
        exit.addActionListener(this);
        res = new JTextField(" ");

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (catalogLegend);
        add (productList);
        add (addToCart);
        add (comboProduct);
        add (addToCartBtn);
        add (checkOut);
        add (profileBtn);
        add (exit);

        //set component bounds (only needed by Absolute Positioning)
        catalogLegend.setBounds (425, 10, 200, 25);
        productList.setBounds (25, 70, 900, 320);
        addToCart.setBounds (25, 400, 900, 35);
        comboProduct.setBounds (25, 450, 750, 25);
        addToCartBtn.setBounds (820, 450, 100, 25);
        checkOut.setBounds (25, 510, 300, 25);
        profileBtn.setBounds (25, 545, 100, 25);
        exit.setBounds (825, 540, 100, 25);
        res.setBounds(25, 480, 300, 25);
    }


    public void open (User user) throws IOException {
        JFrame frame = new JFrame ("Catalogue Panel");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new CataloguePanel(user));
        frame.pack();
        frame.setVisible (true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==profileBtn){
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
                try {
                    SellerDashboard sellerDashboard=new SellerDashboard((Seller) user);
                    sellerDashboard.open((Seller) user);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if(user instanceof Buyer){
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
        }
        else if(e.getSource()==addToCartBtn){

            String productStr=comboProduct.getSelectedItem().toString().trim();
            for (Product product :
                    products) {
                if (Objects.equals(product.getName(), productStr)) {
                    cart.addProduct(product);
                    add(res);
                    res.setText("Product added");
                }
                }
        }
        else if(e.getSource()==checkOut){
            CheckoutPanel checkoutPanel=new CheckoutPanel(cart,user);
            checkoutPanel.open(user,cart);
        }
        else if(e.getSource()==exit){
            System.exit(0);
        }
    }
}
