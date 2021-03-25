package com.company;

import java.util.ArrayList;

//this class stores an array of statements
class path {

    ArrayList<statement> fullPath = new ArrayList<>();

    path() {

    }

    path(statement stat) {
        fullPath.add(stat);
    }

}

// this class stores an array of strings containing the sub-concept and super-concept along with their state
class statement {

    boolean state;
    String list[];

    statement() {

    }

    statement(String sub, String sup, boolean tempState) {

        state = tempState;
        list[0] = sub;
        list[1] = sup;

    }

    statement(String stat) {

        //the state of the statement is set to true if the string contains "IS-A"
        if (stat.contains("IS-A")) {
            list = stat.split(" IS-A ");
            state = true;

            //or false if the string contains "IS-NOT-A"
        } else if (stat.contains("IS-NOT-A")) {
            list = stat.split(" IS-NOT-A ");
            state = false;

        }
    }


}

public class Task2 {

    //this method displays each path in an understandable way
    public static void displayPath(ArrayList<path> tempPath) {

        for (path p : tempPath) {

            //the first statement in the list is displayed
            System.out.print(p.fullPath.get(0).list[0]);

            //is the state is true then " IS-A " is displayed
            if (p.fullPath.get(0).state) {
                System.out.print(" IS-A ");
                //is the state is false then " IS-NOT-A " is displayed
            } else {
                System.out.print(" IS-NOT-A ");
            }

            System.out.print(p.fullPath.get(0).list[1]);

            //after the first statement is displayed, only the state and super-concept of the following statements are displayed
            for (int i = 1; i < p.fullPath.size(); i++) {

                if (p.fullPath.get(i).state) {
                    System.out.print(" IS-A ");
                } else {
                    System.out.print(" IS-NOT-A ");
                }

                System.out.print(p.fullPath.get(i).list[1]);

            }
            System.out.println();
        }

    }


    public static ArrayList<statement> paths = new ArrayList<>();

    public static void allPaths(String query, String goal, ArrayList<statement> inh) {

        //base case
        if (query.equals(goal)) {

            return;
        }

        //the inheritance network is traversed and if the query is equal to the statement checked then the statement is stored in paths
        for (int i = 0; i < inh.size(); i++) {

            if (inh.get(i).list[0].equals(query)) {
                paths.add(inh.get(i));
                allPaths(inh.get(i).list[1], goal, inh);
            }


        }

    }

    public static ArrayList<path> splitPaths = new ArrayList<>();
    static int pNum = 0;

    public static void split(String goal) {


        //the path no to be added to
        path no0 = new path();
        splitPaths.add(no0);

        //this loop is used to add the statements to splitPaths
        for (int i = 0; i < paths.size(); i++) {

            splitPaths.get(pNum).fullPath.add(paths.get(i));

            //if the super-concept is equal to the goal than a new path is created in splitPaths
            // statements are now added to this new path instead
            if (paths.get(i).list[1].equals(goal)) {
                path newPath = new path();
                splitPaths.add(newPath);
                pNum++;
            }
        }

        int size = splitPaths.size();
        splitPaths.remove(size - 1);

    }

    public static ArrayList<path> rejoinPaths = new ArrayList<>();
    static int rNum = 0;

    public static void rejoin(String query) {

        //stores the last full path which was found
        int full = 0;

        path path0 = new path();
        rejoinPaths.add(path0);

        for (int i = 0; i < splitPaths.size(); i++) {

            //the path is checked if the sub-concept of its first literal is equal to the query
            if (splitPaths.get(i).fullPath.get(0).list[0].equals(query)) {

                //a full path has been found and so is added to rejoinPaths
                for (statement s : splitPaths.get(i).fullPath) {
                    rejoinPaths.get(rNum).fullPath.add(s);
                }

                path newPath = new path();
                rejoinPaths.add(newPath);
                //rNum is incremented to indicate the location that the next path has to be saved to
                rNum++;
                full = i;

            } else {

                for (int j = 0; j < rejoinPaths.get(full).fullPath.size(); j++) {

                    //the the first sub-concept of the broken path is compared to each sub-path of the last full path
                    if (rejoinPaths.get(full).fullPath.get(j).list[0].equals(splitPaths.get(i).fullPath.get(0).list[0])) {
                        for (int k = 0; k < j; k++) {
                            //every statement in the full list up to j is added to the next path in rejoinPaths
                            rejoinPaths.get(rNum).fullPath.add(rejoinPaths.get(full).fullPath.get(k));
                        }
                    }

                }

                //every statement in the broken path is then added to the same path in rejoinPaths
                for (statement s : splitPaths.get(i).fullPath) {

                    rejoinPaths.get(rNum).fullPath.add(s);
                }

                path newPath = new path();
                rejoinPaths.add(newPath);
                //the path is now complete and so the pointer is incremented
                rNum++;

            }
        }

        int size = rejoinPaths.size();
        rejoinPaths.remove(size - 1);

    }

