package com.test.utils;

import java.util.*;

public class HousingTicketService {
    private static final int maxCellFill = 2;
    private static final int totalNumber = 90;
    private static final int totalCells = 15;
    private static final int eachRowCellsFilled = 5;
    private static final int totalCount = totalNumber / totalCells;


    public static void main(String[] args) {
        final int totalRows = 3;
        final int totalColumns = 9;
        Cell[] housie = createTicketList(3, 9);
        printTickets(totalColumns, totalRows, housie);

    }

    private static Cell[] createTicketList(int totalRows, int totalColumns) {
        Cell[] housie = new Cell[totalCount];
        for (int i = 0; i < totalCount; i++) {
            housie[i] = new Cell(totalRows, totalColumns);
        }

        List<List<Integer>> columns = getPossibleColumnValueLists();
        //create row and columns
        List<List<List<Integer>>> sets = getRowColumnSet(totalColumns);

        for (int i = 0; i < totalColumns; i++) {
            List<Integer> li = columns.get(i);
            for (int j = 0; j < totalCount; j++) {
                int randNumIndex = getRand(li.size() - 1);
                int randNum = li.get(randNumIndex);

                List<Integer> set = sets.get(j).get(i);
                set.add(randNum);

                li.remove(randNumIndex);
            }
        }

        // assign element from last column to random set
        List<Integer> lastCol = columns.get(totalColumns - 1);
        int randNumIndex = getRand(lastCol.size() - 1);
        int randNum = lastCol.get(randNumIndex);

        int randSetIndex = getRand(sets.size() - 1);
        List<Integer> randSet = sets.get(randSetIndex).get(totalColumns - 1);
        randSet.add(randNum);
        lastCol.remove(randNumIndex);

        for (int pass = 0; pass < totalRows; pass++) {
            checkAndFillVacantSet(totalColumns, totalRows, columns, sets, maxCellFill);
        }

        checkAndFillVacantSet(totalColumns, totalRows, columns, sets, maxCellFill);


        // got the sets - need to arrange in tickets now
        for (int setIndex = 0; setIndex < totalCount; setIndex++) {
            List<List<Integer>> currSet = sets.get(setIndex);
            Cell currTicket = housie[setIndex];
            // fill first row
            fillRowsCells(totalColumns, currSet, currTicket, 3, 0);
            // fill second row
            fillRowsCells(totalColumns, currSet, currTicket, 2, 1);
            // fill third row
            fillRowsCells(totalColumns, currSet, currTicket, 1, 2);
        }


        for (int i = 0; i < totalCount; i++) {
            Cell currTicket = housie[i];
            currTicket.sortColumns();
        }
        return housie;
    }

    private static List<List<List<Integer>>> getRowColumnSet(int totalColumns) {
        List<List<Integer>> set1 = new ArrayList<>();
        List<List<Integer>> set2 = new ArrayList<>();
        List<List<Integer>> set3 = new ArrayList<>();
        List<List<Integer>> set4 = new ArrayList<>();
        List<List<Integer>> set5 = new ArrayList<>();
        List<List<Integer>> set6 = new ArrayList<>();

        for (int i = 0; i < totalColumns; i++) {
            set1.add(new ArrayList<>());
            set2.add(new ArrayList<>());
            set3.add(new ArrayList<>());
            set4.add(new ArrayList<>());
            set5.add(new ArrayList<>());
            set6.add(new ArrayList<>());
        }

        return Arrays.asList(set1, set2, set3, set4, set5, set6);
    }

    private static void checkAndFillVacantSet(int totalColumns, int totalRows, List<List<Integer>> columns, List<List<List<Integer>>> sets, int cellFill) {
        for (int i = 0; i < totalColumns; i++) {
            List<Integer> col = columns.get(i);
            if (col.size() == 0) {
                continue;
            }

            int randNumIndex_p = getRand(col.size() - 1);
            int randNum_p = col.get(randNumIndex_p);

            boolean vacantSetFound = false;

            int count = totalColumns * totalRows;
            while (!vacantSetFound && count >= 0) {
                int randSetIndex_p = getRand(sets.size() - 1);
                List<List<Integer>> randSet_p = sets.get(randSetIndex_p);

                if (getNumberOfElementsInSet(randSet_p) == 15 || randSet_p.get(i).size() == cellFill) {
                    count--;
                    continue;
                }
                vacantSetFound = true;
                randSet_p.get(i).add(randNum_p);
                col.remove(randNumIndex_p);
            }
        }
    }

