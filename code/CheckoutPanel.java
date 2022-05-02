import java.awt.*;
        import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
        import javax.swing.event.*;

public class CheckoutPanel extends JPanel implements ActionListener {
    private JList productSummary;
    private JLabel orderLabel;
    private JLabel totalLabel;
    private JTextField totalText;
    private JButton checkoutBtn;
    private JButton cancelBtn;
    private JTextField res;

    Cart cart;
    User user;

    public CheckoutPanel(Cart cart,User user) {
        this.cart=cart;
        this.user=user;
        ArrayList<Product> products=cart.getProducts();

        //construct preComponents
        String[] productSummaryItems = new String[products.size()];

        productSummaryItems[0]="ID" + padString("Name",20) + padString("Price",20)+ padString("Seller Email",20);
        for (int i = 0; i < productSummaryItems.length; i++) {
            productSummaryItems[i+1]=products.get(i).toString();
        }


        //construct components
        productSummary = new JList (productSummaryItems);
        orderLabel = new JLabel ("Order");
        totalLabel = new JLabel ("Total");
        totalText = new JTextField (5);
        checkoutBtn = new JButton ("Make Payment");
        checkoutBtn.addActionListener(this);
        cancelBtn = new JButton ("Cancel Order");
        cancelBtn.addActionListener(this);
        res = new JTextField(" ");
        totalText.setText("$"+ cart.calculateTotal());

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (productSummary);
        add (orderLabel);
        add (totalLabel);
        add (totalText);
        add (checkoutBtn);
        add (cancelBtn);

        //set component bounds (only needed by Absolute Positioning)
        productSummary.setBounds (225, 55, 450, 370);
        orderLabel.setBounds (225, 20, 100, 25);
        totalLabel.setBounds (225, 435, 100, 25);
        totalText.setBounds (375, 435, 300, 25);
        checkoutBtn.setBounds (225, 480, 200, 25);
        cancelBtn.setBounds (475, 480, 200, 25);
        res.setBounds(225, 510, 450, 50);
    }

    public void open(User user,Cart cart) {
        JFrame frame = new JFrame ("Checkout Panel");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new CheckoutPanel(cart, user));
        frame.pack();
        frame.setVisible (true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==checkoutBtn){
            if (cart.canCheckout()){
                add(res);
                res.setText("Thank you for shopping with us. Your order will be delivered");
            }
            else {
                add(res);
                res.setText("Dear customer, You have insufficient coins in your wallet. Kindly recharge the tyr again");
            }
        }
        else if(e.getSource()==cancelBtn){
            try {
                CataloguePanel cataloguePanel=new CataloguePanel(user);
                cataloguePanel.open(user);
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
