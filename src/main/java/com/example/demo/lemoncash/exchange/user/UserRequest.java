package com.example.demo.lemoncash.exchange.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRequest {
    private String name;
    private String surname;
    private String alias;
    private String email;

    public User toUser() {
        return User.builder()
                .name(name)
                .surname(surname)
                .alias(StringUtils.lowerCase(alias))
                .email(StringUtils.lowerCase(email))
                .build();
    }
}
