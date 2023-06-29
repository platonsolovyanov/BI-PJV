package thedrake;

public class Board {

    private final int dimension;
    private BoardTile[][] tabel;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        tabel = new BoardTile[dimension][dimension];
        this.dimension = dimension;
        for (int g = 0; g < dimension; g++) {
            for (int k = 0; k < dimension; k++) {
                this.tabel[g][k] = BoardTile.EMPTY;
            }
        }

    }

    // Rozměr hrací desky
    public int dimension() {
        return dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return this.tabel[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(TileAt... ats) {
        Board nw = new Board(this.dimension);
        for (int g = 0; g < nw.dimension; g++) {
            for (int k = 0; k < nw.dimension; k++) {
                nw.tabel[g][k] = this.tabel[g][k];
            }
        }
        for (TileAt s : ats) {
            nw.tabel[s.pos.i()][s.pos.j()] = s.tile;
        }
        return nw;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        PositionFactory tile = new PositionFactory(this.dimension);
        return tile;
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

