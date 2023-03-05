package window_object;

import views.AddSentenceView;
import views.MainView;

public class WindowNav {
    WindowObject mainWindow;

    public WindowNav(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        toAddSentences();
    }

    public void toMainView() {
        mainWindow.clearWindow();
        MainView newMain = new MainView(mainWindow);
        mainWindow.showNewContent();
    }

    public void toAddSentences() {
        mainWindow.clearWindow();
        AddSentenceView newSentences = new AddSentenceView(mainWindow);
        mainWindow.showNewContent();
    }
}
