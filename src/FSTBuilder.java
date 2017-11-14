import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FSTBuilder implements FSTConstants {

    public static String chainFST(String input, FST... fsts) {
        int i = 0;
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
        WordHelper regularVerbHelper = new WordHelper();
        regularVerbHelper.addToArrayList(sequenceList, "regular_verbs.txt");
        WordHelper ousAdjHelper = new WordHelper();
        ousAdjHelper.addToArrayList(sequenceList, "ous_adjectives.txt");
        WordHelper alAdjHelper = new WordHelper();
        alAdjHelper.addToArrayList(sequenceList, "al_adjectives.txt");

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
        regularVerbHelper.makeFile("regular_verbs_FST.txt", trieMap, REGULAR_VERB_END_STATE_NUM, REGULAR_VERB_POS);
        fstInputList.add("regular_verbs_FST.txt");
        ousAdjHelper.makeFile("ous_adjectives_FST.txt", trieMap, OUS_ADJ_END_STATE_NUM, OUS_ADJ_POS);
        fstInputList.add("ous_adjectives_FST.txt");
        alAdjHelper.makeFile("al_adjectives_FST.txt", trieMap, AL_ADJ_END_STATE_NUM, AL_ADJ_POS);
        fstInputList.add("al_adjectives_FST.txt");


        StartStateHelper.makeFile("start_states_FST.txt", trieMap);
        fstInputList.add("start_states_FST.txt");

        fstInputList.add("fst1_edited.txt");

        //Make main FST (lexical intermediate FST)
        FST mainFST = FST.buildFST(fstInputList, MAIN_FST_START_STATE_NUM);

        //Make inverted main FST
        fstInputList.add("int_to_lex_reverse.txt");
        FST mainReverseFST = FST.buildReverseFST(fstInputList, MAIN_FST_START_STATE_NUM);

        //Make orthographic FSTs

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
        FST yReplacementFST = FST.buildFST(yReplacementInputFiles, Y_REP_FST_START_STATE_NUM);
        
        //Caret(^) deletion FST
        ArrayList<String> carDeletionInputFiles = new ArrayList<>();
        String carDeletionRuleFile = "cariot_deletion.txt";
        String carDeletionOthersFileName = OrthographicRulesHelper.modifyFile(carDeletionRuleFile);
        carDeletionInputFiles.add(carDeletionRuleFile);
        carDeletionInputFiles.add(carDeletionOthersFileName);
        FST carDeletionFST = FST.buildFST(carDeletionInputFiles, CAR_DEL_FST_START_STATE_NUM);


        //Reverse orthographic FSTs

        //Reverse E insertion
        ArrayList<String> revEInsertionInputFiles = new ArrayList<>();
        String revEInsertionRuleFile = "rev_s_insertion.txt";
        String revEInsertionOthersFileName = OrthographicRulesHelper.modifyFile(revEInsertionRuleFile);
        revEInsertionInputFiles.add(revEInsertionRuleFile);
        revEInsertionInputFiles.add(revEInsertionOthersFileName);
        FST revEInsertionFST = FST.buildFST(revEInsertionInputFiles, REV_E_INS_FST_START_STATE_NUM);

        //Reverse y-replacement
        ArrayList<String> revYReplacementInputFiles = new ArrayList<>();
        String revYReplacementRuleFile = "rev_y_replacement.txt";
        String revYReplacementOthersFileName = OrthographicRulesHelper.modifyFile(revYReplacementRuleFile);
        revYReplacementInputFiles.add(revYReplacementRuleFile);
        revYReplacementInputFiles.add(revYReplacementOthersFileName);
        FST revYReplacementFST = FST.buildFST(revYReplacementInputFiles, REV_Y_REP_FST_START_STATE_NUM);

        //Reverse k-insertion
        ArrayList<String> revKInsertionInputFiles = new ArrayList<>();
        String revKInsertionRuleFile = "rev_k_insertion.txt";
        String revKInsertionOthersFileName = OrthographicRulesHelper.modifyFile(revKInsertionRuleFile);
        revKInsertionInputFiles.add(revKInsertionRuleFile);
        revKInsertionInputFiles.add(revKInsertionOthersFileName);
        FST revKInsertionFST = FST.buildFST(revKInsertionInputFiles, REV_K_INS_FST_START_STATE_NUM);

        //Reverse suffixes FST (^-insertion)
        ArrayList<String> revSuffixesInputFiles = new ArrayList<>();
        String revSuffixesRuleFile = "rev_suffixes.txt";
        String revSuffixesOthersFileName = OrthographicRulesHelper.modifyFile(revSuffixesRuleFile);
        revSuffixesInputFiles.add(revSuffixesRuleFile);
        revSuffixesInputFiles.add(revSuffixesOthersFileName);
        FST revSuffixesFST = FST.buildFST(revSuffixesInputFiles, REV_SUFFIX_FST_START_STATE_NUM);

        //User Interface
        System.out.println("Surface level to lexical leve? (1)");
        System.out.println("Lexical level to surface level? (2)");
        int direction = 0;
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a number: ");
        direction = reader.nextInt(); // Scans the next token of the input as an int.
        System.out.println("Enter your word: ");
        reader = new Scanner(System.in);
        String originalString = reader.nextLine();
        reader.close();

        if (direction == 1) {
        	String carettedString = chainFST((
                    new StringBuffer(originalString).reverse().toString()),
                    revKInsertionFST, revYReplacementFST, revEInsertionFST, revSuffixesFST);
        	String res = chainFST((new StringBuffer(carettedString).reverse().toString()), mainReverseFST);
        	System.out.println("Your word is " + res);
        } else if (direction == 2) {
        	String inter = chainFST(originalString, mainFST);
        	String res = chainFST(inter,kInsertionFST, yReplacementFST, eInsertionFST);
        	System.out.println("Your word is " + res);
        }
    }
}
