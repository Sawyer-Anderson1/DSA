import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class SAANPA1 {
    public static class circularLinkedList {
        Node[] rowHeaders;
        Node[] columnHeaders;
        int n;

        circularLinkedList() { } // default constructor
        circularLinkedList(int n) {
            this.n = n;
            Node head = new Node(0, 0, 0); // creating the header node
            Node[] rowHeaders = new Node[n];
            Node[] columnHeaders = new Node[n];

            for (int i = 1; i <= n; i++) {
                rowHeaders[i] = new Node(i, 0, 0);
            }
            for (int j = 1; j <= n; j++) {
                columnHeaders[j] = new Node(0, j, 0);
            }
            rowHeaders[n].nextDown = head;
            columnHeaders[n].nextRight = head;
        }

        public circularLinkedList add(circularLinkedList list, int i, int j, int x) {
            Node new_Node = new Node(i,j,x);
            Node rowIterator = rowHeaders[i - 1];
            Node columnIterator = columnHeaders[j - 1];

            while (rowIterator.columnIndex < j - 1) {
                rowIterator = rowIterator.nextRight;
            }
            while (columnIterator.rowIndex < i - 1){
                columnIterator = columnIterator.nextDown;
            }

            // These cover the nodes that are added between existing nodes or after existing nodes
            new_Node.nextRight = rowIterator.nextRight; // if its the last node in the row then it links to the null or the row header (the null is handled below in the if statements)
            rowIterator.nextRight = new_Node;

            new_Node.nextDown = columnIterator.nextDown;
            columnIterator.nextDown = new_Node;

            if (new_Node.nextRight == null && new_Node.nextDown == null) {
                new_Node.nextRight = rowHeaders[i - 1];
                new_Node.nextDown = columnHeaders[j - 1];
            } else if (new_Node.nextRight == null) {
                new_Node.nextRight = rowHeaders[i - 1];
            } else if (new_Node.nextDown == null) {
                new_Node.nextDown = columnHeaders[j - 1];
            } // for the first nodes in a row and/or column, linking the node's next to the header

            return list;
        }

        public Node rowIterate(int i, int j) {
            Node iterator = rowHeaders[i - 1];

            int column = 1;
            while (iterator.columnIndex != j || column <= j - 1) {
                iterator = iterator.nextRight;
                column++;
            }

            return iterator;
        }
        public Node colIterate(int i, int j) {
            Node iterator = columnHeaders[j - 1];

            int row = 1;
            while (iterator.rowIndex != i || row < j) {
                iterator = iterator.nextDown;
                row++;
            }

            return iterator;
        }
    }

    static class Node {
        int data;
        int rowIndex;
        int columnIndex;
        Node nextRight;
        Node nextDown;

        public Node (int i, int j, int x) {
            data = x;
            rowIndex = i;
            columnIndex = j;
            nextRight = null;
            nextDown = null;
        }
    }

    public static class SparseMatrix {
        circularLinkedList finalMatrix;
        Scanner reader;
        int n;

        public SparseMatrix() {}
        public SparseMatrix(String inputFile) {
            try {
                File reading = new File(inputFile);
                reader = new Scanner(reading);

                readInFile();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        public void readInFile(){
            String operationType = reader.next();
            reader.next(); // skip ,
            n = Integer.parseInt(reader.next());
            reader.next(); // skip ,
            reader.nextLine();
            reader.nextLine(); // skip ,,

            // implement the matrix one
            circularLinkedList matrix1 = new circularLinkedList(n);

            if (!reader.nextLine().equals(",,")) {
                int i = Integer.parseInt(reader.next()); // get row index
                reader.next(); // skip ,
                int j = Integer.parseInt(reader.next()); // get column index
                reader.next(); // skip ,
                int value = Integer.parseInt(reader.next());

                matrix1.add(matrix1, i, j, value);

                while (!reader.nextLine().equals(",,")) {
                    i = Integer.parseInt(reader.next()); // get row index
                    reader.next(); // skip ,
                    j = Integer.parseInt(reader.next()); // get column index
                    reader.next(); // skip ,
                    value = Integer.parseInt(reader.next());

                    // probably call a method here to go implement into list
                    matrix1.add(matrix1, i, j, value);
                }
            } else {
                // handle if there were no values in a matrix
            }
            if (operationType.equals("A") || operationType.equals("M")) {
                // implement the matrix two
                circularLinkedList matrix2 = new circularLinkedList(n);

                if (reader.hasNextLine() || !reader.nextLine().equals(" ")) {
                    int i = Integer.parseInt(reader.next()); // get row index
                    reader.next(); // skip ,
                    int j = Integer.parseInt(reader.next()); // get column index
                    reader.next(); // skip ,
                    int value = Integer.parseInt(reader.next());

                    matrix2.add(matrix2, i, j, value);

                    while (reader.hasNextLine()) {
                        i = Integer.parseInt(reader.next()); // get row index
                        reader.next(); // skip ,
                        j = Integer.parseInt(reader.next()); // get column index
                        reader.next(); // skip ,
                        value = Integer.parseInt(reader.next());

                        // probably call a method here to go implement into list
                        matrix2.add(matrix2, i, j, value);
                    }
                } else {
                    // handle if there were no values in a matrix
                }

                switch (operationType) {
                    case "A":
                        matrixAddition(matrix1, matrix2);
                        break;
                    case "M":
                        matrixMultiplication(matrix1, matrix2);
                        break;
                }
            } else if (operationType.equals("S")) {
                int scalar = 0;
                while (reader.next().equals(",")) {
                    scalar = reader.nextInt();
                }
                scalarMultiplication(matrix1, scalar);
            } else if (operationType.equals("T")){
                matrixTransposition(matrix1);
            }

            reader.close();
        }

        public circularLinkedList matrixAddition(circularLinkedList matrix1, circularLinkedList matrix2) {
            finalMatrix = new circularLinkedList(n);
            int x;

            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if ((matrix1.rowIterate(i, j).columnIndex == j && matrix2.rowIterate(i, j).columnIndex == j) &&
                            (matrix1.colIterate(i, j).rowIndex == i && matrix2.colIterate(i, j).rowIndex == i)) {
                        x = matrix1.rowIterate(i, j).data + matrix2.rowIterate(i, j).data;
                    } else if (matrix1.rowIterate(i, j).columnIndex == j && matrix1.rowIterate(i, j).rowIndex == i) {
                        x = matrix1.rowIterate(i, j).data;
                    } else if (matrix2.rowIterate(i, j).columnIndex == j && matrix2.rowIterate(i, j).rowIndex == i){
                        x = matrix2.rowIterate(i, j).data;
                    } else {
                        x = 0;
                    }
                    finalMatrix.add(finalMatrix, i, j, x);
                }
            }

            return finalMatrix;
        }

        public circularLinkedList matrixMultiplication(circularLinkedList matrix1, circularLinkedList matrix2) {
            finalMatrix = new circularLinkedList(n);
            int v = 0;

            for (int row = 1; row <= n; row++) {
                for (int j = 1; j <= n; j++) {
                    for (int i = 1; i <= n; i++) {
                        if ((matrix1.rowIterate(i, j).columnIndex == j && matrix2.rowIterate(i, j).columnIndex == j) &&
                                (matrix1.colIterate(i, j).rowIndex == i && matrix2.colIterate(i, j).rowIndex == i)) {
                            v = matrix1.rowIterate(row, i).data * matrix2.rowIterate(i, j).data;

                            v += v;
                        } else {
                            v = 0;
                        }
                    }
                    finalMatrix.add(finalMatrix, row, j, v);
                }
            }

            return finalMatrix;
        }

        public circularLinkedList matrixTransposition(circularLinkedList matrix1) {
            finalMatrix = new circularLinkedList(n);
            int x = 0;

            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (matrix1.rowIterate(i, j).columnIndex == j && matrix1.colIterate(i, j).rowIndex == i) {
                        x = matrix1.rowIterate(i, j).data;
                    } else {
                        x = 0;
                    }
                    finalMatrix.add(finalMatrix, j, i, x);
                }
            }
            return finalMatrix;
        }

        public circularLinkedList scalarMultiplication(circularLinkedList matrix1, int scalar) {
            finalMatrix = new circularLinkedList(n);
            int x = 0;

            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (matrix1.rowIterate(i, j).columnIndex == j && matrix1.colIterate(i, j).rowIndex == i) {
                        x = matrix1.rowIterate(i, j).data * scalar;
                    } else {
                        x = 0;
                    }
                    finalMatrix.add(finalMatrix, i, j, x);
                }
            }
            return finalMatrix;
        }

        public void write(String outputFile) {
            try {
                File outputF = new File(outputFile);
                PrintWriter output = new PrintWriter(outputFile);
                outputF.createNewFile();

                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        if (finalMatrix.rowIterate(i, j).columnIndex == j && finalMatrix.colIterate(i, j).rowIndex == i) {
                            if (finalMatrix.rowIterate(i, j).data != 0) {
                                output.println(i + "," + j + "," + finalMatrix.rowIterate(i, j).data);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.print("Error");
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a file name: ");
        String inputFile = input.next();

        SparseMatrix sparseMatrix = new SparseMatrix(inputFile);

        String outputFile = "";
        for (int cIndex = 0; cIndex < inputFile.length(); cIndex++) {
            if (cIndex < (inputFile.length() - 1) && String.valueOf(inputFile.charAt(cIndex + 1)).equals(".")) {
                outputFile = outputFile.concat(String.valueOf(inputFile.charAt(cIndex)));
                outputFile = outputFile.concat("_output");
            } else {
                outputFile = outputFile.concat(String.valueOf(inputFile.charAt(cIndex)));
            }
        }

        sparseMatrix.write(outputFile);
    }
}
