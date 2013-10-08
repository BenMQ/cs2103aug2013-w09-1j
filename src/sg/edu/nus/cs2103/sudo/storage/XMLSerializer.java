package sg.edu.nus.cs2103.sudo.storage;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

public class XMLSerializer {
    public static void write(History h) throws Exception{
        XMLEncoder encoder =
           new XMLEncoder(
              new BufferedOutputStream(
                new FileOutputStream(h.getName())));
        encoder.writeObject(h);
        encoder.close();
    }

    public static History read(String filename) throws Exception {
        XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        History h = (History)decoder.readObject();
        decoder.close();
        return h;
    }
}