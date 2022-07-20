package com.denofprogramming.model;

/**
 * javadoc for PMD satisfaction :)
 */
public class Course extends DomainObject {
    /**
     * javadoc for PMD satisfaction :)
     */
    private String name;

    /**
     * javadoc for PMD satisfaction :)
     */
    public Course() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Course [name=" + name + "]";
    }
}
