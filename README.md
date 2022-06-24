# java_swim_in_rising_water

You are given an `n x n` integer matrix `grid` where each value `grid[i][j]` represents the elevation at that point `(i, j)`.

The rain starts to fall. At time `t`, the depth of the water everywhere is `t`. You can swim from a square to another 4-directionally adjacent square if and only if the elevation of both squares individually are at most `t`. You can swim infinite distances in zero time. Of course, you must stay within the boundaries of the grid during your swim.

Return *the least time until you can reach the bottom right square* `(n - 1, n - 1)` *if you start at the top left square* `(0, 0)`.

## Examples

**Example 1:**

![https://assets.leetcode.com/uploads/2021/06/29/swim1-grid.jpg](https://assets.leetcode.com/uploads/2021/06/29/swim1-grid.jpg)

```
Input: grid = [[0,2],[1,3]]
Output: 3
Explanation:
At time 0, you are in grid location (0, 0).
You cannot go anywhere else because 4-directionally adjacent neighbors have a higher elevation than t = 0.
You cannot reach point (1, 1) until time 3.
When the depth of water is 3, we can swim anywhere inside the grid.

```

**Example 2:**

![https://assets.leetcode.com/uploads/2021/06/29/swim2-grid-1.jpg](https://assets.leetcode.com/uploads/2021/06/29/swim2-grid-1.jpg)

```
Input: grid = [[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]]
Output: 16
Explanation: The final route is shown.
We need to wait until time 16 so that (0, 0) and (4, 4) are connected.

```

**Constraints:**

- `n == grid.length`
- `n == grid[i].length`
- `1 <= n <= 50`
- `0 <= grid[i][j] < n2`
- Each value `grid[i][j]` is **unique**.

## 解析

題目給定一個整數矩陣 grid ，其中每個 entry, grid[r][c] 代表該 cell 的高度

假設要經過時間 t , 水才會注滿到  t 單位高度 ，代表在時間 t 時， 可以到達 grid[r][c] ≤ t 的相鄰 cell

要求寫出一個演算法計算從 row = 0, col = 0 開始出發達到 row = N-1, col = N-1 最小需要花多少時間

這題的關鍵在於每個 grid[r][t] 到表該 cell 要到達至少需要多少時間

而每次要需要當下 cell 的時間與之前 cell 時間取最大值這樣才能確保能從之前的 cell 到達這個 cell

透過這樣資訊還有 grid 每個 cell 的水平與垂直相對結構

使用 **[Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)**

也就是透過 MinHeap 做 BFS 當走到 row = N - 1 , col = N - 1 所累計的時間就是答案

如下圖

![](https://i.imgur.com/OFUKJ7S.png)
## 程式碼
```java
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

```
## 困難點

1. 要理解每個 grid[r][c] 與時間的關係
2. 理解 **[Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)**
## Solve Point

- [x]  透過 grid[r][c] 把每個座標對應可到達最小時間放到 priorityQueue 中
- [x]  透過 HashTable 紀錄已經拜訪過的 vertex 避免重複拜訪
- [x]  每次放入的時間都取 max(grid[r][c], 上一個到達時間)