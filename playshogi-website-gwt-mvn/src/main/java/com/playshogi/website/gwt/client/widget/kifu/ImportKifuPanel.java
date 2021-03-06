package com.playshogi.website.gwt.client.widget.kifu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import com.playshogi.library.models.record.GameRecord;
import com.playshogi.library.shogi.models.formats.kif.KifFormat;
import com.playshogi.library.shogi.models.formats.usf.UsfFormat;
import com.playshogi.website.gwt.client.events.GameRecordChangedEvent;

public class ImportKifuPanel extends Composite implements ClickHandler {

    private final Button loadFromURLButton;
    private final Button loadFromTextButton;
    private final TextArea textArea;
    private final TextBox urlText;
    private EventBus eventBus;

    public ImportKifuPanel() {

        FlowPanel verticalPanel = new FlowPanel();

        urlText = new TextBox();
        urlText.setVisibleLength(50);

        verticalPanel.add(urlText);

        loadFromURLButton = new Button("Load from URL");
        loadFromURLButton.addClickHandler(this);

        verticalPanel.add(loadFromURLButton);

        verticalPanel.add(new HTML(SafeHtmlUtils.fromSafeConstant("<br>")));

        textArea = new TextArea();
        textArea.setCharacterWidth(80);
        textArea.setVisibleLines(15);

        verticalPanel.add(textArea);

        loadFromTextButton = new Button("Import from text");
        loadFromTextButton.addClickHandler(this);

        verticalPanel.add(new HTML(SafeHtmlUtils.fromSafeConstant("<br>")));

        verticalPanel.add(loadFromTextButton);

        initWidget(verticalPanel);
    }

    @Override
    public void onClick(final ClickEvent event) {
        Object source = event.getSource();
        if (source == loadFromTextButton) {
            importGame();
        }
    }

    public void activate(final EventBus eventBus) {
        GWT.log("Activating kifu importer panel");
        this.eventBus = eventBus;
    }

    private void importGame() {
        GWT.log("Importing game...");
        String gameText = textArea.getText();
        GameRecord gameRecord;
        if (gameText.startsWith("USF")) {
            GWT.log("Will parse as USF game");
            gameRecord = UsfFormat.INSTANCE.read(gameText);
        } else {
            GWT.log("Will parse as KIF game");
            gameRecord = KifFormat.INSTANCE.read(gameText);
        }
        GWT.log("Firing game record changed event...");
        eventBus.fireEvent(new GameRecordChangedEvent(gameRecord));
    }
}
