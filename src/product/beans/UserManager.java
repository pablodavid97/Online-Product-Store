package product.beans;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


@Named("user_session")
@SessionScoped
public class UserManager implements Serializable {
    private ProductsBean productsBean;
    FacesContext fCtx = FacesContext.getCurrentInstance();
    //HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
    //String sessionId = session.getId();
    private HtmlDataTable table;
    private int rowsOnPage;
    private String nameCriteria = "";
    private String priceCriteria = "all";
    private ArrayList<Products> purchasedProducts = new ArrayList<>();
    private ArrayList<Products> filteredProducts = new ArrayList<>();
    public ArrayList<Products> getFilteredProducts() {
        return filteredProducts;
    }

    public ArrayList<ArrayList<Products>> getPurchases() {
        return purchases;
    }

    private ArrayList<ArrayList<Products>> purchases = new ArrayList<>();
    private String errorString = "";

    public ArrayList<Products> getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(ArrayList<Products> lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    private ArrayList<Products> lastPurchase = new ArrayList<>();
    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }


    public UserManager(){
        productsBean = ProductsBean.getSingleton();
        //System.out.println("Id usuario");
        //System.out.println(sessionId);
        rowsOnPage = 5;

        setPurchasedProducts(productsBean.getProductCopy());
        setFilteredProducts(new ArrayList<>(purchasedProducts));

    }

    public ProductsBean getProductsBean() {
        return productsBean;
    }

    public void setProductsBean(ProductsBean productsBean) {
        this.productsBean = productsBean;
    }

    public ArrayList<Products> getProducts() {
        return purchasedProducts;
    }

    public void setProducts(ArrayList<Products> products) {
        this.productsBean.setProductData(products);
    }

    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        //this.purchasedProducts.addAll(purchasedProducts);
        this.purchasedProducts = new ArrayList<>(purchasedProducts);
    }

    public void setFilteredProducts(ArrayList<Products> newFiltered) {
        filteredProducts.clear();
        filteredProducts.addAll(newFiltered);
    }

    public double getLastPurchaseTotalPrice(){
        double total = 0;
        for(Products p: lastPurchase){
            total += p.getTotalPrice();
        }
        return (double) Math.round(total * 100d) / 100d;
    }
}
