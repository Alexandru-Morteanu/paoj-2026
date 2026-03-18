package com.pao.laboratory03.bonus;

import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private static TaskService instance;
    private final Map<String, Task> tasksById = new HashMap<>();
    private final Map<Priority, List<Task>> tasksByPriority = new EnumMap<>(Priority.class);
    private final List<String> auditLog = new ArrayList<>();
    private int taskCounter = 1;

    private TaskService() {
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
    }

    public static TaskService getInstance() {
        if (instance == null) instance = new TaskService();
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", taskCounter++);
        if (tasksById.containsKey(id)) throw new DuplicateTaskException("ID deja existent: " + id);

        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);

        auditLog.add(String.format("[ADD] %s: '%s' (%s)", id, title, priority));
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = getTaskOrThrow(taskId);
        task.setAssignee(assignee);
        auditLog.add(String.format("[ASSIGN] %s → %s", taskId, assignee));
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = getTaskOrThrow(taskId);
        if (!task.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(task.getStatus(), newStatus);
        }
        auditLog.add(String.format("[STATUS] %s: %s → %s", taskId, task.getStatus(), newStatus));
        task.setStatus(newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.getOrDefault(priority, Collections.emptyList()));
    }

    public Map<Status, Long> getStatusSummary() {
        return tasksById.values().stream()
                .collect(Collectors.groupingBy(Task::getStatus, () -> new EnumMap<>(Status.class), Collectors.counting()));
    }

    public List<Task> getUnassignedTasks() {
        return tasksById.values().stream()
                .filter(t -> t.getAssignee() == null)
                .collect(Collectors.toList());
    }

    public double getTotalUrgencyScore(int baseDays) {
        return tasksById.values().stream()
                .filter(t -> t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED)
                .mapToDouble(t -> t.getPriority().calculateScore(baseDays))
                .sum();
    }

    public void printAuditLog() {
        auditLog.forEach(System.out::println);
    }

    private Task getTaskOrThrow(String id) {
        if (!tasksById.containsKey(id)) throw new TaskNotFoundException("Task-ul '" + id + "' nu a fost găsit");
        return tasksById.get(id);
    }
}

