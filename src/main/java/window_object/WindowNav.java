package window_object;

import views.AddSentenceView;
import views.BrowseView;
import views.MainView;
import views.SetLinkView;

import javax.swing.*;

public class WindowNav {

    WindowObject mainWindow;

    public WindowNav(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void toMain() {
        mainWindow.clearWindow();
        new MainView(mainWindow);
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
