package thedrake;

import java.util.*;

public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.leaderPosition = TilePos.OFF_BOARD;
        this.troopMap = Collections.emptyMap();
        this.playingSide = playingSide;
        this.guards = 0;
    }

    public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        TroopTile tile = troopMap.get(pos);
        return Optional.ofNullable(tile);
    }

    public PlayingSide playingSide() {
        return this.playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return this.guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return leaderPosition != TilePos.OFF_BOARD && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> bd = new HashSet<>();
        for(Map.Entry<BoardPos, TroopTile> e : troopMap.entrySet())
            if(at(e.getKey()).isPresent())
                bd.add(e.getKey());
        return bd;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if(at(target).isPresent()) {
            throw new IllegalArgumentException();
        }
        Map<BoardPos, TroopTile> nTr = new HashMap<>(troopMap);
        nTr.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));

        if(isPlacingGuards()) {
            return new BoardTroops(playingSide, nTr, leaderPosition, guards + 1);
        } else if (!isLeaderPlaced()){
            return new BoardTroops(playingSide, nTr, target, guards);
        }
         return new BoardTroops(playingSide, nTr, leaderPosition, guards);
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }
        if(!at(origin).isPresent() || at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile newTile = newTroops.get(origin);
        newTroops.put(target, newTile.flipped());
        newTroops.remove(origin);

        return leaderPosition.equals(origin) ? new BoardTroops(playingSide, newTroops, target, guards)
                : new BoardTroops(playingSide, newTroops, leaderPosition, guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }
        if(!at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);

        return leaderPosition.equals(target) ? new BoardTroops(playingSide, newTroops, TilePos.OFF_BOARD, guards)
                : new BoardTroops(playingSide, newTroops, leaderPosition, guards);
    }
}