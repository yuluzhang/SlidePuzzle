import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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
    private final List<SearchNode> result = new ArrayList<>();
    private int step = -1;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        start = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        Board twin = start.twin();
        MinPQ<SearchNode> pq = new MinPQ<>((a,b)->(2*a.move+a.hamming+a.manhattan - 2*b.move-b.hamming-b.manhattan));
        pq.insert(new SearchNode(null,0,twin));
        pq.insert(new SearchNode(null, 0, start));
        SearchNode last = null;
        while(!pq.isEmpty()){
            SearchNode cur = pq.delMin();
            for (Board bd : cur.current.neighbors()) {
                if ((cur.previous!= null && bd.equals(cur.previous.current))) continue;
                pq.insert(new SearchNode(cur, cur.move + 1, bd));
            }
            if (cur.current.isGoal()) {
                last = cur;
                break;
            }
        }
        while(last.previous != null){
            last = last.previous;
        }
        return last.current.equals(start);
    }


    // min number of moves to solve initial board
    public int moves() {
        if(isSolvable()) {
            calculate();
        }
        return step;
    }
    private void calculate(){
        MinPQ<SearchNode> pq = new MinPQ<>((a,b)->(2*a.move+a.hamming+a.manhattan - 2*b.move-b.hamming-b.manhattan));
        SearchNode ini = new SearchNode(null, 0, start);
        pq.insert(ini);
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            for (Board bd : cur.current.neighbors()) {
                if(cur.previous != null && bd.equals(cur.previous.current)) continue;
                else pq.insert(new SearchNode(cur, cur.move + 1, bd));
            }
            step = Math.max(step, cur.move);
            result.add(cur);
            if (cur.current.isGoal()) break;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if(!isSolvable()) {
            return null;
        } else{
            calculate();
            List<Board> res = new ArrayList<>();
            SearchNode last = result.get(result.size()-1);
            while(last != null){
                res.add(last.current);
                last = last.previous;
            }
            Collections.reverse(res);
            return res;
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
