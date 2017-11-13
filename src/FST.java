import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FST implements FSTConstants {
    class State {
        int stateNumber;
        HashMap<Character, Pair<String, Integer>> transitions = new HashMap<>();

        State(int _stateNumber) {
            stateNumber = _stateNumber;
        }

        @Override
        //Needed to implement HashMap
        public int hashCode() {return stateNumber;}
        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof  State)) return false;
            State otherState = (State) other;
            return this.stateNumber == otherState.stateNumber;
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
        State currentState = stateList.get(START_STATE_NUM);

        //Keep a map of current states to their outputs. Add to this set when we see epsilon-transitions, and remove them
        //when we encounter a transition that does not apply to that state
        HashMap<State, String> stateSet = new HashMap<>();
        stateSet.put(currentState, "");

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            for (State possibleState : stateSet.keySet()) {
                Pair<String, Integer> transition = possibleState.transitions.get(c);
                if (transition == null) {
                    //Remove from set, do nothing
                } else {
                    State newState = stateList.get(transition.getValue());
                    String possibleStateOutput = stateSet.get(possibleState);
                    String newOutput = possibleStateOutput;

                    String transitionOutput = transition.getKey();
                    if (transitionOutput.equals(EPSILON)) {
                        //Don't append anything to current output
                    } else {
                        newOutput = possibleStateOutput + transitionOutput;
                    }
                    stateSet.put(newState, newOutput);
                }
                stateSet.remove(possibleState);
            }

            //Add all possible epsilon transitions
            for (State possibleState : stateSet.keySet()) {
                Pair<String, Integer> epsTransition = possibleState.transitions.get(EPSILON);
                if (epsTransition == null) {
                    //No epsilon transition
                } else {
                    State newState = stateList.get(epsTransition.getValue());
                    String possibleStateOutput = stateSet.get(possibleState);
                    String newOutput = possibleStateOutput;

                    String transitionOutput = epsTransition.getKey();
                    if (transitionOutput.equals(EPSILON)) {
                        //Don't append anything to current output
                        //This should technically only happen for final state
                    } else {
                        newOutput = possibleStateOutput + transitionOutput;
                    }
                    stateSet.put(newState, newOutput);
                }
            }

            //If no states remaining in set of possible sets, input is invalid
            if (stateSet.isEmpty()) return WRONG_INPUT_MESSAGE;
        }

        //There should only be one final state in set
        for (State possibleState : stateSet.keySet()) {
            if (possibleState.stateNumber == FINAL_STATE_NUMBER) return stateSet.get(possibleState);
        }

        return WRONG_INPUT_MESSAGE;
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
