import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class SAANPA1 {
    public class circularLinkedList {
        Node head; // creating the header node
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
            Node rowIterator = rowHeaders[i];
            Node columnIterator = columnHeaders[j];

            while (rowIterator.columnIndex < j) {
                rowIterator = rowIterator.nextRight;
            }
            while (columnIterator.rowIndex < i){
                columnIterator = columnIterator.nextDown;
            }

            // These cover the nodes that are added between existing nodes or after existing nodes
            new_Node.nextRight = rowIterator.nextRight; // if its the last node in the row then it links to the null or the row header (the null is handled below in the if statements)
            rowIterator.nextRight = new_Node;

            new_Node.nextDown = columnIterator.nextDown;
            columnIterator.nextDown = new_Node;

            if (new_Node.nextRight == null && new_Node.nextDown == null) {
                new_Node.nextRight = rowHeaders[i];
                new_Node.nextDown = columnHeaders[j];
            } else if (new_Node.nextRight == null) {
                new_Node.nextRight = rowHeaders[i];
            } else if (new_Node.nextDown == null) {
                new_Node.nextDown = columnHeaders[j];
            } // for the first nodes in a row and/or column, linking the node's next to the header

            return list;
        }

        public Node rowIterate(int i, int j) {
            Node iterator = rowHeaders[i];

            int column = 1;
            while (iterator.columnIndex != j || column <= n) {
                    iterator = iterator.nextRight;
            }

            return iterator;
        }
        public Node colIterate(int i, int j) {
            Node iterator = columnHeaders[j];

            int row = 1;
            while (iterator.rowIndex != i || row <= n) {
                iterator = iterator.nextDown;
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

    public class SparseMatrix {
        Scanner reader;
        int n;

        public SparseMatrix(String inputFile) {
            try {
                File reading = new File(inputFile);
                Scanner reader = new Scanner(reading);

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

            if (reader.nextLine() != ",,") {
                int i = Integer.parseInt(reader.next()); // get row index
                reader.next(); // skip ,
                int j = Integer.parseInt(reader.next()); // get column index
                reader.next(); // skip ,
                int value = Integer.parseInt(reader.next());

                matrix1.add(matrix1, i, j, value);

                while (reader.nextLine() != ",,") {
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
            if (operationType == "A" || operationType == "M") {
                // implement the matrix two
                circularLinkedList matrix2 = new circularLinkedList(n);

                if (reader.hasNextLine() || reader.nextLine() != " ") {
                    int i = Integer.parseInt(reader.next()); // get row index
                    reader.next(); // skip ,
                    int j = Integer.parseInt(reader.next()); // get column index
                    reader.next(); // skip ,
                    int value = Integer.parseInt(reader.next());

                    matrix2.add(matrix2, i, j, value);

                    while (reader.hasNextLine() || reader.nextLine() != " ") {
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
            } else if (operationType == "S") {
                int scalar = 0;
                while (reader.next() !=",") {
                    scalar = Integer.parseInt(reader.next());
                }
                scalarMultiplication(matrix1, scalar);
            } else {
                matrixTransposition(matrix1);
            }
        }

        public circularLinkedList matrixAddition(circularLinkedList matrix1, circularLinkedList matrix2) {
            circularLinkedList finalMatrix = new circularLinkedList(n);
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
            circularLinkedList finalMatrix = new circularLinkedList(n);
            int v = 0;

            for (int row = 1; row <= n; row++) {
                for (int j = 1; j <= n; j++) {
                    for (int i = 1; i <= n; i++) {
                        if ((matrix1.rowIterate(i, j).columnIndex == j && matrix2.rowIterate(i, j).columnIndex == j) &&
                                (matrix1.colIterate(i, j).rowIndex == i && matrix2.colIterate(i, j).rowIndex == i)) {
                            v = matrix1.rowIterate(row, i).data * matrix2.rowIterate(i, j).data;

                            v += v;
                        }
                    }
                    finalMatrix.add(finalMatrix, row, j, v);
                }
            }

            return finalMatrix;
        }

        public circularLinkedList matrixTransposition(circularLinkedList matrix1) {
            circularLinkedList finalMatrix = new circularLinkedList(n);

            return finalMatrix;
        }

        public circularLinkedList scalarMultiplication(circularLinkedList matrix1, int scalar) {
            circularLinkedList finalMatrix = new circularLinkedList(n);

            return finalMatrix;
        }
    }

    public void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a file name: ");
        String inputFile = input.next();

        SparseMatrix sparseMatrix = new SparseMatrix(inputFile);
        // write into output here sparseMatrix.write();
    }
}