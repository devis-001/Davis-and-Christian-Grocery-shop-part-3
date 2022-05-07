import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CataloguePanel extends JPanel implements ActionListener {
    private static final JFrame currentFrame = new JFrame("Catalogue Panel");
    private final JLabel catalogLegend;
    private final JList productList;
    private final JLabel addToCart;
    private final JComboBox comboProduct;
    private final JButton addToCartBtn;
    private final JButton checkOut;
    private final JButton profileBtn;
    private final JButton exit;
    private final JTextField res;
    private final User user;
    ArrayList<Product> products;
    Cart cart = null;

    public CataloguePanel(User user) throws IOException {
        this.user = user;
        products = Store.retrieve();
        cart = new Cart((Buyer) user);
        //construct preComponents
        String[] productListItems = new String[products.size()];
        String[] comboProducts = new String[products.size()];

        for (int i = 0; i < productListItems.length; i++) {
            productListItems[i] = products.get(i).toString();
            comboProducts[i] = products.get(i).getName();
        }


        //construct components
        catalogLegend = new JLabel("Product Catalogue");
        productList = new JList(productListItems);
        addToCart = new JLabel("Pick Products by their number");
        comboProduct = new JComboBox(comboProducts);
        addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.addActionListener(this);
        checkOut = new JButton("Checkout");
        checkOut.addActionListener(this);
        profileBtn = new JButton("Dashboard");
        profileBtn.addActionListener(this);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        res = new JTextField(" ");

        //adjust size and set layout
        setPreferredSize(new Dimension(944, 601));
        setLayout(null);

        //add components
        add(catalogLegend);
        add(productList);
        add(addToCart);
        add(comboProduct);
        add(addToCartBtn);
        add(checkOut);
        add(profileBtn);
        add(exit);

        //set component bounds (only needed by Absolute Positioning)
        catalogLegend.setBounds(425, 10, 200, 25);
        productList.setBounds(25, 70, 900, 320);
        addToCart.setBounds(25, 400, 900, 35);
        comboProduct.setBounds(25, 450, 750, 25);
        addToCartBtn.setBounds(820, 450, 100, 25);
        checkOut.setBounds(25, 510, 300, 25);
        profileBtn.setBounds(25, 545, 100, 25);
        exit.setBounds(825, 540, 100, 25);
        res.setBounds(25, 480, 300, 25);
    }


    public static void open(User user, JFrame previousJFrame) throws IOException {
        if (previousJFrame != null)
            previousJFrame.dispose();
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.getContentPane().add(new CataloguePanel(user));
        currentFrame.pack();
        currentFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == profileBtn) {
            if (user instanceof Admin) {
                AdminDashboard adminDashboard = null;
                try {
                    adminDashboard = new AdminDashboard((Admin) user);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    AdminDashboard.open((Admin) user, currentFrame);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (user instanceof Seller) {
                try {
                    SellerDashboard sellerDashboard = new SellerDashboard((Seller) user);
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
                    BuyerDashboard.open((Buyer) user, currentFrame);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == addToCartBtn) {

            String productStr = comboProduct.getSelectedItem().toString().trim();
            for (Product product :
                    products) {
                if (Objects.equals(product.getName(), productStr)) {
                    cart.addProduct(product);
                    JOptionPane.showMessageDialog(null, "Item Added to cart successfully", "Cart Notification", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else if (e.getSource() == checkOut) {
            if (cart.getProducts().size() < 1)
                JOptionPane.showMessageDialog(null, "Please add at least one item to checkout", "Checkout Notification", JOptionPane.ERROR_MESSAGE);
            else {
                CheckoutPanel checkoutPanel = new CheckoutPanel(cart, user);
                checkoutPanel.open(user, cart, currentFrame);
            }
        } else if (e.getSource() == exit) {
            LoginPanel.open(currentFrame);
        }
    }
}
