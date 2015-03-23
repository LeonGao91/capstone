/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datatransform;

/**
 *
 * @author wangjerome
 */
public class MyDataStructure implements DataStructure{

    @Override
    public int getParamNum() {
        return 5;
    }

    @Override
    public int translate(int columnNum, String value) {
        switch(columnNum) {
            case 0: return firstColumn(value);
            case 1: return secondColumn(value);
            case 2: return thirdColumn(value);
            case 3: return fourthColumn(value);
            case 4: return fifthColumn(value);
        }
        
        throw new RuntimeException("Not valid column number.");
    }
    
    private int firstColumn(String value) {
        return Integer.parseInt(value);
    }
    
    private int secondColumn(String value) {
        return Integer.parseInt(value);
    }
    
    private int thirdColumn(String value) {
        if (value.equals("WrV")) {
            return 0;
        }
        if (value.equals("RdV")) {
            return 1;
        }
        if (value.equals("WrT")) {
            return 2;
        }
        if (value.equals("RdT")) {
            return 3;
        }
        
        return -1;
    }
    
    private int fourthColumn(String value) {
        return Integer.parseInt(value);
    }
    
    private int fifthColumn(String value) {
        if (value.equals("H")) {
            return 0;
        }
        
        if (value.equals("L")) {
            return 1;
        }
        return -1;
    }

    @Override
    public int getSize(int columnNum) {
        switch(columnNum) {
            case 0: return 2;
            case 1: return 2;
            case 2: return 4;
            case 3: return 64;
            case 4: return 2;
                
        }
        return -1;
    }

    @Override
    public Object getData() {
        return new int[2][2][4][64][2];
    }
    
    @Override
    public String toString(Object data) {
        StringBuilder sb = new StringBuilder();
        int[][][][][] converted = (int[][][][][])data;
        
        for (int a = 0; a < converted.length; a++) {
            for (int b = 0; b < converted[0].length; b++) {
                for (int c = 0; c < converted[0][0].length; c++) {
                    for (int d = 0; d < converted[0][0][0].length; d++) {
                        for (int e = 0; e < converted[0][0][0][0].length; e++) {
                            sb.append(converted[a][b][c][d][e]);
                            sb.append(",");
                        }
                    }
                }
            }
        }
        
        sb.deleteCharAt(sb.length() - 1);
        
        return sb.toString();
    }

    @Override
    public void setData(Object data, int value, int[] params) {
        int[][][][][] converted = (int[][][][][])data;
        converted[params[0]][params[1]][params[2]][params[3]][params[4]] = value;
    }
    
}
