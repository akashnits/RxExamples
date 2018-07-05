package com.example.akash.rxbasics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SecondActivity extends AppCompatActivity {


    public static final String TAG = SecondActivity.class.getSimpleName();
    private CompositeDisposable mCompositeDisposable= new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Observable<String> observable= Observable.fromArray("Ant", "Ape", "Bat", "Bee", "Bear", "Butterfly",
                "Cat", "Crab", "Cod", "Dog", "Dove", "Fox", "Frog");

        DisposableObserver<String> observer1= new DisposableObserver<String>() {

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
        };

        DisposableObserver<String> observer2= new DisposableObserver<String>() {

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
        };


        //subscribe on pool of threads ; observe on main thread (data stream emitted by observable is received on main thread);
        // subscribed by observer

        mCompositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                })
                .subscribeWith(observer1));

        mCompositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("c");
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .subscribeWith(observer2));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dispose the observer (no longer observing on observable)
        mCompositeDisposable.clear();
    }
}
