package a2223.hw1.student;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


// A client program that send two matrices to a server to compute their product.

public class Client {

    public static final String DEFAULT_HOST = "3.235.93.184";
    public static final int DEFAULT_PORT = 42210;
    
    /**
     * Driver function.
     */
    public static void main(String[] args) throws IOException, UnknownHostException, ClassNotFoundException{
        
        // get host from command line argument
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;

        // Create two matrices with random values
        Matrix matA = new Matrix(1000, 1200, 100);
        Matrix matB = new Matrix(1200, 1500, 100);
        
        // We request the server to compute the product first
        long start = System.currentTimeMillis();
        Matrix multiProduct = remoteMultiply(host, DEFAULT_PORT, matA, matB);
        double multiTime = System.currentTimeMillis() - start;
        
        // Then we compute the product locally using the built-in mutliply() method
        start = System.currentTimeMillis();
        Matrix singleProduct = matA.multiply(matB);
        double singleTime = System.currentTimeMillis() - start;

        // Finally, we check if they are the same and their runtime ratio.
        if (singleProduct.equals(multiProduct)) {
            System.out.println("The computation is correct.");
            System.out.println("Single-to-Multi Ratio: " + singleTime/multiTime);
        } else {
            System.out.println("The computation is NOT correct.");
        }
    }

    /**
     * Connect to the matrix server at the specified host and port, and then
     * request the server to compute the matrix product of AxB.
     *
     * @param host IP address of the server
     * @param port port number used by the server
     * @param matA matrix A
     * @param matB matrix B
     * @return the matrix product of AxB
     */
    public static Matrix remoteMultiply(String host, int port, Matrix matA, Matrix matB) throws IOException, UnknownHostException, ClassNotFoundException{

        Matrix product = null;
        
        
        // your implementation here
        
        // 1. connect to server 
        // 2. send out two matrices via the socket
        // 3. read the product matrix replied by server
        
        Socket socket = new Socket(host, port);
        //PrintWriter out = null;
        //BufferedReader in = null;
        ObjectInputStream is = null;
        ObjectOutputStream os = null;
        
        os = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("sending out two matrices");
        os.writeObject(matA);
        os.writeObject(matB);
        System.out.println("sending out done");
        System.out.println("Waiting for the server response:");
        
        is = new ObjectInputStream(socket.getInputStream());
        product = (Matrix)is.readObject();
        
        /*try{
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream());
        } catch(UnknownHostException e){
            System.err.println("Don't know about this server");
            System.exit(1);
        } catch(IOException e){
            System.err.print("Couldn't get I/O for the connection to server");
            System.exit(1);
        }*/
       //socket = new Socket(host, port);
       BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
       String serverInput;
       
       //out.println(matA);
       //out.println(matB);
       //ObjectOutputStream(matA);
       
       /*try{
           ois = new ObjectInputStream(socket.getInputStream());
           
       } catch(IOException e){
            try {
                System.err.println("Couldn't get I/O for the connection from server");
                Matrix message = (Matrix) ois.readObject();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
       }*/
       
        //out.close();
        //in.close();
        
        is.close();
        os.close();
        socket.close();
        return product;
    }

}

//------------------------------------------------------------------------------------------------------

/**
 * Immutable Integer Matrix. Do not modify this class.
 * 
 * @author vanting
 */
class Matrix implements Serializable {

    private long[][] nums;

    public Matrix() {
        this(1, 1);
    }

    public Matrix(int row, int col) {
        nums = new long[row][col];
    }

    // Initialize this matrix with random numbers between 0 to n (exclusive)
    public Matrix(int row, int col, int n) {
        this(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                nums[i][j] = (int) (Math.random() * n);
            }
        }
    }

    public Matrix(long[][] n) {
        this(n.length, n[0].length);
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[0].length; j++) {
                nums[i][j] = n[i][j];
            }
        }
    }

    // Copy constructor
    public Matrix(Matrix other) {
        this(other.row(), other.col());
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums[0].length; j++) {
                nums[i][j] = other.at(i, j);
            }
        }
    }

    //-----------------------------------------------------------------
    
    public long at(int row, int col) {
        return nums[row][col];
    }

    public int row() {
        return nums.length;
    }

    public int col() {
        return nums[0].length;
    }

    // Return the product of this multiplying other; return null on fail
    public Matrix multiply(Matrix other) {

        int row1 = this.nums.length;
        int col1 = this.nums[0].length;
        int row2 = other.nums.length;
        int col2 = other.nums[0].length;

        if (col1 == row2) {

            long sum = 0;
            long[][] product = new long[row1][col2];

            for (int i = 0; i < row1; i++) {
                for (int j = 0; j < col2; j++) {
                    for (int k = 0; k < col1; k++) {
                        sum = sum + nums[i][k] * other.nums[k][j];
                    }
                    product[i][j] = sum;
                    sum = 0;
                }
            }
            return new Matrix(product);
        } else {
            return null;
        }
    }

    public void print() {
        System.out.println(this);
    }

    // Return a string representation of this matrix
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        int row = this.nums.length;
        int col = this.nums[0].length;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                fmt.format("[%4d]", this.nums[i][j]);
            }
            fmt.format("%n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (o != null && o instanceof Matrix) {
            Matrix other = (Matrix) o;

            if (nums.length != other.row()) {
                return false;
            }

            if (nums[0].length != other.col()) {
                return false;
            }

            for (int i = 0; i < nums.length; i++) {
                for (int j = 0; j < nums[0].length; j++) {
                    if (nums[i][j] != other.at(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Arrays.deepHashCode(this.nums);
        return hash;
    }

}
