package com.example.application.taskmanagement.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    // If you don't need a total row count, Slice is better than Page as it only performs a select query.
    // Page performs both a select and a count query.
    Slice<Task> findAllBy(Pageable pageable);
}
