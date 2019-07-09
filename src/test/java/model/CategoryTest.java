package model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CategoryTest {
    private static final String CATEGORY_TITLE = "food";

    @Test
    public void categoryMayHaveParentCategory() {
        var parentCategory = new Category(CATEGORY_TITLE);
        var category = new Category(CATEGORY_TITLE,parentCategory);
        Assert.assertNotNull(category.getParent());
    }

    @Test
    public void categoryMayNotHaveParentCategory() {
        var category = new Category(CATEGORY_TITLE);
        assertNull(category.getParent());
    }

    @Test
    public void categoryShouldHaveTitle() {
        String title = "Food";
        var category = new Category(title);
        Assert.assertEquals(title,category.getTitle());

        category = new Category(title, new Category("Parent Category"));
        assertEquals(title,category.getTitle());
    }
}