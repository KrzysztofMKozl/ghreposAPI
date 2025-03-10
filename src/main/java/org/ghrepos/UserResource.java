package org.ghrepos;

import com.google.gson.Gson;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.ghrepos.models.Branch;
import org.ghrepos.models.Repo;
import org.jboss.resteasy.reactive.RestMulti;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

@Path("/users")
public class UserResource {
    HttpClient client = HttpClient.newHttpClient();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

/// getUserRepos sends a Multi<String> to the client with github users repos and their branches and ssh
/// https://api.github.com/users/{name}/repos
///
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}")
    public Multi<String> getUserRepos(@PathParam("name") String name) {
        try{
            Multi<String> out;
            Gson gson = new Gson();
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/users/" + name + "/repos"))
                    .header("Accept", "application/vnd.github+json")
                    .build();
            HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if(getResponse.statusCode() == 404){
                out = Multi.createFrom().items("{\"status\":\"404\",\"message\":\"User does not exist\"}");
                return RestMulti.fromMultiData(out).status(404).build();
            };
            Repo[] repos = Arrays.stream(gson.fromJson(getResponse.body(),Repo[].class)).filter(repo -> repo.fork.equals("false")).toArray(Repo[]::new);

            return Multi.createFrom().iterable(Arrays.asList(repos))
                    .onItem().transform(repo -> {
                        repo.branches = getBranches(repo.branches_url.replace("{/branch}", ""));
                        return repo.toJson();
                    });
        }
        catch(InterruptedException | IOException e){
            return Multi.createFrom().empty();
        }

    }

    /// getBranches returns an array Branch[] of Branches from the github api url
    /// returns an empty array when InterruptedException or IOException occurs during request send
    public Branch[] getBranches(String url){
        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            return gson.fromJson(getResponse.body(), Branch[].class);
        }catch(InterruptedException | IOException e){
            return new Branch[0];
        }
    }
}
