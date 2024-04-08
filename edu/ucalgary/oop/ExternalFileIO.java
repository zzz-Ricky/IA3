/**
 * The ExternalFileIO interface defines methods for interfacing with external files.
 * Implementing classes must provide functionality to read, write, mount, or dismount files.
 * <p>
 * This interface is part of the edu.ucalgary.oop package.
 * </p>
 *
 * @author Ricky Huynh
 * @version 1.0
 * @since 07/04/24
 */

package edu.ucalgary.oop;

import java.util.ArrayList;

public interface ExternalFileIO {

    /**
     * Reads the contents of an external file.
     *
     * @return An ArrayList containing the lines read from the file.
     */
    public ArrayList<String> readFile();

    /**
     * Writes data to an external file.
     */
    public void writeFile();

    /**
     * Mounts the external file system.
     */
    public void mountFile();

    /**
     * Dismounts the external file system.
     */
    public void dismountFile();

}
