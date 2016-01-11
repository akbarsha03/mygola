package in.shaapps.mygola.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
