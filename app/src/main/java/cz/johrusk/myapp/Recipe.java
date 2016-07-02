package cz.johrusk.myapp;

/**
 * Created by Pepa on 02.07.2016.
 */

public class Recipe {

    public Recipe(){}
    public Recipe(String t,String b){
        this.title = t;
        this.body = b;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String title;
    private String body;
}
