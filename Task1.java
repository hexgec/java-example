package com.company;

import java.util.ArrayList;
import java.util.Comparator;

// this object stores the literal and whether it is negative or positive
class literal {
    String name;
    boolean state;

    literal(String n, boolean s) {
        name = n;
        state = s;
    }
}

//this object stores an array of literals
class clause {

    private int size = 0;
    public int getSize() {
        return size;
    }

    public ArrayList<literal> litList = new ArrayList<>();

    clause(String list) {
        //removing the brackets from the string and splitting up each literal
        list = list.substring(1, (list.length() - 1));
        String temp[] = list.split(", ");

        //checks whether each literal is positive or negative and stores it as a literal object
        for (String name : temp) {

            size++;

            if (name.contains("!")) {
                name = name.substring(1);
                litList.add(new literal(name, false));
            } else {
                litList.add(new literal(name, true));
            }
        }
    }
}

public class Task1 {

    public static boolean check(clause query, ArrayList<clause> list) {


        int i;

        //this is the base case which returns true if there are no literals left to be checked in the query
        if (query.litList.size() == 0) {
            return true;

        }

        //this stores the first literal in the query to be checked
        literal toCheck = query.litList.get(0);

        //this allows for every clause in the knowledge base to be checked
        for (clause c : list) {

            i=-1;

            //this allows for every literal in the clause to be checked
            for (literal l : c.litList) {

                i++;

                if (toCheck.name.equals(l.name)) {
                    if (toCheck.state != l.state) {

                        //if the literals are the same however one is positive and the other is negative,
                        //then the literal checked is removed form the query and the remaining literals in the checked clause are added to the query
                        ArrayList<literal> tempList = c.litList;
                        tempList.remove(i);
                        query.litList.remove(0);
                        query.litList.addAll(tempList);
                        //the method is then recursively called using the new query
                        return check(query, list);

                    }
                }
            }
        }

        return false;

    }

    public static void main1() {

        System.out.println("Enter the knowledge base. \nEnter q to stop inputting.");

        //an array of clauses is created which stores the knowledge base
        ArrayList<clause> clauseList = new ArrayList<>();

        String input;

        while (true) {

            input = Keyboard.readString();

            if (input.equals("q")) {
                break;
            }

            clauseList.add(new clause(input));

        }

        //sorting the clauses in the knowledge base according to the number of literals in each clause
        clauseList.sort(Comparator.comparingInt(clause::getSize));

        System.out.println("Enter your query.");
        String tempQuery = Keyboard.readString();

        //a new clause is created of the users query
        clause query = new clause(tempQuery);

        //the check method is called to check whether the query can be solved or not
        //if the method returns true then the query has been solved, if it returns false than it has not been solved
        if(check(query, clauseList)){
            System.out.println("SOLVED");
        }else {
            System.out.println("NOT SOLVED");
        }
    }
}
