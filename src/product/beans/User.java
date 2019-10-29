package product.beans;

import java.util.ArrayList;

public class User {
    String username = "";
    String password = "";
    ArrayList<Products> purchasedProducts = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }
}