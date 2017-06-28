package my.task.voting.util;

public class RepeatedVoteException extends RuntimeException {
    public RepeatedVoteException(String message) {
        super(message);
    }
}
