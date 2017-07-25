package my.task.voting;

import my.task.voting.model.Vote;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static my.task.voting.LunchTestData.LUNCH_1;
import static my.task.voting.LunchTestData.LUNCH_2;
import static my.task.voting.LunchTestData.LUNCH_3;
import static my.task.voting.UserTestData.USER_1;
import static my.task.voting.UserTestData.USER_2;
import static my.task.voting.model.BaseEntity.START_SEQ;

public class VoteTestData {

    public static final int VOTE1_ID = START_SEQ + 13;

    public static final Vote VOTE_1 = new Vote(VOTE1_ID, LocalDate.of(2017, 6, 20), USER_2.getId(), LUNCH_2.getId());
    public static final Vote VOTE_2 = new Vote(VOTE1_ID + 1, LocalDate.now(), USER_2.getId(), LUNCH_3.getId());

    public static Vote getNewVote() {
        return new Vote(null, LocalDate.now(), USER_1.getId(), LUNCH_3.getId());
    }

    public static Vote getRepeatedVote() {
        return new Vote(null, LocalDate.now(), USER_1.getId(), LUNCH_2.getId());
    }

    public static Vote getVoteFromPast() {
        return new Vote(null, LocalDate.now().minusDays(1), USER_1.getId(), LUNCH_1.getId());
    }

    public static int getNonexistentVoteId() {
        return 123;
    }

    public static List<Vote> getExpectedListVotes(boolean sorted, Vote... votes) {
        return !sorted ?
                Stream.of(votes).collect(Collectors.toList()) :
                Stream.of(votes).sorted(Comparator.comparing(Vote::getVotingDate).reversed()).collect(Collectors.toList());
    }
}
