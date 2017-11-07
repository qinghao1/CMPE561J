import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/* Takes a input file of regular nouns adds them to FST in two steps:
 * 1) addToArrayList adds all prefixes to Trie arraylist
 * 2) makeFile writes the relevant FST arcs to a text file
 * Run FSTTrieHelper.build() after adding all word classes to arrayList before running makeFile!
 */
public class RegularNounHelper implements FSTConstants{
    //List of lists (each list is a list of transitions (C, CA, CAT; D, DO, DOG; etc.)
    static ArrayList<ArrayList<String>> transitionListList = new ArrayList<>();

    static boolean hasRunAddToArrayList = false;

    public static void addToArrayList(ArrayList<String> currentList, String fileName) {
        buildTransitionListList(fileName);
        for (ArrayList<String> transitionList : transitionListList) {
            for (String transition : transitionList) {
                currentList.add(transition);
            }
        }

        hasRunAddToArrayList = true;
    }

    public static void makeFile(String outputFileName, HashMap<String, Integer> trieMap) {
        if (!hasRunAddToArrayList) {
            System.out.println("Run addToArrayList() first before calling makeFile()");
        } else {
            BufferedWriter bw = null;
            try {
                File file = new File(outputFileName);

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file);
                bw = new BufferedWriter(fw);

                for (ArrayList<String> transitionList : transitionListList) {
                    String state1 = null;
                    StringBuilder s = new StringBuilder();

                    boolean hasSetState1 = false;

                    for (String state2 : transitionList) {
                        if (!hasSetState1) {
                            state1 = state2;
                            hasSetState1 = true;
                        } else {
                            s.append(trieMap.get(state1)); //State1 ID
                            s.append(FILE_WHITESPACE_SEPARATOR);
                            s.append(trieMap.get(state2)); //State2 ID
                            s.append(FILE_WHITESPACE_SEPARATOR);
                            //For regular nouns, input == output == last character
                            char inputOutputChar = state2.charAt(state2.length() - 1);
                            s.append(inputOutputChar);
                            s.append(FILE_WHITESPACE_SEPARATOR);
                            s.append(inputOutputChar);
                            s.append('\n');

                            state1 = state2;
                        }
                    }
                    //Write end state
                    s.append(trieMap.get(state1)); //Final string ID
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(REGULAR_NOUN_END_STATE_NUM); //End state ID
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(REGULAR_NOUN_POS); //Input
                    s.append(FILE_WHITESPACE_SEPARATOR);
                    s.append(EPSILON); //No output
                    s.append('\n');

                    bw.write(s.toString());
                }

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

    private static void buildTransitionListList(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine == null) continue;
                ArrayList<String> transitionList = new ArrayList<>();
                for(int i = 1; i <= currentLine.length(); i++) {
                    transitionList.add(currentLine.substring(0, i));
                }
                transitionListList.add(transitionList);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: No file found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error while reading file: " + fileName);
        }
    }
}
