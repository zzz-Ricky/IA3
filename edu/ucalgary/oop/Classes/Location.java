package edu.ucalgary.oop;

import java.util.List;
import java.util.ArrayList;  

public class Location {
    private String name;
    private String address;
    private List<DisasterVictim> occupants;
    private ArrayList<Supply> supplies;

    public Location(String name, String address) { 
        this.name = name;
        this.address = address;
        this.occupants = new ArrayList<>();
        this.supplies = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public List<DisasterVictim> getOccupants() {
        return occupants;
    }
    public void setOccupants(List<DisasterVictim> occupants) {
        this.occupants = occupants;
    }
    public ArrayList<Supply> getSupplies() {
        return supplies;
    }
    public void setSupplies(ArrayList<Supply> supplies) {
        this.supplies = supplies;
    }
    public void addOccupant(DisasterVictim occupant) {
        this.occupants.add(occupant);
    }
    public void removeOccupant(DisasterVictim occupant) {
        this.occupants.remove(occupant);
    }
    public void addSupply(Supply supply) {
        this.supplies.add(supply);
    }
    public void removeSupply(Supply supply) {
        this.supplies.remove(supply);
    }
}
