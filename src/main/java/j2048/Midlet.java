package j2048;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

public class Midlet extends MIDlet implements CommandListener {
    private final Display display;
    private final Game2048Canvas canvas;
    

    public Midlet() {
        this.display = Display.getDisplay(this);
        this.canvas = new Game2048Canvas();
        this.canvas.setCommandListener(this);
    }

    public void startApp() {
        display.setCurrent(canvas);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        display.setCurrent(null);
    }

    public void commandAction(Command c, Displayable d) {
        if (c.getCommandType() == Command.EXIT) {
            destroyApp(false);
            notifyDestroyed();
        }
    }
}
