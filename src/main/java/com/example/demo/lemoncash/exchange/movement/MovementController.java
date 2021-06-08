package com.example.demo.lemoncash.exchange.movement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movement")
public class MovementController {

    private final MovementService movementService;

    @PostMapping
    public ResponseEntity transaction(@RequestBody MovementRequest movementRequest) throws Exception {
        movementService.applyMovement(movementRequest);
        return ResponseEntity.ok("Successfully performed transaction");
    }

    @GetMapping
    public List<MovementResponse> listMovementsByUserId(@RequestParam(name = "user_id") Long userId,
                                                        @RequestParam(name = "coin_type", required = false) String coinTypeNameAbbr,
                                                        @RequestParam(name = "movement_type", required = false) MovementType movementType,
                                                        @RequestParam(name = "limit", required = false) Integer limit,
                                                        @RequestParam(name = "offset", required = false) Integer offset) {
        return movementService.listMovementsByUserId(userId, coinTypeNameAbbr, movementType, limit, offset);
    }

}
