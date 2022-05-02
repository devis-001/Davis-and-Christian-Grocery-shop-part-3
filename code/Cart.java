import java.io.IOException;
import java.util.ArrayList;

public class Cart {
    private ArrayList<Product> products=new ArrayList<>();
    private int cartID;
    private Buyer buyer;
//    private User buyer;
    private int cartsCreated=0;
    
    public Cart(Buyer buyer){
        this.buyer=buyer;
        this.cartID=400000000 + this.getCartsCreated();
        cartsCreated+=1;
    }


    //methods
    public String cartInfo(){
        return this.toString();
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public void removeProduct(Product product){
        products.remove(product);
    }

    public boolean canCheckout(){
        double total=calculateTotal();
        if (total > buyer.getWallet()){
            return false;
        }
        else {
            buyer.setWallet(buyer.getWallet()-total);
            return true;
        }
    }

    public double calculateTotal(){
        double total=0;
        for (Product product :
                products) {
            total+= product.getPrice();
        }
        return total;
    }

    public int getCartsCreated() {
        return cartsCreated;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public String padding(String input, char ch, int L)
    {
        String result = String.format("%" + L + "s", input).replace(' ', ch);

        return result;
    }
    public ArrayList<Product> getProducts(){
        return this.products;
    }

    @Override
    public String toString() {
        StringBuilder receipt=new StringBuilder();
        int pad=20;
//        double total=0;
        receipt.append( "ID" + padding("Name",' ',pad) +  padding("Price",' ', pad) +  padding("Seller ID",' ',pad) + "\n");
        for (Product product :
                products) {
            String productInfo= product.getProductID() + padding(product.getName(),' ',pad) + padding(Double.toString(product.getPrice()),' ',pad) + padding(product.getSeller(),' ',pad) + "\n" ;
            receipt.append(productInfo);
//            total+= product.getPrice();
        }

        receipt.append(padding("Total cost:",' ',40)+ calculateTotal() + "\n");
        if (canCheckout()){
            receipt.append("Dear," + buyer.getFirstName() + " " + buyer.getLastName() + ", Thank you for shopping with us");
            for (Product product :
                    products) {

                try {
                    Store.delete(product.getProductID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            receipt.append("Dear," + buyer.getFirstName() + " " + buyer.getLastName() + ", You do not have enough coins in your wallet to make this purchase");
        }


        return receipt.toString();
    }
}
