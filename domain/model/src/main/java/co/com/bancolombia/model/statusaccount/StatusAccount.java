package co.com.bancolombia.model.statusaccount;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatusAccount {

    private final String status;
}
