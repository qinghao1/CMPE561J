import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Expands "OTHER" terms in the orthographic
//transition list to all other characters in ALL_VALID_INPUTS
//To simplify the coding, these "other" transitions are written
//to filename_others.extension
public class OrthographicRulesHelper implements FSTConstants {
    public static String modifyFile(String filename) {
        BufferedWriter bw = null;
        try {
            //Setup
            Pattern extensionPattern = Pattern.compile("^(.+)\\.(.+)$");
            Matcher extensionMatcher = extensionPattern.matcher(filename);
            extensionMatcher.find();

            String outputFileName = extensionMatcher.group(1) + "_others." + extensionMatcher.group(2);
            File file = new File(outputFileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            StringBuilder s = new StringBuilder();

            //Read from input file
            LineNumberReader br = new LineNumberReader(new FileReader(filename));
            String currentLine;
            String othersRegex = "^##(\\d+)\\s+(\\d+)\\s+(OTHER)\\s+(OTHER|.+)$";
            Pattern othersPattern = Pattern.compile(othersRegex);
            ArrayList<String> stringsArrayList = new ArrayList<>();

            while ((currentLine = br.readLine()) != null) {
                stringsArrayList.add(currentLine);
                if (!currentLine.matches(othersRegex)) continue;

                int currentLineNumber = br.getLineNumber();
                Matcher othersMatcher = othersPattern.matcher(currentLine);
                othersMatcher.find();
                String startState = othersMatcher.group(1);
                String endState = othersMatcher.group(2);
                String output = othersMatcher.group(4);

                //Construct regex for previous line with same start and end state
                String previousLineRegex = "^" + startState + "\\s+\\d+\\s+(.)\\s+.+";
                Pattern previousPattern = Pattern.compile(previousLineRegex);
                HashSet<Character> previousInputs = new HashSet<>();
                for (int i  = 0; i < currentLineNumber; i++) {
                    //Populate set with previous inputs
                    String currentPreviousLine = stringsArrayList.get(i);
                    if (!currentPreviousLine.matches(previousLineRegex)) continue;

                    Matcher previousMatcher = previousPattern.matcher(currentPreviousLine);
                    previousMatcher.find();
                    previousInputs.add(previousMatcher.group(1).charAt(0));
                }

                boolean inputSameAsOutput = output.equals("OTHER");

                //Print the "OTHER" transitions for remaining valid inputs
                for (int i = 0; i < ALL_VALID_INPUTS.length(); i++) {
                    char currentChar = ALL_VALID_INPUTS.charAt(i);
                    if (previousInputs.contains(currentChar)) continue;

                    s.append(startState);
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(endState);
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(currentChar);
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(inputSameAsOutput ? currentChar : output);
                    s.append('\n');
                }
            }

            //Write to file
            bw.write(s.toString());
            return outputFileName;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(bw != null) bw.close();
            } catch (Exception e) {
                System.out.println("Error closing the BufferedWriter" + e);
            }
        }
        return null; //Shouldn't come here
    }
}
