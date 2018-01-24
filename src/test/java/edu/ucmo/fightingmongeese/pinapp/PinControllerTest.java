package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Validator;
import java.util.Optional;

import static edu.ucmo.fightingmongeese.pinapp.TestUtils.asJsonString;
import static edu.ucmo.fightingmongeese.pinapp.TestUtils.getClaimPin;
import static edu.ucmo.fightingmongeese.pinapp.TestUtils.getCompletePin;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PinController.class)
public class PinControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PinService pinService;

    @MockBean
    Validator validator;

    @MockBean
    PinRepository pinRepository;

    @Autowired
    private WebApplicationContext wac;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @Test
    public void test_add_pin_success() throws Exception {
        Pin pin = getNewPin();
        when(pinService.add(any(Pin.class))).thenReturn(pin);
        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
//        when(pinRepository.save(any(Pin.class))).thenReturn(pin);
//        when(pinRepository.save(any())).thenReturn(getNewPin());
        mockMvc.perform(
                post("/api/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pin)))
                .andExpect(status().isOk());
        verify(pinService, times(1)).add(pin);
    }

    @Test
    public void test_claim_pin_success() throws Exception {
        Pin pin = getClaimPin();
        when(pinService.claim(pin)).thenReturn(getCompletePin());
        mockMvc.perform(
                post("/api/claim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pin)))
                .andExpect(status().isOk());
        verify(pinService, times(1)).claim(pin);
    }

    @Test
    public void test_cancel_pin_success() throws Exception {
        Pin pin = getClaimPin();
        when(pinService.cancel(pin)).thenReturn(getCompletePin());
        mockMvc.perform(
                post("/api/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pin)))
                .andExpect(status().isOk());
        verify(pinService, times(1)).cancel(pin);
    }


    private static Pin getNewPin() {
        return new Pin("SallysSavings2", "127.0.0.1", "sally");
    }


}
