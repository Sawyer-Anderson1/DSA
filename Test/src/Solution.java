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


import java.util.Scanner;
import java.util.List;

class Result {

    /*
     * Complete the 'getSpamEmails' function below.
     *
     * The function is expected to return a STRING_ARRAY.
     * The function accepts following parameters:
     *  1. STRING_ARRAY subjects
     *  2. STRING_ARRAY spam_words
     */

    public static List<String> getSpamEmails(List<String> subjects, List<String> spam_words) {
    // Write your code here
        List<String> spam = new ArrayList<String>();
        for (int i = 0; i < subjects.size(); i++) {
            int spam_counter = 0;
            String subject_word = "";
            String subject = subjects.get(i);
            for (int w = 0; w < subject.length(); i++) {
                if (!String.valueOf(subject.charAt(w)).equals(" ")) {
                    subject_word = subject_word.concat(String.valueOf(subject.charAt(i)));   
                } else {
                    for (int j = 0; j < spam_words.size(); j++) {
                        if (subject_word.equals(spam_words.get(j))) {
                            spam_counter++;
                        }
                    }
                    if (spam_counter >= 2) {
                        spam.add(subject_word);
                    }
                    subject_word = "";
                }
            }
        }
        
        return spam;
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int subjectsCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> subjects = IntStream.range(0, subjectsCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .collect(toList());

        int spam_wordsCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> spam_words = IntStream.range(0, spam_wordsCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .collect(toList());

        List<String> result = Result.getSpamEmails(subjects, spam_words);

        bufferedWriter.write(
            result.stream()
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
