import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FST {
    class State {
        int state_number;
        HashMap<Character, Pair<Character, Integer>> transitions;

        State(int _state_number) {
            state_number = _state_number;
        }

        void add_transition(char i, char o, int nxt) {
            transitions.put(i, new Pair(o, nxt));
        }
    }

    HashMap<Integer, State>stateList = new HashMap<>();

    void add_arc(int _state1, int _state2, char i, char o) {
        State state1 = stateList.containsKey(_state1) ? stateList.get(_state1) : stateList.put(_state1, new State(_state1));
        State state2 = stateList.containsKey(_state2) ? stateList.get(_state2) : stateList.put(_state2, new State(_state2));
        state1.add_transition(i, o, _state2);
    }

    void feed(String s) {
        State current_state = stateList.get(0);
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            Pair<Character, Integer> current_state_transition = current_state.transitions.get(c);
            System.out.println(current_state_transition.getKey());
            current_state = stateList.get(current_state_transition.getValue());
        }
    }

    public static FST buildFST(ArrayList<String> fileNames) {
        String inputLineRegex = "^([\\d]+)\\s+([\\d]+)\\s+(.)\\s+(.)$";
        Pattern inputPattern = Pattern.compile(inputLineRegex);

        String commentRegex = "^#";

        FST fst = new FST();
        for (String fileName : fileNames) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    //Skip if comment line
                    if (currentLine.matches(commentRegex)) continue;

                    Matcher inputMatcher = inputPattern.matcher(currentLine);
                    inputMatcher.find();

                    int state1 = Integer.parseInt(inputMatcher.group(1));
                    int state2 = Integer.parseInt(inputMatcher.group(2));
                    char inputChar = inputMatcher.group(3).charAt(0);
                    char outputChar = inputMatcher.group(4).charAt(0);

                    fst.add_arc(state1, state2, inputChar, outputChar);
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
