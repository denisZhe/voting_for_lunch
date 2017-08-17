package my.task.voting.controller;

import my.task.voting.model.Lunch;
import my.task.voting.service.LunchService;
import my.task.voting.to.LunchTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static my.task.voting.controller.LunchRestController.REST_URL;
import static my.task.voting.util.LunchUtil.*;
import static my.task.voting.util.ValidationUtil.checkIdConsistent;
import static my.task.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class LunchRestController {

    static final String REST_URL = "/rest/lunches";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final LunchService service;

    @Autowired
    public LunchRestController(LunchService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LunchTO> createWithLocation(@Valid @RequestBody LunchTO lunchTO) {
        Lunch lunch = createLunchFromTOWithMeals(lunchTO);
        log.info("create {}", lunch);
        checkNew(lunch);
        LunchTO created = createTOFromLunchWithMeals(service.save(lunch));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LunchTO> update(@Valid @RequestBody LunchTO lunchTO, @PathVariable("id") int id) {
        Lunch lunch = createLunchFromTOWithMeals(lunchTO);
        log.info("update {}", lunch);
        checkIdConsistent(lunch, id);
        LunchTO updated = createTOFromLunchWithMeals(service.save(lunch));
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    @GetMapping(value = "/{id}")
    public LunchTO get(@PathVariable("id") int id) {
        log.info("get {}", id);
        return createTOFromLunch(service.get(id));
    }

    @GetMapping
    public List<LunchTO> getAll() {
        log.info("get all");
        return createListTOFromLunches(service.getAll());
    }

    @GetMapping(value = "/by-date")
    public List<LunchTO> getByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get by date {}", date);
        return createListTOFromLunches(service.getByDate(date));
    }

    @GetMapping(value = "/detailed-by-date")
    public List<LunchTO> getByDateWithMeals(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get by date with meals {}", date);
        return createListTOFromLunchesWithMeals(service.getByDateWithMeals(date));
    }
}
