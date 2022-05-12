import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutPanel extends JPanel {
    private static final JFrame currentFrame = new JFrame("Checkout Panel");
    public static JList productSummary;
    private final JLabel orderLabel;
    private final JLabel totalLabel;
    private final JTextField totalText;
    private final JButton checkoutBtn;
    private final JButton cancelBtn;
    private final JButton backBtn;
    private final JTextField res;
    //    Cart cart;
    private final User user;
    private final String usersDbFile = "usersDB.txt";

    public CheckoutPanel(Cart cart, User user) {
//        this.cart = cart;
        this.user = user;
        ArrayList<Product> products = cart.getProducts();

        //construct preComponents
        String[] productSummaryItems = new String[products.size()];

        productSummaryItems[0] = "ID" + padString("Name", 20) + padString("Price", 20) + padString("Seller Email", 20);
        for (int i = 0; i < productSummaryItems.length; i++) {
            String productString = products.get(i).toString();
            int m = productString.lastIndexOf("m");
            productString = productString.replace(productString.substring(m + 1), "    1");
            productSummaryItems[i] = productString;
        }


        //construct components
        productSummary = new JList(productSummaryItems);
        orderLabel = new JLabel("Order");
        totalLabel = new JLabel("Total");
        totalText = new JTextField(5);
        checkoutBtn = new JButton("Make Payment");
        checkoutBtn.addActionListener((e) -> {
            if (cart.canCheckout()) {
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

                User.updateWalletBalance(user.getEmail(), user.getWallet() - cartTotal);

//                BuyerDashboard.walletText.setText(String.valueOf(newAccountBalance));

                JOptionPane.showMessageDialog(null, "Your purchase was successful. The items will be delivered soon", "Checkout", JOptionPane.INFORMATION_MESSAGE);
                // reset products
//                CataloguePanel.cart = null;

            } else {
                JOptionPane.showMessageDialog(null, "You have insufficient balance. Kindly recharge", "Checkout", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn = new JButton("Cancel Order");
        cancelBtn.addActionListener((e) -> {
            try {
                CataloguePanel.open(user, currentFrame);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        backBtn = new JButton("DashBoard");
        backBtn.setFocusable(false);
        backBtn.addActionListener((e) -> {
            try {
                BuyerDashboard.open((Buyer) user, currentFrame);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        res = new JTextField(" ");
        totalText.setText("$" + cart.calculateTotal());

        //adjust size and set layout
        setPreferredSize(new Dimension(944, 601));
        setLayout(null);

        //add components
        add(productSummary);
        add(orderLabel);
        add(totalLabel);
        add(totalText);
        add(checkoutBtn);
        add(cancelBtn);
        add(backBtn);

        //set component bounds (only needed by Absolute Positioning)
        backBtn.setBounds(225, 20, 100, 25);
        productSummary.setBounds(225, 55, 450, 370);
        orderLabel.setBounds(225, 50, 100, 25);
        totalLabel.setBounds(225, 465, 100, 25);
        totalText.setBounds(375, 465, 300, 25);
        checkoutBtn.setBounds(225, 510, 200, 25);
        cancelBtn.setBounds(475, 510, 200, 25);
        res.setBounds(225, 540, 450, 50);
    }

    public static void open(User user, Cart cart, JFrame previousFrame) {
        if (previousFrame != null)
            previousFrame.dispose();
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.getContentPane().add(new CheckoutPanel(cart, user));
        currentFrame.pack();
        currentFrame.setVisible(true);
    }

    private Double getOldAccountBalance(String userEmail) {
        Double oldAccountBalance = null;
        try {
            List<String> userByEmail = Files.readAllLines(Paths.get(usersDbFile)).stream().filter(userFromFile -> userFromFile.contains(userEmail)).collect(Collectors.toList());
            if (userByEmail.size() != 0) {
                oldAccountBalance = Double.valueOf(userByEmail.get(0).split(",")[5]);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return oldAccountBalance;
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
