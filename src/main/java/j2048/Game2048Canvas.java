package j2048;

import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Game2048Canvas extends Canvas {
                                           //2        4         8         16        32        64        128       256       512      1024      2048      4096...
    private final int[] backgroudColors = {0xEEE4DA, 0xEDE0C8, 0xF2B179, 0xF59563, 0xF67C5F, 0xF65E3B, 0xEDCF72, 0xEDCC61, 0xEDC850, 0xedc53f, 0xedc22e, 0x3c3a32};
    
    int freeCount;
    private final Font font;
    private final Random random = new Random();
    short[][] state = new short[4][4];

    public Game2048Canvas() {
        setFullScreenMode(true);
        this.font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);

        addCommand(command);
        freeCount = 16;
        insertNew();
        insertNew();
    }
    
    private Command command = new Command("Exit", Command.EXIT, 60);

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);

        boolean isModified = false;
        switch (action) {
            case Canvas.LEFT:
                isModified = moveLeft();
                break;
                
            case Canvas.UP:
                isModified = moveUp();
                break;

            case Canvas.RIGHT:
                isModified = moveRight();
                break;

            case Canvas.DOWN:
                isModified = moveDown(); 
                break;
        }
        if (!isModified)  {
            return;
        }
        
        insertNew();       
        repaint();
    }

    private boolean moveDown() {
        boolean result = false;
        int[] maxCheck = {3, 3, 3, 3};
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                if (state[i][j] == 0) {
                    continue;
                }

                //Найдем индекс следующего "занятого" элемента
                int nextFilled = -1;
                for (int k = i+1; k <= maxCheck[j]; k++) {
                    if (state[k][j] != 0) {
                        nextFilled = k;
                        break;
                    }
                }

                if (nextFilled == -1) {
                    //в конце 0 - двигаем в конец (если это возможно)
                    if (maxCheck[j] != i) {
                        state[maxCheck[j]][j] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                } else if (state[i][j] == state[nextFilled][j]) {
                    //в конце точно такой же tile - объединяем их
                    state[nextFilled][j] *= 2;
                    state[i][j] = 0;
                    maxCheck[j] = nextFilled - 1;
                    freeCount++;
                    result = true;
                } else {
                    //в конце какой-то обычный tile. Просто двигаем в плотную к нему
                    if (nextFilled - 1 != i) {
                        state[nextFilled - 1][j] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private boolean moveRight() {
        boolean result = false;
        int[] maxCheck = {3, 3, 3, 3};
        for (int j = 2; j >= 0; j--) {
            for (int i = 0; i < 4; i++) {

                if (state[i][j] == 0) {
                    continue;
                }

                //Найдем индекс следующего "занятого" элемента
                int nextFilled = -1;
                for (int k = j+1; k <= maxCheck[i]; k++) {
                    if (state[i][k] != 0) {
                        nextFilled = k;
                        break;
                    }
                }

                if (nextFilled == -1) {
                    //в конце 0 - двигаем в конец (если это возможно)
                    if (maxCheck[i] != j) {
                        state[i][maxCheck[i]] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                } else if (state[i][j] == state[i][nextFilled]) {
                    //в конце точно такой же tile - объединяем их
                    state[i][nextFilled] *= 2;
                    state[i][j] = 0;
                    maxCheck[i] = nextFilled - 1;
                    freeCount++;
                    result = true;
                } else {
                    //в конце какой-то обычный tile. Просто двигаем в плотную к нему
                    if (nextFilled - 1 != j) {
                        state[i][nextFilled - 1] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private boolean moveLeft() {
        boolean result = false;
        int[] minCheck = {0, 0, 0, 0};
        for (int j = 1; j < 4; j++) {
            for (int i = 0; i < 4; i++) {

                if (state[i][j] == 0) {
                    continue;
                }

                //Найдем индекс следующего "занятого" элемента
                int nextFilled = -1;
                for (int k = j-1; k >= minCheck[i]; k--) {
                    if (state[i][k] != 0) {
                        nextFilled = k;
                        break;
                    }
                }

                if (nextFilled == -1) {
                    //в конце 0 - двигаем в конец (если это возможно)
                    if (minCheck[i] != j) {
                        state[i][minCheck[i]] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                } else if (state[i][j] == state[i][nextFilled]) {
                    //в конец не 0, а точно такой же tile - объединяем их
                    state[i][nextFilled] *= 2;
                    state[i][j] = 0;
                    minCheck[i] = nextFilled + 1;
                    freeCount++;
                    result = true;
                } else {
                    //в конце какой-то обычный tile. Просто двигаем в плотную к нему
                    if (nextFilled + 1 != j) {
                        state[i][nextFilled + 1] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private boolean moveUp() {
        boolean result = false;
        int[] minCheck = {0, 0, 0, 0};
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (state[i][j] == 0) {
                    continue;
                }
                
                //Найдем индекс следующего "занятого" элемента
                int nextFilled = -1;
                for (int k = i-1; k >= minCheck[j]; k--) {
                    if (state[k][j] != 0) {
                        nextFilled = k;
                        break;
                    }
                }
                
                if (nextFilled == -1) {
                    //в конце 0 - двигаем в конец (если это возможно)
                    if (minCheck[j] != i) {
                        state[minCheck[j]][j] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                } else if (state[i][j] == state[nextFilled][j]) {
                    //в конце точно такой же tile - объединяем их
                    state[nextFilled][j] *= 2;
                    state[i][j] = 0;
                    minCheck[j] = nextFilled + 1;
                    freeCount++;
                    result = true;
                } else {
                    //в конце какой-то обычный tile. Просто двигаем в плотную к нему
                    if (nextFilled + 1 != i) {
                        state[nextFilled + 1][j] = state[i][j];
                        state[i][j] = 0;
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private void insertNew() {
        int cond = random.nextInt(10);
        short next = (short) (cond < 9 ? 2 : 4);
        
        int pos = random.nextInt(freeCount);
        
        freeCount--;
        int currentFreePos = 0;
        for (int i = 0; i < state.length; i++) {
            short[] ses = state[i];
            for (int j = 0; j < ses.length; j++) {
                short s = ses[j];
                if (s == 0) {
                    if (currentFreePos == pos) {
                        state[i][j] = next;
                        return;
                    } else {
                        currentFreePos++;
                    }
                }
            }
        }
    }

    protected void paint(Graphics g) {
        g.setColor(205, 192, 180);
        int width = getWidth();
        int height = getHeight();
        
        g.fillRect(0, 0, width, height);
        
        g.setColor(0xBB, 0xAD, 0xA0);
        int widthOne = (width - 3) / 4; //3 pixels - for draw lines
        int heightOne = (height - 3) / 4;

        g.drawLine(widthOne, 0, widthOne, height);
        g.drawLine(2*widthOne + 1, 0, 2*widthOne + 1, height);
        g.drawLine(3*widthOne + 2, 0, 3*widthOne + 2, height);
        
        g.drawLine(0, heightOne, width, heightOne);
        g.drawLine(0, 2*heightOne + 1, width, 2*heightOne + 1);
        g.drawLine(0, 3*heightOne + 2, width, 3*heightOne + 2);
        
        g.setFont(font);
        for (int i = 0; i < state.length; i++) {
            short[] ses = state[i];
            for (int j = 0; j < ses.length; j++) {
                short val = ses[j];
                if (val != 0) {
                    String text = Integer.toString(val);
                    int colorIndex = 0;
                    val = (short) (val >> 2);
                    while (val != 0) {
                        val = (short) (val >> 1);
                        colorIndex++;
                    }
                    if (colorIndex >= backgroudColors.length) {
                        colorIndex = backgroudColors.length - 1;
                    }
                    g.setColor(backgroudColors[colorIndex]);
                    g.fillRect(j * (widthOne + 1), i * (heightOne + 1), widthOne-1, heightOne-1);
                    g.setColor( colorIndex < 2 ? 0x776E65 : 0xf9f6f2 );
                    g.drawString(text, j*widthOne+j, i*heightOne+i, 0);
                }
            }
        }
    }

}
