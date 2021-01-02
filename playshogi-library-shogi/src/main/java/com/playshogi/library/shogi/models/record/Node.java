package com.playshogi.library.shogi.models.record;

import com.playshogi.library.shogi.models.moves.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Node {

    private Node parent = null;
    private int parentIndex = 0;

    private final List<Node> children = new ArrayList<>();

    private Move move;

    private String comment = null;
    private MoveAnnotation annotation = MoveAnnotation.NONE;
    private Integer evaluation = null;
    private String objects = null;
    private String additionalTags = null;

    public Node(final Move move) {
        this.move = move;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(final Node parent) {
        this.parent = parent;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(final int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(final Move move) {
        this.move = move;
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Optional<String> getObjects() {
        return Optional.ofNullable(objects);
    }

    public Optional<String> getAdditionalTags() {
        return Optional.ofNullable(additionalTags);
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public void setObjects(final String objects) {
        this.objects = objects;
    }

    public void setAdditionalTags(final String additionalTags) {
        this.additionalTags = additionalTags;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getFirstChild() {
        return children.get(0);
    }

    public MoveAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(final MoveAnnotation annotation) {
        this.annotation = annotation;
    }

    public Optional<Integer> getEvaluation() {
        return Optional.ofNullable(evaluation);
    }

    public void setEvaluation(final Integer evaluation) {
        this.evaluation = evaluation;
    }

    public void addChild(final Node node) {
        node.setParent(this);
        node.setParentIndex(children.size());
        children.add(node);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean hasChildWithMove(final Move move) {
        return getChildWithMove(move) == null;
    }

    public Node getChildWithMove(final Move move) {
        for (Node node : children) {
            if (Objects.equals(node.getMove(), move)) {
                return node;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "move=" + move +
                ", parentIndex=" + parentIndex +
                ", children.size=" + children.size() +
                ", comment='" + comment + '\'' +
                ", annotation=" + annotation +
                ", evaluation=" + evaluation +
                ", objects='" + objects + '\'' +
                ", additionalTags='" + additionalTags + '\'' +
                '}';
    }
}
