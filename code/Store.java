import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Store {
    static String filename= "productsDB.txt";


    //    retrieve products
    public static ArrayList<Product> retrieve() throws IOException {
        ArrayList<Product> products = new ArrayList<>();

        // Reading the content of the file
        Path fileName = Paths.get(filename);
//        String file_content = ;
        String[] productList = Files.readAllLines(fileName).toArray(new String[0]);

        for (String product :
                productList) {
            String[] productDetails = product.split(",");

            int ID = Integer.parseInt(productDetails[0]);
            String name = (productDetails[1]);
            double price = Double.parseDouble(productDetails[2]);
            String seller = productDetails[3];
            BigDecimal quantity = BigDecimal.valueOf(Integer.parseInt(productDetails[4]));

            Product newProduct = new Product(name, price, seller, quantity);
            newProduct.setProductID(ID);

            if (Integer.parseInt(quantity.toString()) > 0)
                products.add(newProduct);
        }
        return products;
    }

    //    retrieve products
    public static ArrayList<Product> retrieveBySeller(String email) throws IOException {
        ArrayList<Product> products = retrieve();
        ArrayList<Product> sellerProducts = new ArrayList<>();
        if (products.size() == 0) {
            System.out.println("No products");
            return null;
        } else {
            for (Product product :
                    products) {

                if (product.getSeller().equals(email)) {
                    sellerProducts.add(product);
                }
            }
        }

        return sellerProducts;
    }

    //    save products
    public static void save(Product product) throws IOException {
        // Writing into the file
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
            out.write(product.getProductID() + "," + product.getName() + "," + product.getPrice() + "," + product.getSeller() + "," + product.getQuantity() + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
    }

    //    Delete product
    public static void delete(int productID) throws IOException {
        //Log the product data to log file
        StringBuilder data = new StringBuilder();

        // Read products database
        Path fileName = Paths.get(filename);
        String[] products = Files.readAllLines(fileName).toArray(new String[0]);

        for (String line :
                products) {
            if (line.startsWith(Integer.toString(productID))) {
                continue;
            }
            data.append(line).append("\n");
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(String.valueOf(data));
            out.close();

        } catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
    }

    //    Update product
    public static void update(int productID, String name, double price, BigDecimal quantity) throws IOException {
        //Log the product data to log file
        StringBuilder data = new StringBuilder();

        // Read products database

        Path fileName = Paths.get(filename);
        String[] products = Files.readAllLines(fileName).toArray(new String[0]);

        for (String line :
                products) {
            if (line.startsWith(Integer.toString(productID))) {

                String[] details = line.split(",");
                details[1] = name;
                details[2] = String.valueOf(price);
                details[4] = String.valueOf(quantity);

                StringBuilder new_details = new StringBuilder();

                for (int i = 0; i < details.length; i++) {
                    if (i == details.length - 1) {
                        new_details.append(details[i]).append("\n");
                    } else {
                        new_details.append(details[i]).append(",");
                    }
                }
                data.append(new_details);
                continue;
            }
            data.append(line).append("\n");
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(String.valueOf(data));
            out.close();

        } catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
    }
}

