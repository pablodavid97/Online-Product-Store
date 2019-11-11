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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

@Named("user")
@ViewScoped
public class ProductsViewBean implements Serializable {
    private ProductsBean productsBean;
    @Inject
    private UserManager userManager;
    private HtmlDataTable table;
    private int rowsOnPage;
    private String nameCriteria = "";
    private String priceCriteria = "all";
    private ArrayList<Products> purchasedProducts = new ArrayList<>();
    private ArrayList<Products> filteredProducts = new ArrayList<>();
    public ArrayList<Products> getFilteredProducts() {
        return filteredProducts;
    }
    private String errorString = "";

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public ProductsViewBean(){
        productsBean = ProductsBean.getSingleton();
        rowsOnPage = 5;

        setPurchasedProducts(productsBean.getProductCopy());
        setFilteredProducts(new ArrayList<>(purchasedProducts));

        /*
        try {
            readProductData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
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
        this.purchasedProducts = new ArrayList<>(purchasedProducts);
    }

    public double getTotalPrice() {
        double qnty = 0;
        for(Products p : purchasedProducts){
            qnty += p.getTotalPrice();
        }
        return (double) Math.round(qnty*100.0) / 100.0;
    }

    public void setProductUnits(Products p){
        Iterator<Products> iter = purchasedProducts.iterator();
        while (iter.hasNext()){
            Products products = iter.next();

            if(products.getSerialNum().equals(p.getSerialNum()) && p.getPurchaseNum() >= 0){
                products.setPurchaseNum(p.getPurchaseNum());
                products.setTotalPrice();
            }
        }

        setErrorString(getAllErrors());
    }

    public void viewProductData(){

        System.out.println("Products");
        for(Products p: purchasedProducts){
            System.out.println(getTotalPrice());
        }
    }

    public String doPurchase(){
        ArrayList<Products> invalidProducts = productsBean.validatePurchase(getPurchasedProducts());
        String action;
        userManager.getLastPurchase().clear();
        boolean atLeast1Item = false;
        if (invalidProducts.size() == 0) {
            for(Products p: purchasedProducts) {
                if (p.getPurchaseNum() > 0){
                    userManager.getLastPurchase().add(p);
                    atLeast1Item = true;
                }
            }
            if (atLeast1Item){
                action = "purchasedItems";
                userManager.getPurchases().add(userManager.getLastPurchase());
            }
            else {
                setErrorString("You need to add at least 1 item in order to make a purchase.");
                action = null;
            }
        }

        else {
            action = null;
            StringBuilder err_sb = new StringBuilder();
            err_sb.append("Unable to purchase - not enough stock to fulfill your request for the following: ");
            for(Products p: invalidProducts) {
                if (p.getStockNum() == 0) {
                    for (int i = 0; i < purchasedProducts.size(); i++) {
                        Products cur = purchasedProducts.get(i);
                        if (p.getSerialNum().equals(cur.getSerialNum())) {
                            purchasedProducts.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < filteredProducts.size(); i++) {
                        Products cur = filteredProducts.get(i);
                        if (p.getSerialNum().equals(cur.getSerialNum())) {
                            filteredProducts.remove(i);
                            break;
                        }
                    }
                }
                err_sb.append(p.getProductName()).append(", ");
            }
            err_sb.append(". All of those amounts set to 0.");
            setErrorString(err_sb.toString());
        }

        return action;
    }


    public String getPriceCriteria() {
        return priceCriteria;
    }

    public void setPriceCriteria(String criteria) {
        this.priceCriteria = criteria;
    }

    public String getNameCriteria() {
        return nameCriteria;
    }

    public void setNameCriteria(String criteria) {
        this.nameCriteria = criteria;
    }


    public HtmlDataTable getTable() {
        return table;
    }

    public void setTable(HtmlDataTable table) {
        this.table = table;
    }

    public void goToFirstPage() {
        table.setFirst(0);
    }

    public void goToPreviousPage() {
        table.setFirst(table.getFirst() - table.getRows());
    }

    public void goToNextPage() {
        table.setFirst(table.getFirst() + table.getRows());

    }

    public void goToLastPage() {
        int totalRows = table.getRowCount();
        int displayRows = table.getRows();
        int full = totalRows / displayRows;
        int modulo = totalRows % displayRows;

        if (modulo > 0) {
            table.setFirst(full * displayRows);
        } else {
            table.setFirst((full - 1) * displayRows);
        }
    }

    public int getRowsOnPage() {
        return rowsOnPage;
    }

    public void setRowsOnPage(int rowsOnPage) {
        this.rowsOnPage = rowsOnPage;
    }

    public void refreshTable() {
        priceCriteria = "all";
        nameCriteria = "";
        addTableFilter();
    }

    public void addPriceFilter(){
        filteredProducts.clear();
        nameCriteria = "";
        if (priceCriteria.equals(">=10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() >= 10.0) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        if (priceCriteria.equals("<10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() < 10.0) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        else
            filteredProducts.addAll(getPurchasedProducts());
    }

    public void addNameFilter(){
        filteredProducts.clear();
        priceCriteria = "all";
        if(!nameCriteria.equals("")){
            String regex = nameCriteria.replace("*", ".*");
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getProductName().matches(regex)) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }
    }

    public void addTableFilter(){
        filteredProducts.clear();

        if(priceCriteria.equals("all") && nameCriteria.equals("")){
            setFilteredProducts(getPurchasedProducts());
            Collections.sort(filteredProducts, new Comparator<Products>() {
                @Override
                public int compare(Products key_1, Products key_2) {
                    return (Integer.parseInt(key_1.getSerialNum()) - Integer.parseInt(key_2.getSerialNum()));
                }
            });
            return;
        }

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                return (int) (key_1.getPricePerUnit() - key_2.getPricePerUnit());
            }
        });

        if (priceCriteria.equals(">=10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() >= 10.0) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        if (priceCriteria.equals("<10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() < 10) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        if(!nameCriteria.equals("")){
            String[] temp;
            String filter = "";
            if(nameCriteria.contains("%")){
                temp = nameCriteria.split("%");
                filter = temp[0];
            } else {
                filter = nameCriteria;
            }

            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getProductName().contains(filter)) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        else {
            setFilteredProducts(getPurchasedProducts());
        }
        table.setFirst(0);
    }

    public void setFilteredProducts(ArrayList<Products> newFiltered) {
        filteredProducts.clear();
        filteredProducts.addAll(newFiltered);
    }

    /*
    public void addTableFilter(){
        productsBean.clearAll();
        purchasedProducts.addAll(filteredProducts);
        filteredProducts.clear();

        if(priceCriteria.equals("all") && nameCriteria.equals("")){
            Collections.sort(purchasedProducts, new Comparator<Products>() {
                @Override
                public int compare(Products key_1, Products key_2) {
                    return (Integer.parseInt(key_1.getSerialNum()) - Integer.parseInt(key_2.getSerialNum()));
                }
            });
            return;
        }

        Collections.sort(purchasedProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                return (int) (key_1.getPricePerUnit() - key_2.getPricePerUnit());
            }
        });

        if (priceCriteria.equals(">=10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() < 10) {
                    filteredProducts.add(purchasedProducts.remove(i));
                    i = 0;
                }
            }
            productsBean.setFilteredProducts(purchasedProducts);
        }

        if (priceCriteria.equals("<10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() >= 10) {
                    filteredProducts.add(purchasedProducts.remove(i));
                    i = 0;
                }
            }
            productsBean.setFilteredProducts(purchasedProducts);
        }

        if(!nameCriteria.equals("")){
            String[] temp;
            String filter = "";
            if(nameCriteria.contains("%")){
                temp = nameCriteria.split("%");
                filter = temp[0];
            } else {
                filter = nameCriteria;
            }

            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (!products.getProductName().contains(filter)) {
                    filteredProducts.add(purchasedProducts.remove(i));
                    i = 0;
                }
            }
        }

        table.setFirst(0);

    }
    */

    private String serialSortType = "asc";
    private String nameSortType = "asc";
    private String unitPriceSortType = "asc";
    private String stockSortType = "asc";
    private String amountSortType = "asc";
    private String totalPriceSortType = "asc";

    public String sortProductsBySerialNum() {

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                if(serialSortType.equals("asc")){
                    return key_1.getSerialNum().compareTo(key_2.getSerialNum());
                } else {
                    return (-1) * key_1.getSerialNum().compareTo(key_2.getSerialNum());
                }
            }
        });
        serialSortType=(serialSortType.equals("asc")) ? "dsc" : "asc";
        return null;
    }

    public String sortProductsByName() {

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                if(nameSortType.equals("asc")){
                    return key_1.getProductName().compareTo(key_2.getProductName());
                } else {
                    return (-1) * key_1.getProductName().compareTo(key_2.getProductName());
                }
            }
        });
        nameSortType=(nameSortType.equals("asc")) ? "dsc" : "asc";
        return null;
    }

    public String sortProductsByUnitPrice() {

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                if(unitPriceSortType.equals("asc")){
                    return key_1.getPricePerUnit() > key_2.getPricePerUnit() ? 1 : -1; //"==" es irrelevante porque es sorting
                } else {
                    return (-1) * (key_1.getPricePerUnit() > key_2.getPricePerUnit() ? 1 : -1);
                }
            }
        });
        unitPriceSortType=(unitPriceSortType.equals("asc")) ? "dsc" : "asc";
        return null;
    }

    public String sortProductsByStockNum() {

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                if(stockSortType.equals("asc")){
                    return key_1.getStockNum() - key_2.getStockNum();
                } else {
                    return (-1) * (key_1.getStockNum() - key_2.getStockNum());
                }
            }
        });
        stockSortType=(stockSortType.equals("asc")) ? "dsc" : "asc";
        return null;
    }

    public String sortProductsByAmountToPurchase() {

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                if(amountSortType.equals("asc")){
                    return key_1.getPurchaseNum() - key_2.getPurchaseNum();
                } else {
                    return (-1) * (key_1.getPurchaseNum() - key_2.getPurchaseNum());
                }
            }
        });
        amountSortType=(amountSortType.equals("asc")) ? "dsc" : "asc";
        return null;
    }

    public String sortProductsByTotalPrice() {

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                if(totalPriceSortType.equals("asc")){
                    return key_1.getTotalPrice() > key_2.getTotalPrice() ? 1 : -1; //"==" es irrelevante porque es sorting
                } else {
                    return (-1) * (key_1.getTotalPrice() > key_2.getTotalPrice() ? 1 : -1);
                }
            }
        });
        totalPriceSortType=(totalPriceSortType.equals("asc")) ? "dsc" : "asc";
        return null;
    }

    public String getAllErrors(){
        StringBuilder errStrBuilder = new StringBuilder();
        for(Products p: purchasedProducts){
            errStrBuilder.append(p.getErrorStr());
            if(!p.getErrorStr().equals(""))
                errStrBuilder.append("\n");
        }
        return errStrBuilder.toString();
    }
}
