package eu.qm.fiszki.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class Flashcard implements Serializable {

    public static final String columnFlashcardId = "id";
    public static final String columnFlashcardWord =  "word";
    public static final String columnFlashcardTranslation =  "translation";
    public static final String columnFlashcardPriority =  "priority";
    public static final String columnFlashcardCategoryID =  "categoryID";

    @DatabaseField(generatedId = true, allowGeneratedIdInsert=true)
    private int id;
    @DatabaseField
    public String word;
    @DatabaseField
    private String translation;
    @DatabaseField
    private int priority;
    @DatabaseField
    private int categoryID;

    public Flashcard() {
    }

    public Flashcard (int id, String word, String translation, int priority, int categoryID) {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.priority = priority;
        this.categoryID = categoryID;
    }

    public Flashcard (String word, String translation, int priority, int categoryID) {
        this.word = word;
        this.translation = translation;
        this.priority = priority;
        this.categoryID = categoryID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getCategoryId() {
        return categoryID;
    }

    public void setCategoryId(int categoryID) {
        this.categoryID = categoryID;
    }
}



