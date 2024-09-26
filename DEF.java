public class DEF {
    
    // Definitions class contating all constants
    // easier referncing.

    // solver consts
    static final int MIN_VELOCITY = 5;
    static final int MAX_VELOCITY = 300;

    // board consts
    enum DIFFICULTY {
        EASY,
        MEDIUM,
        HARD,
        INSANE
    }
    
    static final int BOARD_SIZE = 9;
    static final int BOARD_X_COORDIANTE = 410;
    static final int BOARD_Y_COORDIANTE = 105;

    // cell consts
    static final int CELL_SIZE = 60;

    // component sizes
    static final int GAME_HEIGHT = 550;
    static final int GAME_WIDTH = 600;


    public DEF(){}
}