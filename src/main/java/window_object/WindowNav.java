package window_object;

import views.AddSentenceView;
import views.BrowseView;
import views.HomeView;
import views.SetLinkView;

import javax.swing.*;

public class WindowNav {

    WindowObject mainWindow;

    //Constructor
    public WindowNav(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void toHome(boolean isStartup) {
        mainWindow.clearWindow();
        new HomeView(mainWindow, isStartup);
        mainWindow.showNewContent();
    }

    public void toAddSentence() {
        mainWindow.clearWindow();
        new AddSentenceView(mainWindow);
        mainWindow.showNewContent();
    }

    public void toBrowse() {
        mainWindow.clearWindow();
        new BrowseView(mainWindow);
        mainWindow.showNewContent();
    }

}
