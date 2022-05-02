import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {
    static String filename= "productsDB.txt";


    //    retrieve products
    public static ArrayList<Product> retrieve() throws IOException {
        ArrayList<Product> products=new ArrayList<>();

        // Reading the content of the file
        Path fileName = Path.of(filename);
        String file_content = Files.readString(fileName);
        String[] productList=file_content.split("\n");

        for (String product :
                productList) {
            String[] productDetails = product.split(",");

            int ID=Integer.parseInt(productDetails[0]);
            String name=(productDetails[1]);
            double price=Double.parseDouble(productDetails[2]);
            String seller=productDetails[3];

            Product newProduct=new Product(name,price,seller);
            newProduct.setProductID(ID);

            products.add(newProduct);
        }
        return products;
    }

    //    retrieve products
    public static ArrayList<Product> retrieveBySeller(String email) throws IOException {
        ArrayList<Product> products=retrieve();
        ArrayList<Product> sellerProducts=new ArrayList<>();
        if (products.size()==0){
            System.out.println("No products");
            return null;
        }
        else {
            for (Product product:
                    products) {

                if (product.getSeller().equals(email)){
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
            BufferedWriter out = new BufferedWriter(new FileWriter(filename,true));
            out.write(product.getProductID() +","+product.getName() +","+product.getPrice()+","+product.getSeller()+"\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred"+ e);
        }
    }

    //    Delete product
    public static void delete(int productID) throws IOException {
        //Log the product data to log file
        StringBuilder data=new StringBuilder();

        // Read products database
        String databaseContent = Files.readString(Path.of(filename));
        String[] products=databaseContent.split("\n");

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

        }
        catch (IOException e) {
            System.out.println("exception occurred"+ e);
        }
    }

    //    Update product
    public static void update(int productID,String name,double price) throws IOException {
        //Log the product data to log file
        StringBuilder data=new StringBuilder();

        // Read products database
        String databaseContent = Files.readString(Path.of(filename));
        String[] products=databaseContent.split("\n");

        for (String line :
                products) {
            if (line.startsWith(Integer.toString(productID))) {

                String[] details=line.split(",");
                details[1]=name;
                details[2]= String.valueOf(price);

                StringBuilder new_details=new StringBuilder();

                for (int i = 0; i < details.length; i++) {
                    if (i==details.length-1){
                        new_details.append(details[i]).append("\n");
                    }
                    else {
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

        }
        catch (IOException e) {
            System.out.println("exception occurred"+ e);
        }
    }
}

