package com.jobflow.job.dto;

import java.time.LocalDateTime;

public record JobResponse (Long id,
                           String title,
                           String description,
                           String location,
                           String salary,
                           Long recruiterId,
                           String recruiterName,
                           LocalDateTime createdAt){
}
