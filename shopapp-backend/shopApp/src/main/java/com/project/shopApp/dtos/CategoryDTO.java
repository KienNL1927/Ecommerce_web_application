package com.project.shopApp.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDTO {
    @NotEmpty(message = "Category's must not be empty")
    private String name;
}
