package alfaware.testApp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import alfaware.testApp.abstract_classes.DataResult;
import alfaware.testApp.abstract_classes.ListResult;
import alfaware.testApp.entities.Joke;
import alfaware.testApp.repositories.JokeRepository;
import alfaware.testApp.utilities.Status;

public class ChuckNorrisActivityViewModel extends SuperViewModel {

    private MutableLiveData<ListResult<Joke>> list = new MutableLiveData<>();

    public LiveData<ListResult<Joke>> getList() {
        if (this.list.getValue() == null) {
            list.setValue(new ListResult<>());
            list.getValue().setResult(new DataResult<>());
            list.getValue().getResult().setData(new ArrayList<>());
        }
        return list;
    }

    public void get() {
        setStatus(Status.STATUS_LOADING);
        JokeRepository.getInstance().get(list);
    }

}
