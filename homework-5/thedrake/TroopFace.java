package thedrake;

import java.io.PrintWriter;

public enum TroopFace implements JSONSerializable {
    REVERS, AVERS;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"" + this.toString() + "\"");
    }
}
