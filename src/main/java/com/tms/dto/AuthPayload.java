package com.tms.dto;

import com.tms.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthPayload {
    private String token;
    private User user;
}
