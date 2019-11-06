package product.beans;

import java.util.ArrayList;

public class User {
    String id = "";
    ArrayList<Products> purchasedProducts = new ArrayList<>();

    public User(String id, ArrayList<Products> purchasedProducts) {
        this.id = id;
        this.purchasedProducts = purchasedProducts;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }


    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    @Override
    public boolean equals(Object o){
        User user = (User) o;
        return this.id == user.getId();
    }

    @Override
    public String toString(){
        return id;
    }
}