package edu.ucalgary.oop;

import javax.swing.*;

public class MenuPages extends JFrame{
	private JTabbedPane tabbedPane;
	
	public MenuPages() {
		setTitle("Individual Assignment User Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        addPagesToTabbedPane();

        getContentPane().add(tabbedPane);
        
        setVisible(true);
	}
	
	private void addPagesToTabbedPane() {
        tabbedPane.addTab("Disaster Victims", new DisasterVictimPage());
        tabbedPane.addTab("Inquirers/Inquiries", new InquirySQLPage());
    }
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPages::new);
    }
}
