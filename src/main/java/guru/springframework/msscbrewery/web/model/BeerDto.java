package guru.springframework.msscbrewery.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class BeerDto {
    private final UUID id;
    private final String beerName;
    private final String beerStyle;
    private final Long upc;

}
