package edu.ucalgary.oop;

public class DietaryRestrictions {
    public enum DietaryRestriction {
        AVML,
        DBML,
        GFML,
        KSML,
        LSML,
        MOML,
        PFML,
        VGML,
        VJML;
    }

    private DietaryRestriction restriction;

    public DietaryRestrictions(DietaryRestriction restriction) {
        this.restriction = restriction;
    }

    public DietaryRestriction getRestriction() {
        return restriction;
    }
}
