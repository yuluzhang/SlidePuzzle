import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class Solver {
    private class SearchNode {
        private final SearchNode previous;
        private final int move;
        private final Board current;

        public SearchNode(SearchNode prev, int m, Board cur) {
            current = cur;
            previous = prev;
            move = m;
        }
    }

    private class HammingComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode s1, SearchNode s2) {
            int d1 = s1.current.hamming() + s1.move;
            int d2 = s2.current.hamming() + s2.move;
            return Integer.compare(d1, d2);
        }
    }

    private class ManhattanComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode s1, SearchNode s2) {
            int d1 = s1.current.manhattan() + s1.move;
            int d2 = s2.current.manhattan() + s2.move;
            return Integer.compare(d1, d2);
        }
    }

    private final Board start;
    private final List<SearchNode> result = new ArrayList<>();
    private int step = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        start = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (start.isGoal()) return false;
        return true;
    }

    // min number of moves to solve initial board
    public int moves() {
        MinPQ<SearchNode> pq = new MinPQ<>(new ManhattanComparator());
        SearchNode ini = new SearchNode(null, 0, start);
        pq.insert(ini);
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            for (Board bd : cur.current.neighbors()) {
                if (!result.contains(bd)){
                   if(cur.previous!=null) {
                       if (!bd.equals(cur.previous.current)) pq.insert(new SearchNode(cur, cur.move + 1, bd));
                   } else pq.insert(new SearchNode(cur, cur.move + 1, bd));
                }
            }
            step = Math.max(step, cur.move);
            result.add(cur);
            if (cur.current.isGoal()) break;
        }
        return step;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (step == 0) {
            moves();
        }
        List<Board> res = new ArrayList<>();
        SearchNode last = result.get(result.size()-1);
        while(last != null){
            res.add(last.current);
            last = last.previous;
        }
        Collections.reverse(res);
        return res;
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
