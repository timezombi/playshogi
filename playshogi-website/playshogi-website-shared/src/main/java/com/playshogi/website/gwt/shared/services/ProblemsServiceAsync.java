package com.playshogi.website.gwt.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.playshogi.website.gwt.shared.models.*;

public interface ProblemsServiceAsync {

    void getProblem(ProblemOptions options, AsyncCallback<ProblemDetails> callback);

    void getProblem(String problemId, AsyncCallback<ProblemDetails> callback);

    void getRandomProblem(AsyncCallback<ProblemDetails> callback);

    void getRandomProblem(int numMoves, AsyncCallback<ProblemDetails> callback);

    void saveUserProblemAttempt(String sessionId, String problemId, boolean success, int timeMs,
                                AsyncCallback<Void> callback);

    void getProblemStatisticsDetails(String sessionId, AsyncCallback<ProblemStatisticsDetails[]> callback);

    void saveHighScore(String userName, int score, AsyncCallback<Void> callback);

    void getHighScores(AsyncCallback<SurvivalHighScore[]> callback);

    void saveCollectionTime(String sessionId, String collectionId, int timeMs, boolean complete, int solved,
                            AsyncCallback<Void> callback);

    void saveProblemsCollection(String sessionId, String draftId, ProblemCollectionDetails details,
                                AsyncCallback<String> callback);

    void addDraftToProblemCollection(String sessionId, String draftId, String collectionId,
                                     AsyncCallback<Void> callback);

    void getAllProblemCollections(String sessionId, AsyncCallback<ProblemCollectionDetails[]> callback);

    void getUserProblemCollections(String sessionId, String userName,
                                   AsyncCallback<ProblemCollectionDetails[]> callback);

    void getPublicProblemCollections(String sessionId, AsyncCallback<ProblemCollectionDetails[]> callback);

    void getProblemCollection(String sessionId, String collectionId, boolean includeHiddenProblems,
                              AsyncCallback<ProblemCollectionDetailsAndProblems> callback);

    void getLearnFromMistakeProblemCollection(String sessionId, String gameCollectionId,
                                              AsyncCallback<ProblemCollectionDetailsAndProblems> callback);

    void deleteProblemCollection(String sessionId, String problemSetId, boolean alsoDeleteKifus,
                                 AsyncCallback<Void> callback);

    void updateProblemCollectionDetails(String sessionId, ProblemCollectionDetails problemCollectionDetails,
                                        AsyncCallback<Void> callback);

    void updateProblemCollectionDetailsAdmin(String sessionId, ProblemCollectionDetails problemCollectionDetails,
                                             AsyncCallback<Void> callback);

    void createProblemCollection(String sessionId, ProblemCollectionDetails details,
                                 AsyncCallback<String> stringAsyncCallback);

    void saveProblemAndAddToCollection(String sessionId, String usf, String collectionId, AsyncCallback<Void> callback);

    void removeProblemFromCollection(String sessionId, String problemId, String collectionId,
                                     AsyncCallback<Void> callback);

    void addExistingKifuToProblemCollection(String sessionId, String kifuId, String collectionId,
                                            AsyncCallback<Void> callback);

    void convertGameCollection(String sessionId, String collectionId, AsyncCallback<Void> callback);

    void createCollectionsByDifficulty(String sessionId, AsyncCallback<Void> callback);

    void swapProblemsInCollection(String sessionId, String collectionId, String firstProblemId,
                                  String secondProblemId, AsyncCallback<Void> callback);

    void createRace(String sessionId, String collectionId, RaceDetails.RaceType raceType,
                    AsyncCallback<String> callback);

    void getRaceDetails(String sessionId, String raceId, AsyncCallback<RaceDetails> callback);

    void waitForRaceUpdate(String sessionId, String raceId, AsyncCallback<RaceDetails> callback);

    void joinRace(String sessionId, String raceId, AsyncCallback<Void> callback);

    void withdrawFromRace(String sessionId, String raceId, AsyncCallback<Void> callback);

    void startRace(String sessionId, String raceId, AsyncCallback<Void> callback);

    void reportUserProgressInRace(String sessionId, String raceId, String problemId, RaceDetails.ProblemStatus status
            , AsyncCallback<Void> callback);

    void reportBadProblem(String sessionId, String kifuId, String problemId, String collectionId, String reason,
                          String comment, AsyncCallback<Void> callback);
}
