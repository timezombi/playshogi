package com.playshogi.website.gwt.client.widget.board;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.playshogi.library.shogi.models.Piece;
import com.playshogi.library.shogi.models.PieceType;

public class PieceGraphics {

    private static RyokoPieceBundle resources = GWT.create(RyokoPieceBundle.class);

    public static ImageResource getPieceImage(final Piece piece) {
        switch (piece) {
            case GOTE_BISHOP:
                return resources.gkaku();
            case GOTE_GOLD:
                return resources.gkin();
            case GOTE_KING:
                return resources.gou();
            case GOTE_KNIGHT:
                return resources.gkei();
            case GOTE_LANCE:
                return resources.gkyo();
            case GOTE_PAWN:
                return resources.gfu();
            case GOTE_PROMOTED_BISHOP:
                return resources.guma();
            case GOTE_PROMOTED_KNIGHT:
                return resources.gnkei();
            case GOTE_PROMOTED_LANCE:
                return resources.gnkyo();
            case GOTE_PROMOTED_PAWN:
                return resources.gto();
            case GOTE_PROMOTED_ROOK:
                return resources.gryu();
            case GOTE_PROMOTED_SILVER:
                return resources.gngin();
            case GOTE_ROOK:
                return resources.ghi();
            case GOTE_SILVER:
                return resources.ggin();
            case SENTE_BISHOP:
                return resources.skaku();
            case SENTE_GOLD:
                return resources.skin();
            case SENTE_KING:
                return resources.sou();
            case SENTE_KNIGHT:
                return resources.skei();
            case SENTE_LANCE:
                return resources.skyo();
            case SENTE_PAWN:
                return resources.sfu();
            case SENTE_PROMOTED_BISHOP:
                return resources.suma();
            case SENTE_PROMOTED_KNIGHT:
                return resources.snkei();
            case SENTE_PROMOTED_LANCE:
                return resources.snkyo();
            case SENTE_PROMOTED_PAWN:
                return resources.sto();
            case SENTE_PROMOTED_ROOK:
                return resources.sryu();
            case SENTE_PROMOTED_SILVER:
                return resources.sngin();
            case SENTE_ROOK:
                return resources.shi();
            case SENTE_SILVER:
                return resources.sgin();
            default:
                throw new IllegalArgumentException();

        }
    }

    public static ImageResource getPieceImage(final PieceType pieceType, boolean promoted) {
        switch (pieceType) {
            case PAWN:
                return promoted ? resources.sto() : resources.sfu();
            case LANCE:
                return promoted ? resources.snkyo() : resources.skyo();
            case KNIGHT:
                return promoted ? resources.snkei() : resources.skei();
            case SILVER:
                return promoted ? resources.sngin() : resources.sgin();
            case GOLD:
                return resources.skin();
            case BISHOP:
                return promoted ? resources.suma() : resources.skaku();
            case ROOK:
                return promoted ? resources.sryu() : resources.shi();
            case KING:
                return resources.sou();
            default:
                throw new IllegalArgumentException();
        }
    }
}
