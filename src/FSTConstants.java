//Constants for FST "end" states and so on
public interface FSTConstants {
    public static final int MAIN_FST_START_STATE_NUM = 0;
    public static final int Y_REP_FST_START_STATE_NUM = 20;
    public static final int K_INS_FST_START_STATE_NUM = 30;
    public static final int E_INS_FST_START_STATE_NUM = 40;
    public static final int CAR_DEL_FST_START_STATE_NUM = 300;

    public static final int REV_E_INS_FST_START_STATE_NUM = 80;
    public static final int REV_K_INS_FST_START_STATE_NUM = 90;
    public static final int REV_Y_REP_FST_START_STATE_NUM = 100;

    public static final int REV_SUFFIX_FST_START_STATE_NUM = 50;

    public static final int REGULAR_NOUN_END_STATE_NUM = 1;
    public static final char REGULAR_NOUN_POS = 'N';

    public static final int L_NOUN_END_STATE_NUM = 11;
    public static final char L_NOUN_POS = 'N';

    public static final int J_VERB_END_STATE_NUM = 7;
    public static final char J_VERB_POS = 'V';

    public static final int K_VERB_END_STATE_NUM = 10;
    public static final char K_VERB_POS = 'V';

    public static final int OUS_ADJ_END_STATE_NUM = 8;
    public static final char OUS_ADJ_POS = 'M';

    public static final int AL_ADJ_END_STATE_NUM = 5;
    public static final char AL_ADJ_POS = 'M';

    public static final int REGULAR_VERB_END_STATE_NUM = 17;
    public static final char REGULAR_VERB_POS = 'V';

    //Alphabet
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    //Symbols
    public static final String EPSILON = "&";
    public static final char EPSILON_INPUT = EPSILON.charAt(0); //Input is char, output is String
    public static final char MORPHEME_BOUNDARY = '^';

    //Alphabet + Symbols
    public static final String ALL_VALID_INPUTS = ALPHABET
            + MORPHEME_BOUNDARY;

    //Number from which non-constant states start
    public static final int START_STATE_NUMBER = 1000;

    //Final state number (valid for ALL FSTs)
    public static final int FINAL_STATE_NUMBER = 100000;

    //Used to delimit FST input text file
    public static final char FILE_WHITESPACE_SEPARATOR = '\t';

    public static final String WRONG_INPUT_MESSAGE = "Warning: invalid input";
}
