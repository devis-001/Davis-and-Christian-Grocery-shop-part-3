public class Product {
    private int productID;
    private String name;
    private double price;
    private String seller;

    private static int productsCreated=0;

    public Product(String name,double price,String seller){
        this.name=name;
        this.price=price;
        this.seller=seller;
        this.productID=500000000 + getProductsCreated();
        productsCreated+=1;
    }

    public static int getProductsCreated() {
        return productsCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public int getProductID() {
        return productID;
    }
    public void setProductID(int id){
        this.productID=id;
    }


    // Update product
    public void updateProduct(String name,float price){
        this.setName(name);
        this.setPrice(price);
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

    @Override
    public String toString() {
        return productID + padString(name,20) + padString(String.valueOf(price),20) + padString(seller,20);
    }



}
