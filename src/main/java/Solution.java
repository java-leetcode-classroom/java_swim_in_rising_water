import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;

public class Solution {
  static class Pair {
    final private int Row;
    final private int Col;

    public Pair(int row, int col) {
      Row = row;
      Col = col;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Pair)) return false;
      Pair pair = (Pair) o;
      return Row == pair.Row && Col == pair.Col;
    }

    @Override
    public int hashCode() {
      return Objects.hash(Row, Col);
    }
  }
  static class AdjacentNode {
    private final Pair Coord;
    private final int Time;

    public AdjacentNode(Pair coord, int time) {
      Coord = coord;
      Time = time;
    }
  }
  public int swimInWater(int[][] grid) {
    int N = grid.length;
    HashSet<Pair> visit = new HashSet<>();
    Pair[] directions = new Pair[]{new Pair(-1, 0), new Pair(1, 0), new Pair(0,-1), new Pair(0,1)};
    PriorityQueue<AdjacentNode> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.Time));
    queue.add(new AdjacentNode(new Pair(0,0), grid[0][0]));
    while(queue.size() != 0) {
      AdjacentNode node = queue.poll();
      if (node.Coord.Col == N - 1 && node.Coord.Row == N - 1) {
        return node.Time;
      }
      for (Pair direction: directions) {
        int shiftedRow = node.Coord.Row + direction.Row;
        int shiftedCol = node.Coord.Col + direction.Col;
        if (shiftedRow < 0 || shiftedRow == N || shiftedCol < 0 || shiftedCol == N
            || visit.contains(new Pair(shiftedRow, shiftedCol))) {
          continue;
        }
        visit.add(new Pair(shiftedRow, shiftedCol));
        queue.add(new AdjacentNode(new Pair(shiftedRow, shiftedCol),
            Math.max(node.Time, grid[shiftedRow][shiftedCol])));
      }
    }
    return 0;
  }
}
