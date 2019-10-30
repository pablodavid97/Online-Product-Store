package product.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Named("user")
@SessionScoped
public class UserManager implements Serializable {
    @Inject private ProductsBean productsBean;

    private ArrayList<Products> purchasedProducts = new ArrayList<>();
    private ProductDataModel productDataModel;
    private String username = "";
    private String password = "";

    public UserManager(){
        try {
            readProductData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProductsBean getProductsBean() {
        return productsBean;
    }

    public void setProductsBean(ProductsBean productsBean) {
        this.productsBean = productsBean;
    }

    public ArrayList<Products> getProducts() {
        return productsBean.getProductData();
    }

    public void setProducts(ArrayList<Products> products) {
        this.productsBean.setProductData(products);
    }

    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public ProductDataModel getProductDataModel() {
        return productDataModel;
    }

    public void setProductDataModel(ProductDataModel productDataModel) {
        this.productDataModel = productDataModel;
    }

    public boolean userRegistered(String username) {
        boolean contains = false;
        for(User u : productsBean.getUsers()) {
            if(username.equals(u.getUsername())){
                contains = true;
                System.out.println("user registered");
            }
        }
        return contains;
    }

    public boolean userMatchPwd(String username, String password){
        boolean match = false;

        for(User user : productsBean.getUsers()) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                match = true;
                System.out.println("Passwords match");
            }
        }
        return match;
    }

    public String loginUser(){
        System.out.println("users");
        System.out.println(productsBean.getUsers());

        System.out.println("username");
        System.out.println(username);

        System.out.println("pwd");
        System.out.println(password);

        if(userRegistered(username) && userMatchPwd(username, password) && username != "" && password != ""){
            return "productDetails";
        }
        return "errorPage";
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

    public double getTotalPrice() {
        int qnty = 0;
        for(Products p : productsBean.getProductData()){
            qnty += p.getTotalPrice();
        }
        return qnty;
    }

    public void readProductData() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Products.csv");

//        System.out.println("Input Stream");
//        System.out.println(is);

        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {
//            System.out.println("Line.....");
//            System.out.println(line);
            String[] data = line.split(",");
            productsBean.getProductData().add(new Products(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            productDataModel = new ProductDataModel(productsBean.getProductData(), productsBean.getProductData().size());
//            System.out.println("Product DAta");
//            System.out.println(productData);
        }
        setPurchasedProducts(productsBean.getProductData());
    }

    public void setProductUnits(Products p){
        for(Products products : productsBean.getProductData()){
            if(products.getSerialNum().equals(p.getSerialNum())){
                p.setPurchaseNum(p.getPurchaseNum());
                p.setTotalPrice();
            }
        }
    }

    public void viewProductData(){
        System.out.println("user");
        System.out.println(username);

        System.out.println("Products");
        for(Products p: ProductsBean.getProductData()){
            System.out.println(getTotalPrice());
        }
    }
}
