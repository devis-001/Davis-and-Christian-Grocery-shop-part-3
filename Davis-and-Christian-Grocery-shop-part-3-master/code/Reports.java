import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author christopherochiengotieno@gmail.com
 * @version 1.0.0
 * @since Saturday, 07/05/2022 , 07:28:18
 */

public class Reports extends JPanel {

    private final JLabel title;
    private final JButton backToDashboard;

    private final JLabel checkoutReport;
    public static JList<String> checkouts;
    private final JLabel productReports;
    public static JList<String> products;

    private final JLabel deletedUsersReports;
    public static JList<String> deletedUsers;

    private static final JFrame currentFrame = new JFrame("Reports");

    public Reports(Admin admin) {

        String[] checkoutDetails = null;
        String[] productsDetails = null;
        String[] deletedUsersDetails = null;

        // checkout records
        checkoutDetails = generateCheckOutDetailsForReport();

        // products in stock
        productsDetails = generateProductDetailsForReport();

        // deleted users
        deletedUsersDetails = getDeletedUsersDetails();


        checkouts = new JList<>(checkoutDetails);
        products = new JList<>(productsDetails);
        deletedUsers = new JList<>(deletedUsersDetails);
        backToDashboard = new JButton("Dashboard");
        backToDashboard.setFocusable(false);
        backToDashboard.addActionListener((e) -> {
            try {
                AdminDashboard.open(admin, currentFrame);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        title = new JLabel("Reports:");
        checkoutReport = new JLabel("Checkouts:");
        productReports = new JLabel("Products In Stock:");
        deletedUsersReports = new JLabel("Deleted users:");


        setLayout(null);
        add(backToDashboard);
        add(title);
        add(checkouts);
        add(products);
        add(checkoutReport);
        add(productReports);
        add(deletedUsersReports);
        add(deletedUsers);


        backToDashboard.setBounds(20,60, 100, 25);
        title.setBounds(20,20,100, 25);
        checkoutReport.setBounds(45,180,400,25);
        checkouts.setBounds(45, 220, 800, 250);
        productReports.setBounds(850,180,400,25);
        products.setBounds(850, 220, 800, 250);
        deletedUsersReports.setBounds(45,500,400,25);
        deletedUsers.setBounds(45, 550, 800, 250);
    }

    public static String[] getDeletedUsersDetails() {
        String[] deletedUsersDetails;
        try {
            List<String> readAllLines = Files.readAllLines(Paths.get("deleted-users.txt"));
            deletedUsersDetails = new String[readAllLines.size()];
            for (int i = 0; i < deletedUsersDetails.length; i++) {
                deletedUsersDetails[i] = readAllLines.get(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return deletedUsersDetails;
    }

    public static String[] generateProductDetailsForReport() {
        String[] productsDetails;
        try {
            List<String> readAllLines = Files.readAllLines(Paths.get("productsDB.txt"));
            productsDetails = new String[readAllLines.size()];
            for (int i = 0; i < productsDetails.length; i++) {
                productsDetails[i] = readAllLines.get(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productsDetails;
    }

    public static String[] generateCheckOutDetailsForReport() {
        String[] checkoutDetails;
        try {
            List<String> readAllLines = Files.readAllLines(Paths.get("checkout-records.txt"));
            checkoutDetails = new String[readAllLines.size()];
            for (int i = 0; i < checkoutDetails.length; i++) {
                checkoutDetails[i] = readAllLines.get(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return checkoutDetails;
    }

    public static void open(Admin admin, JFrame prevFrame){
        prevFrame.dispose();

        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.getContentPane().add(new Reports(admin));
        currentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        currentFrame.setVisible(true);
    }
}
