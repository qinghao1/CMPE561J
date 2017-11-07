import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

//Uses a trie data structure to generate a unique state_ID for every unique sequence of characters
//FSTTrieHelper.build(ArrayList<String> sequenceList) returns a HashMap<String, Integer> of sequence to state_ID
public class FSTTrieHelper implements FSTConstants {
    static char ROOT_CHAR = '\0'; //Doesn't matter what this value is

    static class TreeNode {
        HashMap<Character, TreeNode> childNodes;
        char nodeChar;
        TreeNode parentNode; //Back pointer to construct string

        TreeNode(char c, TreeNode parent) {
            childNodes = new HashMap<>();
            nodeChar = c;
            parentNode = parent;
        }

        TreeNode addChild(char childChar) {
            if (!childNodes.containsKey(childChar)) {
                TreeNode childNode = new TreeNode(childChar, this);
                childNodes.put(childChar, childNode);
                return childNode;
            } else {
                return childNodes.get(childChar);
            }
        }

        String printPath() {
            StringBuilder s = new StringBuilder();
            TreeNode currentNode = this;
            while(currentNode.parentNode != null) {
                s.insert(0, currentNode.nodeChar);
                currentNode = currentNode.parentNode;
            }
            return s.toString();
        }
    }

    static HashMap<String, Integer> build(ArrayList<String> sequences) {
        TreeNode root = new TreeNode(ROOT_CHAR, null);

        //Build trie
        for (String seq : sequences) {
            TreeNode currentNode = root;
            for (int i = 0; i < seq.length(); i++) {
                char currentChar = seq.charAt(i);
                currentNode = currentNode.addChild(currentChar);
            }
        }

        //BFS trie to get unique stateIDs
        Queue<TreeNode> BFS = new LinkedList<>();
        BFS.add(root);

        HashMap<String, Integer> stateIDMapping = new HashMap<>();
        int stateIDCounter = START_STATE_NUMBER;

        while(!BFS.isEmpty()) {
            TreeNode currentNode = BFS.remove();
            for (TreeNode child : currentNode.childNodes.values()) {
                BFS.add(child);
            }

            if (currentNode != root) stateIDMapping.put(currentNode.printPath(), stateIDCounter++);
        }

        return stateIDMapping;
    }

}
