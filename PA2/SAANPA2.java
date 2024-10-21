// This is Sawyer Anderson's Programming Assignment 2:
// Implementing the improved "monkey simulation" using binary search tree for the map

// includes
import java.io.*;
import java.util.Scanner;

// main and the constructor are at the bottom of the class
public class SAANPA2 {
    // Some global variables and the map
    BST<String> map;
    String firstK = "";

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

    // the map: binary search tree
    // the tree node:
    class TreeNode<E> {
        protected E key;
        protected TreeNode<E> left;
        protected TreeNode<E> right;
        protected CharDistribution charDistribution;

        public TreeNode(E k) {
            key = k;
            charDistribution = new CharDistribution();
        }
    }
    // the functions
    public class BST<E extends Comparable<E>> {
        protected TreeNode<E> root;
        protected int size = 0;

        public BST() { }

        public int size() {
            return size;
        }

        public boolean empty() {
            if (size == 0) {
                return true;
            } else {
                return false;
            }
        }

        // finding the char distribution and returning it, else a null is returned
        public CharDistribution find(E k) {
            CharDistribution dis = null;
            TreeNode<E> current = root;
            TreeNode<E> parent = null;

            while (current != null) {
                if (k.compareTo(current.key) < 0) {
                    parent = current;
                    current = current.left;
                } else if (k.compareTo(current.key) > 0) {
                    parent = current;
                    current = current.right;
                } else if (k.compareTo(current.key) == 0) {
                    dis = current.charDistribution;
                    return dis;
                }
            }

            return dis;
        }

        // creating or updating a node with a CharDistribution
        // and incrementing for the character c of the CharDistribution
        // in the node with key sequence k
        public void insert(E k, char c) {
            if (empty()) {
                root = new TreeNode<E>(k);
                root.charDistribution.occurs(c); // incrementing the counter for character c for the sequence k
                size++;
            } else {
                TreeNode<E> parent = null;
                TreeNode<E> current = root;

                // if it already exists
                while (current != null) {
                    if (k.compareTo(current.key) < 0) {
                        parent = current;
                        current = current.left;
                    } else if (k.compareTo(current.key) > 0) {
                        parent = current;
                        current = current.right;
                    } else if (k.compareTo(current.key) == 0) {
                        current.charDistribution.occurs(c); // incrementing the counter for character
                        return;
                    }
                }

                if (k.compareTo(parent.key) < 0) {
                    parent.left = new TreeNode<E>(k);
                    parent.left.charDistribution.occurs(c); // incrementing the counter for character
                } else {
                    parent.right = new TreeNode<E>(k);
                    parent.right.charDistribution.occurs(c); // incrementing the counter for character
                }
                size++;
            }
        }

        public void remove(E k) {
            TreeNode<E> current = root;
            TreeNode<E> parent = null;

            // Find the node to delete
            while (current != null && !current.key.equals(k)) {
                parent = current;
                if (k.compareTo(current.key) < 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }

            // If the node to be deleted was not found
            if (current == null) {
                return; // Key not found, nothing to remove
            }

            // Case 1: Node has no children
            if (current.left == null && current.right == null) {
                if (current == root) {
                    root = null; // Tree becomes empty
                } else if (parent.left == current) {
                    parent.left = null; // Remove from parent's left
                } else {
                    parent.right = null; // Remove from parent's right
                }
            }
            // Case 2: Node has one child
            else if (current.left == null) {
                if (current == root) {
                    root = current.right; // Replace root
                } else if (parent.left == current) {
                    parent.left = current.right; // Replace parent's left
                } else {
                    parent.right = current.right; // Replace parent's right
                }
            } else if (current.right == null) {
                if (current == root) {
                    root = current.left; // Replace root
                } else if (parent.left == current) {
                    parent.left = current.left; // Replace parent's left
                } else {
                    parent.right = current.left; // Replace parent's right
                }
            }
            // Case 3: Node has two children
            else {
                // Find the inorder successor (smallest in the right subtree)
                TreeNode<E> successor = current.right;
                TreeNode<E> successorParent = current;

                while (successor.left != null) {
                    successorParent = successor;
                    successor = successor.left;
                }

                // Replace current's key with successor's key
                current.key = successor.key;

                // Remove the successor
                if (successorParent.left == successor) {
                    successorParent.left = successor.right; // Bypass successor
                } else {
                    successorParent.right = successor.right; // Bypass successor
                }
            }
        }
    }
    // end of BST implementation

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

    // the constructor for the class SAANPA2
    public SAANPA2 (int w, int length) {
        // intialize the map
        map = new BST<String>();

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

        // call SAANPA2 class constructor
        SAANPA2 read = new SAANPA2(w, length);
    }
}