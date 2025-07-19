package com.example.application.taskmanagement.service;

import com.example.application.TestcontainersConfiguration;
import com.example.application.security.dev.SampleUsers;
import com.example.application.taskmanagement.domain.Task;
import com.example.application.taskmanagement.domain.TaskRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class TaskServiceIT { // TODO Rename to TaskServiceTest to run it together with the unit tests.

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    Clock clock;

    @Test
    @WithUserDetails(SampleUsers.USER_USERNAME)
    public void tasks_are_stored_in_the_database_with_the_current_timestamp() {
        var now = clock.instant();
        var due = LocalDate.of(2025, 2, 7);
        taskService.createTask("Do this", due);
        assertThat(taskService.list(PageRequest.ofSize(1))).singleElement()
                .matches(task -> task.getDescription().equals("Do this") && due.equals(task.getDueDate())
                        && task.getCreationDate().isAfter(now));
    }

    @Test
    @WithUserDetails(SampleUsers.ADMIN_USERNAME)
    public void tasks_are_validated_before_they_are_stored() {
        assertThatThrownBy(() -> taskService.createTask("X".repeat(Task.DESCRIPTION_MAX_LENGTH + 1), null))
                .isInstanceOf(ValidationException.class);
    }
}
