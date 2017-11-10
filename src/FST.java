import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FST implements FSTConstants {
    class State {
        int stateNumber;
        HashMap<Character, Pair<String, Integer>> transitions = new HashMap<>();

        State(int _stateNumber) {
            stateNumber = _stateNumber;
        }

        void addTransition(char i, String o, int nxt) {
            transitions.put(i, new Pair(o, nxt));
        }
    }

    void addArc(int _state1, int _state2, char i, String o) {
        if (!stateList.containsKey(_state1)) stateList.put(_state1, new State(_state1));
        if (!stateList.containsKey(_state2)) stateList.put(_state2, new State(_state2));
        stateList.get(_state1).addTransition(i, o, _state2);
    }

    int START_STATE_NUM;
    HashMap<Integer, State>stateList = new HashMap<>();

    FST(int _START_STATE_NUM) {
        START_STATE_NUM = _START_STATE_NUM;
    }

    String feed(String s) {
        StringBuilder output = new StringBuilder();

        State currentState = stateList.get(START_STATE_NUM);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Pair<String, Integer> currentStateTransition = currentState.transitions.get(c);
            if (currentStateTransition == null) return WRONG_INPUT_MESSAGE;
            String transitionOutput = currentStateTransition.getKey();
            //"&" represents Epsilon i.e. no output
            if (transitionOutput.equals(EPSILON)) {
                //Don't output anything
            } else {
                //If output is -X, where X is a number, we remove the previous X characters
                if (transitionOutput.matches("^-\\d+$")) {
                    int numberToRemove = Integer.parseInt(transitionOutput.substring(1));
                    output.delete(output.length() - numberToRemove, output.length());
                } else {
                    output.append(transitionOutput);
                }
            }
            currentState = stateList.get(currentStateTransition.getValue());
        }

        return output.toString();
    }

    public static FST buildFST(ArrayList<String> fileNames, int _START_STATE_NUM) {
        String inputLineRegex = "^([\\d]+)\\s+([\\d]+)\\s+(.)\\s+(.+)$";
        Pattern inputPattern = Pattern.compile(inputLineRegex);

        String commentRegex = "^#.+";

        FST fst = new FST(_START_STATE_NUM);
        for (String fileName : fileNames) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    //Skip if comment line
                    if (currentLine.matches(commentRegex)) continue;

                    //Skip if not valid input (Prints warning)
                    if (!currentLine.matches(inputLineRegex)) {
                        System.out.println("WARNING: Following line is not valid FST input");
                        System.out.println(currentLine);
                        continue;
                    }

                    Matcher inputMatcher = inputPattern.matcher(currentLine);
                    inputMatcher.find();

                    int state1 = Integer.parseInt(inputMatcher.group(1));
                    int state2 = Integer.parseInt(inputMatcher.group(2));
                    char inputChar = inputMatcher.group(3).charAt(0);
                    String outputString = inputMatcher.group(4);

                    fst.addArc(state1, state2, inputChar, outputString);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: No file found: " + fileName);
            } catch (IOException e) {
                System.out.println("Error while reading file: " + fileName);
            }
        }

        return fst;
    }
}
