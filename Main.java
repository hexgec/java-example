package com.company;

public class Main {

    public static void main(String[] args) {

        int choice;
        boolean loop = true;

        //the loop is existed when the user chooses option 3
        while (loop) {

            //the options are displayed to the user
            System.out.print("Enter the task number you would like to execute: \n" +
                    "1) Parsing of Horn Clauses, and Reasoning using Back-Chaining \n" +
                    "2) Construction and Reasoning with Inheritance Networks \n" +
                    "3) Quit\n" +
                    "Option: ");
            choice = Keyboard.readInt();
            System.out.println("\n");

            //depending on the users choice, a different function is carried out
            switch (choice) {

                //if option 1 is chosen then Task 1 is executed
                case 1:

                    Task1.main1();
                    System.out.println("\n\nTask completed.\n\n");

                    break;

                //if option 2 is chosen then Task 2 is executed
                case 2:

                    Task2.main2();
                    System.out.println("\n\nTask completed.\n\n");

                    break;

                //if option 3 is chosen then the 'loop' variable is set to false so that the while loop will be exited
                case 3:

                    System.out.println("Goodbye");
                    loop = false;
                    break;

                // if none of the 3 options are chosen then the user is prompted to enter another option
                default:

                    System.out.println("\n\nIncorrect Input.\n\n");
                    break;

            }
        }

    }
}
