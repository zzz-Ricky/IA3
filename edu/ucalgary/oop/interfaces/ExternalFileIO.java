package edu.ucalgary.oop;

import java.util.ArrayList;

interface ExternalFileIO {

    public ArrayList<String> readGenderOptionsFromFile(String filename) {
        ArrayList<String> genderOptions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line contains a single gender option
                genderOptions.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading gender options from file: " + e.getMessage());
            // Handle the error appropriately, e.g., by using default gender options
        }
        return genderOptions;
    }
}
