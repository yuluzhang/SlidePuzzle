import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

public class Solver {
    private static class SearchNode {
        private final SearchNode previous;
        private final int move;
        private final Board current;
        private final int manhattan;

        public SearchNode(SearchNode prev, int m, Board cur) {
            current = cur;
            previous = prev;
            move = m;
            manhattan = cur.manhattan();
        }
    }

    private static class StarComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode a, SearchNode b) {
            int val =  a.move + a.manhattan - b.move - b.manhattan;
            if ( val == 0)
                return a.manhattan - b.manhattan;
            return val;
        }
    }
//    private static class ManComparator implements Comparator<SearchNode> {
//        @Override
//        public int compare(SearchNode a, SearchNode b) {
//            int val = a.move + a.manhattan - b.move -b.manhattan;
//            if ( val == 0)
//                return a.manhattan - b.manhattan;
//            return val;
//        }
//    }


    private final Board start;
    private int step = -1;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        start = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        Board twin = start.twin();
        MinPQ<SearchNode> pq = new MinPQ<>(new StarComparator());
        pq.insert(new SearchNode(null, 0, twin));
        pq.insert(new SearchNode(null, 0, start));
        SearchNode last = null;
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            if (cur.current.isGoal()) {
                last = cur;
                break;
            }
            boolean found = false;
            for (Board bd : cur.current.neighbors()) {
                if ((cur.previous != null && bd.equals(cur.previous.current))) continue;
                if (bd.isGoal()) {
                    last = new SearchNode(cur, cur.move + 1, bd);
                    found = true;
                    break;
                }
                pq.insert(new SearchNode(cur, cur.move + 1, bd));
            }
            if (found) break;
        }
        while (last.previous != null) {
            last = last.previous;
        }
        return last.current.equals(start);
    }


    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) {
            calculate();
        }
        return step;
    }

    private SearchNode calculate() {
        SearchNode last = null;
        MinPQ<SearchNode> pq = new MinPQ<>(new StarComparator());
        SearchNode ini = new SearchNode(null, 0, start);
        pq.insert(ini);
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            step = Math.max(step, cur.move);
            if (cur.current.isGoal()) {
                last = cur;
                break;
            }
            boolean found = false;
            for (Board bd : cur.current.neighbors()) {
                if (cur.previous != null && bd.equals(cur.previous.current)) continue;
                SearchNode sn = new SearchNode(cur, cur.move + 1, bd);
                if (bd.isGoal()) {
                    found = true;
                    step = Math.max(step, sn.move);
                    last = sn;
                    break;
                }
                pq.insert(sn);
            }
            if (found) break;
        }
        return last;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        } else {
            Deque<Board> dq = new ArrayDeque<>();
            SearchNode last = calculate();
            while (last != null) {
                dq.addFirst(last.current);
                last = last.previous;
            }
            return dq;
        }

    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        //int[][] tiles = {{2, 4, 3}, {0, 7, 6}, {5, 1, 8}};
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
