import java.util.ArrayList;
import java.util.HashMap;

public class FSTBuilder implements FSTConstants {
    public static String chainFST(String input, FST... fsts) {
        for (FST fst : fsts) {
            if (input.equals(WRONG_INPUT_MESSAGE)) return WRONG_INPUT_MESSAGE;
            input = fst.feed(input);
        }
        return input;
    }

    public static void main(String[] args) {
        //Initialize sequence list and list of FST input filenames
        ArrayList<String> sequenceList = new ArrayList<>();
        ArrayList<String> fstInputList = new ArrayList<>();

        //Run all helpers
        WordHelper iNounHelper = new WordHelper();
        iNounHelper.addToArrayList(sequenceList, "i_nouns.txt");
        WordHelper lNounHelper = new WordHelper();
        lNounHelper.addToArrayList(sequenceList, "l_nouns.txt");
        WordHelper jVerbHelper = new WordHelper();
        jVerbHelper.addToArrayList(sequenceList, "j_verbs.txt");
        WordHelper kVerbHelper = new WordHelper();
        kVerbHelper.addToArrayList(sequenceList, "k_verbs.txt");
        WordHelper ousAdjHelper = new WordHelper();
        ousAdjHelper.addToArrayList(sequenceList, "ous_adjectives.txt");

        //Make String -> StateID Map with Trie
        HashMap<String, Integer> trieMap = FSTTrieHelper.build(sequenceList);

        //Make and add all FST input files
        iNounHelper.makeFile("i_nouns_FST.txt", trieMap, REGULAR_NOUN_END_STATE_NUM, REGULAR_NOUN_POS);
        fstInputList.add("i_nouns_FST.txt");
        lNounHelper.makeFile("l_nouns_FST.txt", trieMap, L_NOUN_END_STATE_NUM, L_NOUN_POS);
        fstInputList.add("l_nouns_FST.txt");
        jVerbHelper.makeFile("j_verbs_FST.txt", trieMap, J_VERB_END_STATE_NUM, J_VERB_POS);
        fstInputList.add("j_verbs_FST.txt");
        kVerbHelper.makeFile("k_verbs_FST.txt", trieMap, K_VERB_END_STATE_NUM, K_VERB_POS);
        fstInputList.add("k_verbs_FST.txt");
        ousAdjHelper.makeFile("ous_adjectives_FST.txt", trieMap, OUS_ADJ_END_STATE_NUM, OUS_ADJ_POS);
        fstInputList.add("ous_adjectives_FST.txt");

        StartStateHelper.makeFile("start_states_FST.txt", trieMap);
        fstInputList.add("start_states_FST.txt");

        fstInputList.add("fst1.txt");

        //Make main FST (lexical intermediate FST)
        FST mainFST = FST.buildFST(fstInputList, MAIN_FST_START_STATE_NUM);

        //Make orthographic FSTs
//        ArrayList<FST> orthographicFSTs = new ArrayList<>();

        //E-insertion FST
        ArrayList<String> eInsertionInputFiles = new ArrayList<>();
        String eInsertionRuleFile = "rule_e-insertion.txt";
        String eInsertionOthersFileName = OrthographicRulesHelper.modifyFile(eInsertionRuleFile);
        eInsertionInputFiles.add(eInsertionRuleFile);
        eInsertionInputFiles.add(eInsertionOthersFileName);
        FST eInsertionFST = FST.buildFST(eInsertionInputFiles, E_INS_FST_START_STATE_NUM);

        //K-insertion FST
        ArrayList<String> kInsertionInputFiles = new ArrayList<>();
        String kInsertionRuleFile = "rule_k-insertion.txt";
        String kInsertionOthersFileName = OrthographicRulesHelper.modifyFile(kInsertionRuleFile);
        kInsertionInputFiles.add(kInsertionRuleFile);
        kInsertionInputFiles.add(kInsertionOthersFileName);
        FST kInsertionFST = FST.buildFST(kInsertionInputFiles, K_INS_FST_START_STATE_NUM);

        //Y-replacement FST
        ArrayList<String> yReplacementInputFiles = new ArrayList<>();
        String yReplacementRuleFile = "rule_y-replacement.txt";
        String yReplacementOthersFileName = OrthographicRulesHelper.modifyFile(yReplacementRuleFile);
        yReplacementInputFiles.add(yReplacementRuleFile);
        yReplacementInputFiles.add(yReplacementOthersFileName);
        FST yReplacementOthersFileNameFST = FST.buildFST(yReplacementInputFiles, Y_REP_FST_START_STATE_NUM);

        //Test
        System.out.println(chainFST("blissNP", mainFST, kInsertionFST, yReplacementOthersFileNameFST, eInsertionFST));
//        System.out.println(fst.feed("factorNZ"));
//        System.out.println(fst.feed("materialNP"));
//        System.out.println(fst.feed("materialNZA"));
//        System.out.println(fst.feed("materialNZAP"));

    }
}
