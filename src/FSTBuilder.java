import java.util.ArrayList;
import java.util.HashMap;

public class FSTBuilder {
    public static void main(String[] args) {
        //Initialize sequence list and list of FST input filenames
        ArrayList<String> sequenceList = new ArrayList<>();
        ArrayList<String> fstInputList = new ArrayList<>();

        //Run all helpers
        RegularNounHelper.addToArrayList(sequenceList, "i_nouns.txt");

        //Make String -> StateID Map with Trie
        HashMap<String, Integer> trieMap = FSTTrieHelper.build(sequenceList);

        //Make and add all FST input files
        RegularNounHelper.makeFile("i_nouns_FST.txt", trieMap);
        fstInputList.add("i_nouns_FST.txt");

        StartStateHelper.makeFile("start_states_FST.txt", trieMap);
        fstInputList.add("start_states_FST.txt");

        fstInputList.add("fst1.txt");

        //Make FST!
        FST fst = FST.buildFST(fstInputList);

        //Test
        System.out.println(fst.feed("alphabetNZ"));
        System.out.println(fst.feed("factorNZ"));
        System.out.println(fst.feed("materialNP"));
        System.out.println(fst.feed("materialNZA"));
        System.out.println(fst.feed("materialNZAP"));

    }
}