    public static void notCheck(String query) {

        for (path p : rejoinPaths) {
            for (int i = 0; i < p.fullPath.size(); i++) {
                //checks if there exists a not anywhere other than the last statement and deletes it
                if (!p.fullPath.get(i).state && !p.fullPath.get(i).list[1].equals(query)) {
                    rejoinPaths.remove(i);
                }
            }
        }
    }

    public static ArrayList<path> shortestPath() {

        int shortestSize = rejoinPaths.get(0).fullPath.size();
        int shortest = 0;

        //the shortest path is found
        for (int i = 0; i < rejoinPaths.size(); i++) {
            if (rejoinPaths.get(i).fullPath.size() < shortestSize) {
                shortestSize = rejoinPaths.get(i).fullPath.size();
                shortest = i;
            }
        }

        ArrayList<path> shortestP = new ArrayList<>();
        shortestP.add(rejoinPaths.get(shortest));
        return shortestP;

    }

    public static void inferential() {

        //to compare path
        for (int i = 0; i < rejoinPaths.size(); i++) {
            // to compare statement
            path nameCheck = rejoinPaths.get(i);
            for (int j = 0; j < rejoinPaths.size(); j++) {
                //to check path
                statement toCompare = rejoinPaths.get(i).fullPath.get(j);
                for (int k = 1; k < rejoinPaths.size(); k++) {
                    //to check statement
                    for (int l = 0; l < rejoinPaths.size(); l++) {
                        //checks if the 2 sub concepts are found to be equal
                        if (toCompare.list[0].equals(rejoinPaths.get(k).fullPath.get(l).list[0])) {
                            //checks that their super concepts are not equal
                            if (!rejoinPaths.get(i).fullPath.get(j).list[1].equals(rejoinPaths.get(k).fullPath.get(l).list[1])) {
                                //the rest of the statements in one of the paths is checked to see if its sub-concept is equal to the others super-concept
                                for (statement s : nameCheck.fullPath) {
                                    if (s.list[0].equals(rejoinPaths.get(k).fullPath.get(l).list[1])) {
                                        //the shorter path is removed
                                        rejoinPaths.remove(k);
                                        k--;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        boolean flag = true;

        for (int k = 0; flag && k < rejoinPaths.size(); k++) {

            path p = rejoinPaths.get(k);
            int size = p.fullPath.size();
            //checks if the last state in the list is false
            if (!p.fullPath.get(size - 1).state) {

                flag = true;

                int p2No = -1;

                for (path p2 : rejoinPaths) {

                    p2No++;

                    for (int i = 0; flag && i < p2.fullPath.size(); i++) {

                        statement s = p2.fullPath.get(i);

                        //checks if sub-concept of 1st path is equal to the sub-concept of a statement of another path
                        if (p.fullPath.get(size - 1).list[0].equals(s.list[0])) {

                            //checks if both their super-concepts are equal but their states are diff
                            if (p.fullPath.get(size - 1).list[1].equals(s.list[1]) && s.state) {

                                //path is preempted and so is removed
                                rejoinPaths.remove(p2No);
                                flag = false;

                            } else if (!p.fullPath.get(size - 1).list[1].equals(s.list[1])) {

                                //check rest of list after s if is equal to p.fullPath.get(size-1).list[1]
                                for (int j = i; flag && j < p2.fullPath.size(); j++) {

                                    //checks if the super-concept of a statement further along the list is equal to p.fullPath.get(size-1).list[1] and their states are diff
                                    if (p2.fullPath.get(j).list[1].equals(p.fullPath.get(size - 1).list[1]) && s.state) {

                                        //path is preempted and so is removed
                                        rejoinPaths.remove(p2No);
                                        flag = false;
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static void main2() {

        System.out.println("Enter the inheritance network. \nEnter q to stop inputting.");

        //an array of statements is created which stores the inheritance network
        ArrayList<statement> inh = new ArrayList<>();

        String input;

        //allows the inheritance network to be entered
        while (true) {

            input = Keyboard.readString();

            if (input.equals("q")) {
                break;
            }

            inh.add(new statement(input));


        }

        System.out.println("\nEnter your query.");
        String tempQuery = Keyboard.readString();

        //a new statement is created of the users query
        statement query = new statement(tempQuery);

        //creates all the possible paths
        allPaths(query.list[0], query.list[1], inh);
        split(query.list[1]);
        rejoin(query.list[0]);
        notCheck(query.list[1]);

        System.out.println("\nAll possible paths.\n");
        displayPath(rejoinPaths);

        System.out.println("\nPreferred path using the shortest distance metric\n");
        ArrayList<path> shortest = shortestPath();
        displayPath(shortest);

        System.out.println("\nPreferred path using the inferential distance metric\n");
        inferential();
        displayPath(rejoinPaths);

    }
}
