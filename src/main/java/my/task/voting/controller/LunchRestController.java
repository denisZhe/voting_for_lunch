package my.task.voting.controller;

import my.task.voting.model.Lunch;
import my.task.voting.service.LunchService;
import my.task.voting.to.LunchTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static my.task.voting.controller.LunchRestController.REST_URL;
import static my.task.voting.util.LunchUtil.createLunchFromTO;
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
    public ResponseEntity<Lunch> createWithLocation(@Valid @RequestBody LunchTO lunchTO) {
        Lunch lunch = createLunchFromTO(lunchTO);
        log.info("create {}", lunch);
        checkNew(lunch);
        Lunch created = service.save(lunch);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody LunchTO lunchTO, @PathVariable("id") int id) {
        Lunch lunch = createLunchFromTO(lunchTO);
        log.info("update {}", lunch);
        checkIdConsistent(lunch, id);
        service.save(lunch);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    @GetMapping(value = "/{id}")
    public Lunch get(@PathVariable("id") int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @GetMapping
    public List<Lunch> getAll() {
        log.info("get all");
        return service.getAll();
    }

    @GetMapping(value = "/by-date")
    public List<Lunch> getByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get by date {}", date);
        return service.getByDate(date);
    }

    @GetMapping(value = "/detailed-by-date")
    public List<Lunch> getByDateWithMeals(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get by date with meals {}", date);
        return service.getByDateWithMeals(date);
    }
}
