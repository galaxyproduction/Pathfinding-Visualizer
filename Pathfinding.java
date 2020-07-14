import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javafx.util.Pair;

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
    }

    public static void GreedyBestFirstSearch(Tile start, Tile goal, Tile[][] tiles){
        PriorityQueue<Pair<Tile, Integer>> frontier = new PriorityQueue<>(new Comparator<Pair<Tile, Integer>>(){
            @Override
            public int compare(Pair<Tile, Integer> o1, Pair<Tile, Integer> o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });
        frontier.add(new Pair<Tile, Integer>(start, 0));
        HashMap<Tile, Tile> cameFrom = new HashMap<Tile, Tile>();
        cameFrom.put(start, null);

        while(!frontier.isEmpty()){
            Tile current = frontier.poll().getKey();
            current.SetType(NodeType.VISITIED);

            if(current == goal)
                break;

            for(Tile next : GetNeighbors(current, tiles)){
                if(!cameFrom.containsKey(next)){
                    int priority = Heuristic(goal, next);
                    frontier.add(new Pair<Tile, Integer>(next, priority));
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
    }

    public static void AStar(Tile start, Tile goal, Tile[][] tiles){
        PriorityQueue<Pair<Tile, Integer>> frontier = new PriorityQueue<>(new Comparator<Pair<Tile, Integer>>(){
            @Override
            public int compare(Pair<Tile, Integer> o1, Pair<Tile, Integer> o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });
        frontier.add(new Pair<Tile, Integer>(start, 0));
        HashMap<Tile, Tile> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        HashMap<Tile, Integer> costSoFar = new HashMap<>();
        costSoFar.put(start, 0);

        while(!frontier.isEmpty()){
            Tile current = frontier.poll().getKey();
            current.SetType(NodeType.VISITIED);

            if(current == goal)
                break;

            for(Tile next : GetNeighbors(current, tiles)){
                int newCost = costSoFar.get(current);
                if(!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    int priority = newCost + Heuristic(goal, next);
                    frontier.add(new Pair<Tile, Integer>(next, priority));
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
    }

    private static int Heuristic(Tile a, Tile b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static List<Tile> GetNeighbors(Tile current, Tile[][] tiles){
        List<Tile> neighbors = new ArrayList<Tile>();

        int x = current.x;
        int y = current.y;

        if(x > 0 && tiles[x - 1][y].type != NodeType.BARRIER)
            neighbors.add(tiles[x - 1][y]);
        
        if(x < tiles[0].length - 1 && tiles[x + 1][y].type != NodeType.BARRIER)
            neighbors.add(tiles[x + 1][y]);
        
        if(y > 0 && tiles[x][y - 1].type != NodeType.BARRIER)
            neighbors.add(tiles[x][y - 1]);
        
        if(y < tiles.length - 1 && tiles[x][y + 1].type != NodeType.BARRIER)
            neighbors.add(tiles[x][y + 1]);

        return neighbors;
    }
}