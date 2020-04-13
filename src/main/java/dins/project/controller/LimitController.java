package dins.project.controller;

import dins.project.model.Limit;
import dins.project.model.LimitType;
import dins.project.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("limit")
@RequiredArgsConstructor
public class LimitController {

    private final LimitService limitService;

    @GetMapping("/get")
    public ResponseEntity<List<Limit>> getLimits(HttpServletRequest request) {
        List<Limit> limits = new ArrayList<>();
        limits.add(limitService.getLimit(LimitType.MAX));
        limits.add(limitService.getLimit(LimitType.MIN));
        return new ResponseEntity<>(limits, HttpStatus.OK);
    }

    @PostMapping("/set")
    public ResponseEntity<String> setLimit(@RequestBody Limit limit) {
        if (limitService.addLimit(limit.getType(), limit.getValue())) {
            return new ResponseEntity<>("Limit updated.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Limit update failed. Try again.", HttpStatus.BAD_REQUEST);
        }
    }

}
