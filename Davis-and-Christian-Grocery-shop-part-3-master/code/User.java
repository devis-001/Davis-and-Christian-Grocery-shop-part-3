import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private double wallet;

    private static final String usersDbFile = "usersDB.txt";

    public User(String firstName,String lastName,String email,String phone,String password){
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.password=password;
    }


    // Access modifiers
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public double getWallet() {
        return wallet;
    }
    public void setWallet(double newWallet){
        wallet=newWallet;
    }
    // Open files to save user as per type of the user
    public String padding(String input, char ch, int L)
    {
        String result = String.format("%" + L + "s", input).replace(' ', ch);

        return result;
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
        return firstName + padString(lastName,20) + padString(email,25) + padString( phone,20);
    }

    public static String findClass(User o){
        ArrayList<String> list = new ArrayList<>();
        Class<? extends Object> cl = o.getClass();

        list.add(cl.getSimpleName());
        while (cl.getEnclosingClass() != null) {
            cl = cl.getEnclosingClass();
            list.add(cl.getSimpleName());
        }

        StringBuilder builder = new StringBuilder();
        for (int i=list.size()-1; i>=0; i--){
            builder.append(list.get(i));
            builder.append(".");
        }
        builder.delete(builder.length()-1, builder.length());
        return builder.toString();
    }

//    register
public static String register(String firstName, String lastName, String email, String password, String phone, String userType){

    User user = null;


    if (Objects.equals(userType, "Admin")) {
        user=new Admin(firstName,lastName,email,phone,password);
    } else if (Objects.equals(userType, "Seller")){
        user=new Seller(firstName,lastName,email,phone,password);
    }
    else if (Objects.equals(userType, "Buyer")){
        user=new Buyer(firstName,lastName,email,phone,password);
    }

    // Save user
    try {
        StringBuilder userInfo = new StringBuilder();
        userInfo.append(user.getFirstName()).append(",").append(user.getLastName()).append(",").append(user.getEmail()).append(",").append(user.getPhone()).append(",").append(user.getPassword()).append(",").append(user.getWallet()).append(",").append(findClass(user)).append("\n");

        FileWriter file = new FileWriter(usersDbFile,true);
        file.write(userInfo.toString());
        file.close();
        return "User successfully registered";
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Registration not successful");
    }
}

//    login
public static User login(String email,String password){
    User user=null;

    try {
        String currentLine;

        BufferedReader in = new BufferedReader(new FileReader(usersDbFile));

        while ((currentLine = in.readLine()) != null) {
            String[] details=currentLine.split(",");

            String firstName=details[0];
            String lastName=details[1];
            String emailAddress=details[2];
            String phone=details[3];
            String userPassword=details[4];
            double wallet=Double.parseDouble(details[5]);
            String type=details[6].replace("\n","");

            if (Objects.equals(email, emailAddress) && Objects.equals(password, userPassword)){
                // Create user object
                if (Objects.equals(type, "Admin")){
                    user=new Admin(firstName,lastName,email,phone,password);
                }
                else if(Objects.equals(type, "Seller")){
                    user=new Seller(firstName,lastName,email,phone,password);
                }
                else if(Objects.equals(type, "Buyer")){
                    user=new Buyer(firstName,lastName,email,phone,password);
                }

                if (user != null){
                    user.setWallet(wallet);
                }
//                    System.out.println(user);
                break;
            }

        }
    }
    catch (IOException e) {
        System.out.println("exception occurred"+ e);
    }
    return user;
}

    // Delete user
    public static void delete(String email) throws IOException {
        StringBuilder data=new StringBuilder();

        // Read products database
        List<String> databaseContent = Files.readAllLines(Paths.get(usersDbFile));
        String[] users= databaseContent.toArray(new String[0]);

        for (String line :
                users) {
            if (line.split(",")[2].equals(email)) {
                continue;
            }
            data.append(line).append("\n");
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(usersDbFile));
            out.write(String.valueOf(data));
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occurred"+ e);
        }
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users=new ArrayList<>();

        try {
            String currentLine;

            BufferedReader in = new BufferedReader(new FileReader(usersDbFile));

            while ((currentLine = in.readLine()) != null) {
                String[] details=currentLine.split(",");
                String firstName=details[0];
                String lastName=details[1];
                String email=details[2];
                String phone=details[3];
                String password=details[4];
                double wallet=Double.parseDouble(details[5]);
                String type=details[6].replace("\n","");

                User user=new User(firstName,lastName,email,phone,password);
                user.setWallet(wallet);
                if (Objects.equals(type, "Admin")){
                    users.add((Admin)user);
                }
                else if(Objects.equals(type, "Seller")){
                    users.add((Seller)user);
                }
                else if(Objects.equals(type, "buyer")){
                    users.add((Buyer) user);
                }
            }

        }
        catch (IOException e) {
            System.out.println("exception occurred"+ e);
        }
        return users;
    }

    public static void updateWalletBalance(String userEmail, double newAccountBalance) {

        StringBuilder outputString = new StringBuilder();
        try {
            List<String> allUsers = Files.readAllLines(Paths.get(usersDbFile));
            List<String> updatedUsers = allUsers.stream().map(user -> {
                if (user.contains(userEmail)) {
                    String oldAmount = user.split(",")[5];
                    String updatedUser = user.replace(oldAmount, String.valueOf(newAccountBalance));
                    return updatedUser;
                } else return user;
            }).collect(Collectors.toList());

            updatedUsers.forEach(user -> {
                outputString.append(user).append("\n");
            });

            BufferedWriter out = new BufferedWriter(new FileWriter(usersDbFile));
            out.write(outputString.toString());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
