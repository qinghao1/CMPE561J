//Constants for FST "end" states and so on
public interface FSTConstants {
    public static final int START_STATE_NUM = 0;

    public static final int REGULAR_NOUN_END_STATE_NUM = 1;
    public static final char REGULAR_NOUN_POS = 'N';

    public static final char MORPHEME_BOUNDARY = '^';
    public static final String ALPHABET = "abcdefghijklmnopqrstuvxyz";

    //Used to delimit FST input text file
    public static final char FILE_WHITESPACE_SEPARATOR = '\t';
}
