import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class SellerDashboard extends JPanel {
    public static JList products;
    public static JTextField walletText;
    static JFrame currentFrame = new JFrame("Seller Dashboard");
    private final JLabel sellerDashLabel;
    private final JLabel nameLabel;
    private final JLabel emailLabel;
    private final JLabel phoneLabel;
    private final JLabel walletLabel;
    private final JTextField nameText;
    private final JTextField emailText;
    private final JTextField phoneText;
    private final JLabel sellerProducts;
    private final JButton newBtn;
    private final JButton exit;
    private final Seller seller;
    private final JButton updateBtn;
    private final JButton deleteBtn;

    private final JLabel newNameLabel;
    private final JTextField newProductName;
    private final JLabel newQuantityLabel;
    private final JTextField newQuantityText;
    private final JTextField newProductPrice;
    private final JLabel newPriceLabel;
    private final JButton saveProduct;

    public SellerDashboard(Seller seller) throws IOException {
        this.seller = seller;

        String[] productsList = generateProductList(seller.getProducts(seller.getEmail()));

        //construct components
        sellerDashLabel = new JLabel("Seller Dashboard");
        nameLabel = new JLabel("Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone:");
        walletLabel = new JLabel("Wallet:");
        nameText = new JTextField(5);
        nameText.setEditable(false);
        emailText = new JTextField(5);
        emailText.setEditable(false);
        phoneText = new JTextField(5);
        phoneText.setEditable(false);
        walletText = new JTextField(5);
        walletText.setEditable(false);

        sellerProducts = new JLabel("My Products");
        products = new JList(productsList);

        newNameLabel = new JLabel("New name:");
        newProductName = new JTextField();
        newQuantityLabel = new JLabel("New Quantity");
        newQuantityText = new JTextField();
        newProductPrice = new JTextField();
        newPriceLabel = new JLabel("New price");
        saveProduct = new JButton();
        saveProduct.setFocusable(false);

        newBtn = new JButton("New Product");
        newBtn.setFocusable(false);
        newBtn.setBackground(Color.GREEN);
        newBtn.addActionListener((e) -> {
            newProductPrice.setText("");
            newProductName.setText("");
            newQuantityText.setText("");
            int updateProductDialog = JOptionPane.showOptionDialog(
                    null,
                    new Object[]{newNameLabel,
                            newProductName,
                            newQuantityLabel,
                            newQuantityText,
                            newPriceLabel, newProductPrice}, "new product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

            try {
                if (updateProductDialog == 0) {
                    Store.save(new Product(newProductName.getText(), Double.parseDouble(newProductPrice.getText()), seller.getEmail(), BigDecimal.valueOf(Long.parseLong(newQuantityText.getText()))));
                    products.setListData(generateProductList(Store.retrieveBySeller(seller.getEmail())));
                    JOptionPane.showMessageDialog(null, "Item added successfully");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        exit = new JButton("Exit");
        exit.setFocusable(false);
        exit.addActionListener((e) -> {
            LoginPanel.open(currentFrame);
        });
        updateBtn = new JButton("Update");
        updateBtn.setFocusable(false);
        updateBtn.addActionListener((e) -> {

            Object selectedValue = products.getSelectedValue();
            System.out.println(selectedValue);
            if (selectedValue != null) {

                String[] prevProd = selectedValue.toString().split("\\s+");
                newProductName.setText(prevProd[2].trim());
                newProductPrice.setText(prevProd[3].trim());
                newQuantityText.setText(prevProd[5].trim());

                int newProductDialog = JOptionPane.showOptionDialog(
                        null,
                        new Object[]{newNameLabel,
                                newProductName,
                                newQuantityLabel,
                                newQuantityText,
                                newPriceLabel, newProductPrice}, "new product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);


                try {
                    if (newProductDialog == 0) {
                        Store.update(Integer.parseInt(prevProd[1].trim()), newProductName.getText(), Double.parseDouble(newProductPrice.getText()), BigDecimal.valueOf(Long.parseLong(newQuantityText.getText())));
                        products.setListData(generateProductList(Store.retrieveBySeller(seller.getEmail())));
                        JOptionPane.showMessageDialog(null, "Item updated successfully", "Update", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an item to update", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteBtn = new JButton("Delete");
        deleteBtn.setFocusable(false);
        deleteBtn.setBackground(Color.RED);
        deleteBtn.addActionListener((e) -> {

            Object selectedValue = products.getSelectedValue();
            if (selectedValue != null) {
                String productId = selectedValue.toString().split("\\s+")[1].trim();
                try {
                    Store.delete(Integer.parseInt(productId.trim()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    int shouldDelete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                    if (shouldDelete == 0)
                        products.setListData(generateProductList(Objects.requireNonNull(Store.retrieveBySeller(seller.getEmail()))));

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an item to delete", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //adjust size and set layout
        setPreferredSize(new Dimension(944, 601));
        setLayout(null);

        //add components
        add(sellerDashLabel);
        add(nameLabel);
        add(emailLabel);
        add(phoneLabel);
        add(walletLabel);
        add(nameText);
        add(emailText);
        add(phoneText);
        add(walletText);
        add(sellerProducts);
        add(products);
        add(newBtn);
        add(exit);
        add(updateBtn);
        add(deleteBtn);

        //set component bounds (only needed by Absolute Positioning)
        sellerDashLabel.setBounds(10, 10, 100, 25);
        nameLabel.setBounds(15, 50, 100, 25);
        emailLabel.setBounds(15, 80, 100, 25);
        phoneLabel.setBounds(15, 110, 100, 25);
        walletLabel.setBounds(15, 145, 100, 25);
        nameText.setBounds(70, 50, 300, 25);
        emailText.setBounds(70, 85, 300, 25);
        phoneText.setBounds(70, 115, 300, 25);
        walletText.setBounds(70, 145, 300, 25);
        sellerProducts.setBounds(410, 195, 100, 25);
        products.setBounds(20, 240, 900, 200);
        updateBtn.setBounds(230, 520, 100, 25);
        deleteBtn.setBounds(340, 520, 100, 25);
        newBtn.setBounds(20, 520, 200, 25);
        exit.setBounds(815, 520, 100, 25);


        // Set field values
        nameText.setText(seller.getFirstName() + " " + seller.getLastName());
        emailText.setText(seller.getEmail());
        phoneText.setText(seller.getPhone());
        walletText.setText("$ " + seller.getWallet());
//        sellerWallet.setText("$" + seller.getWallet());
    }

    public static void open(Seller seller, JFrame prevFrame) throws IOException {
        if (prevFrame != null)
            prevFrame.dispose();
        products = new JList<>(generateProductList(Objects.requireNonNull(Store.retrieveBySeller(seller.getEmail()))));
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.getContentPane().add(new SellerDashboard(seller));
        currentFrame.pack();
        currentFrame.setVisible(true);
    }

    public static String[] generateProductList(ArrayList<Product> sellersProduct) throws IOException {

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
