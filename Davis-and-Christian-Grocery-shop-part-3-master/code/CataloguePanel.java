import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final JLabel cartTitle;
    // for checkout
    private final JList productSummary;
    private final JLabel totalLabel;
    private final JTextField totalText;
    private final String usersDbFile = "usersDB.txt";
    ArrayList<Product> products;
    Cart cart = null;


    public CataloguePanel(User user) throws IOException {
        this.user = user;
        products = Store.retrieve();
        cart = new Cart((Buyer) user);
        //construct preComponents

        String[] comboProducts = getComboProducts();

        String[] productListItems = getProductListItems();

        String[] productSummaryForCheckout = getProductSummaryForCheckout();


        //construct components
        totalLabel = new JLabel("Total");
        totalText = new JTextField(5);
        totalText.setEditable(false);
        totalText.setText("$ " + cart.calculateTotal());
        catalogLegend = new JLabel("Product Catalogue");
        cartTitle = new JLabel("Items in Cart");
        productList = new JList(productListItems);
        productSummary = new JList(productSummaryForCheckout);
        addToCart = new JLabel("Select product: ");
        comboProduct = new JComboBox(comboProducts);
        addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.setFocusable(false);
        addToCartBtn.setBackground(Color.lightGray);
        addToCartBtn.addActionListener((e) -> {
            String productStr = comboProduct.getSelectedItem().toString().trim();

            try {
                products = Store.retrieve();
                for (Product product :
                        products) {
                    if (Objects.equals(product.getName(), productStr)) {
                        cart.addProduct(product);
                        productSummary.setListData(getProductSummaryForCheckout());
                        totalText.setText("$ " + cart.calculateTotal());
                        JOptionPane.showMessageDialog(null, "Item Added to cart successfully", "Cart Notification", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        checkOut = new JButton("Checkout");
        checkOut.setFocusable(false);
        checkOut.setBackground(Color.green);
        checkOut.addActionListener((e) -> {

            if (cart.getProducts().size() < 1)
                JOptionPane.showMessageDialog(null,
                        "Please add at least one item to cart to checkout",
                        "Checkout", JOptionPane.ERROR_MESSAGE);
            else {
                int confirmPayment = JOptionPane.showOptionDialog(null, "You are about to pay: " + cart.calculateTotal(),
                        "Checkout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Pay", "Cancel"}, null);

                if (confirmPayment == 0 && cart.canCheckout()) {
                    cart.getProducts().forEach(product -> {
                        try {
                            int productID = product.getProductID();
                            Store.update(productID, product.getName(), product.getPrice(), product.getQuantity().subtract(BigDecimal.ONE));

                            // update seller account
                            Double oldAccountBalance = getOldAccountBalance(product.getSeller());
                            User.updateWalletBalance(product.getSeller(), oldAccountBalance + product.getPrice());
                            if (SellerDashboard.walletText != null)
                                SellerDashboard.walletText.setText(String.valueOf(getOldAccountBalance(product.getSeller())));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    BufferedWriter checkoutFile = null;
                    try {
                        checkoutFile = new BufferedWriter(new FileWriter("checkout-records.txt", true));
                        checkoutFile.write(String.format("\n%s%30s%30s%30s", cart.getCartID(), LocalDate.now(), user.getEmail(), totalText.getText()));
                        checkoutFile.close();

                        if (Reports.checkouts != null)
                            Reports.checkouts.setListData(Reports.generateCheckOutDetailsForReport());


                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // update user balance
                    double cartTotal = cart.getProducts().stream()
                            .mapToDouble(Product::getPrice)
                            .reduce(Double::sum).getAsDouble();
                    Double oldAccountBalance = getOldAccountBalance(user.getEmail());
                    assert oldAccountBalance != null;
                    double newAccountBalance = oldAccountBalance - cartTotal;

                    User.updateWalletBalance(user.getEmail(), newAccountBalance);

                    BuyerDashboard.walletText.setText(String.valueOf(newAccountBalance));

                    JOptionPane.showMessageDialog(null, "Your purchase was successful. The items will be delivered soon", "Checkout", JOptionPane.INFORMATION_MESSAGE);
                    cart.setProducts(new ArrayList<>());
                    productSummary.setListData(new String[]{});
                    totalText.setText("$ 0.0");

                    // update displayed product list
                    productList.setListData(getProductListItems());

                } else {
                    JOptionPane.showMessageDialog(null, "You have insufficient balance. Kindly recharge", "Checkout", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
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
        add(cartTitle);
        add(productList);
        add(productSummary);
        add(addToCart);
        add(comboProduct);
        add(addToCartBtn);
        add(checkOut);
        add(profileBtn);
        add(exit);
        add(totalLabel);
        add(totalText);

        //set component bounds (only needed by Absolute Positioning)
        catalogLegend.setBounds(25, 40, 450, 25);
        productList.setBounds(25, 70, 450, 320);
        cartTitle.setBounds(485, 40, 450, 25);
        productSummary.setBounds(485, 70, 450, 320);
        addToCart.setBounds(25, 400, 900, 35);
        comboProduct.setBounds(180, 400, 150, 25);
        addToCartBtn.setBounds(330, 400, 100, 25);
        totalLabel.setBounds(485, 400, 100, 25);
        totalText.setBounds(550, 400, 60, 25);
        checkOut.setBounds(610, 400, 250, 25);
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

    private String[] getProductListItems() {
        String[] productListItems = null;
        try {
            List<String> currentInfo = Files.readAllLines(Paths.get("productsDB.txt"));
            productListItems = new String[currentInfo.size()];
//            productListItems[0] = "Id          Title         price         seller         No. available";
            for (int i = 1; i < productListItems.length; i++) {
                productListItems[i] = currentInfo.get(i).replace(",","         ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productListItems;
    }

    private String[] getComboProducts() {
        String[] comboProducts = new String[products.size()];
        for (int i = 0; i < comboProducts.length; i++) {
            comboProducts[i] = products.get(i).getName();
        }
        return comboProducts;
    }

    private String[] getProductSummaryForCheckout() {
        String[] productSummaryForCheckout = new String[cart.getProducts().size()];
        for (int i = 0; i < productSummaryForCheckout.length; i++) {
            String product = cart.getProducts().get(i).toString();
            productSummaryForCheckout[i] = product.substring(0, product.lastIndexOf("m") + 1);
        }
        return productSummaryForCheckout;
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


        } else if (e.getSource() == checkOut) {
            if (cart.getProducts().size() < 1)
                JOptionPane.showMessageDialog(null, "Please add at least one item to checkout", "Checkout Notification", JOptionPane.ERROR_MESSAGE);
            else {
                CheckoutPanel checkoutPanel = new CheckoutPanel(cart, user);
                CheckoutPanel.open(user, cart, currentFrame);
            }
        } else if (e.getSource() == exit) {
            LoginPanel.open(currentFrame);
        }
    }

    private Double getOldAccountBalance(String userEmail) {
        Double oldAccountBalance = null;
        try {
            Path path = Paths.get(usersDbFile);
            List<String> userByEmail = Files.readAllLines(path).stream().filter(userFromFile -> userFromFile.contains(userEmail)).collect(Collectors.toList());

            if (userByEmail.size() != 0) {
                oldAccountBalance = Double.valueOf(userByEmail.get(0).split(",")[5]);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return oldAccountBalance;
    }
}
