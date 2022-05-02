import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Admin extends User{

    private String type="Admin";
    public Admin(String firstName, String lastName, String email, String phone, String password) {
        super(firstName, lastName, email, phone, password);
    }

    //methods
    public void manageAccount(){

    }

    // get buyers
    public ArrayList<Buyer> getBuyers(){
        ArrayList<Buyer> buyers=new ArrayList<>();

        try {
            String currentLine;

            BufferedReader in = new BufferedReader(new FileReader("usersDB.txt"));

            while ((currentLine = in.readLine()) != null) {
                String[] details=currentLine.split(",");
                String firstName=details[0];
                String lastName=details[1];
                String email=details[2];
                String phone=details[3];
                String password=details[4];
                double wallet=Double.parseDouble(details[5]);
                String type=details[6].replace("\n","").strip();

                if (Objects.equals(type, "Buyer")){
                    User buyer=new Buyer(firstName,lastName,email,phone,password);
                    buyer.setWallet(wallet);
                    buyers.add((Buyer) buyer);
                }

            }

        }
        catch (IOException e) {
            System.out.println("exception occurred"+ e);
        }
        return buyers;
    }

    // get sellers
    public ArrayList<Seller> getSellers(){
        ArrayList<Seller> sellers=new ArrayList<>();

        try {
            String currentLine;

            BufferedReader in = new BufferedReader(new FileReader("usersDB.txt"));

            while ((currentLine = in.readLine()) != null) {
                String[] details=currentLine.split(",");
                String firstName=details[0];
                String lastName=details[1];
                String email=details[2];
                String phone=details[3];
                String password=details[4];
                double wallet=Double.parseDouble(details[5]);
                String type=details[6].replace("\n","").strip();

                if (Objects.equals(type, "Seller")){
                    User seller=new Seller(firstName,lastName,email,phone,password);
                    seller.setWallet(wallet);
                    sellers.add((Seller) seller);
                }

            }

        }
        catch (IOException e) {
            System.out.println("exception occurred"+ e);
        }
        return sellers;
    }
}
