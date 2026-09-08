package com.example.iocdi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Category {
    private Long categoryId;
    private String categoryName;
    private String description;
    private String useYn;
    private LocalDateTime createdAt;
}
