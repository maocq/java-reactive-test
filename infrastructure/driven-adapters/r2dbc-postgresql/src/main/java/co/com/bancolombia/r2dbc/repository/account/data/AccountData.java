package co.com.bancolombia.r2dbc.repository.account.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("account")
public class AccountData {

    @Id
    private long id;
    private String name;
    private String status;
}
