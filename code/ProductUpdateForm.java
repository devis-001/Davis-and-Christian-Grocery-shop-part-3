import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;

public class ProductUpdateForm extends JPanel implements ActionListener {
    private JLabel productForm;
    private JLabel nameLabel;
    private JTextField productName;
    private JTextField productPrice;
    private JLabel priceLabel;
    private JButton saveProduct;
    private JButton exit;
    private JButton backSeller;
    private Seller seller;
    private JTextField res;

    private int productID;


    public ProductUpdateForm(Seller seller,int productID) {
        //construct components
        productForm = new JLabel ("Product Update Form");
        nameLabel = new JLabel ("Product Name:");
        productName = new JTextField (5);
        productPrice = new JTextField (5);
        priceLabel = new JLabel ("Product Price:");
        saveProduct = new JButton ("Update Product");
        saveProduct.addActionListener(this);
        exit = new JButton ("Exit");
        backSeller = new JButton ("Back To Dashboard");
        backSeller.addActionListener(this);
        res = new JTextField(" ");

        this.seller=seller;
        this.productID=productID;

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (productForm);
        add (nameLabel);
        add (productName);
        add (productPrice);
        add (priceLabel);
        add (saveProduct);
        add (exit);
        add (backSeller);

        //set component bounds (only needed by Absolute Positioning)
        productForm.setBounds (370, 60, 200, 25);
        nameLabel.setBounds (220, 115, 100, 25);
        productName.setBounds (365, 115, 300, 25);
        productPrice.setBounds (365, 170, 300, 25);
        priceLabel.setBounds (220, 170, 100, 25);
        saveProduct.setBounds (410, 235, 200, 25);
        exit.setBounds (805, 535, 100, 25);
        backSeller.setBounds (35, 540, 200, 25);
        res.setBounds(365, 420, 300, 25);
    }

    public static void open(Seller seller, int id) {
        JFrame frame = new JFrame ("Product Update Form");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new ProductUpdateForm(seller,id));
        frame.pack();
        frame.setVisible (true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveProduct){

            String name=productName.getText();
            double price=Double.parseDouble(productPrice.getText());

            try {
                Store.update(productID,name,price);
                add(res);
                res.setText("Product updated");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if(e.getSource()==backSeller){
            SellerDashboard sellerDashboard= null;
            try {
                sellerDashboard = new SellerDashboard(seller);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                sellerDashboard.open(seller);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
