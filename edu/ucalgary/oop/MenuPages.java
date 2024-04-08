/**
 * The MenuPages class represents the base GUI window of the application.
 * <p>
 * This class is part of the edu.ucalgary.oop package.
 * </p>
 * <p>
 * The MenuPages class stores central information relating to the management of
 * DisasterVictim data, and relief center locations. It serves as the central
 * Class of the file.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class MenuPages extends JFrame {
    private JTabbedPane tabbedPane;
    private ArrayList<Location> locations;
    private ArrayList<DisasterVictim> victims;
    private Location workLocation;
    private FamilyRelationManager familyManager;
    private SupplyManager supplyManager;
    
    /**
     * Constructs a MenuPages object and initializes necessary components.
     */
    public MenuPages() {
        this.locations = new ArrayList<Location>();
        this.victims = new ArrayList<DisasterVictim>();

        initiateSetup();
        promptUserType();

        setTitle("Individual Assignment User Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        addPagesToTabbedPane();

        getContentPane().add(tabbedPane);

        setVisible(true);
    }
    
    /**
     * Adds pages to the tabbed pane based on the current work location.
     */
    private void addPagesToTabbedPane() {
    	if (workLocation != null) {
    		add(new JLabel(workLocation.getName() + " ,  " + workLocation.getAddress()), BorderLayout.NORTH);
    	}
    	else {
            add(new JLabel("Central Disaster Management Center"), BorderLayout.NORTH);
    	}
        tabbedPane.addTab("Disaster Victims", new DisasterVictimPage(locations, victims, workLocation,familyManager,supplyManager));
        tabbedPane.addTab("Inquirers/Inquiries", new InquirySQLPage());
        if (workLocation != null){
        	tabbedPane.addTab("Location", new LocationManagementPage(workLocation));
        }
        
    }
    
    /**
     * Prompts the user to select their work location or enter as a central worker.
     */
    private void promptUserType() {
        JComboBox<Object> locationComboBox = new JComboBox<>();
        locationComboBox.addItem("Enter as Central worker"); // Add "Enter as Central worker" option
        for (Location location : locations) {
            locationComboBox.addItem(location);
        }

        // Custom renderer to display both Location objects and the "Enter as Central worker" option
        locationComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Location) {
                    Location location = (Location) value;
                    value = location.getName() + ", " + location.getAddress();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        JLabel descriptionLabel = new JLabel("Please select your location:");
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BorderLayout());
        comboPanel.add(descriptionLabel, BorderLayout.NORTH);
        comboPanel.add(locationComboBox, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null, comboPanel, "Select Location",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Object selectedItem = locationComboBox.getSelectedItem();
            if (selectedItem instanceof Location) {
                workLocation = (Location) selectedItem; // Set workLocation to the selected location object
            } else {
                workLocation = null; // Set workLocation to null - "Enter as Central worker"
            }
        } else {
            System.exit(0);
        }
    }
    
    /**
     * Initializes the setup by creating manager objects and adding sample data.
     */
    private void initiateSetup() {
        // Initiate manager objects
        this.familyManager = new FamilyRelationManager();
        this.supplyManager = new SupplyManager();

        // Create Sample data to populate the initial page

        Location location1 = new Location("Shelter A", "1234 Shelter Ave");
        Location location2 = new Location("Shelter B", "678 Park Street");
        Location location3 = new Location("Shelter C", "9876 Oak Drive");
        locations.add(location1);
        locations.add(location2);
        locations.add(location3);

        Supply waterBottles = new Supply("Water Bottles", 99);
        Supply bandages = new Supply("Bandages", 50);
        Supply toiletPaper = new Supply("Toilet Paper", 4);
        Supply medicalKits = new Supply("Medical Kits", 25);
        Supply batteries = new Supply("Batteries", 100);
        Supply blankets = new Supply("Blankets", 30);
        Supply cannedFood = new Supply("Canned Food", 75);
        Supply flashlights = new Supply("Flashlights", 20);

        location1.addSupply(waterBottles);
        location1.addSupply(toiletPaper);
        location1.addSupply(cannedFood);
        location2.addSupply(bandages);
        location2.addSupply(blankets);
        location3.addSupply(flashlights);
        location3.addSupply(medicalKits);
        location3.addSupply(batteries);

        DisasterVictim samplevictim1 = new DisasterVictim("Alex", "2024-01-18");
        samplevictim1.setDateOfBirth("1987-05-21");
        samplevictim1.setLastName("Smith");
        samplevictim1.setDescription("Wearing a blue jacket");
        samplevictim1.setGender(getRandomGender(samplevictim1));
        location1.addOccupant(samplevictim1);

        DisasterVictim samplevictim2 = new DisasterVictim("Jordan", "2024-02-14");
        samplevictim2.setDateOfBirth("1967-03-05");
        samplevictim2.setLastName("Waller");
        samplevictim2.setDescription("Appears anxious and restless");
        samplevictim2.setGender(getRandomGender(samplevictim2));
        location2.addOccupant(samplevictim2);

        DisasterVictim samplevictim3 = new DisasterVictim("Taylor", "2024-05-12");
        samplevictim3.setDateOfBirth("1982-09-28");
        samplevictim3.setLastName("Garcia");
        samplevictim3.setDescription("Looks tired and disheveled");
        samplevictim3.setGender(getRandomGender(samplevictim3));
        location2.addOccupant(samplevictim3);

        DisasterVictim samplevictim4 = new DisasterVictim("Casey", "2024-03-10");
        samplevictim4.setDateOfBirth("1975-08-15");
        samplevictim4.setLastName("Johnson");
        samplevictim4.setDescription("Wearing glasses and a baseball cap");
        samplevictim4.setGender(getRandomGender(samplevictim4));
        location3.addOccupant(samplevictim4);

        DisasterVictim samplevictim5 = new DisasterVictim("Riley", "2024-04-05");
        samplevictim5.setDateOfBirth("1990-11-30");
        samplevictim5.setLastName("Johnson");
        samplevictim5.setDescription("Appears calm but slightly shaken");
        samplevictim5.setGender(getRandomGender(samplevictim5));
        location1.addOccupant(samplevictim5);

        FamilyRelation relationship1 = new FamilyRelation(samplevictim1, "Parent", samplevictim2, familyManager);
        samplevictim1.addFamilyConnection(relationship1, familyManager);

        FamilyRelation relationship2 = new FamilyRelation(samplevictim1, "Parent", samplevictim3, familyManager);
        samplevictim3.addFamilyConnection(relationship2, familyManager);

        FamilyRelation relationship3 = new FamilyRelation(samplevictim4, "Sibling", samplevictim5, familyManager);
        samplevictim5.addFamilyConnection(relationship3, familyManager);

        victims.add(samplevictim1);
        victims.add(samplevictim2);
        victims.add(samplevictim3);
        victims.add(samplevictim4);
        victims.add(samplevictim5);
    }
    
    /**
     * Generates a random gender for a disaster victim.
     *
     * @param victim The disaster victim for which gender is generated.
     * @return A randomly selected gender from the available options.
     */
    private String getRandomGender(DisasterVictim victim) {
        ArrayList<String> genderOptions = victim.getGenderOptions();
        Random rand = new Random();
        return genderOptions.get(rand.nextInt(genderOptions.size()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPages::new);
    }
}
