package cz.johrusk.myapp.Realm;

import io.realm.RealmObject;


/**
 * Class for RealmDatabase
 *
 * @author Josef Hruška (pepa.hruska@gmail.com)
 */

public class Recipe extends RealmObject {
    private String title;
    private String body;

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
}
