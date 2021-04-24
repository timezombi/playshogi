package com.playshogi.website.gwt.server;

import com.playshogi.library.shogi.models.formats.usf.UsfFormat;
import com.playshogi.library.shogi.models.record.GameRecord;
import com.playshogi.library.shogi.models.record.KifuCollection;
import com.playshogi.website.gwt.shared.models.ProblemCollectionDetails;
import com.playshogi.website.gwt.shared.models.ProblemCollectionDetailsAndProblems;
import com.playshogi.website.gwt.shared.models.ProblemDetails;
import com.playshogi.website.gwt.shared.models.ProblemOptions;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum ProblemsCache {
    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(ProblemsCache.class.getName());

    private int nextId = 1;

    private final Map<String, ProblemDetails> byId = new HashMap<>();
    private final Map<Integer, List<ProblemDetails>> byLength = new HashMap<>();
    private final List<ProblemDetails> allProblems = new ArrayList<>();
    private final Map<String, List<ProblemDetails>> byCollectionId = new HashMap<>();
    private final Map<String, KifuCollection> kifuCollectionById = new HashMap<>();

    private final Random random = new Random();


    public void saveProblemsCollection(final KifuCollection collection) {
        int i = 0;

        ArrayList<ProblemDetails> list = new ArrayList<>();

        for (GameRecord record : collection.getKifus()) {
            try {
                ProblemDetails details = buildProblemDetails(record);
                byLength.putIfAbsent(details.getNumMoves(), new ArrayList<>());
                byLength.get(details.getNumMoves()).add(details);
                allProblems.add(details);
                byId.put(details.getId(), details);
                list.add(details);
                i++;
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error importing problem", ex);
            }
        }

        String collectionId = UUID.randomUUID().toString();
        byCollectionId.put(collectionId, list);
        kifuCollectionById.put(collectionId, collection);

        LOGGER.info("Successfully imported " + i + " problems.");
        for (Map.Entry<Integer, List<ProblemDetails>> entry : byLength.entrySet()) {
            LOGGER.info(entry.getKey() + " moves: " + entry.getValue().size() + " problems");
        }
        System.out.println(byLength.get(0));

    }

    public ProblemDetails getProblem(final ProblemOptions options) {
        List<ProblemDetails> list = options.getNumMoves() == 0 ? allProblems : byLength.get(options.getNumMoves());
        if (options.isRandom()) {
            if (list != null) {
                return list.get(random.nextInt(list.size()));
            }
        } else {
            ProblemDetails next = byId.get(String.valueOf(Integer.parseInt(options.getPreviousProblemId()) + 1));
            if (next == null || next.getNumMoves() != options.getNumMoves()) {
                return list.get(0);
            } else {
                return next;
            }
        }
        return null;
    }

    private ProblemDetails buildProblemDetails(final GameRecord record) {
        ProblemDetails details = new ProblemDetails();
        details.setUsf(UsfFormat.INSTANCE.write(record.getGameTree()));
        details.setNumMoves(record.getGameTree().getMainVariationLength());
        details.setId(String.valueOf(nextId++));
        return details;
    }

    public ProblemCollectionDetails[] getProblemsCollectionDetails() {
        List<ProblemCollectionDetails> result = new ArrayList<>();

        for (String id : kifuCollectionById.keySet()) {
            result.add(getProblemCollectionDetails(id));
        }

        return result.toArray(new ProblemCollectionDetails[0]);
    }

    private ProblemCollectionDetails getProblemCollectionDetails(final String id) {
        ProblemCollectionDetails details = new ProblemCollectionDetails();
        details.setNumProblems(kifuCollectionById.get(id).getKifus().size());
        details.setName(kifuCollectionById.get(id).getName());
        details.setDescription(kifuCollectionById.get(id).getName());
        details.setId(id);
        return details;
    }

    public ProblemCollectionDetailsAndProblems getProblemCollectionDetailsAndProblems(final String collectionId) {
        return new ProblemCollectionDetailsAndProblems(getProblemCollectionDetails(collectionId),
                byCollectionId.get(collectionId).toArray(new ProblemDetails[0]));
    }
}
