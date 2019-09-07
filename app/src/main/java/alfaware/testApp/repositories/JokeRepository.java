package alfaware.testApp.repositories;

import androidx.lifecycle.MutableLiveData;

import alfaware.testApp.abstract_classes.ListResult;
import alfaware.testApp.entities.Joke;
import alfaware.testApp.interfaces.JokeListener;
import alfaware.testApp.retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JokeRepository {

    private static JokeRepository instance;

    public static JokeRepository getInstance() {
        if (instance == null) {
            instance = new JokeRepository();
        }
        return instance;
    }

    //==============================================================================================

    public void get(MutableLiveData<ListResult<Joke>> list) {
        JokeListener service = RetrofitClientInstance.getRetrofitInstance().create(JokeListener.class);
        service.get().enqueue(new Callback<Joke>() {
            @Override
            public void onResponse(Call<Joke> call, Response<Joke> response) {
                list.getValue().getResult().getData().add(response.body());
                list.postValue(list.getValue());
            }

            @Override
            public void onFailure(Call<Joke> call, Throwable t) {
                list.getValue().getResult().setException(new Exception(t.getMessage()));
                list.postValue(list.getValue());
            }
        });
    }
}
