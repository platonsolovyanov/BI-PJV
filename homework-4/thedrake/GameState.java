package thedrake;

public class GameState {
    private final Board board;
    private final PlayingSide onTuS;
    private final Army arBl;
    private final Army arOr;
    private final GameResult result;

    public GameState(
            Board board,
            Army arBl,
            Army arOr) {
        this(board, arBl, arOr, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army arBl,
            Army arOr,
            PlayingSide onTuS,
            GameResult result) {
        this.board = board;
        this.onTuS = onTuS;
        this.arBl = arBl;
        this.arOr = arOr;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide onTuS() {
        return onTuS;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return arBl;
        }

        return arOr;
    }

    public Army armyOnTurn() {
        return army(onTuS);
    }

    public Army armyNotOnTurn() {
        if (onTuS == PlayingSide.BLUE)
            return arOr;

        return arBl;
    }

    public Tile tileAt(TilePos pos) {
        if(arBl.boardTroops().at(pos).isPresent())
            return arBl.boardTroops().at(pos).get();
        if(arOr.boardTroops().at(pos).isPresent())
            return arOr.boardTroops().at(pos).get();
        return board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {
        if(result != GameResult.IN_PLAY || origin == TilePos.OFF_BOARD || armyOnTurn().boardTroops().isPlacingGuards() || !armyOnTurn().boardTroops().isLeaderPlaced())
            return false;
        return armyOnTurn().boardTroops().at(origin).isPresent();
    }

    private boolean canStepTo(TilePos target) {
        if(target == TilePos.OFF_BOARD || result != GameResult.IN_PLAY)
            return false;
        return tileAt(target).canStepOn();
    }

    public boolean canCaptureOn(TilePos target) {
        return !target.equals(TilePos.OFF_BOARD)
        && result == GameResult.IN_PLAY
        && armyOnTurn().boardTroops().isLeaderPlaced()
        && !armyOnTurn().boardTroops().isPlacingGuards()
        && armyNotOnTurn().boardTroops().at(target).isPresent();
    }

    public boolean canStep(TilePos origin, TilePos target) {
        if(canStepFrom(origin) && canStepTo(target)){
            return true;
        } else {
            return false;
        }
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        if (result != GameResult.IN_PLAY) {
        return false;
        }
        if (target.equals(TilePos.OFF_BOARD)) {
        return false;
        }
        if (armyOnTurn().stack().isEmpty()) {
        return false;
        }
        if (!tileAt(target).canStepOn()) {
        return false;
        }
        if (!board.at(target).canStepOn()) {
        return false;
        }
        if ((!target.isNextTo(armyOnTurn().boardTroops().leaderPosition())
        && armyOnTurn().boardTroops().isPlacingGuards())) {
        return false;
        }
        if (!armyOnTurn().boardTroops().isLeaderPlaced()) {
        if (onTuS == PlayingSide.BLUE) {
        if (target.j() != 0) {
        return false;
        }
        } else {
        if (target.j() != board.dimension() - 1) {
        return false;
        }
        }
        }
        if (armyOnTurn().boardTroops().isLeaderPlaced()
        && !armyOnTurn().boardTroops().isPlacingGuards()) {
        for (TilePos el: target.neighbours()) {
        if (armyOnTurn().boardTroops().at(el).isPresent()) {
        return true;
        }
        }
        return false;
        }
        return true;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (!canStep(origin, target)) {
        throw new IllegalArgumentException();
        }
        return createNewGameState(armyNotOnTurn(), armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
    }
    

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }
}