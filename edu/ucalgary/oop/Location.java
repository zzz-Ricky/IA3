package edu.ucalgary.oop;

import java.util.HashSet;

public class Location {
    private String name;
    private String address;
    private HashSet<DisasterVictim> occupants;
    private HashSet<Supply> supplies;

    public Location(String name, String address) {
        this.name = name;
        this.address = address;
        this.occupants = new HashSet<>();
        this.supplies = new HashSet<>();
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

    public HashSet<DisasterVictim> getOccupants() {
        return occupants;
    }

    public void setOccupants(HashSet<DisasterVictim> occupants) {
        this.occupants = occupants;
    }

    public HashSet<Supply> getSupplies() {
        return supplies;
    }

    public void setSupplies(HashSet<Supply> supplies) {
        this.supplies = supplies;
    }

    public void addOccupant(DisasterVictim occupant) {
        this.occupants.add(occupant);
    }

    public void removeOccupant(DisasterVictim occupant) {
        this.occupants.remove(occupant);
    }

    public void addSupply(Supply supply) {
        // does not use SupplyManager class, as this is implicitly used for adding new
        // supplies
        this.supplies.add(supply);
    }

    public void removeSupply(Supply supply) {
        // used for removing a supply without transferring it to a victim, such as
        // when de-listing it.
        this.supplies.remove(supply);
    }
}
