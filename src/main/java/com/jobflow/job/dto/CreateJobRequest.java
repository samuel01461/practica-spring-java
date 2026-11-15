package com.jobflow.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateJobRequest (@NotBlank String title,
                                @NotBlank String description,
                                @NotBlank String location,
                                @NotBlank String salary,
                                @NotNull Long recruiterId){
}
