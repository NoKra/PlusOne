import window_object.WindowObject;

public class Main {

    public static void main(String[] args) {
        WindowObject mainWindow = new WindowObject();
        mainWindow.changeWindowSize();

        mainWindow.getNav().toMainView();

        mainWindow.setWindowVisible();
        mainWindow.centerWindow();
    }
}
