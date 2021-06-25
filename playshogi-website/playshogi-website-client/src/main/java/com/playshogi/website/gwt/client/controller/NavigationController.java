package com.playshogi.website.gwt.client.controller;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.playshogi.library.shogi.models.PieceType;
import com.playshogi.library.shogi.models.Player;
import com.playshogi.library.shogi.models.formats.usf.UsfMoveConverter;
import com.playshogi.library.shogi.models.moves.DropMove;
import com.playshogi.library.shogi.models.moves.ShogiMove;
import com.playshogi.library.shogi.models.position.ReadOnlyShogiPosition;
import com.playshogi.library.shogi.models.record.GameNavigation;
import com.playshogi.library.shogi.models.record.GameTree;
import com.playshogi.library.shogi.rules.ShogiRulesEngine;
import com.playshogi.website.gwt.client.events.gametree.*;
import com.playshogi.website.gwt.client.events.kifu.ArrowDrawnEvent;
import com.playshogi.website.gwt.client.events.kifu.ClearDecorationsEvent;
import com.playshogi.website.gwt.client.widget.gamenavigator.NavigatorConfiguration;

import java.util.Objects;

public class NavigationController {


    interface MyEventBinder extends EventBinder<NavigationController> {
    }

    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private final String activityId;
    private final NavigatorConfiguration navigatorConfiguration;
    private final GameNavigation gameNavigation;
    private final ShogiRulesEngine shogiRulesEngine = new ShogiRulesEngine();

    private EventBus eventBus;

    public NavigationController(final String activityId) {
        this(activityId, new NavigatorConfiguration(), new GameNavigation(new ShogiRulesEngine(), new GameTree()));
    }

    public NavigationController(final String activityId, final boolean problemMode) {
        this(activityId, new NavigatorConfiguration(problemMode), new GameNavigation(new ShogiRulesEngine(),
                new GameTree()));
    }

    public NavigationController(final String activityId, final NavigatorConfiguration navigatorConfiguration,
                                final GameNavigation gameNavigation) {
        GWT.log(activityId + ": Creating NavigationController");
        this.activityId = activityId;
        this.navigatorConfiguration = navigatorConfiguration;
        this.gameNavigation = gameNavigation;
    }

    public void activate(final EventBus eventBus) {
        GWT.log("Activating Navigation controller");
        this.eventBus = eventBus;
        eventBinder.bindEventHandlers(this, eventBus);
    }

    private void fireNodeChanged() {
        eventBus.fireEvent(new NodeChangedEvent());
    }

    private void firePositionChanged(final boolean triggeredByUser) {
        GWT.log(activityId + " GameNavigator: firing position changed");
        eventBus.fireEvent(new PositionChangedEvent(gameNavigation.getPosition(),
                gameNavigation.getBoardDecorations(), gameNavigation.getPreviousMove(), triggeredByUser));
    }

    public NavigatorConfiguration getNavigatorConfiguration() {
        return navigatorConfiguration;
    }

    public GameNavigation getGameNavigation() {
        return gameNavigation;
    }

    public ReadOnlyShogiPosition getPosition() {
        return gameNavigation.getPosition();
    }

    @EventHandler
    public void onGameTreeChanged(final GameTreeChangedEvent gameTreeChangedEvent) {
        GWT.log(activityId + " NavigationController: Handling game tree changed event - move " + gameTreeChangedEvent.getGoToMove());
        gameNavigation.setGameTree(gameTreeChangedEvent.getGameTree(), gameTreeChangedEvent.getGoToMove());
        firePositionChanged(false);
    }

    @EventHandler
    public void onEditMovePlayed(final EditMovePlayedEvent event) {
        GWT.log(activityId + " NavigationController: Handling EditMovePlayedEvent");
        gameNavigation.addMove(event.getMove(), true);
        firePositionChanged(true);
        eventBus.fireEvent(new NewVariationPlayedEvent(false));
    }

