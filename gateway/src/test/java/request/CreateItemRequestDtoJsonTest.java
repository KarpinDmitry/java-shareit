package request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.request.dto.CreateItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ShareItGateway.class)
@JsonTest
class CreateItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<CreateItemRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeRequestDto_withDescription() throws Exception {
        CreateItemRequestDto dto = new CreateItemRequestDto();
        dto.setDescription("Need a drill");

        var result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Need a drill");
    }

    @Test
    void deserializeRequestDto_fromJson() throws Exception {
        String input = """
                {
                  "description": "Need a drill"
                }
                """;

        var result = json.parse(input);

        assertThat(result.getObject().getDescription()).isEqualTo("Need a drill");
    }
}