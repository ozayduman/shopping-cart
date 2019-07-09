package model;

public class Category {
    //TODO: Optional can be used
    private Category parent;
    private String title;

    public Category(String title) {
        this.title = title;
    }

    public Category(String title, Category parent) {
        this(title);
        this.parent = parent;
    }

    public Category getParent() {
        return parent;
    }

    public String getTitle() {
        return title;
    }
}
