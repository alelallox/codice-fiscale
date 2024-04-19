import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class CalcoloCodiceFiscale {

    public static void main(String[] args) {
        String nome = "Alessandro";
        String cognome = "Donati";
        String sesso = "m";
        String luogoNascita = "rho";
        String provincia = "mi";
        int[] dataNascita = {30, 04, 2006};
        String risultato = calcolaCodiceFiscale(nome, cognome, sesso, luogoNascita, provincia, dataNascita);
        System.out.println("Il codice fiscale è: " + risultato);
    }

    public static String calcolaCodiceFiscale(String nome, String cognome, String sesso, String luogoNascita, String provincia, int[] dataNascita) {
        String codiceAnagrafico = calcolaCodiceAnagrafico(nome, cognome, dataNascita, sesso);
        luogoNascita = luogoNascita.toUpperCase();
        String codiceLuogoNascita = calcolaCodiceLuogoNascita(luogoNascita);
        String codiceFiscale = codiceAnagrafico + codiceLuogoNascita;
        char carattereControllo = calcolaCarattereControllo(codiceFiscale);
        codiceFiscale += carattereControllo;
        return codiceFiscale;
    }


    //questo metodo calcola i primi 15 caratteri del codice fiscale (cognome, nome, anno mese e giorno di nascita)
    public static String calcolaCodiceAnagrafico(String nome, String cognome, int[] dataNascita, String sesso) {
        String cognomeCodice = calcolaCodiceCognome(cognome);
        String nomeCodice = calcolaCodiceNome(nome);
        //prendo le ultime 2 cifre dell'anno di nascita
        String annoNascita = String.format("%02d", dataNascita[2] % 100);
        //ogni mese a una lettera associata
        String[] mesiCodice = {"A", "B", "C", "D", "E", "H", "L", "M", "P", "R", "S", "T"};
        String meseNascita = mesiCodice[dataNascita[1] - 1];
        int giornoNascita = dataNascita[0];
        //per le donne si aggiunge 40 al giorno della nascita
        if (sesso.equalsIgnoreCase("F")) {
            giornoNascita += 40;
        }
        String giornoNascitaCodice = String.format("%02d", giornoNascita);
        return cognomeCodice + nomeCodice + annoNascita + meseNascita + giornoNascitaCodice;
    }

    //metodo che calcola le 3 lettere del cognome prendendo le prime 3 consonanti e altre eccezioni
    public static String calcolaCodiceCognome(String Cognome) {
        Cognome = Cognome.toUpperCase();
        String consonanti = "";
        String vocali = "";
    
        //creo della stringhe con le vocali e leconsonanti del nome
        for (char carattere : Cognome.toCharArray()) {
            if (Character.isLetter(carattere)) {
                if ("AEIOU".indexOf(carattere) != -1) {
                    vocali += carattere;
                } else {
                    consonanti += carattere;
                }
            }
        }

        //System.out.println(consonanti);
    
        String codice = "";
        if (consonanti.length() >= 3) {
            codice = "" + consonanti.charAt(0) + consonanti.charAt(1) + consonanti.charAt(2);
        }else{
            int numVocaliToAdd = 3 - codice.length();
            for (char vocale : vocali.toCharArray()) {
                if (numVocaliToAdd > 0) {
                    codice += vocale;
                    numVocaliToAdd--;
                } else {
                    break;
                }
            }
        }
        //System.out.println(codice);
        return codice;
    }

    //metodo che calcola le 3 lettere del cognome prendendo la prima, la terza e la quarta consonante e altre eccezioni
    public static String calcolaCodiceNome(String nome) {
        nome = nome.toUpperCase();
        String consonanti = "";
        String vocali = "";
    
        for (char carattere : nome.toCharArray()) {
            if (Character.isLetter(carattere)) {
                if ("AEIOU".indexOf(carattere) != -1) {
                    vocali += carattere;
                } else {
                    consonanti += carattere;
                }
            }
        }

        //System.out.println(consonanti);
    
        String codice = "";
        if (consonanti.length() > 3) {
            codice = "" + consonanti.charAt(0) + consonanti.charAt(2) + consonanti.charAt(3);
        }else if (consonanti.length()==3) {
            codice += consonanti;
        }else{
            codice += consonanti;
            int numVocaliToAdd = 3 - codice.length();
            for (char vocale : vocali.toCharArray()) {
                if (numVocaliToAdd > 0) {
                    codice += vocale;
                    numVocaliToAdd--;
                } else {
                    break;
                }
            }
        }
        //System.out.println(codice);
        return codice;
    }
    

    
    public static String calcolaCodiceLuogoNascita(String luogoNascita) {
        HashMap<String, String> codiciLuoghi = new HashMap<>();
        codiciLuoghi.put("ROMA", "H501");
        codiciLuoghi.put("MILANO","F205");
        codiciLuoghi.put("RHO","H264");
        codiciLuoghi.put("LAINATE","E415");

        if (codiciLuoghi.containsKey(luogoNascita)) {
            // La chiave è presente, restituisci il valore associato
            return codiciLuoghi.get(luogoNascita);
        } else {
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

    public static char calcolaCarattereControllo(String codiceFiscaleParziale) {
        int sommaPari = 0;
        int sommaDispari = 0;
    
        // Conversione dei caratteri con posizione di ordine pari
        for (int i = 1; i < 15; i += 2) {
            char carattere = codiceFiscaleParziale.charAt(i);
            int valore = convertiCaratterePari(carattere);
            sommaPari += valore;
        }
    
        // Conversione dei caratteri con posizione di ordine dispari
        for (int i = 0; i < 15; i += 2) {
            char carattere = codiceFiscaleParziale.charAt(i);
            int valore = convertiCarattereDispari(carattere);
            sommaDispari += valore;
        }
    
        // Calcolo del totale e del resto della divisione per 26
        int totale = sommaPari + sommaDispari;
        int resto = totale % 26;
    
        // Conversione del resto nel carattere alfabetico corrispondente
        char carattereControllo = (char) ('A' + resto);
    
        return carattereControllo;
    }
    

    public static int convertiCaratterePari(char carattere) {
        switch (Character.toUpperCase(carattere)) {
            case 'A': case '0': return 0;
            case 'B': case '1': return 1;
            case 'C': case '2': return 2;
            case 'D': case '3': return 3;
            case 'E': case '4': return 4;
            case 'F': case '5': return 5;
            case 'G': case '6': return 6;
            case 'H': case '7': return 7;
            case 'I': case '8': return 8;
            case 'J': case '9': return 9;
            case 'K': return 10;
            case 'L': return 11;
            case 'M': return 12;
            case 'N': return 13;
            case 'O': return 14;
            case 'P': return 15;
            case 'Q': return 16;
            case 'R': return 17;
            case 'S': return 18;
            case 'T': return 19;
            case 'U': return 20;
            case 'V': return 21;
            case 'W': return 22;
            case 'X': return 23;
            case 'Y': return 24;
            case 'Z': return 25;
            default: return 0; 
        }
    }
    
    public static int convertiCarattereDispari(char carattere) {
        switch (Character.toUpperCase(carattere)) {
            case 'A': case '0': return 1;
            case 'B': case '1': return 0;
            case 'C': case '2': return 5;
            case 'D': case '3': return 7;
            case 'E': case '4': return 9;
            case 'F': case '5': return 13;
            case 'G': case '6': return 15;
            case 'H': case '7': return 17;
            case 'I': case '8': return 19;
            case 'J': case '9': return 21;
            case 'K': return 2;
            case 'L': return 4;
            case 'M': return 18;
            case 'N': return 20;
            case 'O': return 11;
            case 'P': return 3;
            case 'Q': return 6;
            case 'R': return 8;
            case 'S': return 12;
            case 'T': return 14;
            case 'U': return 16;
            case 'V': return 10;
            case 'W': return 22;
            case 'X': return 25;
            case 'Y': return 24;
            case 'Z': return 23;
            default: return 0; 
        }
    }
    
}

