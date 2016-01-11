package in.shaapps.mygola.view;

import java.util.List;

import in.shaapps.mygola.model.Activity;

public interface MainMvpView extends MvpView {

    void showRepositories(List<Activity> activityList);

    void showMessage(int stringId);

    void showProgressIndicator();
}
