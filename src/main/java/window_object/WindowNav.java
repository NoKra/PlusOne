package window_object;

import views.MainView;

public class WindowNav {
    WindowObject mainWindow;

    public WindowNav(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void toMainView() {
        mainWindow.clearWindow();
        MainView newMain = new MainView(mainWindow);
        mainWindow.showNewContent();
    }
}
