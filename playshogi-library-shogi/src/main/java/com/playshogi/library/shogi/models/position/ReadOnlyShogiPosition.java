package com.playshogi.library.shogi.models.position;

import com.playshogi.library.models.Position;
import com.playshogi.library.models.Square;
import com.playshogi.library.shogi.models.Piece;
import com.playshogi.library.shogi.models.Player;

import java.util.List;
import java.util.Optional;

public interface ReadOnlyShogiPosition extends Position {
    int getMoveCount();

    Player getPlayerToMove();

    ShogiBoardState getShogiBoardState();

    int getColumns();

    int getRows();

    KomadaiState getSenteKomadai();

    KomadaiState getGoteKomadai();

    Optional<Piece> getPieceAt(Square square);

    boolean isEmptySquare(Square square);

    boolean hasBlackPieceAt(Square square);

    boolean hasWhitePieceAt(Square square);

    boolean hasSenteKingOnBoard();

    List<Square> getAllSquares();
}
