package sg.edu.nus.cs2103.sudo.storage;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.ArrayList;

public class XMLSerializer {
    public static void write(ArrayList<ArrayList<String>> h, String name) throws FileNotFoundException{
        XMLEncoder encoder =
           new XMLEncoder(
              new BufferedOutputStream(
                new FileOutputStream(name)));
        encoder.writeObject(h);
        encoder.close();
    }

    public static ArrayList<ArrayList<String>> read(String filename) throws FileNotFoundException {
        XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        ArrayList<ArrayList<String>> h = (ArrayList<ArrayList<String>>) decoder.readObject();
        decoder.close();
        return h;
    }
}