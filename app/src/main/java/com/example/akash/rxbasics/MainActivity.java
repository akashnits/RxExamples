package com.example.akash.rxbasics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> observable= Observable.just("Ant", "Bee", "Cat");

        //subscribe on pool of threads ; observe on main thread (data stream emitted by observable is received on main thread);
        // subscribed by observer
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //called when observer subscribes to observable
                        mDisposable= d;
                    }

                    @Override
                    public void onNext(String s) {
                        Log.v(TAG, s + " ");
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dispose the observer (no longer observing on observable)
        mDisposable.dispose();
    }
}
