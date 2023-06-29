package thedrake;

public class Board {
    private final BoardTile board[][];
    private final int dimension;
    public Board(int dimension) {
        this.dimension = dimension;
        this.board = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                this.board[i][j] = BoardTile.EMPTY;
            }                
        }            
    }
    public int dimension() {
        return this.dimension;
    }
    public BoardTile at(TilePos pos) {
        return this.board[pos.i()][pos.j()];
    }
    public Board withTiles(TileAt... ats) {
        Board big = new Board(this.dimension);
        for(int i = 0; i < this.dimension; i++)
            big.board[i] = this.board[i].clone();
        for(TileAt elem : ats)
            big.board[elem.pos.i()][elem.pos.j()] = elem.tile;
        return big;
    }
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

