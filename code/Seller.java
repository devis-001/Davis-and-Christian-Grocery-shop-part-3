import java.io.IOException;
import java.util.ArrayList;

public class Seller extends User{

    private double wallet;

    // Subclass constructor
    public Seller(String firstName, String lastName, String email, String phone, String password){
        super(firstName, lastName, email, phone, password);
        this.wallet=0;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public void addProduct(Product product) throws IOException {
        Store.save(product);
    }

    public ArrayList<Product> getProducts(String email) throws IOException {
        return Store.retrieveBySeller(email);
    }
}
