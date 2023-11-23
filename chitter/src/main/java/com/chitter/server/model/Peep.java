package com.chitter.server.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "peeps")
public class Peep {

    @Id
    private String id;

    private String username;

    private String name;

    private String content;

    private String dateCreated;

    public Peep(String username, String name, String content, String dateCreated) {
        this.username = username;
        this.name = name;
        this.content = content;
        this.dateCreated = dateCreated;
    }

    public String getid() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Peep [id: " + id + ", username: " + username + ", name: " + name + ", content: " + content + ", dateCreated: " + dateCreated + "]";
    }
}
