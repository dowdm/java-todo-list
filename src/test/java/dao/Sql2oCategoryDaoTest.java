package dao;

import models.Category;
import models.Task;
import org.sql2o.*;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;


public class Sql2oCategoryDaoTest {
    private Sql2oCategoryDao categoryDao; //ignore me for now. We'll create this soon.
    private Sql2oTaskDao taskDao; //ignore me for now. We'll create this soon.
    private Connection conn; //must be sql2o class conn

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        categoryDao = new Sql2oCategoryDao(sql2o);
        taskDao = new Sql2oTaskDao(sql2o);
        conn = sql2o.open(); 
    }

    public Category setupNewCategory(){
        return new Category("home");
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        Category category = new Category ("home");
        int originalCategoryId = category.getId();
        categoryDao.add(category);
        assertNotEquals(originalCategoryId, category.getId()); //how does this work?
    }

    @Test
    public void existingCategorysCanBeFoundById() throws Exception {
        Category category = new Category ("home");
        categoryDao.add(category); //add to dao (takes care of saving)
        Category foundCategory = categoryDao.findById(category.getId()); //retrieve
        assertEquals(category, foundCategory); //should be the same
    }

    @Test
    public void noCategoriesAreFound() throws Exception {
        List<Category> allCategories = categoryDao.getAll();
        assertEquals(0, allCategories.size());
    }

    @Test
    public void allCategoriesAreFound() throws Exception {
        Category category = new Category ("home");
        Category category2 = new Category ("work");
        categoryDao.add(category);
        categoryDao.add(category2);
        List<Category> allCategories = categoryDao.getAll();
        assertEquals(2, allCategories.size());
    }

    @Test
    public void categoryIsUpdated() throws Exception {
        Category category = new Category ("home");
        categoryDao.add(category);
        int id = category.getId();
        categoryDao.update(id, "school");
        Category updatedCategory = categoryDao.findById(id);
        assertEquals("school", updatedCategory.getName());
    }

    @Test
    public void categoryDeletesById() throws Exception {
        Category category = new Category ("home");
        Category category2 = new Category ("work");
        categoryDao.add(category);
        categoryDao.add(category2);
        categoryDao.deleteById(2);
        List<Category> allCategorys = categoryDao.getAll();
        assertEquals(1, allCategorys.size());
        assertTrue(allCategorys.contains(category));
        assertFalse(allCategorys.contains(category2));
    }

    @Test
    public void allCategoriesAreDeleted() throws Exception {
        Category category = new Category ("home");
        Category category2 = new Category ("work");
        categoryDao.add(category);
        categoryDao.add(category2);
        categoryDao.clearAllCategories();
        List<Category> allCategories = categoryDao.getAll();
        assertEquals(0, allCategories.size());
    }

    @Test
    public void getAllTasksByCategoryReturnsTasksCorrectly() throws Exception {
        Category category = setupNewCategory();
        categoryDao.add(category);
        int categoryId = category.getId();
        Task newTask = new Task("mow the lawn", categoryId);
        Task otherTask = new Task("pull weeds", categoryId);
        Task thirdTask = new Task("trim hedge", categoryId);
        taskDao.add(newTask);
        taskDao.add(otherTask); //we are not adding task 3 so we can test things precisely.
        assertEquals(2, categoryDao.getAllTasksByCategory(categoryId).size());
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(newTask));
        assertTrue(categoryDao.getAllTasksByCategory(categoryId).contains(otherTask));
        assertFalse(categoryDao.getAllTasksByCategory(categoryId).contains(thirdTask)); //things are accurate!
    }

}
