package local.newMock.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.newMock.Model.RequestDTO;
import local.newMock.Model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO){

        try {
            String clientID = requestDTO.getClientId();
            char firstDigit = clientID.charAt(0);
            BigDecimal maxLimit;
            String rqUID = requestDTO.getRqUID();
            String monetaryUnit;

            if (firstDigit == '8') {
                maxLimit = new BigDecimal(2000);
                monetaryUnit= "US";
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000);
                monetaryUnit= "EU";
            } else {
                maxLimit = new BigDecimal(10000);
                monetaryUnit= "RUB";
            }

            Random random = new Random();
            BigDecimal balanceNotRounded = maxLimit.multiply(BigDecimal.valueOf(random.nextDouble()));
            BigDecimal balance = balanceNotRounded.setScale(2, RoundingMode.HALF_UP);

            ResponseDTO responseDTO = new ResponseDTO();

            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientID);
            responseDTO.setAccount(responseDTO.getAccount());
            responseDTO.setCurrency(monetaryUnit);
            responseDTO.setBalance(balance);
            responseDTO.setMaxLimit(maxLimit);


            log.error("*** RequestDTO ***" + "{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("*** ResponseDTO ***" + "{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
