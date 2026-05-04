package smilecare;

import java.io.FileWriter;
import java.io.IOException;

public class FileSave {

    public static void saveToFile(String data) {
        // Saves invoice to user's home directory to avoid path issues
        String path = System.getProperty("user.home") + "/smilecare_invoice.txt";

        try (FileWriter fw = new FileWriter(path)) {
            fw.write(data);
            System.out.println("Invoice Saved at: " + path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
