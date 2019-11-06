package product.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

@Named("user")
@SessionScoped
public class UserManager implements Serializable {
    @Inject private ProductsBean productsBean;

    private ArrayList<Products> purchasedProducts = new ArrayList<>();
    private ProductDataModel productDataModel;
    FacesContext fCtx = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
    String sessionId = session.getId();


    public UserManager(){
        System.out.println("Id usuario");
        System.out.println(sessionId);

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

    public ProductDataModel getProductDataModel() {
        return productDataModel;
    }

    public void setProductDataModel(ProductDataModel productDataModel) {
        this.productDataModel = productDataModel;
    }


    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        this.purchasedProducts = new ArrayList<>(purchasedProducts);
    }

    public double getTotalPrice() {
        int qnty = 0;
        for(Products p : purchasedProducts){
            qnty += p.getTotalPrice();
        }
        return qnty;
    }

    public void readProductData() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Products.csv");

        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {

            String[] data = line.split(",");
            productsBean.getProductData().add(new Products(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            productDataModel = new ProductDataModel(productsBean.getProductData(), productsBean.getProductData().size());
        }
        setPurchasedProducts(productsBean.getProductData());
    }

    public void setProductUnits(Products p){
        Iterator<Products> iter = purchasedProducts.iterator();


        while (iter.hasNext()){
            Products products = iter.next();

            if(products.getSerialNum().equals(p.getSerialNum())){
                products.setPurchaseNum(p.getPurchaseNum());
                products.setTotalPrice();
            }
        }

//        User user = new User(sessionId, purchasedProducts);
//        if(!productsBean.getUsers().contains(user)){
//            productsBean.getUsers().add(user);
//            System.out.println("Users");
//            System.out.println(productsBean.getUsers());
//        }
    }

    public void viewProductData(){
//        System.out.println("user");
//        System.out.println(username);

        System.out.println("Products");
        for(Products p: ProductsBean.getProductData()){
            System.out.println(getTotalPrice());
        }
    }

    public void validatePurchae(FacesContext arg0, UIComponent arg1, Object arg2)
            throws ValidatorException

    {
        if ((int)arg2 > -1) {
            //throw new ValidatorException(new FacesMessage("Al menos 5 caracteres "));
        }else {
            //FacesMessage error = new FacesMessage("Solo valores positivos");
            //FacesContext.getCurrentInstance().addMessage(null, error);
            throw new ValidatorException(new FacesMessage("Solo Valores Positivos "));
        }
    }

    public void localeChanged(ValueChangeEvent e) {

        System.out.println("HOLA "+e.getNewValue().toString());
        int temp= Integer.parseInt(e.getNewValue().toString());

        if (temp>-1) {
            //throw new ValidatorException(new FacesMessage("Al menos 5 caracteres "));
        }else {

            FacesMessage error = new FacesMessage("Error: '"+e.getNewValue().toString()+"' Solo valores positivos");
            error.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, error);

        }

    }

}
