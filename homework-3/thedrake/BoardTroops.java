package thedrake;

import java.util.*;

public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.leaderPosition = TilePos.OFF_BOARD;
        this.troopMap = Collections.emptyMap();
        this.guards = 0;
    }

    public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards) {
        this.leaderPosition = leaderPosition;
        this.guards = guards;
        this.playingSide = playingSide;
        this.troopMap = troopMap;
    }

    public Optional<TroopTile> at(TilePos pos) {
        TroopTile ttile = troopMap.get(pos);
        return Optional.ofNullable(ttile);
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
        if(leaderPosition != TilePos.OFF_BOARD){
            return true;
        }else{
            return false;
        }
    }

    public boolean isPlacingGuards() {
        if(leaderPosition != TilePos.OFF_BOARD && guards < 2){
            return true;
        }else{
            return false;
        }
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> boardP = new HashSet<>();

        for(Map.Entry<BoardPos, TroopTile> a : troopMap.entrySet()){
            if(at(a.getKey()).isPresent()){
                boardP.add(a.getKey());
            }
        }
        return boardP;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {

        if (at(target).isPresent()) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.put(target, new TroopTile(troop,playingSide,TroopFace.AVERS));

        if (isPlacingGuards()) {
            return new BoardTroops(playingSide(), newTroops, leaderPosition, guards + 1);
        }else if (!isLeaderPlaced()) {
            return new BoardTroops(playingSide(), newTroops, target, guards);
        }

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
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

        if (!at(origin).isPresent() || at(target).isPresent()){
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.get(origin);
        newTroops.put(target, tile.flipped());
        TroopTile tTile = newTroops.remove(origin);

        if(leaderPosition.equals(origin)){
            return new BoardTroops(playingSide,newTroops,target,guards);
        }else{
            return new BoardTroops(playingSide,newTroops,leaderPosition, guards);
        }
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

        if (!at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(target);

        if(leaderPosition.equals(target)){
            return new BoardTroops(playingSide,newTroops,TilePos.OFF_BOARD,guards);
        }else{
            return new BoardTroops(playingSide,newTroops,leaderPosition, guards);
        }
    }
}
