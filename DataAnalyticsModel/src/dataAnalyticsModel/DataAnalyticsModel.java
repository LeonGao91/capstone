package dataAnalyticsModel;

public class DataAnalyticsModel {
	 /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int directionNum = 2;
        int systemNum = 2;
        int repeatNum = 2;
        int channelNum = 2;
        int rankNum = 2;
        int laneNum = 4;
        TestLane[][][] lanes;
        TestMarginsDDR margins;
        TestRepeat repeat1;
        TestRepeat repeat2;
        TestRepeat[] repeats;
        TestSystem system1;
        TestSystem system2;
        TestSystem[] systems;
        TestDirection direction1;
        TestDirection direction2;
        TestDirection[] directions;
        Test test;
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(10);
        lanes[0][0][1] = new TestLane(8);
        lanes[0][0][2] = new TestLane(11);
        lanes[0][0][3] = new TestLane(10);
        
        lanes[0][1][0] = new TestLane(9);
        lanes[0][1][1] = new TestLane(10);
        lanes[0][1][2] = new TestLane(12);
        lanes[0][1][3] = new TestLane(11);
        
        lanes[1][0][0] = new TestLane(7);
        lanes[1][0][1] = new TestLane(8);
        lanes[1][0][2] = new TestLane(9);
        lanes[1][0][3] = new TestLane(10);
        
        lanes[1][1][0] = new TestLane(13);
        lanes[1][1][1] = new TestLane(12);
        lanes[1][1][2] = new TestLane(9);
        lanes[1][1][3] = new TestLane(10);
        
        margins = new TestMarginsDDR(lanes);
        repeat1 = new TestRepeat(margins);
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(8);
        lanes[0][0][1] = new TestLane(9);
        lanes[0][0][2] = new TestLane(12);
        lanes[0][0][3] = new TestLane(11);
        
        lanes[0][1][0] = new TestLane(10);
        lanes[0][1][1] = new TestLane(8);
        lanes[0][1][2] = new TestLane(9);
        lanes[0][1][3] = new TestLane(7);
        
        lanes[1][0][0] = new TestLane(9);
        lanes[1][0][1] = new TestLane(10);
        lanes[1][0][2] = new TestLane(13);
        lanes[1][0][3] = new TestLane(12);
        
        lanes[1][1][0] = new TestLane(12);
        lanes[1][1][1] = new TestLane(11);
        lanes[1][1][2] = new TestLane(8);
        lanes[1][1][3] = new TestLane(10);
        
        margins = new TestMarginsDDR(lanes);
        repeat2 = new TestRepeat(margins);
        
        repeats = new TestRepeat[repeatNum];
        repeats[0] = repeat1;
        repeats[1] = repeat2;
        
        system1 = new TestSystem(repeats);
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(9);
        lanes[0][0][1] = new TestLane(7);
        lanes[0][0][2] = new TestLane(10);
        lanes[0][0][3] = new TestLane(13);
        
        lanes[0][1][0] = new TestLane(7);
        lanes[0][1][1] = new TestLane(8);
        lanes[0][1][2] = new TestLane(9);
        lanes[0][1][3] = new TestLane(10);
        
        lanes[1][0][0] = new TestLane(8);
        lanes[1][0][1] = new TestLane(9);
        lanes[1][0][2] = new TestLane(8);
        lanes[1][0][3] = new TestLane(9);
        
        lanes[1][1][0] = new TestLane(11);
        lanes[1][1][1] = new TestLane(12);
        lanes[1][1][2] = new TestLane(10);
        lanes[1][1][3] = new TestLane(9);
        
        margins = new TestMarginsDDR(lanes);
        repeat1 = new TestRepeat(margins);
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(10);
        lanes[0][0][1] = new TestLane(13);
        lanes[0][0][2] = new TestLane(10);
        lanes[0][0][3] = new TestLane(12);
        
        lanes[0][1][0] = new TestLane(9);
        lanes[0][1][1] = new TestLane(10);
        lanes[0][1][2] = new TestLane(7);
        lanes[0][1][3] = new TestLane(11);
        
        lanes[1][0][0] = new TestLane(9);
        lanes[1][0][1] = new TestLane(11);
        lanes[1][0][2] = new TestLane(12);
        lanes[1][0][3] = new TestLane(10);
        
        lanes[1][1][0] = new TestLane(8);
        lanes[1][1][1] = new TestLane(9);
        lanes[1][1][2] = new TestLane(10);
        lanes[1][1][3] = new TestLane(9);
        
        margins = new TestMarginsDDR(lanes);
        repeat2 = new TestRepeat(margins);
        
        repeats = new TestRepeat[repeatNum];
        repeats[0] = repeat1;
        repeats[1] = repeat2;
        
