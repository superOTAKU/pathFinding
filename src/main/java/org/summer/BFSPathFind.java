package org.summer;

import java.util.*;

public class BFSPathFind {

    public static void main(String[] args) {
        int[][] grid = new int[10][10];
        List<Position> path = findPath(grid, new Position(2, 2), new Position(9, 5));
        System.out.println("path: " + path);
    }

    /**
     * 广度优先搜索，从起点开始，搜索到达终点的路径
     */
    public static List<Position> findPath(int[][] grid, Position start, Position target) {
        List<Position> frontier = new ArrayList<>();
        frontier.add(start);
        Map<Position, Position> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        outer:
        while (!frontier.isEmpty()) {
            Position current = frontier.remove(0);
            List<Position> neighbors = getNeighbors(grid, current);
            for (var next : neighbors) {
                if (!cameFrom.containsKey(next)) {
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
                if (next.equals(target)) {
                    break outer;
                }
            }
        }
        //从cameFrom收集path
        List<Position> path = new ArrayList<>();
        Position current = target;
        while (!current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    private static List<Position> getNeighbors(int[][] grid, Position current) {
        int xRange = grid.length - 1;
        int yRange = grid[0].length - 1;
        List<Position> neighbors = new ArrayList<>();
        if (current.getX() > 0) {
            neighbors.add(new Position(current.getX() - 1, current.getY()));
        }
        if (current.getX() + 1 <= xRange) {
            neighbors.add(new Position(current.getX() + 1, current.getY()));
        }
        if (current.getY() > 0) {
            neighbors.add(new Position(current.getX(), current.getY() - 1));
        }
        if (current.getY() + 1 <= yRange) {
            neighbors.add(new Position(current.getX(), current.getY() + 1));
        }
        return neighbors;
    }

}
