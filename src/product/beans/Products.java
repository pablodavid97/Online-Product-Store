package product.beans;

public class Products {
    private  String serialNum;
    private String productName;
    private double pricePerUnit;
    private int stockNum;
    private int purchaseNum;
    private double totalPrice;

    public Products(String serialNum, String productName, double pricePerUnit, int stockNum, int purchaseNum, double totalPrice) {
        this.serialNum = serialNum;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.stockNum = stockNum;
        this.purchaseNum = purchaseNum;
        this.totalPrice = totalPrice;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getStockNum() {
        return stockNum;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
    }

    public int getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(int purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = purchaseNum * pricePerUnit;
    }

    @Override
    public String toString(){
        return productName;
    }
}
