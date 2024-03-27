package edu.ucalgary.oop;

public class Supply implements InfoManagement {
    private String type;
    private int quantity;

    public Supply(String type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public String getDescription() {
        return type;
    }

    public void setDescription(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
