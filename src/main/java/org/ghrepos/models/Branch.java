package org.ghrepos.models;

public class Branch {
    public String name;
    public Commit commit;

    public String toJson() {
        return "{\"name\":\""+name+"\",\"commit_sha\":\""+commit.sha+"\"}";
    }
}
