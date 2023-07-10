package co.com.bancolombia.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterAccountRequest {

    private long id;
    private String name;
    private String statusId;
}
