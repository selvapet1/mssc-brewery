package guru.springframework.msscbrewery.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class CustomerDto {
    private final UUID customerId;
    private final String customerName;

}
