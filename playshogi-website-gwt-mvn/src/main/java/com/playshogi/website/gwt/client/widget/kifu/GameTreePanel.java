package com.playshogi.website.gwt.client.widget.kifu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.playshogi.library.shogi.models.formats.kif.KifMoveConverter;
import com.playshogi.library.shogi.models.formats.sfen.SfenConverter;
import com.playshogi.library.shogi.models.moves.EditMove;
import com.playshogi.library.shogi.models.moves.Move;
import com.playshogi.library.shogi.models.moves.ShogiMove;
import com.playshogi.library.shogi.models.record.GameNavigation;
import com.playshogi.library.shogi.models.record.Node;
import com.playshogi.website.gwt.client.events.gametree.GameTreeChangedEvent;
import com.playshogi.website.gwt.client.events.gametree.NewVariationPlayedEvent;
import com.playshogi.website.gwt.client.events.gametree.NodeChangedEvent;
import com.playshogi.website.gwt.client.events.gametree.PositionChangedEvent;

import java.util.Iterator;
import java.util.List;

public class GameTreePanel extends Composite {

    interface MyEventBinder extends EventBinder<GameTreePanel> {
    }

    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private final String activityId;
    private final GameNavigation gameNavigation;
    private final Tree tree;
    private EventBus eventBus;

    public GameTreePanel(final String activityId, final GameNavigation gameNavigation) {
        this.activityId = activityId;
        this.gameNavigation = gameNavigation;
        FlowPanel panel = new FlowPanel();

        tree = new Tree();
        tree.addSelectionHandler(selectionEvent -> {
            TreeItem item = selectionEvent.getSelectedItem();
            if (item.getUserObject() instanceof Node) {
                Node node = (Node) item.getUserObject();
                gameNavigation.moveToNode(node);
                eventBus.fireEvent(new PositionChangedEvent(gameNavigation.getPosition(), true));
            }
        });
        panel.add(tree);

        initWidget(panel);
    }

    public void activate(final EventBus eventBus) {
        GWT.log(activityId + ": Activating GameTreePanel");
        this.eventBus = eventBus;
        eventBinder.bindEventHandlers(this, this.eventBus);
    }

    @EventHandler
    public void onGameTreeChanged(final GameTreeChangedEvent gameTreeChangedEvent) {
        GWT.log(activityId + " GameTreePanel: Handling game tree changed event - move " + gameTreeChangedEvent.getGoToMove());
        populateTree();
    }

    @EventHandler
    public void onNewVariationPlayed(final NewVariationPlayedEvent event) {
        GWT.log(activityId + " GameTreePanel: Handling NewVariationPlayedEvent");
        populateTree();
    }

    @EventHandler
    public void onNodeChangedEvent(final NodeChangedEvent event) {
        GWT.log(activityId + " GameTreePanel: Handling NodeChangedEvent");
        updateSelection();
    }

    private void updateSelection() {
        Iterator<TreeItem> iterator = tree.treeItemIterator();
        while (iterator.hasNext()) {
            TreeItem item = iterator.next();
            if (item.getUserObject() == gameNavigation.getCurrentNode()) {
                tree.setSelectedItem(item);
            }
        }
    }


    private void populateTree() {
        tree.clear();
        Node node = gameNavigation.getGameTree().getRootNode();
        populateMainVariationAndBranches(tree, node, 0);
        openTree();
        updateSelection();
    }

    private void openTree() {
        Iterator<TreeItem> iterator = tree.treeItemIterator();
        while (iterator.hasNext()) {
            iterator.next().setState(true);
        }
    }

    /**
     * Add all the moves of the main variation to parent
     * Also create branches if a node has multiple moves
     */
    private void populateMainVariationAndBranches(final HasTreeItems parent, final Node variationNode,
                                                  final int moveCount) {
        Node currentNode = variationNode;
        int variationMoveCount = moveCount;

        // First add the move of the current node
        TreeItem item = new TreeItem();
        setMoveNode(item, currentNode, variationMoveCount);
        parent.addItem(item);

        // Then add all children, along the main line and variations
        while (currentNode.hasChildren()) {
            item = new TreeItem();
            populateMainMoveAndBranches(item, currentNode, ++variationMoveCount);
            parent.addItem(item);
            currentNode = currentNode.getChildren().get(0);
        }
    }

    /**
     * Displays the main move at node, and adds branches for sibling moves. Populate the sibling trees.
     * - If there is only one move, display it at item.
     * - If there is one main move and one variation, display the main move at item, then create a child item and
     * populate the variation there.
     * - If there is one main move and two or more variations, display the main move at item, create one child item
     * for each move, then show the variation under each child.
     */
    private void populateMainMoveAndBranches(final TreeItem item, final Node node, final int moveCount) {
        if (!node.hasChildren()) return;

        List<Node> children = node.getChildren();
        setMoveNode(item, children.get(0), moveCount);

        if (children.size() == 2) {
            Node variationNode = children.get(1);
            populateMainVariationAndBranches(item, variationNode, moveCount);
        } else if (children.size() > 2) {
            for (int i = 1; i < children.size(); i++) {
                Node variationNode = children.get(i);
                TreeItem variationItem = new TreeItem();
                populateMainVariationAndBranches(variationItem, variationNode, moveCount);
                item.addItem(variationItem);
            }
        }
    }

    private void setMoveNode(final TreeItem item, final Node node, final int moveCount) {
        Move move = node.getMove();
        if (move != null) {
            item.setUserObject(node);
            if (move instanceof EditMove) {
                item.setText("START (" + SfenConverter.toSFEN(((EditMove) move).getPosition()) + ")");
            } else if (move instanceof ShogiMove) {
                item.setText(moveCount + ". " + KifMoveConverter.toKifStringShort((ShogiMove) move));
            } else {
                item.setText(moveCount + ". " + move);
            }
        }
    }

}
