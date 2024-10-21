// This is Sawyer Anderson's Programming Assignment 3:
// Implementing the improved "monkey simulation" using hash table for the map

// includes
import java.io.*;
import java.util.Scanner;

// the constructor and main at the bottom
public class SAANPA3 {
    // Some global variables and the map
    SAANPA3.HashTableBuilder map;
    String firstK = "";
    // int inputSize = 0;

    // The class that creates the CharDistribution object
    public static class CharDistribution {
        // The vector that holds the counter for each character possible in this problem,
        // That is '[a-z]' and ' ' (which will be index 26)
        int[] charCounter = new int[27];

        // the constructor takes the input size of the text file
        CharDistribution() {}

        // The method that increments the counter of characters
        void occurs(char c) {
            if (String.valueOf(c).equals(" ")) {
                charCounter[26]++;
            } else if (c >= 'a' && c <= 'z') {
                charCounter[c - 'a']++;
            }
        }

        // The method/function that returns a random character using the way described in the instructions
        char getRandomChar() {
            int totalOccurences = 0;
            for (int count : charCounter) {
                totalOccurences += count;
            }

            // get the random number
            int randomNum = (int)(Math.random() * ((totalOccurences - 1) + 1)) + 1;

            // now scan through the vector of counters, keep cumulative sum
            int sum = 0;
            for (int i = 0; i < charCounter.length; i++) {
                sum += charCounter[i];

                if (sum >= randomNum) {
                    if (i == 26) {
                        return ' ';
                    } // else
                    return (char) (i + 'a');
                }
            }

            // place holder
            return 'a';
        }
    }

    // the map: hash table
    // The node struture
    public class Element<E> {
        protected E keyword;
        protected CharDistribution charDistribution;

        public Element(E k){
            keyword = k;
            charDistribution = new CharDistribution();
        }
    }
    // The class for the hash table
    public class HashTableBuilder {
        Element<String>[][] hashTable; // the 2d array is for seperate chaining
        int tableSize;
        int totalElements = 0;

        public HashTableBuilder(int w) {
            tableSize = nextPrime(w * 2);
            hashTable = new Element[tableSize][];

            for (int i = 0; i < tableSize; i++) {
                hashTable[i] = new Element[0];
                totalElements++;
            }
        }

        public CharDistribution find(String k) {
            CharDistribution dis = null;

            int index = convertStringToInt(k) % tableSize;
            for (Element<String> element : hashTable[index]) { // go through the chains
                if (element != null && element.keyword.equals(k)) {
                    dis = element.charDistribution;
                    return dis;
                }
            }

            return dis;
        }

        void insert(String k, char c) {
            int index = convertStringToInt(k) % tableSize;

            for (Element<String> element : hashTable[index]) { // go through the chains
                if (element != null && element.keyword.equals(k)) {
                    element.charDistribution.occurs(c);
                    return;
                }
            }

            // adding new element
            Element<String> newElement = new Element<>(k);
            totalElements++;
            newElement.charDistribution.occurs(c);

            if (totalElements >= tableSize) {
                hashTable[index] = resizeArray(hashTable[index], newElement);
            }
        }

        private Element<String>[] resizeArray(Element<String>[] array, Element<String> newElement) {
            int newSize = nextPrime(array.length + 1);
            Element<String>[] newArray = new Element[newSize];
            System.arraycopy(array, 0, newArray, 0, array.length);
            newArray[array.length] = newElement; // Add new element
            return newArray;
        }

        // the hash function
        private int convertStringToInt(String k) {
            int sum = 0;

            for (int i = 0; i < k.length(); i++) {
                sum += (int) k.charAt(i);
            }

            return sum;
        }

        // for resizing
        private int nextPrime(int num) {
            num++;
            for (int i = 2; i * i <= num; i++) {
                if (num % i == 0) {
                    num++;
                    i = 2;
                } else {
                    continue;
                }
            }
            return num;
        }

