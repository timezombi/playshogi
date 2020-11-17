package com.playshogi.website.gwt.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.playshogi.website.gwt.client.events.tutorial.*;
import com.playshogi.website.gwt.client.i18n.TutorialMessages;
import com.playshogi.website.gwt.client.mvp.AppPlaceHistoryMapper;
import com.playshogi.website.gwt.client.place.TutorialPlace;
import com.playshogi.website.gwt.client.tutorial.PieceMovementTutorial;
import com.playshogi.website.gwt.client.tutorial.Tutorial;
import com.playshogi.website.gwt.client.tutorial.Tutorials;
import com.playshogi.website.gwt.client.widget.PiecesSelectorPanel;
import com.playshogi.website.gwt.client.widget.board.ShogiBoard;

@Singleton
public class TutorialView extends Composite {

    private final TutorialMessages tutorialMessages = GWT.create(TutorialMessages.class);

    interface MyEventBinder extends EventBinder<TutorialView> {
    }

    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private static final String TUTORIAL = "tutorial";

    private final ShogiBoard shogiBoard;
    private EventBus eventBus;
    private final AppPlaceHistoryMapper historyMapper;
    private final Tutorials tutorials;
    private final TextArea textArea;
    private HTML titleHTML;
    private PiecesSelectorPanel piecesSelectorPanel;

    @Inject
    public TutorialView(final AppPlaceHistoryMapper historyMapper) {
        GWT.log("Creating tutorial view");

        this.historyMapper = historyMapper;

        AbsolutePanel absolutePanel = getRightPanel();

        shogiBoard = new ShogiBoard(TUTORIAL);
        this.tutorials = new Tutorials(this);
        shogiBoard.setLowerLeftPanel(getOutline());
        shogiBoard.setUpperRightPanel(absolutePanel);

        VerticalPanel verticalPanel = new VerticalPanel();

        verticalPanel.add(shogiBoard);

        textArea = new TextArea();
        textArea.setSize("782px", "100px");
        verticalPanel.add(textArea);

        initWidget(verticalPanel);
    }

    private AbsolutePanel getRightPanel() {
        AbsolutePanel absolutePanel = new AbsolutePanel();
        titleHTML = new HTML(tutorialMessages.introTitle());
        titleHTML.setStyleName("lesson-title");
        absolutePanel.add(titleHTML, 0, 0);

        Button nextButton = new Button(tutorialMessages.next());
        Button backButton = new Button(tutorialMessages.back());
        Button tryAgainButton = new Button(tutorialMessages.tryAgain());

        absolutePanel.add(backButton, 0, 200);
        absolutePanel.add(tryAgainButton, 50, 200);
        absolutePanel.add(nextButton, 125, 200);

        backButton.addClickHandler(clickEvent -> eventBus.fireEvent(new GoPreviousChapterEvent()));
        nextButton.addClickHandler(clickEvent -> eventBus.fireEvent(new GoNextChapterEvent()));
        tryAgainButton.addClickHandler(clickEvent -> eventBus.fireEvent(new TryChapterAgainEvent()));

        return absolutePanel;
    }

    private FlowPanel getOutline() {
        FlowPanel flowPanel = new FlowPanel();

        flowPanel.add(new Hyperlink(tutorialMessages.introTitle(), historyMapper.getToken(new TutorialPlace(1))));

        // Adds an entry for each piece movement tutorial
        Tutorial[] tutorials = this.tutorials.getTutorials();
        for (int i = 0; i < tutorials.length; i++) {
            if (tutorials[i] instanceof PieceMovementTutorial) {
                PieceMovementTutorial pieceMovementTutorial = (PieceMovementTutorial) tutorials[i];

                flowPanel.add(new Hyperlink(pieceMovementTutorial.getTutorialTitle(),
                        historyMapper.getToken(new TutorialPlace(i + 1))));
            }
        }

        flowPanel.getElement().getStyle().setBackgroundColor("#DBCBCB");

        piecesSelectorPanel = new PiecesSelectorPanel();
        flowPanel.add(piecesSelectorPanel);
        return flowPanel;
    }

    public ShogiBoard getShogiBoard() {
        return shogiBoard;
    }

    public Tutorials getTutorials() {
        return tutorials;
    }

    public void activate(final EventBus eventBus) {
        GWT.log("Activating tutorial view");
        this.eventBus = eventBus;
        eventBinder.bindEventHandlers(this, this.eventBus);
        shogiBoard.activate(eventBus);
        piecesSelectorPanel.activate(eventBus);
        tutorials.activate(eventBus);
    }

    private void setTutorialText(String text) {
        textArea.setText(text);
    }

    @EventHandler
    public void onChangeTutorialText(final ChangeTutorialTextEvent event) {
        GWT.log("TutorialView: handle ChangeTutorialTextEvent");
        setTutorialText(event.getText());
    }

    @EventHandler
    public void onChangeTutorialTitle(final ChangeTutorialTitleEvent event) {
        GWT.log("TutorialView: handle ChangeTutorialTitleEvent");
        titleHTML.setHTML(event.getText());
    }
}