    private static List<List<Integer>> getPossibleColumnValueLists() {
        int range = 90;
        Queue<Integer> ql = new LinkedList<>();
        int count = 0;
        for (int i = 0; i <= range; i++) {
            ql.add(i);
        }
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> l = new ArrayList<>();
        while (ql.size() > 0) {

            if (ql.peek() > 0) {
                l.add(ql.peek());
            }

            ql.poll();
            count++;
            if (count == 10) {
                if (ql.size() == 1) {
                    l.add(ql.poll());
                }
                list.add(l);
                l = new ArrayList<>();
                count = 0;
            }
        }
        return list;
    }

    private static void fillRowsCells(int totalColumns, List<List<Integer>> currSet, Cell currTicket, int i2, int i3) {
        for (int size = i2; size > 0; size--) {
            if (currTicket.getRowCount(i3) == eachRowCellsFilled)
                break;
            for (int colIndex = 0; colIndex < totalColumns; colIndex++) {
                if (currTicket.getRowCount(i3) == eachRowCellsFilled)
                    break;
                if (currTicket.getArr()[i3][colIndex] != 0)
                    continue;

                List<Integer> currSetCol = currSet.get(colIndex);
                if (currSetCol.size() != size)
                    continue;

                currTicket.getArr()[i3][colIndex] = currSetCol.remove(0);
            }
        }
    }


    private static void printTickets(int totalColumns, int totalRows, Cell[] nodes) {
        List<List<String>> list = null;
        for (int i = 0; i < 6; i++) {
            list = new ArrayList<>();
            Cell currTicket = nodes[i];
            for (int r = 0; r < totalRows; r++) {
                List<String> l = new ArrayList<>();
                for (int col = 0; col < totalColumns; col++) {
                    int num = currTicket.getArr()[r][col];
                    if (num != 0) {
                        String value = String.valueOf(num);
                        l.add(value.length() == 1 ? "0" + value : value);
                    } else {
                        l.add("--");
                    }
                }
                list.add(l);
            }
            list.forEach(System.out::println);
            System.out.println();
            System.out.println();
        }
    }

    static int getRand(int max) {
        Random rand = new Random();
        return rand.nextInt(max + 1);
    }

    static int getNumberOfElementsInSet(List<List<Integer>> set) {
        int count = 0;
        for (List<Integer> li : set)
            count += li.size();
        return count;
    }


    /**
     * Internal Helper Class
     */
    public static class Cell {

        private final int[][] arr;
        private final int row;
        private final int column;

        public Cell(int row, int column) {
            this.arr = new int[row][column];
            this.row = row;
            this.column = column;
        }

        public int[][] getArr() {
            return arr;
        }


        public int getEmptyCellInCol(int c) {
            for (int i = 0; i < row; i++) {
                if (arr[i][c] == 0)
                    return i;
            }

            return -1;
        }

        private void sortColumnWithThreeNumbers(int c) {
            int emptyCell = this.getEmptyCellInCol(c);
            if (emptyCell != -1) {
                throw new RuntimeException("column has <3 cells filled");
            }

            int[] tempArr = new int[]{this.arr[0][c], this.arr[1][c], this.arr[2][c]};
            Arrays.sort(tempArr);

            for (int r = 0; r < row; r++) {
                this.arr[r][c] = tempArr[r];
            }
        }

        private void sortColumnWithTwoNumbers(int c) {
            int emptyCell = this.getEmptyCellInCol(c);
            if (emptyCell == -1) {
                throw new RuntimeException("already 3 cells filled!!");
            }

            int cell1, cell2;
            if (emptyCell == 0) {
                cell1 = 1;
                cell2 = 2;
            } else if (emptyCell == 1) {
                cell1 = 0;
                cell2 = 2;
            } else { // emptyCell == 2
                cell1 = 0;
                cell2 = 1;
            }

            if (!(this.arr[cell1][c] < this.arr[cell2][c])) {
                // swap
                int temp = this.arr[cell1][c];
                this.arr[cell1][c] = this.arr[cell2][c];
                this.arr[cell2][c] = temp;
            }
        }


        private void sortColumn(int c) {
            if (this.getColCount(c) == 1) {
                return;
            } else if (this.getColCount(c) == 2) {
                this.sortColumnWithTwoNumbers(c);
            } else {
                this.sortColumnWithThreeNumbers(c);
            }
        }

        public void sortColumns() {
            for (int c = 0; c < column; c++) {
                this.sortColumn(c);
            }
        }

        public int getRowCount(int r) {
            int count = 0;
            for (int i = 0; i < column; i++) {
                if (arr[r][i] != 0)
                    count++;
            }

            return count;
        }

        public int getColCount(int c) {
            int count = 0;
            for (int i = 0; i < row; i++) {
                if (arr[i][c] != 0)
                    count++;
            }

            return count;
        }
    }
}




