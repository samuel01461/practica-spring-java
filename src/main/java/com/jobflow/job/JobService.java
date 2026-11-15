package com.jobflow.job;

import com.jobflow.user.User;
import com.jobflow.user.UserRepository;
import com.jobflow.job.dto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public JobResponse create(CreateJobRequest request) {
        User recruiter = userRepository.findById(request.recruiterId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recruiter no encontrado"));
        Job job = new Job();

        job.setTitle((request.title()));
        job.setDescription(request.description());
        job.setLocation(request.location());
        job.setSalary(request.salary());
        job.setRecruiter(recruiter);

        return toResponse(jobRepository.save(job));
    }

    @Transactional(readOnly = true)
    public Page<JobResponse> findAll(Pageable pageable, String title){
        Page<Job> jobs;
        if(title == null || title.isBlank()) {
            jobs = jobRepository.findAll(pageable);
        } else {
            jobs = jobRepository.findByTitleContainingIgnoreCase(title, pageable);
        }

        return jobs.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public JobResponse findById(Long id){
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job no encontrado"));
        return toResponse(job);
    }

    @Transactional
    public void delete(Long id){
        if(!jobRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job no encontrado");
        }

        jobRepository.deleteById(id);
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getSalary(),
                job.getRecruiter().getId(),
                job.getRecruiter().getName(),
                job.getCreatedAt()
        );
    }
}
