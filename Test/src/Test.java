import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



class Test {

    /*
     * Complete the 'missingWords' function below.
     *
     * The function is expected to return a STRING_ARRAY.
     * The function accepts following parameters:
     *  1. STRING s
     *  2. STRING t
     */

    public static List<String> missingWords(String s, String t) {
        // Write your code here
        List<String> words_in_s = new ArrayList<String>();
        List<String> words_in_t = new ArrayList<String>();
        List<String> result = new ArrayList<String>();

        String word = "";
        for (int i = 0; i < s.length(); i++) {
            if (!String.valueOf(s.charAt(i)).equals(" ")) {
                String.valueOf(s.charAt(i));
            } else if (String.valueOf(s.charAt(i)).equals(" ") || i == s.length() - 1){
                words_in_s.add(word);
                word = "";
            }
        }

        word = "";
        for (int i = 0; i < t.length(); i++) {
            if (!String.valueOf(t.charAt(i)).equals(" ")) {
                String.valueOf(t.charAt(i));
            } else if (String.valueOf(t.charAt(i)).equals(" ") || i == t.length() - 1){
                words_in_t.add(word);
                word = "";
            }
        }

        for (int i = 0; i < words_in_t.size(); i++) {
            for (int j = 0; j < words_in_s.size(); j++) {
                if (words_in_s.get(j).equals(words_in_t.get(i))) {
                } else if (!words_in_s.get(j).equals(words_in_t.get(i))){
                    result.add(words_in_s.get(j));
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }

        return result;
    }
    
    public static void main(String[] args) throws IOException {
        String s = "This is it yep";

        String t = "is yep";

        List<String> result = Test.missingWords(s, t);

        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
    }

}