    @EventHandler
    public void onMovePlayed(final MovePlayedEvent movePlayedEvent) {
        GWT.log(activityId + " NavigationController: Handling move played event");
        ShogiMove move = movePlayedEvent.getMove();
        GWT.log("Move played: " + move.toString());
        boolean existingMove = gameNavigation.hasMoveInCurrentPosition(move);
        boolean mainMove = Objects.equals(gameNavigation.getMainVariationMove(), move);

        gameNavigation.addMove(move, true);
        if (!existingMove) {
            GWT.log("New variation");
            boolean positionCheckmate = shogiRulesEngine.isPositionCheckmate(gameNavigation.getPosition());
            if (move instanceof DropMove) {
                DropMove dropMove = (DropMove) move;
                if (dropMove.getPieceType() == PieceType.PAWN) {
                    positionCheckmate = false;
                }
            }
            GWT.log("Checkmate: " + positionCheckmate);
            eventBus.fireEvent(new NewVariationPlayedEvent(positionCheckmate));
        } else if (gameNavigation.isEndOfVariation()) {
            eventBus.fireEvent(new EndOfVariationReachedEvent(mainMove));
            fireNodeChanged();
        } else if (gameNavigation.getPosition().getPlayerToMove() == Player.WHITE && navigatorConfiguration.isProblemMode()) {
            gameNavigation.moveForward();
            fireNodeChanged();
        }

        firePositionChanged(true);
    }

    @EventHandler
    public void onInsertVariationEvent(final InsertVariationEvent event) {
        GWT.log(activityId + " NavigationController: handling InsertVariationEvent");

        String[] usfMoves = event.getSelectedVariation().getPrincipalVariation().trim().split(" ");
        for (String usfMove : usfMoves) {
            gameNavigation.addMove(UsfMoveConverter.fromUsfString(usfMove, gameNavigation.getPosition()), true);
        }
        for (int i = 0; i < usfMoves.length - 1; i++) {
            gameNavigation.moveBack();
        }

        eventBus.fireEvent(new NewVariationPlayedEvent(false));
        firePositionChanged(true);
    }

    @EventHandler
    public void onClearDecorations(final ClearDecorationsEvent event) {
        GWT.log(activityId + " NavigationController: Handling ClearDecorationsEvent");
        gameNavigation.getCurrentNode().setObjects(null);
        firePositionChanged(true);
    }

    @EventHandler
    public void onArrowDrawnEvent(final ArrowDrawnEvent event) {
        GWT.log(activityId + " NavigationController: Handling ArrowDrawnEvent");
        gameNavigation.getCurrentNode().addArrow(event.getArrow());
    }


    @EventHandler
    public void onNavigateToStart(final NavigateToStartEvent event) {
        GWT.log(activityId + " NavigationController: Handling NavigateToStartEvent");
        gameNavigation.moveToStart();
        eventBus.fireEvent(new UserNavigatedBackEvent());
        firePositionChanged(true);
        fireNodeChanged();
    }

    @EventHandler
    public void onNavigateToEnd(final NavigateToEndEvent event) {
        GWT.log(activityId + " NavigationController: Handling NavigateToEndEvent");
        gameNavigation.moveToEndOfVariation();
        firePositionChanged(true);
        fireNodeChanged();
    }

    @EventHandler
    public void onNavigateForward(final NavigateForwardEvent event) {
        GWT.log(activityId + " NavigationController: Handling NavigateForwardEvent");
        gameNavigation.moveForward();
        firePositionChanged(true);
        fireNodeChanged();
    }

    @EventHandler
    public void onNavigateBack(final NavigateBackEvent event) {
        GWT.log(activityId + " NavigationController: Handling NavigateBackEvent");
        gameNavigation.moveBack();
        eventBus.fireEvent(new UserNavigatedBackEvent());
        firePositionChanged(true);
        fireNodeChanged();
    }
}
