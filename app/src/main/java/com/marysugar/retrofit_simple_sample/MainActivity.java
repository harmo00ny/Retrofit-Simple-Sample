package com.marysugar.retrofit_simple_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv);
        mProgressBar = findViewById(R.id.pb);
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(getListener());
    }

    private SearchView.OnQueryTextListener getListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, query);
                mSearchView.clearFocus();
                searchRepository(query);
                return true;
            }
        };
    }

    private void searchRepository(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        GithubService service = retrofit.create(GithubService.class);
        Observable<List<Repo>> observableList = service.listRepos(query);
        observableList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getRepositoryObserver());
    }

    private Observer<List<Repo>> getRepositoryObserver() {
        return new Observer<List<Repo>>() {
            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Complete");
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe");
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(List<Repo> repos) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getParent());
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(new MainAdapter(repos));
            }
        };
    }
}