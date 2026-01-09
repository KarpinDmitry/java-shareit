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

@JsonTest
@ContextConfiguration(classes = ShareItGateway.class)
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

    @Test
    void deserializeBookingDto_fromFormattedJson() throws Exception {
        String input = """
                {
                  "itemId": 1,
                  "start": "2026-01-01T12:00:00",
                  "end": "2026-01-01T13:00:00"
                }
                """;

        var result = json.parse(input);

        assertThat(result.getObject().getItemId()).isEqualTo(1L);
        assertThat(result.getObject().getStart())
                .isEqualTo(LocalDateTime.of(2026, 1, 1, 12, 0, 0));
        assertThat(result.getObject().getEnd())
                .isEqualTo(LocalDateTime.of(2026, 1, 1, 13, 0, 0));
    }
}