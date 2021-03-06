package com.playshogi.library.shogi.models.position;

import com.playshogi.library.models.Square;
import com.playshogi.library.shogi.models.Piece;
import com.playshogi.library.shogi.models.PieceType;

public class ShogiBoardStateImpl extends ShogiBoardState {

    private ShogiBoardState invert;

    private final int width;
    private final int height;

    private final Piece[][] board;

    public ShogiBoardStateImpl(final int width, final int height) {
        this(width, height, new Piece[width][height]);
    }

    private ShogiBoardStateImpl(final int width, final int height, final Piece[][] board) {
        this.width = width;
        this.height = height;
        this.board = board;
    }

    @Override
    public Piece getPieceAt(final int column, final int row) {
        return board[column - 1][row - 1];
    }

    @Override
    public Piece getPieceAt(final Square square) {
        return getPieceAt(square.getColumn(), square.getRow());
    }

    @Override
    public void setPieceAt(final int column, final int row, final Piece piece) {
        board[column - 1][row - 1] = piece;
    }

    @Override
    public void setPieceAt(final Square square, final Piece piece) {
        setPieceAt(square.getColumn(), square.getRow(), piece);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean hasPlayerPawnOnColumn(final boolean isPlayerSente, final int column) {
        for (Piece piece : board[column - 1]) {
            if (piece != null && (piece.isSentePiece() == isPlayerSente) && piece.getPieceType() == PieceType.PAWN
                    && !piece.isPromoted()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ShogiBoardState opposite() {
        if (invert == null) {
            invert = new InvertedShogiBoardState(this);
        }
        return invert;
    }

    @Override
    public boolean isSquareEmptyOrGote(final Square square) {
        Piece piece = getPieceAt(square);
        return piece == null || !piece.isSentePiece();
    }
}
