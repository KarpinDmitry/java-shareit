package booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ShareItGateway.class)
@JsonTest
class CreateBookingDtoJsonTest {

    @Autowired
    private JacksonTester<CreateBookingDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeBookingDto_withFormattedDates() throws Exception {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.of(2026, 1, 1, 12, 0, 0));
        dto.setEnd(LocalDateTime.of(2026, 1, 1, 13, 0, 0));

        var result = json.write(dto);

        assertThat(result).extractingJsonPathValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isNotNull();
        assertThat(result).extractingJsonPathValue("$.end").isNotNull();
    }

}