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
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            // Ideally, this should never be called due to SupplyManager class, but just in
            // case.
            throw new IllegalArgumentException("Cannot set a supply to a negative quantity.");
        }
    }

}
