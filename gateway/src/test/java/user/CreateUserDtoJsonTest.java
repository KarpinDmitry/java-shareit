package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItGateway;
import ru.practicum.user.dto.CreateUserDto;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ShareItGateway.class)
@JsonTest
class CreateUserDtoJsonTest {

    @Autowired
    private JacksonTester<CreateUserDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeUserDto_withNameAndEmail() throws Exception {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");

        var result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo("john.doe@example.com");
    }

}