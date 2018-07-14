package com.example.akash.rxbasics;

/*Simple observable emitting multiple Notes
* Observable - Emission: Multiple or none
* Observer
*/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FourthActivity extends AppCompatActivity {

    public static final String TAG= FourthActivity.class.getSimpleName();

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        Observable<Note> noteObservable= getNoteObservable();

        noteObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new Observer<Note>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable= d;
                    }

                    @Override
                    public void onNext(Note note) {
                        Log.v(TAG, note.getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private Observable<Note> getNoteObservable(){
        final List<Note> noteList = prepareNoteList();

        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {

                for(Note note: noteList){
                    if(!emitter.isDisposed())
                        emitter.onNext(note);
                }

                if(!emitter.isDisposed())
                    emitter.onComplete();
            }
        });
    }

    private List<Note> prepareNoteList(){
        List<Note> list= new ArrayList<>();
        list.add(new Note(1, "Akash"));
        list.add(new Note(2, "Rishabh"));

        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
