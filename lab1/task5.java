public class task5 {
    public static void main(String[] args) {
        int[][] table = {
        {1, 2},
        {4, 5},
        {7, 8}
    };
    for (int i = 0; i < table.length; i++) {
        for (int j = 0; j < table[i].length; j++) {
            System.out.print(table[i][j] + " ");
        }
        System.out.println();
    }
    int rows = table.length;
    int columns = table[0].length;
    int[][] transposedTable = new int[columns][rows];
    for (int i = 0; i < rows; i++){
        for (int j = 0; j < columns ;j++){
            transposedTable[j][i] = table[i][j];
        }
    }
    System.out.println("Transposed table:");
    for (int i = 0; i < transposedTable.length; i++) {
        for (int j = 0; j < transposedTable[i].length; j++) {
            System.out.print(transposedTable[i][j] + " ");
        }     
           System.out.println();
}
    }
}
