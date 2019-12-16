import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private final int[][] tiles;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        this.tiles = new int[tiles.length][tiles[0].length];
        for(int i = 0; i < tiles.length;i++){
            for(int j = 0; j < tiles[0].length; j++){
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString(){
        int n = tiles.length;
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append("\n");
        for(int i = 0; i < n; i++){
            StringBuilder cur = new StringBuilder();
            for(int j = 0; j < n; j++){
                cur.append(tiles[i][j] + " ");
            }
            cur.setLength(cur.length()-1);
            cur.append("\n");
            sb.append(cur.toString());
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension(){
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming(){
        int res = 0;
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                if(tiles[i][j] == 0) continue;
                if(tiles[i][j] -1 != i* tiles.length + j) res++;
            }
        }
        return res;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        int res = 0;
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                if(tiles[i][j] == 0) continue;
                res += Math.abs(tiles[i][j] - 1 - i * tiles.length -j);
            }
        }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal(){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                if(tiles[i][j] == 0) {
                    if(i != tiles.length -1 || j != tiles[0].length -1) return false;
                } else if(tiles[i][j] != 1 + i * tiles.length + j) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y){
        if(this.dimension()!= (Board) y.dimension()) return false;

    }

    // all neighboring boards
    public Iterable<Board> neighbors()

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()

    // unit testing (not graded)
    public static void main(String[] args)
}
