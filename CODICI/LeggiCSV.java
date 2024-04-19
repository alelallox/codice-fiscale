import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class LeggiCSV {
    public static void main(String[] args) {
        String filePath = new File("C:\\Users\\aless\\Desktop\\CODICI\\codici.csv").getAbsolutePath();
        String comune = "ZUNGRI";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",,");
                String comuneRiga = columns[0];
                String codice = columns[1];

                if (comuneRiga.equals(comune)) {
                    System.out.println("COMUNE: " + comuneRiga + ", CODICE: " + codice);
                    break; // Se vuoi fermarti dopo aver trovato il comune
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

