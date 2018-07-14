package com.example.akash.rxbasics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/*  Flowable */

public class EightActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eight);

        Flowable<Integer> flowable= getIntegerFlowable();

        SingleObserver<Integer> singleObserver= getSingleObserver();

        flowable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribe(singleObserver);
    }

    private Flowable<Integer> getIntegerFlowable(){
        return Flowable.range(1, 100);
    }

    private SingleObserver<Integer> getSingleObserver(){
        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.v("Eight", integer+ "");
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
