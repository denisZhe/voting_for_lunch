package my.task.voting.service;

import my.task.voting.model.Vote;
import my.task.voting.util.ChangeUnacceptableException;
import my.task.voting.util.RepeatedVoteException;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static my.task.voting.LunchTestData.LUNCH_2;
import static my.task.voting.UserTestData.USER_2;
import static my.task.voting.VoteTestDate.*;
import static org.junit.Assert.assertEquals;

public class VotingServiceTest extends AbstractServiceTest{

    @Autowired
    private VotingService votingService;

    @Test
    public void testSave() throws Exception {
        Vote newVote = votingService.save(getNewVote());
        List<Vote> expectedVotes = Stream.of(VOTE_1, VOTE_2, newVote)
                .sorted(Comparator.comparing(Vote::getVotingDate).reversed())
                .collect(Collectors.toList());
        assertEquals(expectedVotes, votingService.getAll());
    }

    @Test
    public void testUpdate() throws Exception {
        setDeadline(LocalTime.now().plusMinutes(5));
        Vote newVote = votingService.save(getNewVote());
        newVote.setLunchId(LUNCH_2.getId());
        Vote updatedVote = votingService.save(newVote);
        assertEquals(updatedVote, votingService.get(updatedVote.getId()));
    }

    @Test
    public void testUnacceptableChangeWhenUpdateAfterDeadline() throws Exception {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("Too late for change vote");
        Vote newVote = votingService.save(getNewVote());
        setDeadline(LocalTime.now().minusMinutes(5));
        newVote.setLunchId(LUNCH_2.getId());
        votingService.save(newVote);
    }


    @Test
    public void testUnacceptableChangeWhenUpdateYesterdayVote() {
        thrown.expect(ChangeUnacceptableException.class);
        thrown.expectMessage("Too late for change vote");
        votingService.save(getYesterdayVote());
    }

    @Test
    public void testRepeatedExceptionWhenSave() throws Exception {
        thrown.expect(RepeatedVoteException.class);
        thrown.expectMessage("Only one vote per day for the user is possible");
        votingService.save(getNewVote());
        votingService.save(getRepeatedVote());
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(VOTE_1, votingService.get(VOTE_1.getId()));
    }

    @Test
    public void testGetByUserId() throws Exception {
        assertEquals(Arrays.asList(VOTE_1, VOTE_2), votingService.getByUserId(USER_2.getId()));
    }

    @Test
    public void testGetByLunchId() throws Exception {
        assertEquals(Collections.singletonList(VOTE_1), votingService.getByLunchId(LUNCH_2.getId()));
    }

    @Test
    public void testGetAll() throws Exception {
        List<Vote> expectedVotes = Stream.of(VOTE_1, VOTE_2)
                .sorted(Comparator.comparing(Vote::getVotingDate).reversed())
                .collect(Collectors.toList());
        assertEquals(expectedVotes, votingService.getAll());
    }

    private void setDeadline(LocalTime newDeadline) throws Exception {
        VotingServiceImpl votingServiceImpl = null;
        if (AopUtils.isJdkDynamicProxy(votingService)) {
            votingServiceImpl = (VotingServiceImpl) ((Advised) votingService).getTargetSource().getTarget();
        } else {
            votingServiceImpl = (VotingServiceImpl) votingService;
        }

        Field field = votingServiceImpl.getClass().getDeclaredField("DEADLINE");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newDeadline);

    }
}