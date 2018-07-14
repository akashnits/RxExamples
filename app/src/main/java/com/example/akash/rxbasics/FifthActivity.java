package com.example.akash.rxbasics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
* Single
*
* SingleObserver
*
* One emission for sure
*/

public class FifthActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        Single<Note> noteSingle= getNoteObservable();

        noteSingle.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Note>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable= d;
                    }

                    @Override
                    public void onSuccess(Note note) {
                        Log.v("Fifth", note.getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private Single<Note> getNoteObservable(){
        return Single.create(new SingleOnSubscribe<Note>() {
            @Override
            public void subscribe(SingleEmitter<Note> emitter) throws Exception {
                if(!emitter.isDisposed()){
                    Note note= new Note(1, "Akash");
                    emitter.onSuccess(note);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
