
import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] tiles;
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] - 1 != i * tiles.length + j) hamming++;
            }
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == 0) continue;
                int r = (tiles[i][j] - 1) / this.dimension();
                int c = (tiles[i][j] - 1) % this.dimension();
                manhattan += Math.abs(r - i) + Math.abs(c - j);
            }
        }
    }

    // string representation of this board
    public String toString() {
        int n = tiles.length;
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            StringBuilder cur = new StringBuilder();
            for (int j = 0; j < n; j++) {
                cur.append(tiles[i][j] + " ");
            }
            cur.setLength(cur.length() - 1);
            cur.append("\n");
            sb.append(cur.toString());
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == 0) {
                    if (i != tiles.length - 1 || j != tiles[0].length - 1) return false;
                } else if (tiles[i][j] != 1 + i * tiles.length + j) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board)) return false;
        if (this.dimension() != ((Board) y).dimension()) return false;
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (tiles[i][j] != ((Board) y).tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> list = new ArrayList<>();
        int x = 0, y = 0;
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < 4; i++) {
            int r = x + dirs[i][0];
            int c = y + dirs[i][1];
            if (r < 0 || r >= this.dimension() || c < 0 || c >= this.dimension()) continue;
            int[][] temp = new int[this.dimension()][this.dimension()];
            for (int m = 0; m < this.dimension(); m++) {
                for (int n = 0; n < this.dimension(); n++) {
                    temp[m][n] = this.tiles[m][n];

                }
            }
            int pre = temp[r][c];
            temp[r][c] = 0;
            temp[x][y] = pre;
            Board next = new Board(temp);
            list.add(next);
        }
        return list;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

//    // unit testing (not graded)
//    public static void main(String[] args){
//        int[][] ts = new int[][]{{8,1,3},{4,0,2},{7,6,5}};
//        Board b = new Board(ts);
//        System.out.println("hamming is "+ b.hamming());
//        System.out.println("manhat is "+ b.manhattan());
//    }
}
