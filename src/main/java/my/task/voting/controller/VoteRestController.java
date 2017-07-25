package my.task.voting.controller;

import my.task.voting.model.User;
import my.task.voting.model.Vote;
import my.task.voting.service.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static my.task.voting.controller.VoteRestController.REST_URL;
import static my.task.voting.util.ValidationUtil.checkIdConsistent;
import static my.task.voting.util.ValidationUtil.checkNew;
import static my.task.voting.util.ValidationUtil.checkUserPermissionForCreateOrUpdateVote;

@RestController
@RequestMapping(REST_URL)
public class VoteRestController {

    static final String REST_URL = "/rest/votes";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final VotingService service;

    @Autowired
    public VoteRestController(VotingService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@Valid @RequestBody Vote vote, @AuthenticationPrincipal User user) {
        log.info("create {} for User {}", vote, user);
        checkNew(vote);
        checkUserPermissionForCreateOrUpdateVote(vote, user);
        Vote created = service.save(vote);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody Vote vote, @PathVariable("id") int id, @AuthenticationPrincipal User user) {
        log.info("update {} with id={} for User {}", vote, id, user);
        checkIdConsistent(vote, id);
        checkUserPermissionForCreateOrUpdateVote(vote, user);
        service.save(vote);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote get(@PathVariable("id") int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getAll() {
        log.info("get all");
        return service.getAll();
    }

    @GetMapping(value = "/by-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getByUser(@RequestParam("user") int userId) {
        log.info("get by user id {}", userId);
        return service.getByUserId(userId);
    }

    @GetMapping(value = "/by-lunch", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getByDateLunch(@RequestParam("lunch") int lunchId) {
        log.info("get by lunch id {}", lunchId);
        return service.getByLunchId(lunchId);
    }
}
