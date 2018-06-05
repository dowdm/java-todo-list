package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void NewCategoryObjectGetsCorrectlyCreated_true() throws Exception {
        Category newCategory = new Category("home");
        assertEquals(true, newCategory instanceof Category);
    }

    @Test
    public void getName_getsName_home() {
        Category newCategory = new Category("home");
        assertEquals("home", newCategory.getName());
    }

    @Test
    public void setName_setsName_work() {
        Category newCategory = new Category("home");
        newCategory.setName("work");
        assertEquals("work", newCategory.getName());
    }

}