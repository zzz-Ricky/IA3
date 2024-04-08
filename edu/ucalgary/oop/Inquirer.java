/**
 * The Inquirer class represents a person who creates inquiries for disaster victims.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The Inquirer class stores information relating to identifying the inquirer.
 * This includes their personal description, and phone number. Personal description
 * Is used to include additional info such as their email, desire to volunteer, etc.
 * </p>
 * <p>
 * The class provides methods to set and get information relating to this class.
 * Since the inquirer extends the person class, it inherits blank data IO methods.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

public class Inquirer extends Person implements InfoManagement {
    private String INFO;
    private String servicesPhone;

    /**
     * Constructs a new Inquirer object with the specified first name, last name,
     * and services phone number.
     *
     * @param firstName     The first name of the inquirer.
     * @param lastName      The last name of the inquirer (set to null).
     * @param servicesPhone The phone number for services.
     */
    public Inquirer(String firstName, String lastName, String servicesPhone) {
        super(firstName, lastName, null); // Pass null for lastName and genderPronoun
        this.servicesPhone = servicesPhone;
    }

    public void setDescription(String INFO) {
        this.INFO = INFO;
    }

    public String getDescription() {
        return INFO;
    }

    public String getServicesPhone() {
        return servicesPhone;
    }

    public void setServicesPhone(String servicesPhone) {
        this.servicesPhone = servicesPhone;
    }

    @Override
    public void writeFile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void mountFile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dismountFile() {
        // TODO Auto-generated method stub

    }
}
