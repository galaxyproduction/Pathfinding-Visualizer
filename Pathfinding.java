import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Pathfinding {
    public static void BreathFirstSearch(Tile start, Tile goal, Tile[][] tiles) {
        Queue<Tile> frontier = new LinkedList<Tile>();
        frontier.add(start);
        HashMap<Tile, Tile> cameFrom = new HashMap<Tile, Tile>();
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            Tile current = frontier.poll();
            current.SetType(NodeType.VISITIED);

            if (current == goal)
                break;

            for (Tile next : GetNeighbors(current, tiles)) {
                if (!cameFrom.containsKey(next)) {
                    frontier.add(next);
                    next.SetType(NodeType.FRONTIER);
                    cameFrom.put(next, current);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Tile current = goal;
        ArrayList<Tile> path = new ArrayList<Tile>();
        while(current != null){
            path.add(current);
            current.SetType(NodeType.PATH);
            current = cameFrom.get(current);
        }
        
        Collections.reverse(path);
    }

    private static List<Tile> GetNeighbors(Tile current, Tile[][] tiles){
        List<Tile> neighbors = new ArrayList<Tile>();

        int x = current.x;
        int y = current.y;

        if(x > 0)
            neighbors.add(tiles[x - 1][y]);
        
        if(x < tiles[0].length - 1)
            neighbors.add(tiles[x + 1][y]);
        
        if(y > 0)
            neighbors.add(tiles[x][y - 1]);
        
        if(y < tiles.length - 1)
            neighbors.add(tiles[x][y + 1]);

        return neighbors;
    }
}