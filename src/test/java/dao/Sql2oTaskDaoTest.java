package dao;

import models.Task;
import org.sql2o.*;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class Sql2oTaskDaoTest {
    private Sql2oTaskDao taskDao; //ignore me for now. We'll create this soon.
    private Connection conn; //must be sql2o class conn

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        taskDao = new Sql2oTaskDao(sql2o); //ignore me for now
        conn = sql2o.open(); //keep connection open through entire test so it does not get erased
    }

    public Task setupNewTask(){
        return new Task("mow the lawn", 1);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTaskSetsId() throws Exception {
        Task task = setupNewTask();
        int originalTaskId = task.getId();
        taskDao.add(task);
        assertNotEquals(originalTaskId, task.getId()); //how does this work?
    }

    @Test
    public void existingTasksCanBeFoundById() throws Exception {
        Task task = setupNewTask();
        taskDao.add(task); //add to dao (takes care of saving)
        Task foundTask = taskDao.findById(task.getId()); //retrieve
        assertEquals(task, foundTask); //should be the same
    }

    @Test
    public void noTasksAreFound() throws Exception {
        List<Task> allTasks = taskDao.getAll();
        assertEquals(0, allTasks.size());
    }

    @Test
    public void allTasksAreFound() throws Exception {
        Task task = setupNewTask();
        Task task2 = new Task ("wash the car", 2);
        taskDao.add(task);
        taskDao.add(task2);
        List<Task> allTasks = taskDao.getAll();
        assertEquals(2, allTasks.size());
    }

    @Test
    public void updateChangesTaskContent() throws Exception {
        String initialDescription = "mow the lawn";
        Task task = new Task (initialDescription, 1);
        taskDao.add(task);
        taskDao.update(task.getId(),"brush the cat", 1);
        Task updatedTask = taskDao.findById(task.getId()); //why do I need to refind this?
        assertNotEquals(initialDescription, updatedTask.getDescription());
    }

    @Test
    public void taskDeletesById() throws Exception {
        Task task = setupNewTask();
        Task task2 = new Task ("wash the car", 2);
        taskDao.add(task);
        taskDao.add(task2);
        taskDao.deleteById(2);
        List<Task> allTasks = taskDao.getAll();
        assertEquals(1, allTasks.size());
        assertTrue(allTasks.contains(task));
        assertFalse(allTasks.contains(task2));
    }

    @Test
    public void allTasksAreDeleted() throws Exception {
        Task task = setupNewTask();
        Task task2 = new Task ("wash the car", 2);
        taskDao.add(task);
        taskDao.add(task2);
        taskDao.clearAllTasks();
        List<Task> allTasks = taskDao.getAll();
        assertEquals(0, allTasks.size());
    }

    @Test
    public void categoryIdIsReturnedCorrectly() throws Exception {
        Task task = setupNewTask();
        int originalCatId = task.getCategoryId();
        taskDao.add(task);
        assertEquals(originalCatId, taskDao.findById(task.getId()).getCategoryId());
    }

    @Test
    public void taskDeletesByCategoryId() throws Exception {
        Task task = setupNewTask();
        Task task2 = new Task ("wash the car", 2);
        taskDao.add(task);
        taskDao.add(task2);
        taskDao.deleteByCategoryId(2);
        List<Task> allTasks = taskDao.getAll();
        assertEquals(1, allTasks.size());
        assertTrue(allTasks.contains(task));
        assertFalse(allTasks.contains(task2));
    }
}