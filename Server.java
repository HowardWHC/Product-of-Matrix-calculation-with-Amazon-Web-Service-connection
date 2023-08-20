package a2223.hw1.student;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Multi-threaded server program that multiplies matrices using fork-join
 * framework.
 *
 * @author vanting
 */
public class Server implements Runnable {

    public static final int DEFAULT_PORT = 42210;
    private Socket socket;

    public server() {
        // 
    }

    public Server(Socket socket) {
        this.socket = socket;
    }
    
    /**
     * Driver function. Start this server at the default port.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        start(DEFAULT_PORT);
    }

    /**
     * Start matrix server at the specified port. It should accept and handle
     * multiple client requests concurrently.
     *
     * @param port port number listened by the server
     */
    public static void start(int port) throws IOException, ClassNotFoundException{
        

        // your implementation here
        
        // 1. accept a new connection from client
        // 2. create a task with the socket
        // 3. submit the task to a thread pool to execute
        
        
        //Socket socket = new Socket(port);
        ServerSocket socket = new ServerSocket(port);
        boolean listening = true;
        Socket server = socket.accept();
        
        // 2. create a task with the socket
        Matrix m3 = new Matrix();
        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //while(listening){
            //Socket server = socket.accept();
        //}
        Executor ex = Executors.newSingleThreadExecutor();
        ex.execute(new Server(server));
    
	
		
    }

    /**
     * Handle a matrix client request. It reads two matrices from socket,
     * compute their product, and then send the product matrix back to the
     * client.
     */
    @Override
    public void run() {

        // your implementation here
        
        // 1. read two matrices from the socket
        // 2. call multiThreadMultiply() to compute their product
        // 3. send back the product matrix 
        
        ObjectInputStream is = null; //Inputstream cannot convert to Inputstream
        ObjectOutputStream os = null;
        InputStream in = null;
        try{
            is = new ObjectInputStream(socket.getInputStream());
            Matrix matA = (Matrix) is.readObject();
            Matrix matB = (Matrix) is.readObject();
            
            Matrix product = multiThreadMultiply(matA, matB);
            os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(product);
            socket.close();
        } catch(IOException e){
            System.err.println("IOException error");
            throw new RuntimeException(e);
        } catch(ClassNotFoundException e){
            System.err.println("ClassNotFoundException error");
            throw new RuntimeException(e);
        }
        
    }
   
    /**
     * Compute A x B using fork-join framework.
     * @param matA  matrix A
     * @param matB  matrix B
     * @return      the matrix product of AxB
     */
    public static Matrix multiThreadMultiply(Matrix matA, Matrix matB) {
        
        
        long[][] product;
        product = new long[matA.row()][matB.col()];
        
        //product = new long[matA.row()][matB.col()];

        // your implementation here
        
        // 1. create a fork-join task (parallelMultiply)
        // 2. submit the task to a fork-join pool
        //Matrix m3 = new Matrix(2, 2);
        ForkJoinPool pool = new ForkJoinPool();
        ParallelMultiply task = new ParallelMultiply(product, matA, matB, 0, matA.row()-1);
        //product = pool.invoke(new ParallelMultiply(m1, m2, m3));
        pool.invoke(task);
        Matrix result = new Matrix(product);
        return result;
    }
}

/**
 * Design a recursive and resultless ForkJoinTask. It splits the matrix multiplication 
 * into multiple tasks to be executed in parallel.
 * 
 */
class ParallelMultiply extends RecursiveAction {

    // your implementation here
    private static final int THRESHOLD = 4;
    private Matrix m1, m2;
    private long[][] product;
    private int start, end;
    //private final int row;
    //private final int column;
    
    public ParallelMultiply(long[][] output, Matrix matA, Matrix matB, int s, int e){
        product = output;
        m1 = matA;
        m2 = matB;
        start = s;
        end = e;
    }
    
    @Override
    protected void compute() {
        //long[][] m3 = new long[4][4];
        //product = m1.multiply(m2);
        //ParallelMultiply t1 = new ParallelMultiply(m3, m1, m2);
        if (start == end){
            long num = 0;
            for (int i = 0; i < m2.col(); i++){
                for (int j = 0; j < m1.col(); j++){
                    num = num + m1.at(start, j) * m2.at(j, i);
                }
                product[start][i] = num;
                num = 0;
            }
        } else{
            int mid = (start + end)/2;
            ParallelMultiply t1 = new ParallelMultiply(product, m1, m2, start, mid);
            ParallelMultiply t2 = new ParallelMultiply(product, m1, m2, mid+1, end);
            
            t1.invoke();
            t2.invoke();
            //t3.invoke();
            //t4.invoke();
        }
    }

}
