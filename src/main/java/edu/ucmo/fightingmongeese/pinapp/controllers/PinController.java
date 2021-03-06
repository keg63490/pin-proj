package edu.ucmo.fightingmongeese.pinapp.controllers;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Primary controller for routing requests sent to API
 * All endpoints are prepended by /api/
 */
@RestController
@RequestMapping("/api")
public class PinController {

    private final PinService pinService;

    @Autowired
    public PinController(PinService pinService) {
        this.pinService = pinService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PinController.class);

    /**
     * Method for handling adding new one-time-use PINs to the database.
     *
     * @param pin     The PIN will be supplied to the method via the Request Body in JSON.
     *                e.g.:
     *                {
     *                "account": "bob",
     *                "create_user": "user"
     *                }
     *                The client can optionally provide an expiration datetime
     *                which defaults to 30 minutes if not given
     * @param request Request metadata for extracting IP Address
     */
    @PostMapping(value = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Pin add(@Validated(Pin.Add.class) @RequestBody Pin pin, HttpServletRequest request) {

        logger.info("New PIN received from User: {} at {} -- Account: {} | PIN: {}",
                pin.getCreate_user(), request.getRemoteAddr(), pin.getAccount(), pin.getPin());
        pin.setCreate_ip(request.getRemoteAddr());
        Pin result = pinService.add(pin);
        logger.info("New PIN successfully saved: Account: {} | PIN: {} | IP: {}", result.getAccount(), result.getPin(), request.getRemoteAddr());
        return result;
    }

    /**
     * Method for handling PIN claim attempts.  All data is passed in the request body via JSON
     * <p>
     *
     * @param pin     Java representation of PIN translated from JSON
     *                The only required fields are account and create_user
     * @param request Request metadata for extracting IP Address
     */
    @PostMapping(value = "/claim")
    public Pin claim(@Validated(Pin.Claim.class) @RequestBody Pin pin, HttpServletRequest request) {

        logger.info("Claim received from user: {} at {} with PIN: {}",
                pin.getClaim_user(), request.getRemoteAddr(), pin.getPin());
        pin.setClaim_ip(request.getRemoteAddr());
        Pin result = pinService.claim(pin);

        logger.info("Pin successfully claimed: Account: {} | PIN: {} | IP: {}", result.getAccount(), result.getPin(), request.getRemoteAddr());

        return result;
    }

    /**
     * Method for handling cancel requests.  Works by simply having the user claim their own pin
     *
     * @param pin     The supplied PIN must have the claim_user, pin, and claim_account fields supplied
     * @param request Request metadata for extracting IP Address
     */
    @PostMapping(value = "/cancel")
    public Pin cancel(@Validated(Pin.Cancel.class) @RequestBody Pin pin, HttpServletRequest request) {

        logger.info("Cancel request received from user: {} at {} with PIN: {} for account: {}",
                pin.getClaim_user(), request.getRemoteAddr(), pin.getPin(), pin.getAccount());

        pin.setClaim_ip(request.getRemoteAddr());
        Pin result = pinService.cancel(pin);

        logger.info("Pin successfully canceled: Account: {} | PIN: {} | IP: {}", result.getAccount(), result.getPin(), request.getRemoteAddr());
        return result;
    }
}

