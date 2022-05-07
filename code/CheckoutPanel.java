import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckoutPanel extends JPanel implements ActionListener {
    private static final JFrame currentFrame = new JFrame("Checkout Panel");
    private final JList productSummary;
    private final JLabel orderLabel;
    private final JLabel totalLabel;
    private final JTextField totalText;
    private final JButton checkoutBtn;
    private final JButton cancelBtn;
    private final JButton backBtn;
    private final JTextField res;
    Cart cart;
    User user;

    public CheckoutPanel(Cart cart, User user) {
        this.cart = cart;
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
        checkoutBtn.addActionListener(this);
        cancelBtn = new JButton("Cancel Order");
        cancelBtn.addActionListener(this);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkoutBtn) {
            if (cart.canCheckout()) {
                cart.getProducts().forEach(product -> {
                    try {
                        int productID = product.getProductID();
                        Store.update(productID, product.getName(), product.getPrice(), product.getQuantity().subtract(BigDecimal.ONE));
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

                JOptionPane.showMessageDialog(null, "Your purchase was successful. The items will be delivered soon", "Checkout", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "You have insufficient balance. Kindly recharge", "Checkout", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancelBtn) {
            try {
                CataloguePanel.open(user, currentFrame);
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
