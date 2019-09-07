package alfaware.testApp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import alfaware.testApp.R;
import alfaware.testApp.databinding.ChuckNorrisActivityBinding;
import alfaware.testApp.utilities.Status;
import alfaware.testApp.viewmodels.ChuckNorrisActivityViewModel;

public class ChuckNorrisActivity extends AppCompatActivity {

    private ChuckNorrisActivityBinding binding;
    private ChuckNorrisActivityViewModel viewModel;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.chuck_norris_activity);
        viewModel = ViewModelProviders.of(this).get(ChuckNorrisActivityViewModel.class);

        init();
        setListeners();
        setObservers();
    }

    private void init() {
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, viewModel.getList().getValue().getResult().getData());

        binding.listView.setAdapter(adapter);
    }

    private void setListeners() {
        binding.floatingActionButton.setOnClickListener(v -> viewModel.get());
    }

    private void setObservers() {
        viewModel.getStatus().observe(this, integer -> {
            binding.status.empty.setVisibility(View.GONE);
            binding.status.error.setVisibility(View.GONE);
            binding.status.loading.setVisibility(View.GONE);

            if (integer != null) {
                switch (integer) {
                    case Status.STATUS_LOADING:
                        binding.status.loading.setVisibility(View.VISIBLE);
                        break;
                    case Status.STATUS_EMPTY:
                        binding.status.empty.setVisibility(View.VISIBLE);
                        break;
                    case Status.STATUS_ERROR:
                        binding.status.error.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        viewModel.getList().observe(this, jokeListResult -> {
            Exception exception = jokeListResult.getResult().getException();
            if (exception != null) {
                exception.printStackTrace();
                viewModel.setStatus(Status.STATUS_ERROR);
                Snackbar.make(getCurrentFocus(), exception.getMessage(), Snackbar.LENGTH_SHORT).show();
            } else if (jokeListResult.getResult().getData().isEmpty()) {
                viewModel.setStatus(Status.STATUS_EMPTY);
            } else {
                viewModel.setStatus(Status.STATUS_DONE);
            }
            adapter.notifyDataSetChanged();
        });
    }


}
