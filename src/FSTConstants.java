//Constants for FST "end" states and so on
public interface FSTConstants {
    public static final int MAIN_FST_START_STATE_NUM = 0;
    public static final int Y_REP_FST_START_STATE_NUM = 20;
    public static final int K_INS_FST_START_STATE_NUM = 30;
    public static final int E_INS_FST_START_STATE_NUM = 40;

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

    //Alphabet
    public static final String ALPHABET = "abcdefghijklmnopqrstuvxyz";

    //Symbols
    public static final String EPSILON = "&";
    public static final char MORPHEME_BOUNDARY = '^';

    //Alphabet + Symbols
    public static final String ALL_VALID_INPUTS = ALPHABET
            + EPSILON
            + MORPHEME_BOUNDARY;

    //Number from which non-constant states start
    public static final int START_STATE_NUMBER = 100;

    //Used to delimit FST input text file
    public static final char FILE_WHITESPACE_SEPARATOR = '\t';

    public static final String WRONG_INPUT_MESSAGE = "Warning: invalid input";
}
