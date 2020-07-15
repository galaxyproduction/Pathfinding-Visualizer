import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Pathfinding {
    public static void BreathFirstSearch(Tile start, Tile goal, Tile[][] tiles) {
        Queue<Tile> frontier = new LinkedList<>(); // Stores the discovered nodes
        frontier.add(start);
        HashMap<Tile, Tile> cameFrom = new HashMap<>(); // Maps from which node we came from
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            Tile current = frontier.poll(); // Gets nodes first in, first out
            current.SetType(NodeType.VISITIED);

            if (current == goal)
                break;

            for (Tile next : GetNeighbors(current, tiles)) {
                if (!cameFrom.containsKey(next)) {
                    next.SetType(NodeType.FRONTIER);

                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }

            // 1/10 sec sleep for animation to be visible
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Reconstructs the path
        Tile current = goal;
        while (current != null) {
            current.SetType(NodeType.PATH);
            current = cameFrom.get(current);
        }
    }

    public static void GreedyBestFirstSearch(Tile start, Tile goal, Tile[][] tiles) {
        PriorityQueue<Tile> frontier = new PriorityQueue<>(new Comparator<Tile>() { // Min-heap the uses the cost of each to node for its comparision
            @Override
            public int compare(Tile o1, Tile o2) {
                return Integer.compare(o1.GetCost(), o2.GetCost());
            }
        });
        frontier.add(start);
        HashMap<Tile, Tile> cameFrom = new HashMap<>(); // Maps from which node we came from
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            Tile current = frontier.poll(); // Gets the node with the lowest cost
            current.SetType(NodeType.VISITIED);

            if (current == goal)
                break;

            for (Tile next : GetNeighbors(current, tiles)) {
                if (!cameFrom.containsKey(next)) {
                    int priority = Heuristic(goal, next); // Uses Manhatten distance to set priority
                    next.SetCost(priority);
                    next.SetType(NodeType.FRONTIER);

                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }

            // 1/10 sec sleep for animation to be visible
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // Reconstructs the path
        Tile current = goal;
        while (current != null) {
            current.SetType(NodeType.PATH);
            current = cameFrom.get(current);
        }
    }

    public static void AStar(Tile start, Tile goal, Tile[][] tiles) {
        PriorityQueue<Tile> frontier = new PriorityQueue<>(new Comparator<Tile>() { // Min-heap the uses the cost of each to node for its comparision
            @Override
            public int compare(Tile o1, Tile o2) {
                return Integer.compare(o1.GetCost(), o2.GetCost());
            }
        });
        HashMap<Tile, Tile> cameFrom = new HashMap<>(); // Maps from which node we came from
        HashMap<Tile, Integer> costSoFar = new HashMap<>(); // Stores the cost it takes to get from the start to the current node

        frontier.add(start);
        cameFrom.put(start, null);
        costSoFar.put(start, 0);

        while (!frontier.isEmpty()) {
            Tile current = frontier.poll(); // Gets the node with the lowest cost
            current.SetType(NodeType.VISITIED);

            if (current == goal)
                break;

            for (Tile next : GetNeighbors(current, tiles)) {
                int newCost = costSoFar.get(current) + 2; // The cost to get to this node + a uniform movement cost 
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) { // It is possible to visit the same node more than once so we update the cost if it is lower
                    costSoFar.put(next, newCost);

                    int priority = newCost + Heuristic(goal, next);
                    next.SetCost(priority);
                    next.SetType(NodeType.FRONTIER);

                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }

            // 1/10 sec sleep for animation to be visible
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Reconstructs the path
        Tile current = goal;
        while (current != null) {
            current.SetType(NodeType.PATH);
            current = cameFrom.get(current);
        }
    }

    // Manhattan distance
    private static int Heuristic(Tile a, Tile b) {
        return Math.abs(a.GetX() - b.GetX()) + Math.abs(a.GetY() - b.GetY());
    }

    // Returns all the adjacent neighbors (not the diagonals)
    private static List<Tile> GetNeighbors(Tile current, Tile[][] tiles) {
        List<Tile> neighbors = new ArrayList<>();

        int x = current.GetX();
        int y = current.GetY();

        if (x > 0 && tiles[x - 1][y].GetType() != NodeType.BARRIER)
            neighbors.add(tiles[x - 1][y]);

        if (x < tiles[0].length - 1 && tiles[x + 1][y].GetType() != NodeType.BARRIER)
            neighbors.add(tiles[x + 1][y]);

        if (y > 0 && tiles[x][y - 1].GetType() != NodeType.BARRIER)
            neighbors.add(tiles[x][y - 1]);

        if (y < tiles.length - 1 && tiles[x][y + 1].GetType() != NodeType.BARRIER)
            neighbors.add(tiles[x][y + 1]);

        return neighbors;
    }
}