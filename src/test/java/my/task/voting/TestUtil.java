package my.task.voting;

import my.task.voting.model.User;
import my.task.voting.service.VotingService;
import my.task.voting.service.VotingServiceImpl;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalTime;

public class TestUtil {

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    // Changes the standard deadline for voting - 11.00
    public static void setDeadline(VotingService votingService, LocalTime newDeadline) throws Exception {
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
