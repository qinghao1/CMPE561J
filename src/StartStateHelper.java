import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

//Creates transitions from start state to all single-letter states that exist in trie
public class StartStateHelper implements FSTConstants {
    public static void makeFile(String outputFileName, HashMap<String, Integer> trieMap) {
        BufferedWriter bw = null;
        try {
            File file = new File(outputFileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            StringBuilder s = new StringBuilder();

            for(int i = 0; i < ALPHABET.length(); i++) {
                if (!trieMap.containsKey(Character.toString(ALPHABET.charAt(i)))) {
                    continue;
                } else {
                    s.append(START_STATE_NUM); //Start state
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(trieMap.get(Character.toString(ALPHABET.charAt(i)))); //Single-letter state
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(ALPHABET.charAt(i)); //Input
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(ALPHABET.charAt(i)); //Output
                    s.append('\n');
                }
            }
            bw.write(s.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(bw != null) bw.close();
            } catch (Exception e) {
                System.out.println("Error closing the BufferedWriter" + e);
            }
        }
    }
}
