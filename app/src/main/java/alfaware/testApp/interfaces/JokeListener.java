package alfaware.testApp.interfaces;

import alfaware.testApp.entities.Joke;
import retrofit2.Call;
import retrofit2.http.GET;

public interface JokeListener {

    @GET("/jokes/random")
    Call<Joke> get();

}
