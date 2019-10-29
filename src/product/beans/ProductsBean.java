package product.beans;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ProductsBean implements Serializable {

    private ProductDataModel productDataModel;
    ArrayList<Products> productData = new ArrayList<>();
    ArrayList<Products> purchasedProducts = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    private double totalPrice = 0;
    private String username = "";
    private String password = "";

    public ProductsBean(){
        users.add(new User("pablo", "123"));
        users.add(new User("diego", "123"));
        System.out.println("users");
        System.out.println(users);
        try {
            readProductData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean userRegistered(String username) {
        boolean contains = false;
        for(User u : users) {
            if(username.equals(u.getUsername())){
                contains = true;
            }
        }
        System.out.println("user registered");
        return contains;
    }

    public boolean userMatchPwd(String username, String password){
        boolean match = false;

        for(User user : users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                match = true;
            }
        }

        return match;
    }

    public String loginUser(){
        if(userRegistered(username) && userMatchPwd(username, password) && username != "" && password != ""){
            return "productDetails";
        }
        return "errorPage";
    }

    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProductDataModel getProductDataModel() {
        return productDataModel;
    }

    public void setProductDataModel(ProductDataModel productDataModel) {
        this.productDataModel = productDataModel;
    }

    public ArrayList<Products> getProductData() {
        return productData;
    }

    public void setProductData(ArrayList<Products> productData) {
        this.productData = productData;
    }

    public double getTotalPrice() {
        int qnty = 0;
        for(Products p : productData){
            System.out.println("Pridce");
            System.out.println(p.getTotalPrice());
            qnty += p.getTotalPrice();
        }

        return qnty;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void readProductData() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Products.csv");

        System.out.println("Input Stream");
        System.out.println(is);

        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {
//            System.out.println("Line.....");
//            System.out.println(line);
            String[] data = line.split(",");
            productData.add(new Products(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            productDataModel = new ProductDataModel(productData, productData.size());
//            System.out.println("Product DAta");
//            System.out.println(productData);
        }
    }

    public void setProductUnits(Products p){
        for(Products products : productData){
            if(products.getSerialNum().equals(p.getSerialNum())){
                products.setPurchaseNum(p.getPurchaseNum());
                products.setTotalPrice();
            }
        }
    }

    public void saveProductData(){
        for(Products p: productData){
            System.out.println(p.getTotalPrice());
        }
    }
}
