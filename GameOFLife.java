public class GameOFLife {

    public static void main(String[] args) {
        try {
            // לוח התחלתי – 3 שורות ו-3 עמודות
            boolean[][] board = {
                    {false, false, false, false, false},
                    {false, false, true, false, false},
                    {false, false, true, false, false},
                    {false, false, true, false, false},
                    {false, false, false, false, false}
            };
            System.out.println("לוח התחלתי:");
            printBoard(board);
            int steps = 5; // כמה דורות להריץ
            for (int i = 1; i <= steps; i++) {
                board = computeNextStep(board); // מחשב את הדור הבא
                System.out.println("שלב " + i + ":");
                printBoard(board);
            }
        } catch (Exception e) {
            System.out.println("אירעה שגיאה במהלך הריצה:");
            e.printStackTrace(); // מדפיס את פרטי השגיאה
        }
    }

    // מחשב את השלב הבא בלוח
    public static boolean[][] computeNextStep(boolean[][] board) {
        int rows = board.length;
        int cols = board[0].length;
        boolean[][] next = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int liveNeighbors = countAliveNeighbors(board, row, col);

                if (board[row][col]) {
                    next[row][col] = (liveNeighbors == 2 || liveNeighbors == 3);
                } else {
                    next[row][col] = (liveNeighbors == 3);
                }
            }
        }

        return next;
    }

    // סופר שכנים חיים סביב תא
    public static int countAliveNeighbors(boolean[][] board, int row, int col) {
        int aliveCount = 0;
        int numRows = board.length;
        int numCols = board[0].length;

        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                if (deltaRow == 0 && deltaCol == 0) {
                    continue; // מדלג על התא עצמו
                }

                int neighborRow = row + deltaRow;
                int neighborCol = col + deltaCol;

                boolean isInsideBoard =
                        neighborRow >= 0 && neighborRow < numRows &&
                                neighborCol >= 0 && neighborCol < numCols;

                if (isInsideBoard && board[neighborRow][neighborCol]) {
                    aliveCount++;
                }
            }
        }

        return aliveCount;
    }

    // מדפיס את הלוח בצורה ברורה
    public static void printBoard(boolean[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                System.out.print(board[row][col] ? "1 " : "0 ");
            }
            System.out.println();
        }
        System.out.println(); // שורה ריקה בין שלבים
    }
}
