/*
 * This class has been translated from C#
 *
 * source: http://csclab.murraystate.edu/bob.pilgrim/445/munkres.html
 */

public class Hungarian {
    private static class P {
        public int row, col;
        public P(int row, int col) { this.row = row; this.col = col; }
        public P() { }
    }

    private static int numRows;
    private static int numCols;

    private static int[][] originalMatrix;
    private static int[][] C;
    private static int[][] M;
    private static int[] rowCover;
    private static int[] colCover;

    private static int path_row_0;
    private static int path_col_0;
    private static int path_count;
    private static int[][] path;

    private static int nextStep;

    public static int hungarianCost(int[][] distanceMatrix) {
        return hungarianCost(distanceMatrix, false);
    }

    public static int hungarianCost(int[][] distanceMatrix, boolean debug) {
        init(distanceMatrix);

        boolean done = false;
        while(!done) {
            switch(nextStep) {
                case 1:
                    step_one();
                    break;
                case 2:
                    step_two();
                    break;
                case 3:
                    step_three();
                    break;
                case 4:
                    step_four();
                    break;
                case 5:
                    step_five();
                    break;
                case 6:
                    step_six();
                    break;
                case 7:
                    step_seven();
                    done = true;
                    break;
            }
        }

        if(debug)
            debugPrint();

        return retreiveSum();
    }

    public static void debugPrint() {
        System.err.println("----- originalMatrix -----");
        System.err.println(originalMatrixToString());
        System.err.println("--------------------------");
        System.err.println("");
        System.err.println("------------ C -----------");
        System.err.println(distanceMatrixToString());
        System.err.println("--------------------------");
        System.err.println("");
        System.err.println("------------ M -----------");
        System.err.println(starMatrixToString());
        System.err.println("--------------------------");
    }

    public static String distanceMatrixToString() {
        return matrixToString(C);
    }

    public static String starMatrixToString() {
        return matrixToString(M);
    }

    public static String originalMatrixToString() {
        return matrixToString(originalMatrix);
    }

