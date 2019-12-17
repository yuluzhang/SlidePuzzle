import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

public class Solver {
    private class SearchNode {
        private final SearchNode previous;
        private final int move;
        private final Board current;
        private int manhattan;
        private int hamming;

        public SearchNode(SearchNode prev, int m, Board cur) {
            current = cur;
            previous = prev;
            move = m;
            manhattan = cur.manhattan();
            hamming = cur.hamming();
        }
    }

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
        MinPQ<SearchNode> pq = new MinPQ<>(new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode a, SearchNode b) {
                if (2 * a.move + a.hamming + a.manhattan - 2 * b.move - b.hamming - b.manhattan == 0)
                    return a.hamming - b.hamming;
                return 2 * a.move + a.hamming + a.manhattan - 2 * b.move - b.hamming - b.manhattan;
            }
        }
        );
        //MinPQ<SearchNode> pq = new MinPQ<>((a, b) -> (2 * a.move + a.hamming + a.manhattan - 2 * b.move - b.hamming - b.manhattan));
        pq.insert(new SearchNode(null, 0, twin));
        pq.insert(new SearchNode(null, 0, start));
        SearchNode last = null;
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            boolean found = false;
            for (Board bd : cur.current.neighbors()) {
                if ((cur.previous != null && bd.equals(cur.previous.current))) continue;
                if(bd.isGoal()){
                    last = new SearchNode(cur, cur.move + 1, bd);
                    found = true;
                    break;
                }
                pq.insert(new SearchNode(cur, cur.move + 1, bd));
            }
            if(found) break;
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

    private List<SearchNode>  calculate() {
        List<SearchNode> result = new ArrayList<>();
        MinPQ<SearchNode> pq = new MinPQ<>(new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode a, SearchNode b) {
                if (2 * a.move + a.hamming + a.manhattan - 2 * b.move - b.hamming - b.manhattan == 0)
                    return a.hamming - b.hamming;
                return 2 * a.move + a.hamming + a.manhattan - 2 * b.move - b.hamming - b.manhattan;
            }
        }
        );
        //MinPQ<SearchNode> pq = new MinPQ<>((a, b) -> (2 * a.move + a.hamming + a.manhattan - 2 * b.move - b.hamming - b.manhattan));
        SearchNode ini = new SearchNode(null, 0, start);
        pq.insert(ini);
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            step = Math.max(step, cur.move);
            result.add(cur);
            if(cur.current.isGoal()) break;
            boolean found = false;
            for (Board bd : cur.current.neighbors()) {
                if (cur.previous != null && bd.equals(cur.previous.current)) continue;
                SearchNode sn = new SearchNode(cur, cur.move + 1, bd);
                if(bd.isGoal()){
                    found = true;
                    step = Math.max(step, sn.move);
                    result.add(sn);
                    break;
                }
                pq.insert(sn);
            }
            if(found) break;
        }
        return result;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        } else {
            List<SearchNode> result = calculate();
            Deque<Board> dq = new ArrayDeque<>();
            SearchNode last = result.get(result.size() - 1);
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
