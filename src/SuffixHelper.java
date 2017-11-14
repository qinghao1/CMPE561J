//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class SuffixHelper {
//    public static final int START_STATE = 1; //Doesn't matter what this is
//
//    public static void makeFile(String inputFile, String outputFile) {
//        ArrayList<String> suffixList = new ArrayList();
//
//        //Populate suffixList
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(fileName));
//            String currentLine;
//            while ((currentLine = br.readLine()) != null) {
//                if (currentLine == null) continue;
//                suffixList.add(currentLine);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Error: No file found: " + fileName);
//        } catch (IOException e) {
//            System.out.println("Error while reading file: " + fileName);
//        }
//        //Map reverse() over arrayList
//        for (int i = 0; i < suffixList.length(); i++ {
//            StringBuilder s = new StringBuilder();
//            String reversedSuffix = s.append(suffixList.get(i)).reverse().toString();
//            suffixList.set(i, reversedSuffix);
//        }
//
//        //Make trieMap from suffixes
//        HashMap<String, Integer> trieMap = FSTTrieHelper.build(suffixList);
//
//        //For every suffix generate transitions
//
//    }
//}
