import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Solver {
    private class SearchNode {
        private final Board previous;
        private final int move;
        private final Board current;

        public SearchNode(Board prev, int m, Board cur) {
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
    private final List<Board> result = new ArrayList<>();

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
                if (!result.contains(bd) && !bd.equals(cur.previous)) {
                    pq.insert(new SearchNode(cur.current, cur.move + 1, bd));
                }
            }
            result.add(cur.current);
            if (cur.current.isGoal()) break;
        }
        return result.size() - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (result.isEmpty()) {
            moves();
        }
        return result;
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
