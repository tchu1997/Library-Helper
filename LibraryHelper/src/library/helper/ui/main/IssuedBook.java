/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.helper.ui.main;

import java.sql.Timestamp;
import library.helper.ui.booklist.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author kumaq
 */

public  class IssuedBook {
    private SimpleStringProperty title;
    private SimpleStringProperty id;
    private SimpleStringProperty author;
    private SimpleStringProperty publisher;
    private SimpleStringProperty issuedTime;
    private SimpleStringProperty renewCount;
    public IssuedBook(String title, String id, String author, String pub, String time, String renewCount){
        this.title = new SimpleStringProperty(title);
        this.id = new SimpleStringProperty(id);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(pub);
        this.issuedTime = new SimpleStringProperty(time);
        this.renewCount = new SimpleStringProperty(renewCount);
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
    public String getIssuedTime() {
        return issuedTime.get();
    }
    public String getRenewCount(){
        return renewCount.get();
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
        }else if(getRenewCount().toLowerCase().contains(text)){
            return true;
        }
        return false;
    }
    
}

