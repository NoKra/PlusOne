package window_object;

import javax.swing.*;
import java.awt.*;

public class WindowObject {
    private final JFrame mainFrame;
    private final Container contentContainer;
    private final SpringLayout layout = new SpringLayout();
    private final WindowNav nav = new WindowNav(this);


    public WindowObject() {
        mainFrame = new JFrame();
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setTitle("Plus One");

        contentContainer = mainFrame.getContentPane();
        contentContainer.setLayout(layout);

        //TODO: Refer back to nav stuff for when there are more frames
    }

    public Container getContentContainer() {
        return contentContainer;
    }

    public SpringLayout getLayout() {
        return layout;
    }

    public WindowNav getNav() {
        return nav;
    }

    public void changeWindowSize() {
        //TODO: include some common dimensions here
        mainFrame.setSize(500, 700);
    }

    public void clearWindow() {
        contentContainer.removeAll();
        contentContainer.repaint();
    }

    public void centerWindow() {
        mainFrame.setLocationRelativeTo(null);
    }

    public void setWindowVisible() {
        mainFrame.setVisible(true);
    }

    public void showNewContent() {
        contentContainer.revalidate();
    }

}
