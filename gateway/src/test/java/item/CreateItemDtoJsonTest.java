package item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.item.dto.CreateItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ShareItGateway.class)
@JsonTest
class CreateItemDtoJsonTest {

    @Autowired
    private JacksonTester<CreateItemDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeItemDto_withAllFields() throws Exception {
        CreateItemDto dto = new CreateItemDto();
        dto.setName("Drill");
        dto.setDescription("Power drill");
        dto.setAvailable(true);
        dto.setRequestId(1L);

        var result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Drill");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Power drill");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.requestId").isEqualTo(1);
    }

}