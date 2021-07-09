package com.playshogi.library.shogi.models.formats.kif;

import com.playshogi.library.shogi.models.Player;
import com.playshogi.library.shogi.models.formats.kif.KifUtils.PieceParsingResult;
import com.playshogi.library.shogi.models.formats.util.GameRecordFormat;
import com.playshogi.library.shogi.models.formats.util.LineReader;
import com.playshogi.library.shogi.models.formats.util.StringLineReader;
import com.playshogi.library.shogi.models.moves.ShogiMove;
import com.playshogi.library.shogi.models.moves.SpecialMove;
import com.playshogi.library.shogi.models.position.MutableKomadaiState;
import com.playshogi.library.shogi.models.position.ShogiPosition;
import com.playshogi.library.shogi.models.position.Square;
import com.playshogi.library.shogi.models.record.*;
import com.playshogi.library.shogi.models.shogivariant.Handicap;
import com.playshogi.library.shogi.models.shogivariant.ShogiInitialPositionFactory;
import com.playshogi.library.shogi.rules.ShogiRulesEngine;

import java.util.Arrays;
import java.util.List;

public enum KifFormat implements GameRecordFormat {
    INSTANCE;

    public static final String GOTE_TO_PLAY = "後手番";
    public static final String SENTE_TO_PLAY = "先手番";
    public static final String MOVE_NUMBER = "手数";
    public static final String START_DATE_AND_TIME = "開始日時";
    public static final String TOURNAMENT = "棋戦";
    public static final String END_DATE_AND_TIME = "終了日時";
    public static final String OPENING = "戦型";
    public static final String PLACE = "場所";
    public static final String TIME_CONTROL = "持ち時間";
    public static final String GAME_DAY = "対局日";
    public static final String HANDICAP = "手合割";
    public static final String HIRATE = "平手";
    public static final String OTHER = "その他"; // Other handicap
    public static final String GOTE = "後手";
    public static final String HANDICAP_GIVER = "上手";
    public static final String SENTE = "先手";
    public static final String HANDICAP_RECEIVER = "下手";
    public static final String REFERENCE = "備考";
    public static final String AUTHOR = "作者";
    public static final String PUBLICATION = "発表誌";
    public static final String ESTIMATED_TIME = "目安時間";
    public static final String THINKING_TIME = "思考時間";
    public static final String NUMBER_OF_MOVES = "詰手数";
    public static final String HEADING = "表題";
    public static final String TIME_SPENT = "消費時間";
    public static final String GOTE_PIECES_IN_HAND = "後手の持駒";
    public static final String HANDICAP_GIVER_PIECES_IN_HAND = "上手の持駒";
    public static final String SENTE_PIECES_IN_HAND = "先手の持駒";
    public static final String HANDICAP_RECEIVER_PIECES_IN_HAND = "下手の持駒";
    public static final String NONE = "なし"; // For pieces in hand

    @Override
    public List<GameRecord> read(String string) {
//        return read(new DebugLineReader(new StringLineReader(string)));
        return read(new StringLineReader(string));
    }

    @Override
    public List<GameRecord> read(final LineReader lineReader) {
        GameInformation gameInformation = new GameInformation();

        ShogiPosition startingPosition = new ShogiPosition();

        while (true) {
            if (!lineReader.hasNextLine()) {
                break;
            }

            String l = lineReader.peekNextLine();

            if (l.startsWith(MOVE_NUMBER)) {
                lineReader.nextLine();
                break;
            }

            if (l.startsWith(GOTE_PIECES_IN_HAND) || l.startsWith(HANDICAP_GIVER_PIECES_IN_HAND)) {
                readStartingPosition(lineReader, startingPosition);
                continue;
            }

            lineReader.nextLine();

            readHeaderLine(gameInformation, startingPosition, l);
        }

        GameTree gameTree;
        if (startingPosition.isEmpty() || startingPosition.isDefaultStartingPosition()) {
            gameTree = new GameTree();
        } else {
            gameTree = new GameTree(startingPosition);
        }
        GameNavigation gameNavigation = new GameNavigation(new ShogiRulesEngine(), gameTree);

        GameResult gameResult = GameResult.UNKNOWN;

        ShogiMove curMove;
        ShogiMove prevMove = null;
        int moveNumber = 1;
        while (lineReader.hasNextLine()) {
            String line = lineReader.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("*")) {
                continue;
            }
            String[] ts = line.split("\\s+", 2);
            int i;
            try {
                i = new Integer(ts[0]);
            } catch (Exception ex) {
                break;
            }
            if (i != moveNumber || ts.length < 2) {
                throw new IllegalArgumentException("Error after move " + moveNumber);
            }
            moveNumber++;
            String move = ts[1];
            curMove = KifMoveConverter.fromKifString(move, gameNavigation.getPosition(), prevMove);

            if (curMove instanceof SpecialMove) {
                SpecialMove specialMove = (SpecialMove) curMove;
                if (specialMove.getSpecialMoveType().isLosingMove()) {
                    gameResult = gameNavigation.getPosition().getPlayerToMove() == Player.BLACK ?
                            GameResult.WHITE_WIN : GameResult.BLACK_WIN;
                }
            }

            gameNavigation.addMove(curMove);
            prevMove = curMove;
        }

