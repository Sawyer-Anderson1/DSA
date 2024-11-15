import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SAANPA4 {
    // Some global variables and the priority queue
    priorityQueue pq;
    ArrayList<Character> firstNChars = new ArrayList<>();
    ArrayList<String> bitStrings = new ArrayList<>(27);
    ArrayList<Element<Integer, Character>> P;

    // Data structure: Priority Queue
    public class Element<K, V> {
        protected K key;
        protected V value;

        // Left and right only need to be used for the dummy nodes
        Element left;
        Element right;

        public Element(K freq, V character) {
            key = freq;
            value = character;
        }
    }
    public class priorityQueue {
        int size;

        public priorityQueue() {P = new ArrayList<>(28);}
        public priorityQueue(ArrayList<Integer> arr) {
            size = 28;

            // The array for the priority queue
            P = new ArrayList<>(28);

            // Copy the array where the index indicates character to
            // an array that can freely change to be a priority queue
            for (int i = 0; i < arr.size(); i++) {
                if (i == 26) {
                    Element<Integer, Character> e = new Element<>(arr.get(i), ' ');
                    int index = i + 1;
                    P.add(index, e);
                } else {
                    Element<Integer, Character> e = new Element<>(arr.get(i), Character.valueOf((char) (i + 'a')));
                    int index = i + 1;
                    P.add(index, e);
                }
            }

            // free up the original array
            arr.clear();

            int size = P.size();
            // BuildHeap from the array that has the frequencies and character value in (k, v) form
            for (int i = (size / 2) - 1; i >= 0; i--) {
                int hole = i;
                Element<Integer, Character> temp = P.get(hole);

                while (2 * hole <= size) {
                    int child = 2 * hole;
                    if (child != size && P.get(child).key > P.get(child + 1).key) {
                        child = child + 1;
                    }
                    if (P.get(child).key < P.get(hole).key) {
                        P.add(hole, P.get(child));
                    }
                    hole = child;
                }
                P.add(hole, temp);
            }

            // create the prefix-free tree T
            // T = new ArrayList<>();

            // an array of the dummy nodes and their children
            // ArrayList<Element<Integer, Character>> T_i = new ArrayList<>();

            int i = 0;
            while (P.size() > 1) {
                // get first min, then delete it from P
                Element<Integer, Character> firstChild = P.get(1);
                deleteMin();
                // get second min, then delete it from P
                Element<Integer, Character> secondChild = P.get(1);
                deleteMin();

                // create the T_i-node/dummy node
                int sum = firstChild.key + secondChild.key;
                Element<Integer, Character> t = new Element<>(sum, null); // nodes with null are the dummy nodes
                t.left = firstChild;
                t.right = secondChild;

                // T_i.add(i, t);

                // add the dummy node back to P
                insert(t);
            }
        }

        // the size operation
        public int size() {
            return size;
        }

        // empty operation
        public boolean empty() {
            if (size > 0) {
                return false;
            }
            return true;
        }

        // min operation
        public Element min(){
            if (empty()) {
                //error
            }

            return P.get(1);
        }

        // deleteMin operation
        public void deleteMin() {
            if (empty()) {
                //error
            }
            Element<Integer, Character> last = P.get(size);
            size--; // deletes via lowering size????
            int hole = 1;

            while (2 * hole <= size) {
                int child = 2 * hole;
                if (child != size && P.get(child).key > P.get(child + 1).key) {
                    child = child + 1;
                }
                if (P.get(child).key < last.key) {
                    P.add(hole, P.get(child));
                }
                hole = child;
            }
            P.add(hole, last);
        }

        // insert operation
        // This does not need to be a general insert, which is why portions
        //      of it are commmented out.
        // This operation is only really used for the dummy nodes being inserted
        // We don't need to adjust frequency in here because all of the frequencies
        //      were recorded prior to calling priorityQueue.
        public void insert(/*Integer k, Character c, */ Element t) {
            // checking if a resize is necessary, i.e. a new element is being added
            // boolean resize = true;
            // int index = 0;

            /*
            for (int i = 1; i < size; i++) {
                if (P.get(i).value == c) {
                    resize = false;
                    index = i;
                    break;
                }
            }
            */

            // Element<Integer, Character> x = new Element<>(k, c);
            // resizing/adding the new element
            int hole = size;
            size++;

            while ((int) t.key < P.get((int) Math.floor(hole/2)).key && hole != 1) {
                P.add(hole, P.get((int) Math.floor(hole/2)));
                hole = (int) Math.floor(hole/2);
            }
            P.add(hole, t);

            /*else {
                // just adding to frequency
                P.get(index).key = P.get(index).key + 1;
            }*/
        }
    }
    // End of data structure implementation

    // functions that traverses "T" tree from the last remaining node in P array/tree
    //      creating the bitstrings for each letter then adding those bit strings to an array
    void bitStringCreation() {
        // for every character we find the bitstring
        for (int i = 0; i < bitStrings.size(); i++) {
            String bitString = "";

            // call the functions that traverses using pre-order
            preorderTraversal(P.get(1), bitString);
        }
    }
    void preorderTraversal(Element e, String s) {
        if (e.left == null && e.right == null && e.value != null) {
            // add the bitstring to the array of them (at the right position)
            int index = (char) e.value - 'a';
            bitStrings.add(index, s);

            return;
        }

        preorderTraversal(e.left, s + "0");
        preorderTraversal(e.right, s + "1");
    }

    // writes to file once the output is handled
    void writeFile(BufferedWriter writer) {
        // writing the the bitstrings of the 27 characters read from the input file
        for (int i = 0; i < 27; i++) {
            // writing the new bitstring for the character represented at that specific index
            try {
                writer.write(bitStrings.get(i)); // writes
            } catch (IOException e) {
                System.out.println("Failure to write.");
            }
        }

        int HuffmanCount = 0;
        int bit7Count = 0;
        // writing the bit strings for the first N characters of the input file
        for (int i = 0; i < firstNChars.size(); i++) {
            bit7Count += 7;
            int index = firstNChars.get(i) - 'a';
            HuffmanCount += Integer.valueOf(bitStrings.get(index));

            String s = bitStrings.get(index) + " " + HuffmanCount + " " + bit7Count;
            try {
                writer.write(s);
            } catch (IOException e) {
                System.out.println("Failure to write.");
            }
        }

        try {
            // After writing the output
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error closing or flushing the output file.");
        }

    }


    // reads file
    void readFile(int N) {
        // Read file and create the array of frequencies
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/merchant.txt"))) {
            // Create the frequency array, but where position indicates the character
            ArrayList<Integer> freqArr = new ArrayList<>(27);

            // initialize the array
            for (int i = 0; i < 27; i++) {
                freqArr.add(i, 0);
            }

            int uniChar; // what is reader by reader.read()
            int count = 0;
            while (count <= N) { // -1 indicates the EOF
                if (reader.ready()) {
                    uniChar = reader.read();
                    char c = (char) uniChar;
                    if ((c < 'a' || c > 'z') && c != ' ') {
                        continue; // skipping non-letter, non-space characters
                    }

                    if (c == ' ') {
                        freqArr.add(26, freqArr.get(26) + 1);
                        firstNChars.add(Character.valueOf(c));
                    } else {
                        int index = (c - 'a');
                        firstNChars.add(Character.valueOf(c));
                        freqArr.add(index, freqArr.get(index) + 1);
                    }
                    count++;
                }
            }
            // after the array of frequencies are made
            // call the data structure (i.e. priorityQueue)
            pq = new priorityQueue(freqArr);

        } catch (IOException e) {
            System.out.println("Error with reading input file's characters.");
        }
    }

    // the constructor for the class SAANPA4
    public SAANPA4(int N) {
        try {
            // creating the buffered writer and output file, with FileWriter append flag set to true
            BufferedWriter writer = new BufferedWriter(new FileWriter("resources/output.txt", true));

            // call the functions of SAANPA4
            readFile(N);
            // call the writer
            writeFile(writer);
        } catch (IOException e) {
            System.out.println("Output file not able to be written to.");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a positive integer for the number of characters to be encoded: ");
        int N = input.nextInt();

        // call SAANPA4 class constructor
        SAANPA4 read = new SAANPA4(N);
    }
}
