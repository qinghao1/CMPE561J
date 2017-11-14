import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FST implements FSTConstants {
    class State {
        int stateNumber;
        HashMap<Character, Pair<String, Integer>> transitions;
        //Special HashSet for epsilon transitions because there can be multiple of them
        HashSet<Pair<String, Integer>> epsTransitions;

        State(int _stateNumber) {
            transitions = new HashMap<>();
            epsTransitions = new HashSet<>();
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
            if (i == EPSILON_INPUT) {
                epsTransitions.add(new Pair(o, nxt));
            } else {
                transitions.put(i, new Pair(o, nxt));
            }
        }
    }

    int START_STATE_NUM;
    HashMap<Integer, State>stateList = new HashMap<>();

    FST(int _START_STATE_NUM) {
        START_STATE_NUM = _START_STATE_NUM;
    }

    //Add arc/transition between two states, and creates them if they don't exist
    void addArc(int _state1, int _state2, char i, String o) {
        if (!stateList.containsKey(_state1)) stateList.put(_state1, new State(_state1));
        if (!stateList.containsKey(_state2)) stateList.put(_state2, new State(_state2));
        stateList.get(_state1).addTransition(i, o, _state2);
    }

    //"Feeds", or inputs String to FST character by character.
    String feed(String s) {
        State currentState = stateList.get(START_STATE_NUM);

        //Keep a map of current states to their outputs. Add to this set when we see epsilon-transitions, and remove them
        //when we encounter a transition that does not apply to that state
        HashMap<State, String> stateSet = new HashMap<>();
        stateSet.put(currentState, "");

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            //Need to use toRemove and toAdd lists to prevent in-place modification
            //while iterating through the set
            List<State> toRemove = new ArrayList<>();
            List<Pair<State, String>> toAdd = new ArrayList<>();
            for (State possibleState : stateSet.keySet()) {
                //Handle epsilon and non-epsilon inputs differently
                if (c == EPSILON_INPUT) {
                    boolean newStateAdded = false;
                    for (Pair <String, Integer> transition : possibleState.epsTransitions) {
                        State newState = stateList.get(transition.getValue());
                        String possibleStateOutput = stateSet.get(possibleState);
                        String newOutput = possibleStateOutput;

                        String transitionOutput = transition.getKey();
                        if (transitionOutput.equals(EPSILON)) {
                            //Don't append anything to current output
                        } else {
                            newOutput = possibleStateOutput + transitionOutput;
                        }
                        toAdd.add(new Pair(newState, newOutput));

                        if (newState.stateNumber != possibleState.stateNumber) newStateAdded = true;
                    }
                    if (!newStateAdded) toRemove.add(possibleState);
                } else {
                    Pair<String, Integer> transition = possibleState.transitions.get(c);
                    if (transition == null) {
                        //Remove from set, do nothing
                        toRemove.add(possibleState);
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
                        toAdd.add(new Pair(newState, newOutput));

                        if (newState.stateNumber != possibleState.stateNumber) {
                            toRemove.add(possibleState);
                        }
                    }
                }
            }
            for (State toBeRemovedState : toRemove) stateSet.remove(toBeRemovedState);
            for (Pair<State, String> addedStateOutput : toAdd) {
                stateSet.put(addedStateOutput.getKey(), addedStateOutput.getValue());
            }
            toRemove.clear();
            toAdd.clear();

            //Add all possible epsilon transitions
            //As there may be more than 1 epsilon transition in series, we run this until no new states are added
            boolean newStateAdded = true;
            while (newStateAdded) {
                newStateAdded = false;
                for (State possibleState : stateSet.keySet()) {
                    for (Pair<String, Integer> epsTransition : possibleState.epsTransitions) {
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
                        if (!stateSet.containsKey(newState)) newStateAdded = true;
                        toAdd.add(new Pair(newState, newOutput));
                    }
                }
                for (Pair<State, String> addedStateOutput : toAdd) {
                    stateSet.put(addedStateOutput.getKey(), addedStateOutput.getValue());
                }
            }

            //If no states remaining in set of possible sets, input is invalid
            if (stateSet.isEmpty()) {
                return WRONG_INPUT_MESSAGE;
            }
        }

        //There can be multiple final states with corresponding outputs, but we just
        //take the first one
        for (State possibleState : stateSet.keySet()) {
            if (possibleState.stateNumber == FINAL_STATE_NUMBER) {
                return stateSet.get(possibleState);
            }
        }

        return WRONG_INPUT_MESSAGE;
    }

    //Class method, builds a FST given list of file names and start state number
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
                    //Skip if empty string
                    if (currentLine.isEmpty()) continue;

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

    //Builds inverted FST (input and output labels are switched)
    public static FST buildReverseFST(ArrayList<String> fileNames, int _START_STATE_NUM) {
        String inputLineRegex = "^([\\d]+)\\s+([\\d]+)\\s+(.+)\\s+(.)$";
        Pattern inputPattern = Pattern.compile(inputLineRegex);

        String commentRegex = "^#.+";

        FST fst = new FST(_START_STATE_NUM);
        for (String fileName : fileNames) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    //Skip if empty string
                    if (currentLine.isEmpty()) continue;

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
                    char inputChar = inputMatcher.group(4).charAt(0);
                    String outputString = inputMatcher.group(3);

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
