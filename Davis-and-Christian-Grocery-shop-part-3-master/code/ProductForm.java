import java.awt.*;
        import java.awt.event.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

public class ProductForm extends JPanel implements ActionListener {
    private JLabel productForm;
    private JLabel nameLabel;
    private JTextField productName;
    private JLabel quantityLabel;
    private JTextField quantityText;
    private JTextField productPrice;
    private JLabel priceLabel;
    private JButton saveProduct;
    private JButton exit;
    private JButton backSeller;
    private Seller seller;
    private JTextField res;
    private static final JFrame currentFrame = new JFrame ("Product Form");

    public ProductForm(Seller seller) {
        //construct components
        productForm = new JLabel ("New Product Form");
        nameLabel = new JLabel ("Product Name:");
        productName = new JTextField (5);
        productPrice = new JTextField (5);
        priceLabel = new JLabel ("Product Price:");
        quantityText = new JTextField (5);
        quantityLabel = new JLabel ("Product Quantity:");
        saveProduct = new JButton ("Save Product");
        saveProduct.addActionListener(this);
        exit = new JButton ("Exit");
        backSeller = new JButton ("Back To Dashboard");
        backSeller.addActionListener(this);
        res = new JTextField(" ");

        this.seller=seller;

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (productForm);
        add (nameLabel);
        add (productName);
        add (quantityLabel);
        add (quantityText);
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
        quantityText.setBounds (365, 210, 300, 25);
        quantityLabel.setBounds (220, 210, 100, 25);
        saveProduct.setBounds (410, 250, 200, 25);
        exit.setBounds (805, 535, 100, 25);
        backSeller.setBounds (35, 540, 200, 25);
        res.setBounds(365, 420, 300, 25);
    }

    public static void open (Seller seller, JFrame prevFrame) {
        prevFrame.dispose();
        currentFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        currentFrame.getContentPane().add (new ProductForm(seller));
        currentFrame.pack();
        currentFrame.setVisible (true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveProduct){
            String name=productName.getText();
            double price=Double.parseDouble(productPrice.getText());

            Product product=new Product(name,price,seller.getEmail(), BigDecimal.valueOf(Integer.parseInt(quantityText.getText())));
            try {
                Store.save(product);
                SellerDashboard.products.setListData(generateProductList(Objects.requireNonNull(Store.retrieveBySeller(seller.getEmail()))));
                JOptionPane.showMessageDialog(null,"Product added successfully","Products",JOptionPane.INFORMATION_MESSAGE);
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
                sellerDashboard.open(seller, currentFrame);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String[] generateProductList(ArrayList<Product> sellersProduct) {

        //construct preComponents
        String[] productsList = new String[sellersProduct.size()];
        if (productsList.length == 0) {
            productsList = new String[]{"You do not have any product in store", "Add your products"};
        } else {

            int longestword = 0;
            for (int i = 0; i < productsList.length; i++) {
                Product product = sellersProduct.get(i);
                String string = String.format("%20d%20s%40s%20s%20s",
                        product.getProductID(),
                        product.getName(),
                        product.getPrice(),
                        product.getSeller(),
                        product.getQuantity());

                productsList[i] = string;
            }
        }
        return productsList;
    }
}
