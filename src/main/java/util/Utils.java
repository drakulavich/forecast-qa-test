package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

    public static byte[] readResourceFileToBytes(String filename) {
        byte[] fileBytes = new byte[0];
        try {
            Path path = Paths.get(Utils.class.getClassLoader().getResource(filename).toURI());
            fileBytes = Files.readAllBytes(path);
        } catch (URISyntaxException|IOException|NullPointerException e) {
            e.printStackTrace();
        }

        return fileBytes;
    }
}
