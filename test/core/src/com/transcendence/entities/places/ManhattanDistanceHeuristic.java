package com.transcendence.entities.places;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanDistanceHeuristic implements Heuristic<Tile> {

    @Override
    public float estimate(Tile node, Tile endNode) {
        return Math.abs(endNode.getX() - node.getX()) + Math.abs(endNode.getY() - node.getY());
    }

}
