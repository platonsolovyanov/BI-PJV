package thedrake;

public class Board {
    private final int dimension;
    private final BoardTile board[][];
    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        this.dimension = dimension;
        this.board = new BoardTile[dimension][dimension];
        for(int i = 0; i < dimension; i++) {
            for (int k = 0; k < dimension; k++) {
                this.board[i][k] = BoardTile.EMPTY;
            }
        }
    }

    // Rozměr hrací desky
    public int dimension() {
        return this.dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return this.board[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(TileAt... ats) {
        Board nextBoard = new Board(this.dimension);
        for(int i = 0; i < this.dimension; i++){
            nextBoard.board[i] = this.board[i].clone();
        }
        for(TileAt unit : ats){
            nextBoard.board[unit.pos.i()][unit.pos.j()] = unit.tile;
        }
        return nextBoard;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return new PositionFactory(this.dimension);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}

