package com.playshogi.website.gwt.client.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.playshogi.website.gwt.client.mvp.AppPlaceHistoryMapper;
import com.playshogi.website.gwt.client.place.ByoYomiPlace;
import com.playshogi.website.gwt.client.widget.board.ShogiBoard;
import com.playshogi.website.gwt.client.widget.gamenavigator.GameNavigator;
import com.playshogi.website.gwt.client.widget.problems.ByoYomiFeedbackPanel;
import com.playshogi.website.gwt.client.widget.problems.ByoYomiProgressPanel;

@Singleton
public class ByoYomiView extends Composite {

    private static final String TSUME = "byoyomi";

    private static final int[] MOVES = {3, 5, 7, 9, 11, 13};

    private final ShogiBoard shogiBoard;
    private final GameNavigator gameNavigator;
    private final ByoYomiFeedbackPanel byoYomiFeedbackPanel;
    private final ByoYomiProgressPanel byoYomiProgressPanel;

    @Inject
    public ByoYomiView(final AppPlaceHistoryMapper historyMapper) {
        GWT.log("Creating byo yomi view");
        shogiBoard = new ShogiBoard(TSUME);
        gameNavigator = new GameNavigator(TSUME);
        byoYomiFeedbackPanel = new ByoYomiFeedbackPanel();
        byoYomiProgressPanel = new ByoYomiProgressPanel(historyMapper);

        shogiBoard.setUpperRightPanel(byoYomiFeedbackPanel);
        shogiBoard.setLowerLeftPanel(byoYomiProgressPanel);

        shogiBoard.getBoardConfiguration().setShowGoteKomadai(false);
        shogiBoard.getBoardConfiguration().setPlayGoteMoves(false);

        gameNavigator.getNavigatorConfiguration().setProblemMode(true);

        initWidget(shogiBoard);
    }

    public void activate(final EventBus eventBus) {
        GWT.log("Activating tsume view");
        shogiBoard.activate(eventBus);
        gameNavigator.activate(eventBus);
        byoYomiFeedbackPanel.activate(eventBus);
        byoYomiProgressPanel.activate(eventBus);
    }

    public void initUi(ByoYomiPlace place) {
        byoYomiProgressPanel.setTimerVisible(place.getMaxTimeSec() != 0);
        byoYomiFeedbackPanel.setTimerVisible(place.getTimePerMove() != 0);
    }
}
