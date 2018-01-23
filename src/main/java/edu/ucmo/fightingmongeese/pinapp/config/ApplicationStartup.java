package edu.ucmo.fightingmongeese.pinapp.config;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
public class ApplicationStartup
        implements ApplicationListener<ContextRefreshedEvent> {

    private final PinRepository pinRepository;


    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    public ApplicationStartup(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        SecureRandom random = new SecureRandom();

        random.setSeed("totessecure".getBytes());

        Pin pin = new Pin("BobsChecking", "192.168.0.22", "bob");
        int randomPin = random.nextInt(1000000);
        String pinString = String.format("%06d", randomPin);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusMinutes(30));
        pinRepository.save(pin);

        pin = new Pin("BobsSavings", "192.168.0.35", "bob");
        int num = 1234;
        pinString = String.format("%06d", num);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusMinutes(30));
        pinRepository.save(pin);

        pin = new Pin("SallysSavings", "192.168.0.35", "sally");
        randomPin = random.nextInt(1000000);
        pinString = String.format("%06d", randomPin);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusMinutes(30));
        pinRepository.save(pin);

        logger.info("Database pre-populated with 3 PINs");
    }
}
