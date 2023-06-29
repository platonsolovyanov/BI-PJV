package thedrake;

import java.util.ArrayList;
import java.util.List;

public class StrikeAction extends TroopAction {

    public StrikeAction(Offset2D offset) {
        super(offset);
    }

    public StrikeAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> mv = new ArrayList<>();
        TilePos tr = origin.stepByPlayingSide(offset(), side);
        if (state.canCapture(origin, tr)) {
            mv.add(new CaptureOnly(origin, (BoardPos) tr));
        }
        return mv;
    }
}