        system2 = new TestSystem(repeats);
        
        systems = new TestSystem[systemNum];
        systems[0] = system1;
        systems[1] = system2;
        
        direction1 = new TestDirection(systems);
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(10);
        lanes[0][0][1] = new TestLane(8);
        lanes[0][0][2] = new TestLane(11);
        lanes[0][0][3] = new TestLane(10);
        
        lanes[0][1][0] = new TestLane(9);
        lanes[0][1][1] = new TestLane(10);
        lanes[0][1][2] = new TestLane(12);
        lanes[0][1][3] = new TestLane(11);
        
        lanes[1][0][0] = new TestLane(7);
        lanes[1][0][1] = new TestLane(8);
        lanes[1][0][2] = new TestLane(9);
        lanes[1][0][3] = new TestLane(10);
        
        lanes[1][1][0] = new TestLane(13);
        lanes[1][1][1] = new TestLane(12);
        lanes[1][1][2] = new TestLane(9);
        lanes[1][1][3] = new TestLane(10);
        
        margins = new TestMarginsDDR(lanes);
        repeat1 = new TestRepeat(margins);
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(10);
        lanes[0][0][1] = new TestLane(8);
        lanes[0][0][2] = new TestLane(11);
        lanes[0][0][3] = new TestLane(13);
        
        lanes[0][1][0] = new TestLane(9);
        lanes[0][1][1] = new TestLane(10);
        lanes[0][1][2] = new TestLane(8);
        lanes[0][1][3] = new TestLane(7);
        
        lanes[1][0][0] = new TestLane(8);
        lanes[1][0][1] = new TestLane(11);
        lanes[1][0][2] = new TestLane(12);
        lanes[1][0][3] = new TestLane(10);
        
        lanes[1][1][0] = new TestLane(9);
        lanes[1][1][1] = new TestLane(10);
        lanes[1][1][2] = new TestLane(12);
        lanes[1][1][3] = new TestLane(13);
        
        margins = new TestMarginsDDR(lanes);
        repeat2 = new TestRepeat(margins);
        
        repeats = new TestRepeat[repeatNum];
        repeats[0] = repeat1;
        repeats[1] = repeat2;
        
        system1 = new TestSystem(repeats);
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(7);
        lanes[0][0][1] = new TestLane(10);
        lanes[0][0][2] = new TestLane(11);
        lanes[0][0][3] = new TestLane(12);
        
        lanes[0][1][0] = new TestLane(9);
        lanes[0][1][1] = new TestLane(9);
        lanes[0][1][2] = new TestLane(10);
        lanes[0][1][3] = new TestLane(8);
        
        lanes[1][0][0] = new TestLane(7);
        lanes[1][0][1] = new TestLane(10);
        lanes[1][0][2] = new TestLane(8);
        lanes[1][0][3] = new TestLane(7);
        
        lanes[1][1][0] = new TestLane(10);
        lanes[1][1][1] = new TestLane(11);
        lanes[1][1][2] = new TestLane(12);
        lanes[1][1][3] = new TestLane(13);
        
        margins = new TestMarginsDDR(lanes);
        repeat1 = new TestRepeat(margins);
        
        lanes = new TestLane[channelNum][rankNum][laneNum];
        lanes[0][0][0] = new TestLane(11);
        lanes[0][0][1] = new TestLane(12);
        lanes[0][0][2] = new TestLane(8);
        lanes[0][0][3] = new TestLane(9);
        
        lanes[0][1][0] = new TestLane(10);
        lanes[0][1][1] = new TestLane(8);
        lanes[0][1][2] = new TestLane(8);
        lanes[0][1][3] = new TestLane(9);
        
        lanes[1][0][0] = new TestLane(11);
        lanes[1][0][1] = new TestLane(12);
        lanes[1][0][2] = new TestLane(12);
        lanes[1][0][3] = new TestLane(13);
        
        lanes[1][1][0] = new TestLane(7);
        lanes[1][1][1] = new TestLane(9);
        lanes[1][1][2] = new TestLane(10);
        lanes[1][1][3] = new TestLane(8);
        
        margins = new TestMarginsDDR(lanes);
        repeat2 = new TestRepeat(margins);
        
        repeats = new TestRepeat[repeatNum];
        repeats[0] = repeat1;
        repeats[1] = repeat2;
        
        system2 = new TestSystem(repeats);
        
        systems = new TestSystem[systemNum];
        systems[0] = system1;
        systems[1] = system2;
        
        direction2 = new TestDirection(systems);
        
        directions = new TestDirection[directionNum];
        directions[0] = direction1;
        directions[1] = direction2;
        
        test = new Test(directions);
        
        System.out.println(test.toString());
    }
}