        // remove function/method
        public void remove(String k) {
            int index = convertStringToInt(k) % tableSize;
            Element<String>[] list = hashTable[index];
            for (int i = 0; i < list.length; i++) {
                if (list[i] != null && list[i].keyword.equals(k)) {
                    // Remove the element by shifting the elements left
                    Element<String>[] newList = new Element[list.length - 1];
                    System.arraycopy(list, 0, newList, 0, i);
                    System.arraycopy(list, i + 1, newList, i, list.length - i - 1);
                    hashTable[index] = newList; // Update the table
                    return; // Remove the node if found
                }
            }
        }
    }
    // end of hash table implementation

    // writes to file once the output is handled
    void writeFile(BufferedWriter writer, int w, int length) {
        int count = w; // the first w characters are already part of the output
        StringBuilder kWriter = new StringBuilder(firstK);

        try {
            writer.write(firstK);
        } catch (IOException e) {
            System.out.println("Couldn't write first w elements to file");
        }

        while (count < length) {
            // get the distribution
            CharDistribution dis = map.find(kWriter.toString());
            // get the appropriate character for that distribution.
            if (dis != null) {
                char c = dis.getRandomChar();

                // writing the new character c
                try {
                    writer.write(c); // writes
                } catch (IOException e) {
                    System.out.println("Failure to write.");
                }

                // update sequence kWriter so that it is incorporates the new character c
                kWriter.deleteCharAt(0);
                kWriter.append(c);
            }
            count++;
        }

        try {
            // After writing the output
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error closing or flushing the output file.");
        }

    }

    /*
    // gets input size
    void getInputSize() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/merchant.txt"))) {
            // getting the input size (to help with the getRandomChar algo
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count += line.length();
            }
            inputSize = count;
        } catch (IOException e) {
            System.out.println("Error with finding size of input file (reading).");
        }
    }
    */

    // reads file
    void readFile(int w) {
        // Read file and create the distributions
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/merchant.txt"))) {
            // CharDistribution[] c = new CharDistribution[27^w]; this would be the total distributions
            int uniChar; // what is reader by reader.read()
            int wCount = 0; // the counter that keeps the string k to the length of the w
            StringBuilder k = new StringBuilder(""); // the sequence k

            while (reader.ready()) { // -1 indicates the EOF
                if (wCount < w) { // wCount is only relevant for the first w characters
                    uniChar = reader.read();
                    char c = (char) uniChar;
                    if ((c < 'a' || c > 'z') && c != ' ') {
                        continue; // skipping non-letters and non-space characters
                    }

                    // creating what wll be the first k to the CharDistribution
                    k.append((char) uniChar);
                    wCount++;
                    continue; // makes sure that as ony as the first
                    // w characters haven't been read that it doesn't start with the below operations
                }

                // When all first w characters are read
                // This conditional statement is done once
                // we read in the characters in lengths of w and then add the character
                // after into the CharDistribution
                if (wCount == w) {
                    // adding the first w characters to the writer/output.txt
                    firstK = k.toString();
                    wCount++; // so it will continue with the below operations and not return to this statement
                }

                // after the first w characters:
                // updating or creating a CharDistribution for the particular sequence k
                // get next char (the one that get incremented in the CharDistribution of the map's node
                uniChar = reader.read();
                char c = (char) uniChar;
                if ((c < 'a' || c > 'z') && c != ' ') {
                    continue; // skipping non-letter, non-space characters
                }
                map.insert(k.toString(), c);

                k.deleteCharAt(0); // remove the first element since we are shifting k
                k.append(c); // new key k
            }
        } catch (IOException e) {
            System.out.println("Error with reading input file's characters.");
        }
    }

    // the constructor for the class SAANPA3
    public SAANPA3(int w, int length) {
        // intialize the map
        map = new HashTableBuilder(w);

        try {
            // creating the buffered writer and output file, with FileWriter append flag set to true
            BufferedWriter writer = new BufferedWriter(new FileWriter("resources/output.txt", true));

            // call the functions of SAANPA2
            // getInputSize();
            readFile(w);
            // call the writer
            writeFile(writer, w, length);
        } catch (IOException e) {
            System.out.println("Output file not able to be written to.");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the window size: ");
        int w = input.nextInt();

        System.out.print("Enter the length of the output text: ");
        int length = input.nextInt();

        // call SAANPA3 class constructor
        SAANPA3 read = new SAANPA3(w, length);
    }
}