    private static String matrixToString(int[][] matrix) {
        if(matrix == null || matrix.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for(int r = 0; r < matrix.length; r++) {
            for(int c = 0; c < matrix[0].length; c++) {
                sb.append(matrix[r][c]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void init(int[][] distanceMatrix) {
        numRows = distanceMatrix.length;
        numCols = distanceMatrix[0].length;

	if(originalMatrix == null)
	    originalMatrix = new int[numRows][numCols];
        for(int r = 0; r < numRows; r++)
            for(int c = 0; c < numCols; c++)
                originalMatrix[r][c] = distanceMatrix[r][c];

        C = distanceMatrix;

	M = cleanMatrix(M, numRows, numCols);

        rowCover = new int[numRows];
        colCover = new int[numCols];

        path_row_0 = 0;
        path_col_0 = 0;
        path_count = 0;
        path = cleanMatrix(path, numRows*3, 2);//new int[numRows * 3][2]; // just have large number of elements

        nextStep = 1;
    }

    private static int[][] cleanMatrix(int[][] m, int r, int c){
	if(m == null)
	    m = new int[r][c];
	else
	    for(int i=0; i<r; i++)
		for(int j=0; j<c; j++)
		    m[i][j] = 0;
	return m;
    }

    private static int retreiveSum() {
        int sum = 0;
        for(int r = 0; r < numRows; r++)
            for(int c = 0; c < numCols; c++)
                if(M[r][c] == 1)
                    sum += originalMatrix[r][c];
        return sum;
    }

    private static void step_one() {
        int minInRow;
        for(int r = 0; r < numRows; r++) {
            minInRow = C[r][0];
            for(int c = 0; c < numCols; c++)
                if(C[r][c] < minInRow)
                    minInRow = C[r][c];
            for(int c = 0; c < numCols; c++)
                C[r][c] -= minInRow;
        }
        nextStep = 2;
    }

    private static void step_two() {
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                if(C[r][c] == 0 && rowCover[r] == 0 && colCover[c] == 0) {
                    M[r][c] = 1;
                    rowCover[r] = 1;
                    colCover[c] = 1;
                }
            }
        }
        for(int r = 0; r < numRows; r++) {
            rowCover[r] = 0;
        }
        for(int c = 0; c < numCols; c++) {
            colCover[c] = 0;
        }
        nextStep = 3;
    }

    private static void step_three() {
        int colCount = 0;
        for(int r = 0; r < numRows; r++)
            for(int c = 0; c < numCols; c++)
                if(M[r][c] == 1)
                    colCover[c] = 1;

        for(int c = 0; c < numCols; c++)
            if(colCover[c] == 1)
                colCount += 1;
        if(colCount >= numCols || colCount >= numRows)
            nextStep = 7;
        else
            nextStep = 4;
    }

    private static void step_four() {
        int row = -1;
        int col = -1;
        boolean done = false;

        while(!done) {
            P p = find_a_zero(row, col);
            row = p.row;
            col = p.col;

            if(row == -1) {
                done = true;
                nextStep = 6;
            } else {
                M[row][col] = 2;
                if(star_in_row(row)) {
                    col = find_star_in_row(row, col);
                    rowCover[row] = 1;
                    colCover[col] = 0;
                } else {
                    done = true;
                    nextStep = 5;
                    path_row_0 = row;
                    path_col_0 = col;
                }
            }
        }
    }

    private static void step_five() {
        boolean done = false;
        int r = -1;
        int c = -1;

        path_count = 1;
        path[path_count-1][0] = path_row_0;
        path[path_count-1][1] = path_col_0;

        while(!done) {
            r = find_star_in_col(path[path_count-1][1], r);
            if(r > -1) {
                path_count++;
                path[path_count-1][0] = r;
                path[path_count-1][1] = path[path_count-2][1];
            } else
                done = true;
            if(!done) {
                c = find_prime_in_row(path[path_count-1][0], c);
                path_count++;
                path[path_count-1][0] = path[path_count-2][0];
                path[path_count-1][1] = c;
            }
        }
        augment_path();
        clear_covers();
        erase_primes();
        nextStep = 3;
    }

    private static void step_six() {
        int minVal = find_smallest();
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                if(rowCover[r] == 1)
                    C[r][c] += minVal;
                if(colCover[c] == 0)
                    C[r][c] -= minVal;
            }
        }
        nextStep = 4;
    }

    private static void step_seven() {
        // DO NOTHING
    }

    /*****************************************************************/
    /****************** Supporting functions below *******************/
    /*****************************************************************/

    private static int find_smallest() {
        int minVal = Integer.MAX_VALUE;
        for(int r = 0; r < numRows; r++)
            for(int c = 0; c < numCols; c++)
                if(rowCover[r] == 0 && colCover[c] == 0)
                    if(minVal > C[r][c])
                        minVal = C[r][c];
        return minVal;
    }

    private static int find_star_in_col(int c, int r) {
        r = -1;
        for(int i = 0; i < numRows; i++)
            if(M[i][c] == 1)
                r = i;
        return r;
    }

    private static int find_prime_in_row(int r, int c) {
        for(int j = 0; j < numCols; j++)
            if(M[r][j] == 2)
                c = j;
        return c;
    }

    private static void augment_path() {
        for(int p = 0; p < path_count; p++) {
            if(M[path[p][0]][path[p][1]] == 1)
                M[path[p][0]][path[p][1]] = 0;
            else
                M[path[p][0]][path[p][1]] = 1;
        }
    }

    private static void clear_covers() {
        for(int r = 0; r < numRows; r++)
            rowCover[r] = 0;
        for(int c = 0; c < numCols; c++)
            colCover[c] = 0;
    }

    private static void erase_primes() {
        for(int r = 0; r < numRows; r++)
            for(int c = 0; c < numCols; c++)
                if(M[r][c] == 2)
                    M[r][c] = 0;
    }

    private static P find_a_zero(int row, int col) {
        int r = 0;
        int c = 0;
        row = -1;
        col = -1;

        while(true) {
            c = 0;

            while(true) {
                if(C[r][c] == 0 && rowCover[r] == 0 && colCover[c] == 0) {
                    row = r;
                    col = c;
                    done = true;
                }
                c++;
                if(c >= numCols || done)
                    break;
            }
            r++;
            if(r >= numRows)
		break;
        }

        return new P(row, col);
    }

    private static boolean star_in_row(int row) {
        boolean tmp = false;
        for(int c = 0; c < numCols; c++)
            if(M[row][c] == 1)
                tmp = true;
        return tmp;
    }

    private static int find_star_in_row(int row, int col) {
        col = -1;
        for(int c = 0; c < numCols; c++)
            if(M[row][c] == 1)
                col = c;
        return col;
    }
}
