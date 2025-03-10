package org.ghrepos.models;

public class Repo{
    public String name;
    public User owner;
    public String branches_url;
    public Branch[] branches;
    public String fork;

    /// get Branches_url returns url without the "{/branch}" ending that comes from a github api
    public String getBranches_url() {
        return branches_url.replace("{/branch}", "");
    }

    /// Function toJson() returns a json in a string that contains repo_name, owner login, branches with their names and ssh
    public String toJson(){
        String json = "{\"repo_name\":\"" + name + "\",\"owner\":\"" + owner.login + "\",\"branches\":{";
        if(branches != null){
            for(Branch b : branches){
                json += b.toJson();
            }
        }
        json += "}}";
        return json;
    }
}
