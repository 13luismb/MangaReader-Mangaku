
package model;

import java.util.ArrayList;

/**
 *
 * @author VulcanoMachine 2.0
 */
public class MangaModel {
    
    private String name,synopsis,genre;
    private Boolean status;
    private int id;
    private ArrayList<ChapterModel> chapters;

    public ArrayList<ChapterModel> getChapters() {
        return chapters;
    }

    public void addChapter(ChapterModel chapter) {
        this.chapters.add(chapter);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
    
}
