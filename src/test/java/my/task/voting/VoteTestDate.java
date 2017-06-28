package my.task.voting;

import my.task.voting.model.Vote;

import java.time.LocalDate;

import static my.task.voting.LunchTestData.LUNCH_1;
import static my.task.voting.LunchTestData.LUNCH_2;
import static my.task.voting.UserTestData.USER_1;
import static my.task.voting.model.BaseEntity.START_SEQ;

public class VoteTestDate {

    private static final int VOTE1_ID = START_SEQ + 13;

    public static final Vote VOTE_1 = new Vote(VOTE1_ID, LocalDate.of(2017, 6, 20), 100002, 100004);
    public static final Vote VOTE_2 = new Vote(VOTE1_ID + 1, LocalDate.of(2017, 6, 21), 100002, 100005);

    public static Vote getNewVote() {
        return new Vote(null,  LocalDate.now(), USER_1.getId(), LUNCH_1.getId());
    }

    public static Vote getYesterdayVote() {
        return new Vote(null, LocalDate.now().minusDays(1), USER_1.getId(), LUNCH_1.getId());
    }

    public static Vote getRepeatedVote() {
        return new Vote(null,  LocalDate.now(), USER_1.getId(), LUNCH_2.getId());
    }
}
