import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



public class REVERSI extends JFrame {
    public int x_of_max_value = 0, y_of_max_value = 0;
    private JPanel panel;

    public static final int FIELD_EMPTY = 0;
    public static final int FIELD_Grey = 1;
    public static final int FIELD_Red = -1;
    public static final int FIELD_possible = 2;
    public  boolean is_it_singleplayer;

    boolean isXturn;
    public int [][] field;
    public int [][] previous_field;
    private static boolean is_it_second_time = false;
    private static int counter_of_presses = 0;


    public REVERSI(boolean is_it_singleplayer_) {
        this.is_it_singleplayer = is_it_singleplayer_;
        field = new int[8][8];
        previous_field = new int[8][8];
        initGame();
        initPanel();
        initFrame();
    }
    private void initGame() {
        field[3][3] = FIELD_Grey;
        field[4][4] = FIELD_Grey;
        field[3][4] = FIELD_Red;
        field[4][3] = FIELD_Red;
        isXturn = true;
    }

    private void initFrame() {

        pack(); //изменит размер формы, чтобы там все поместилось
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // закртиые по крестику
        setTitle("REVERSI");
        setLocationRelativeTo(null); // где находится
        setResizable(false);
        setVisible(true);

    }

    private void initPanel() {

        // при инициализации панели необходимо указывать paint componennt - она всегда вызывается, когда есть задача нарисовать нашу форму
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) { // у графики есть разные методы, в том числе, чтобы выводить картинку

                super.paintComponent(g);
                g.clearRect(0, 0, getWidth(), getHeight());
                drawGrid(g);
                int counter_of_possible = 0;
                for (int i = 0; i < 8; ++i) {
                    for (int j = 0; j < 8; ++j) {
                        if (field[i][j] == FIELD_possible) {
                            // System.out.println();
                            field[i][j] = FIELD_EMPTY;
                            counter_of_possible += 1;
                            // print();
                        }
                    }
                }

                if (is_it_second_time) {
                    if (counter_of_possible == 0) {
                        int counter_of_red = 0;
                        int counter_of_grey = 0;
                        for (int i = 0; i < 8; ++i) {
                            for (int j = 0; j < 8; ++j) {
                                if (field[i][j] == FIELD_Grey) {
                                    ++counter_of_grey;
                                } else if (field[i][j] == FIELD_Red) {
                                    ++counter_of_red;
                                }
                            }
                        }
                        if (counter_of_red > counter_of_grey) {
                            System.out.println("CONGRATILUATIONS TO RED CATS!!!! YOU ARE THE WINNER!!!!!!!");
                            System.out.print("YOUR RESULT IS: ");
                            System.out.println(counter_of_red);
                        } else if (counter_of_red < counter_of_grey) {
                            System.out.println("CONGRATILUATIONS TO GREY CATS!!!! YOU ARE THE WINNER!!!!!!!");
                            System.out.print("YOUR RESULT IS: ");
                            System.out.println(counter_of_grey);
                        } else {
                            System.out.println("CONGRATILUATIONS TO ALL CATS!!!! YOU ARE THE WINNER!!!!!!!(losers)");
                            System.out.print("YOUR RESULT IS: ");
                            System.out.println(counter_of_red);
                            System.out.print("YOUR RESULT IS: ");
                            System.out.println(counter_of_grey);
                        }
//                    panel.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        System.exit(0); // stop program
                        setVisible(false);
                    }
                }
                if (isXturn) {
                    for (int i = 0; i < 8; ++i) {
                        for (int j = 0; j < 8; ++j) {
                            if (field[i][j] == FIELD_Grey) {
                                // System.out.println();
                                Find_legal_move_Red(i, j);
                                // print();
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < 8; ++i) {
                        for (int j = 0; j < 8; ++j) {
                            if (field[i][j] == FIELD_Red) {
                                // System.out.println();
                                Find_legal_move_Grey(i, j);
                                // print();
                            }
                        }
                    }
                    counter_of_presses = 0;
                    if (is_it_singleplayer) {
                        for (int i = 0; i < 8; ++i) {
                            for (int j = 0; j < 8; ++j) {
                                previous_field[i][j] = field[i][j];
                            }
                        }
                        Move();
                        field[x_of_max_value][y_of_max_value] = FIELD_Grey;
                        Edit_FIELD_Grey(x_of_max_value, y_of_max_value);
                        isXturn = !isXturn;
                        panel.repaint();
                    }
                }

                draw(g);
                is_it_second_time = true;
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / 100;
                int y = e.getY() / 100;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    counter_of_presses = 0;
                    for (int i = 0; i < 8; ++i) {
                        for (int j = 0; j < 8; ++j) {
                            previous_field[i][j] = field[i][j];
                        }
                    }
                    if (field[x][y] == FIELD_possible) {
                        if (isXturn) {
                            field[x][y] = FIELD_Red;
                            Edit_field_Red(x, y);
                        } else {
                            if (!is_it_singleplayer) {
                                field[x][y] = FIELD_Grey;
                                Edit_FIELD_Grey(x, y);
                            }
                        }
                        isXturn = !isXturn;

                        repaint();
                    }
//                    System.out.print(x);
//                    System.out.print(" ");
//                    System.out.println(y);
                }
//                print();
                panel.repaint(); //перерисовка поля
            }
        });

        panel.setPreferredSize(new Dimension(800,800));
        add(panel); //add добавляет панель на форму

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
                    System.out.println("a step back has been taken\n");
                    if (counter_of_presses == 0) {
                        for (int i = 0; i < 8; ++i) {
                            for (int j = 0; j < 8; ++j) {
                                field[i][j] = previous_field[i][j];
                            }
                        }
                        isXturn = !isXturn;
                        panel.repaint();
                    }
                    ++counter_of_presses;
                }
            }

        });
    }

    private double R(int i, int j) {
        double R = 0.0;
        int[] arr = new int[3];
        arr[0] = -1;
        arr[1] = 0;
        arr[2] = 1;
        for (int k = 0; k < 3; k++) {
            for (int d = 0; d < 3; d++) {
                if ((k == 1 && d == 1) || (i + arr[k] < 8 && i + arr[k] >= 0 && j + arr[d] < 8 && j + arr[d] >= 0 && field[i + arr[k]][j + arr[d]] == FIELD_Grey)) {
                    continue;
                }
                int sum = 0, x_next, y_next;
                for (x_next = i + arr[k], y_next = j + arr[d];
                     (x_next + arr[k]) < 8 && (x_next + arr[k]) >= 0 && (y_next + arr[d]) < 8 && (y_next + arr[d]) >= 0 && field [x_next][y_next] == FIELD_Red;
                     x_next += arr[k], y_next += arr[d]) {
                    if (x_next % 7 == 0 || y_next % 7 == 0) {
                        sum += 2;
                    } else {
                        sum++;
                    }
                }
                if ((x_next + arr[k]) < 8 && (x_next + arr[k]) >= 0 && (y_next + arr[d]) < 8 && (y_next + arr[d]) >= 0 && field [x_next][y_next] == FIELD_Grey) {
                    R += sum;
                }
            }
        }
        if (i % 8 == 0 && j % 8 == 0) {
            R += 0.8;
        }
        else if (i % 8 == 0 || j % 8 == 0) {
            R += 0.4;
        }
        return R;
    }

    void Move() {
        //now is the !isXturn - now is the turn of GREY
        //here we got a coordinates of possible grey cat
        // and we want to find the R of this grey cat

        double max_of_value = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (field[i][j] == FIELD_possible) {
                    if (R(i, j) > max_of_value) {
                        x_of_max_value = i;
                        y_of_max_value = j;
                        max_of_value = R(i, j);
                    }
                }
            }
        }
        field[x_of_max_value][y_of_max_value] = FIELD_Grey;
    }
    private Image getImage_red() {
        ImageIcon icon = new ImageIcon((getClass().getResource("red.png")));
        return icon.getImage();
    }

    private Image getImage_grey() {
        ImageIcon icon = new ImageIcon((getClass().getResource("grey.png")));
        return icon.getImage();
    }

    void drawGrid(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        int dw = w / 8;
        int dh = h / 8;
        graphics.setColor(Color.BLACK);
        for (int i = 1; i < 8; ++i) {
            graphics.drawLine(0, dh*i, w, dh*i);
            graphics.drawLine(dw*i, 0, dw*i, h);
        }
    }

    public void drawRed(int i, int j, Graphics graphics) {
//        graphics.setColor((Color.BLACK));
        int dw = getWidth() / 8;
        int dh = getHeight() / 8;
        int x = i * dw + 25;
        int y = j * dh + 25;
        graphics.drawImage(getImage_red(), x, y, null);
    }

    void draw(Graphics graphics) {

        for (int i = 0; i < 8; ++i){
            for (int j = 0; j < 8; ++j) {
                if (field[i][j] == FIELD_Grey) {
                    drawGrey(i, j, graphics);
                } else if (field[i][j] == FIELD_Red) {
                    drawRed(i, j, graphics);
                } else if (field[i][j] == FIELD_possible) {
                    drawP(i, j, graphics);
                }
            }
        }
    }

    void drawGrey(int i, int j, Graphics graphics) {
        int dw = getWidth()/8;
        int dh = getHeight()/8;
        int x = i * dw + 25;
        int y = j * dh + 25;

        graphics.drawImage(getImage_grey(), x, y, null);
    }

    void drawP(int i, int j, Graphics graphics) {
        int dw = getWidth() / 8;
        int dh = getHeight() / 8;
        int x = i * dw;
        int y = j * dh;
        if (isXturn) {
            graphics.setColor((Color.BLACK));
            graphics.drawOval(x + 5 * dw / 100, y, dw * 9 / 10, dh);
        } else {
            graphics.setColor((Color.RED));
            graphics.drawOval(x + 5 * dw / 100, y, dw * 9 / 10, dh);
        }
    }
    private void Find_legal_move_Red(int i, int j) {
        if (i > 0 && j > 0 && i < 7 && j < 7) {
            if (field[i+1][j+1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {

                        field[i+1][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i+1][j+1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d-=1;
                    } else {
                        field[i+1][j+1] = FIELD_EMPTY;
                        break;
                    }
                }

            }
            if (field[i][j+1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {

                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i][j+1] = FIELD_possible;
                        break;
                    }
                    if (d < 7) {

                        d-=1;
                    } else {
                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j+1] = FIELD_possible;
            }
            if (field[i+1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i+1][j] = FIELD_possible;
                        break;
                    }
                    if (k < 7) {
                        k-=1;
                        // d-=1;
                    } else {
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i+1][j] = FIELD_possible;
            }
            if (field[i-1][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i-1][j-1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d+=1;
                    } else {
                        field[i-1][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j-1] = FIELD_possible;
            }
            if (field[i][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i][j-1] = FIELD_possible;
                        break;
                    }
                    if (d > 0) {
                        // k+=1;
                        d+=1;
                    } else {
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j-1] = FIELD_possible;
            }
            if (field[i-1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i-1][j] = FIELD_possible;
                        break;
                    }
                    if (k > 0) {
                        k+=1;
                        // d+=1;
                    } else {
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j] = FIELD_possible;
            }
            if (field[i-1][j+1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i-1][j+1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d-=1;
                    } else {
                        field[i-1][j+1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j+1] = FIELD_possible;
            }
            if (field[i+1][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i+1][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i+1][j-1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d+=1;
                    } else {
                        field[i+1][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i+1][j-1] = FIELD_possible;
            }
        } else {
            if (j+1 <= 7 && field[i][j+1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i][j+1] = FIELD_possible;
                        break;
                    }
                    if (d > 0) {
                        // k-=1;
                        d-=1;
                    } else {
                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j+1] = FIELD_possible;
            }
            if (i+1 <= 7 && field[i+1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i+1][j] = FIELD_possible;
                        break;
                    }
                    if (k > 0) {
                        k-=1;
                        // d-=1;
                    } else {
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i+1][j] = FIELD_possible;
            }
            if (j - 1 >= 0 && field[i][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i][j-1] = FIELD_possible;
                        break;
                    }
                    if (d < 7) {
                        // k-=1;
                        d+=1;
                    } else {
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j-1] = FIELD_possible;
            }
            if (i - 1 >= 0 && field[i-1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        field[i-1][j] = FIELD_possible;
                        break;
                    }
                    if (k < 7) {
                        k+=1;
                        // d+=1;
                    } else {
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j] = FIELD_possible;
            }
        }
    }


    private void Find_legal_move_Grey(int i, int j) {
        if (i > 0 && j > 0 && i < 7 && j < 7) {
            if (field[i+1][j+1] == FIELD_EMPTY) {
                // boolean is_it_legal;
                // is_it_legal = true;
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i+1][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i+1][j+1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d-=1;
                    } else {
                        field[i+1][j+1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i+1][j+1] = FIELD_possible;
            }
            if (field[i][j+1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i][j+1] = FIELD_possible;
                        break;
                    }
                    if (d < 7) {
                        // k-=1;
                        d-=1;
                    } else {
                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j+1] = FIELD_possible;
            }
            if (field[i+1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i+1][j] = FIELD_possible;
                        break;
                    }
                    if (k < 7) {
                        k-=1;
                        // d-=1;
                    } else {
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i+1][j] = FIELD_possible;
            }
            if (field[i-1][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i-1][j-1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d+=1;
                    } else {
                        field[i-1][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j-1] = FIELD_possible;
            }
            if (field[i][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i][j-1] = FIELD_possible;
                        break;
                    }
                    if (d > 0) {
                        // k+=1;
                        d+=1;
                    } else {
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j-1] = FIELD_possible;
            }
            if (field[i-1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i-1][j] = FIELD_possible;
                        break;
                    }
                    if (k > 0) {
                        k+=1;
                        // d+=1;
                    } else {
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j] = FIELD_possible;
            }
            if (field[i-1][j+1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i-1][j+1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d-=1;
                    } else {
                        field[i-1][j+1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j+1] = FIELD_possible;
            }
            if (field[i+1][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i+1][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i+1][j-1] = FIELD_possible;
                        break;
                    }
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d+=1;
                    } else {
                        field[i+1][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i+1][j-1] = FIELD_possible;
            }
        } else {
            if (j+1 <= 7 && field[i][j+1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i][j+1] = FIELD_possible;
                        break;
                    }
                    if (d > 0) {
                        // k-=1;
                        d-=1;
                    } else {
                        field[i][j+1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j+1] = FIELD_possible;
            }
            if (i+1 <= 7 && field[i+1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i+1][j] = FIELD_possible;
                        break;
                    }
                    if (k > 0) {
                        k-=1;
                        // d-=1;
                    } else {
                        field[i+1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i+1][j] = FIELD_possible;
            }
            // if (i - 1 >= 0 && j - 1 >= 0 && field[i-1][j-1] == FIELD_EMPTY) {
            //     // field[i-1][j-1] = FIELD_possible;
            // }
            if (j - 1 >= 0 && field[i][j-1] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i][j-1] = FIELD_possible;
                        break;
                    }
                    if (d < 7) {
                        // k-=1;
                        d+=1;
                    } else {
                        field[i][j-1] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i][j-1] = FIELD_possible;
            }
            if (i - 1 >= 0 && field[i-1][j] == FIELD_EMPTY) {
                int k = i;
                int d = j;
                while(true) {
                    if(field[k][d] == FIELD_possible || field[k][d] == FIELD_EMPTY) {
                        // is_it_legal = false;
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        field[i-1][j] = FIELD_possible;
                        break;
                    }
                    if (k < 7) {
                        k+=1;
                        // d+=1;
                    } else {
                        field[i-1][j] = FIELD_EMPTY;
                        break;
                    }
                }
                // field[i-1][j] = FIELD_possible;
            }
        }
    }
    private void Edit_field_Red(int i, int j) {
        //мы ставим рыжего кота и ищем в окрестности серого кота
        if (i > 0 && i < 7 && j > 0 && j < 7) {
            if (field[i-1][j-1] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i - 1;
                int d = j - 1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d-=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k < i && d < j) {
                            k += 1;
                            d += 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
            if (field[i + 1][j + 1] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i + 1;
                int d = j + 1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k > i && d > j) {
                            k -= 1;
                            d -= 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
            if (field[i][j + 1] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i;
                int d = j + 1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (d != 7) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    ++counter;
                    if (d < 7) {
                        // k+=1;
                        d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d > j) {
                            // k -= 1;
                            d -= 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
            if (field[i+1][j] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i+1;
                int d = j;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (k != 7) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    ++counter;
                    if (k < 7) {
                        k+=1;
                        // d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k > i) {
                            k -= 1;
                            // d -= 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
            if (field[i-1][j] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i-1;
                int d = j;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (k != 0) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (k > 0) {
                        k-=1;
                        // d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k < i) {
                            k += 1;
                            // d -= 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
            if (field[i][j-1] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i;
                int d = j-1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (d != 0) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (d > 0) {
                        // k-=1;
                        d-=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d < j) {
                            d += 1;
                            // d -= 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
            if (field[i+1][j-1] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i+1;
                int d = j-1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d-=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d < j && k > i) {
                            d += 1;
                            k -= 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
            if (field[i-1][j+1] == FIELD_Grey) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i-1;
                int d = j+1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Grey) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Red;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Red) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d > j && k < i) {
                            d -= 1;
                            k += 1;
                            field[k][d] = FIELD_Grey;
                        }
                    }
                }
            }
        } else {
            //here was the problem+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            if (j == 0 && i < 7 && i > 0) {
                if (field[i + 1][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i + 1;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i && d > j) {
                                k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i-1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 0) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i-1][j+1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j+1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j && k < i) {
                                d -= 1;
                                k += 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                //-------------------------------------------------------
            } else if (j == 7 && i < 7 && i > 0) {
                if(field[i-1][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i - 1;
                    int d = j - 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i && d < j) {
                                k += 1;
                                d += 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i-1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 0) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 0) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i+1][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j && k > i) {
                                d += 1;
                                k -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                //--------------------------------------------------------------------------
            } else if (i == 0 && j > 1 && j < 7) {

                if (field[i + 1][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i + 1;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i && d > j) {
                                k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if ((d != 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i+1][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j && k > i) {
                                d += 1;
                                k -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                //-----------------------------------------------------------------------------------
            } else if (i == 7 && j < 7 && j > 1) {
                if (field[i-1][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i - 1;
                    int d = j - 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i && d < j) {
                                k += 1;
                                d += 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i-1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 0) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 0 ) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i-1][j+1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j+1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j && k < i) {
                                d -= 1;
                                k += 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                //-------------------------------------------------------------------------------------
            } else if (i == 0 && j == 0) {
                if (field[i + 1][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i + 1;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i && d > j) {
                                k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if ( d != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                //----------------------------------------------------------------------------
            } else if (i == 0 && j == 7) {


                if (field[i+1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 0) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i+1][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j && k > i) {
                                d += 1;
                                k -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                //--------------------------------------------------------------
            } else if (i == 7 && j == 0) {


                if (field[i][j + 1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 7) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i-1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if ((k != 0)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i-1][j+1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j+1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j && k < i) {
                                d -= 1;
                                k += 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                //--------------------------------------------------------------------------------
            } else if (i == 7 && j == 7) {
                if (field[i-1][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i - 1;
                    int d = j - 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i && d < j) {
                                k += 1;
                                d += 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }

                if (field[i-1][j] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (k != 0) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
                if (field[i][j-1] == FIELD_Grey) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Grey) {
                            if (d != 0) {
                                field[k][d] = FIELD_Red;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Red) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Grey;
                            }
                        }
                    }
                }
            }
        }

    }

    private void Edit_FIELD_Grey(int i, int j) {
        //мы ставим рыжего кота и ищем в окрестности серого кота
        if (i > 0 && i < 7 && j > 0 && j < 7) {
            if (field[i-1][j-1] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i - 1;
                int d = j - 1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d-=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k < i && d < j) {
                            k += 1;
                            d += 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
            if (field[i + 1][j + 1] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i + 1;
                int d = j + 1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k > i && d > j) {
                            k -= 1;
                            d -= 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
            if (field[i][j + 1] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i;
                int d = j + 1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (d != 7) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    ++counter;
                    if (d < 7) {
                        // k+=1;
                        d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d > j) {
                            // k -= 1;
                            d -= 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
            if (field[i+1][j] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i+1;
                int d = j;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (k != 7) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    ++counter;
                    if (k < 7) {
                        k+=1;
                        // d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k > i) {
                            k -= 1;
                            // d -= 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
            if (field[i-1][j] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i-1;
                int d = j;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (k != 0) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (k > 0) {
                        k-=1;
                        // d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (k < i) {
                            k += 1;
                            // d -= 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
            if (field[i][j-1] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i;
                int d = j-1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (d != 0) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (d > 0) {
                        // k-=1;
                        d-=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d < j) {
                            d += 1;
                            // d -= 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
            if (field[i+1][j-1] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i+1;
                int d = j-1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k+=1;
                        d-=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d < j && k > i) {
                            d += 1;
                            k -= 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
            if (field[i-1][j+1] == FIELD_Red) {
                boolean is_there_wrong = false;
                int counter = 0;
                int k = i-1;
                int d = j+1;
                while (true) {
                    if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                        is_there_wrong = true;
                        break;
                    } else if (field[k][d] == FIELD_Red) {
                        if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                            field[k][d] = FIELD_Grey;
                        } else {
                            is_there_wrong = true;
                            break;
                        }
                    } else if (field[k][d] == FIELD_Grey) {
                        break;
                    }
                    // print();
                    ++counter;
                    if (k > 0 && d > 0 && k < 7 && d < 7) {
                        k-=1;
                        d+=1;
                    }
                }
                // print();
                if (is_there_wrong) {
                    for (int p = 0; p < counter; ++p) {
                        if (d > j && k < i) {
                            d -= 1;
                            k += 1;
                            field[k][d] = FIELD_Red;
                        }
                    }
                }
            }
        } else {
            //here was the problem+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            if (j == 0 && i < 7 && i > 0) {
                if (field[i + 1][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i + 1;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i && d > j) {
                                k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i-1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 0) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i-1][j+1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j+1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j && k < i) {
                                d -= 1;
                                k += 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                //-------------------------------------------------------
            } else if (j == 7 && i < 7 && i > 0) {
                if(field[i-1][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i - 1;
                    int d = j - 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i && d < j) {
                                k += 1;
                                d += 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i-1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 0) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 0) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i+1][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j && k > i) {
                                d += 1;
                                k -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                //--------------------------------------------------------------------------
            } else if (i == 0 && j > 1 && j < 7) {

                if (field[i + 1][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i + 1;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i && d > j) {
                                k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if ((d != 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i+1][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j && k > i) {
                                d += 1;
                                k -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                //-----------------------------------------------------------------------------------
            } else if (i == 7 && j < 7 && j > 1) {
                if (field[i-1][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i - 1;
                    int d = j - 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i && d < j) {
                                k += 1;
                                d += 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i-1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 0) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 0 ) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i-1][j+1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j+1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j && k < i) {
                                d -= 1;
                                k += 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                //-------------------------------------------------------------------------------------
            } else if (i == 0 && j == 0) {
                if (field[i + 1][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i + 1;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i && d > j) {
                                k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if ( d != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i+1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                //----------------------------------------------------------------------------
            } else if (i == 0 && j == 7) {


                if (field[i+1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k < 7) {
                            k+=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k > i) {
                                k -= 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 0) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i+1][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i+1;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k+=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j && k > i) {
                                d += 1;
                                k -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                //--------------------------------------------------------------
            } else if (i == 7 && j == 0) {


                if (field[i][j + 1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j + 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 7) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (d < 7) {
                            // k+=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j) {
                                // k -= 1;
                                d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i-1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if ((k != 0)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i-1][j+1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j+1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d > j && k < i) {
                                d -= 1;
                                k += 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                //--------------------------------------------------------------------------------
            } else if (i == 7 && j == 7) {
                if (field[i-1][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i - 1;
                    int d = j - 1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (!(k == 0 || d == 0 || k == 7 || d == 7)) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        ++counter;
                        if (k > 0 && d > 0 && k < 7 && d < 7) {
                            k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i && d < j) {
                                k += 1;
                                d += 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }

                if (field[i-1][j] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i-1;
                    int d = j;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (k != 0) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (k > 0) {
                            k-=1;
                            // d+=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (k < i) {
                                k += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
                if (field[i][j-1] == FIELD_Red) {
                    boolean is_there_wrong = false;
                    int counter = 0;
                    int k = i;
                    int d = j-1;
                    while (true) {
                        if (field[k][d] == FIELD_EMPTY || field[k][d] == FIELD_possible) {
                            is_there_wrong = true;
                            break;
                        } else if (field[k][d] == FIELD_Red) {
                            if (d != 0) {
                                field[k][d] = FIELD_Grey;
                            } else {
                                is_there_wrong = true;
                                break;
                            }
                        } else if (field[k][d] == FIELD_Grey) {
                            break;
                        }
                        // print();
                        ++counter;
                        if (d > 0) {
                            // k-=1;
                            d-=1;
                        }
                    }
                    // print();
                    if (is_there_wrong) {
                        for (int p = 0; p < counter; ++p) {
                            if (d < j) {
                                d += 1;
                                // d -= 1;
                                field[k][d] = FIELD_Red;
                            }
                        }
                    }
                }
            }
        }
    }
}