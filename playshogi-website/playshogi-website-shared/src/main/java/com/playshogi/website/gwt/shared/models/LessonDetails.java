package com.playshogi.website.gwt.shared.models;

import java.io.Serializable;
import java.util.Arrays;

public class LessonDetails implements Serializable {
    private String lessonId;
    private String kifuId;
    private String parentLessonId;
    private String title;
    private String description;
    private String[] tags;
    private String previewSfen;
    private int difficulty;
    private int likes;
    private boolean completed;

    public LessonDetails() {
    }

    public LessonDetails(final String lessonId, final String kifuId, final String parentLessonId, final String title,
                         final String description, final String[] tags, final String previewSfen,
                         final int difficulty, final int likes, final boolean completed) {
        this.lessonId = lessonId;
        this.kifuId = kifuId;
        this.parentLessonId = parentLessonId;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.previewSfen = previewSfen;
        this.difficulty = difficulty;
        this.likes = likes;
        this.completed = completed;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(final String lessonId) {
        this.lessonId = lessonId;
    }

    public String getKifuId() {
        return kifuId;
    }

    public void setKifuId(final String kifuId) {
        this.kifuId = kifuId;
    }

    public String getParentLessonId() {
        return parentLessonId;
    }

    public void setParentLessonId(final String parentLessonId) {
        this.parentLessonId = parentLessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(final String[] tags) {
        this.tags = tags;
    }

    public String getPreviewSfen() {
        return previewSfen;
    }

    public void setPreviewSfen(final String previewSfen) {
        this.previewSfen = previewSfen;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(final int difficulty) {
        this.difficulty = difficulty;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(final int likes) {
        this.likes = likes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(final boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "LessonDetails{" +
                "lessonId='" + lessonId + '\'' +
                ", kifuId='" + kifuId + '\'' +
                ", parentLessonId='" + parentLessonId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + Arrays.toString(tags) +
                ", previewSfen='" + previewSfen + '\'' +
                ", difficulty=" + difficulty +
                ", likes=" + likes +
                ", completed=" + completed +
                '}';
    }
}
