package edu.ucalgary.oop; /* package declaration */

public class Inquirer extends Person implements InfoManagement {
    private String INFO;
    private String servicesPhone;

    public Inquirer(String firstName, String lastName, String servicesPhone) {
    	super(firstName,lastName, null); // Pass null for lastName and genderPronoun
        this.servicesPhone = servicesPhone;
    }

    public void setDescription(String INFO) {
        INFO = INFO;
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
