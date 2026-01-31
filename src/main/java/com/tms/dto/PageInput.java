package com.tms.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageInput {
    private Integer page = 0;
    private Integer size = 10;
}
