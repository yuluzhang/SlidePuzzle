import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    private class SearchNode{
        private Board previous;
        private int move;
        private Board current;
        public SearchNode(Board prev, int m, Board cur){
            current = cur;
            previous = prev;
            move = m;
        }
    }
    private class HammingComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode s1, SearchNode s2){
            int d1 = s1.current.hamming() + s1.move;
            int d2 = s2.current.hamming() + s2.move;
            return Integer.compare(d1,d2);
        }
    }
    private class ManhattanComparator implements Comparator<SearchNode>{
        @Override
        public int compare(SearchNode s1, SearchNode s2){
            int d1 = s1.current.manhattan() + s1.move;
            int d2 = s2.current.manhattan() + s2.move;
            return Integer.compare(d1,d2);
        }
    }
    private final Board start;
    private List<Board> result = new ArrayList<>();
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial == null) throw new IllegalArgumentException();
        start = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        if(start.isGoal()) return false;
        return true;
    }

    // min number of moves to solve initial board
    public int moves(){
        return result.size();
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
        Set<Board> seen = new HashSet<>();
        MinPQ<SearchNode> pq= new MinPQ<>(new ManhattanComparator());
        SearchNode ini = new SearchNode(null, 0, start);
        pq.insert(ini);
        seen.add(start);
        int step = 0;
        while(!pq.isEmpty()){
            SearchNode
        }
        return result;
    }

    // test client (see below)
    public static void main(String[] args){

    }
}
