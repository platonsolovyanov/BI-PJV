package thedrake;

import java.io.PrintWriter;

public class Board implements JSONSerializable {
    private final BoardTile board[][];
    private final int dimension;
    public Board(int dimension) {
        this.dimension = dimension;
        this.board = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++)
            for(int j = 0; j < dimension; j++)
                this.board[i][j] = BoardTile.EMPTY;
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
        for(TileAt e : ats)
            big.board[e.pos.i()][e.pos.j()] = e.tile;
        return big;
    }
    public PositionFactory positionFactory() {
        return new PositionFactory(this.dimension);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"dimension\":" + dimension);
        writer.print(",\"tiles\":[");
        int lol = 0;
        for(int i = 0; i < dimension; i++)
            for(int j = 0; j < dimension; j++) {
                board[j][i].toJSON(writer);
                lol++;
                if(lol < dimension * dimension)
                    writer.print(",");
            }
        writer.print("]}");
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