        gameNavigation.moveToStart();
        return Arrays.asList(new GameRecord(gameInformation, gameTree, gameResult));
    }

    private void readHeaderLine(final GameInformation gameInformation, final ShogiPosition startingPosition,
                                String line) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) {
            return;
        }

        if (line.equals(SENTE_TO_PLAY)) {
            return;
        }

        if (line.equals(GOTE_TO_PLAY)) {
            startingPosition.setPlayerToMove(Player.WHITE);
            return;
        }

        String[] sp = line.split("：", 2);
        if (sp.length < 2) {
            System.out.println("WARNING : unable to parse line " + line + " in file " + "???" + " , ignored.");
            return;
        }
        String field = sp[0];
        String value = sp[1];
        switch (field) {
            case START_DATE_AND_TIME:
            case GAME_DAY:
                gameInformation.setDate(value);
                break;
            case TOURNAMENT:
                gameInformation.setEvent(value);
                break;
            case OPENING:
                gameInformation.setOpening(value);
                break;
            case PLACE:
                gameInformation.setLocation(value);
                break;
            case HANDICAP:
                if (!(value.startsWith(HIRATE) || value.startsWith(OTHER))) {
                    boolean found = false;
                    for (Handicap handicap : Handicap.values()) {
                        if (handicap.getJapanese().equals(value)) {
                            found = true;
                            ShogiInitialPositionFactory.fillInitialPosition(startingPosition, handicap);
                        }
                    }

                    if (!found) {
                        throw new IllegalArgumentException("Unknown handicap type: " + value);
                    }
                }
                break;
            case GOTE:
            case HANDICAP_GIVER:
                gameInformation.setWhite(value);
                break;
            case SENTE:
            case HANDICAP_RECEIVER:
                gameInformation.setBlack(value);
                break;
            case "作意":
                // TODO: conception
                break;
            case GOTE_PIECES_IN_HAND:
            case HANDICAP_GIVER_PIECES_IN_HAND:
            case SENTE_PIECES_IN_HAND:
            case HANDICAP_RECEIVER_PIECES_IN_HAND:
                throw new IllegalStateException("Should have processed pieces in hand while reading position");
            case END_DATE_AND_TIME:
            case TIME_CONTROL:
            case PUBLICATION:
            case ESTIMATED_TIME:
            case THINKING_TIME:
            case NUMBER_OF_MOVES:
            case HEADING:
            case TIME_SPENT:
            case REFERENCE:
            case AUTHOR:
                break;
            default:
                System.out.println("WARNING : unknown field " + line + " when parsing kifu, ignored !");
                break;
        }
    }

    // Example position:
    //    後手の持駒： 金三　歩十二　
    //      ９ ８ ７ ６ ５ ４ ３ ２ １
    //    +---------------------------+
    //    | ・v歩 ・ ・ ・ ・ ・v桂 銀|一
    //    | ・ ・ ・v銀 ・v桂v玉 ・ ・|二
    //    | 香 桂 ・ ・v香 ・v歩 飛v銀|三
    //    | ・ ・ ・ 桂 ・ 金 ・ ・ ・|四
    //    | ・ ・ ・ ・ ・ ・ 銀 ・ ・|五
    //    | ・ ・ 歩 歩 ・ 歩 ・ ・ ・|六
    //    | ・ ・ ・ 馬 ・ ・ ・v飛 ・|七
    //    | ・ 香 ・ ・ ・ ・ ・ 香 ・|八
    //    | ・ ・ ・ ・ ・ ・ 馬 ・ ・|九
    //    +---------------------------+
    //    先手の持駒：歩　
    private void readStartingPosition(final LineReader lineReader, final ShogiPosition startingPosition) {
        String goteKomadaiLine = lineReader.nextLine().trim();

        if (!goteKomadaiLine.equals(GOTE_PIECES_IN_HAND) && !goteKomadaiLine.equals(HANDICAP_GIVER_PIECES_IN_HAND)) {
            String[] goteSplit = goteKomadaiLine.split("：", 2);
            if (goteSplit.length < 2 ||
                    (!goteSplit[0].equals(GOTE_PIECES_IN_HAND) && !goteSplit[0].equals(HANDICAP_GIVER_PIECES_IN_HAND))) {
                throw new IllegalArgumentException("ERROR : unable to parse gote komadai line " + goteKomadaiLine);
            }

            MutableKomadaiState komadai = startingPosition.getMutableGoteKomadai();
            readPiecesInHand(goteSplit[1], komadai);
        }

        lineReader.nextLine(); //  ９ ８ ７ ６ ５ ４ ３ ２ １
        lineReader.nextLine(); // +---------------------------+

        for (int row = 1; row <= 9; row++) {
            String l = lineReader.nextLine();
            int pos = 1;
            for (int column = 9; column >= 1; column--) {
                PieceParsingResult pieceParsingResult = KifUtils.readPiece(l, pos);
                pos = pieceParsingResult.nextPosition;
                startingPosition.getMutableShogiBoardState().setPieceAt(Square.of(column, row),
                        pieceParsingResult.piece);
            }
        }
        lineReader.nextLine(); // +---------------------------+

        String senteKomadaiLine = lineReader.nextLine().trim();
        if (senteKomadaiLine.equals(SENTE_PIECES_IN_HAND) || senteKomadaiLine.equals(HANDICAP_RECEIVER_PIECES_IN_HAND)) {
            // Nothing in sente hand
            return;
        }

        String[] senteSplit = senteKomadaiLine.split("：", 2);
        if (senteSplit.length < 2 ||
                (!senteSplit[0].equals(SENTE_PIECES_IN_HAND) &&
                        !senteSplit[0].equals(HANDICAP_RECEIVER_PIECES_IN_HAND))) {
            throw new IllegalArgumentException("ERROR : unable to parse sente komadai line " + senteKomadaiLine);
        }

        MutableKomadaiState senteKomadai = startingPosition.getMutableSenteKomadai();
        readPiecesInHand(senteSplit[1], senteKomadai);
    }

    private void readPiecesInHand(final String value, final MutableKomadaiState komadai) {
        if (NONE.equals(value) || "".equals(value)) {
            // nothing in hand
            return;
        }
        String[] piecesInHandStrings = value.split("[ 　]", 0);

        for (String pieceString : piecesInHandStrings) {
            PieceParsingResult pieceParsingResult = KifUtils.readPiece(pieceString, 0);
            int number;
            if (pieceString.length() == 1) {
                number = 1;
            } else if (pieceString.length() == 2) {
                number = KifUtils.getNumberFromJapanese(pieceString.charAt(1));
            } else if (pieceString.length() == 3 && pieceString.charAt(1) == '十') {
                number = 10 + KifUtils.getNumberFromJapanese(pieceString.charAt(2));
            } else {
                throw new IllegalArgumentException("Error reading pieces in hand: " + value + " at " + pieceString);
            }
            komadai.setPiecesOfType(pieceParsingResult.piece.getPieceType(), number);
        }
    }

    @Override
    public String write(final GameRecord gameRecord) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String write(final GameTree gameTree) {
        throw new UnsupportedOperationException();
    }

    public ShogiPosition readPosition(final String position) {
        List<GameRecord> gameRecords = read(position);
        if (gameRecords.size() == 0) {
            return null;
        }

        GameRecord record = gameRecords.get(0);
        return record.getInitialPosition().clonePosition();
    }
}
