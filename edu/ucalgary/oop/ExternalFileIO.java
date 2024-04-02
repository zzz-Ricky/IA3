package edu.ucalgary.oop;

import java.util.ArrayList;

interface ExternalFileIO {
    public ArrayList<String> readFile();

    public void writeFile();

    public void mountFile();

    public void dismountFile();

}
