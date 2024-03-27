package edu.ucalgary.oop; /* package declaration */

public class Inquirer extends Person implements InfoManagement {
    private String INFO;
    private String SERVICES_PHONE;

    public Inquirer(String FIRST_NAME, String LAST_NAME, String SERVICES_PHONE) {
        super.FIRST_NAME = FIRST_NAME;
        super.LAST_NAME = LAST_NAME;
        this.INFO = INFO;
        this.SERVICES_PHONE = SERVICES_PHONE;
    }

    public void setDescription(String iNFO) {
        INFO = iNFO;
    }

    public String getDescription() {
        return INFO;
    }

    public String getServicesPhoneNum() {
        return SERVICES_PHONE;
    }

    public void setSERVICES_PHONE(String sERVICES_PHONE) {
        SERVICES_PHONE = sERVICES_PHONE;
    }
}
