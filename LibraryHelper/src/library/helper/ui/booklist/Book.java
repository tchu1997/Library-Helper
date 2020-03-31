/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.booklist;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author kumaq
 */

public  class Book {
    private SimpleStringProperty title;
    private SimpleStringProperty id;
    private SimpleStringProperty author;
    private SimpleStringProperty publisher;
    private SimpleStringProperty availability;
    public Book(String title, String i, String author, String pub, Boolean avail){
        this.title = new SimpleStringProperty(title);
        this.id = new SimpleStringProperty(i);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(pub);
        if (avail){
            this.availability = new SimpleStringProperty("Yes");
        }else{
            this.availability = new SimpleStringProperty("No");
        }
    }
    public String getTitle()
    {
        return title.get();
    }
    public String getId()
    {
        return id.get();
    }
    public String getAuthor()
    {
        return author.get();
    }
    public String getPublisher()
    {
        return publisher.get();
    }
    public String getAvailability()
    {
        return availability.get();
   }
    public boolean containsSomeText(String text){
        //String filteredText = text.toLowerCase();
        if (getTitle().toLowerCase().contains(text)){
            return true;
        }
        else if (getId().toLowerCase().contains(text)){
            return true;
        }
        else if (getAuthor().toLowerCase().contains(text)){
            return true;
       }
        else if (getPublisher().toLowerCase().contains(text)){
            return true;
        }
        return false;
    }
    
}

