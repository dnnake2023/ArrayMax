import java.io.*;

//whenever you have a new value assigned to an array, you can put the new value in the array, and then you can swim or sink to sort the array




import java.util.StringTokenizer;
import java.util.NoSuchElementException;
//import java.util.PriorityQueue;


public class IndexMaxPQ<Key extends Comparable<Key>> {
    private int maxN;        // maximum number of elements on PQ
    private int n;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = priority of i



    public IndexMaxPQ(int maxN) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        n = 0;
        keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
        pq   = new int[maxN + 1];
        qp   = new int[maxN + 1];                   // make this of length maxN??
        for (int i = 0; i <= maxN; i++)
            qp[i] = -1;
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Is {@code i} an index on this priority queue?
     *
     * @param  i an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public boolean contains(int i) {
        //validateIndex(i);
        return qp[i] != -1;
    }


    public void insert(int i, Key key) {

        if (contains(i)){
            changeKey(i, key);
            return;
        }

        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }



  public Key maxKey() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return keys[pq[1]];
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public void changeKey(int i, Key key) {
       // validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }



    // throw an IllegalArgumentException if i is an invalid index
   // private void validateIndex(int i) {
     //   if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
     //   if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
   // }

   /***************************************************************************
    * General helper functions.
    ***************************************************************************/
    private boolean less(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }


   /***************************************************************************
    * Heap helper functions.
    ***************************************************************************/
    private void swim(int k) {
        while (k > 1 && less(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && less(j, j+1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    static class Entry implements Comparable<Entry> {
        int i, v;
        Entry(int i, int v) { this.i=i; this.v=v; }
        // We negate the "v" comparison, so that PriorityQueue (a MinPQ)
        // returns the Entry with the maximum v.  We break ties with i,
        // so we can find the leftmost appearance of the maximum.
        public int compareTo(Entry that) {
            int dif = -(this.v - that.v);
            if (dif==0) // break ties with the index i
                dif = this.i - that.i;
            return dif;
        }
    }


    public static void main(String[] args) throws IOException
    {
        // Buffered output (for faster printing)
        PrintWriter out = new PrintWriter(System.out);
        // Buffered input (we also avoid java.util.Scanner)
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int M = Integer.parseInt(st.nextToken()),
            N = Integer.parseInt(st.nextToken());
        IndexMaxPQ<Entry> pq = new IndexMaxPQ<Entry>(M);
        for(int k = 0; k < M; ++k){
            pq.insert(k, new Entry(k, 0));
        }

        for (int n=0; n<N; ++n) {
            // read the line, parse i and v
            st = new StringTokenizer(br.readLine());
            int m = Integer.parseInt(st.nextToken()),
                z = Integer.parseInt(st.nextToken());
            // do the assignment in the array
            pq.insert(m, new Entry(m, z));
            Entry head = pq.maxKey();
            out.println(head.i);
            }
        out.close();
    }
}
