//********************************************************************
//
//  Author:       Afaq Ahmad
//
//  Program #:    9
//
//  File Name:    Program9.java
//
//  Course:       ITSE 2321 Object-Oriented Programming
//
//  Due Date:     December 6th
//
//  Instructor:   Prof. Fred Kumi
//
//  Chapter:      Chapter 1 - 9, 11, and 15: ArrayList, Objects, Classes, Methods, Exception, and Files
//
//  Description: This program reads data from a file and calculates the average income of households, households above
//
//********************************************************************

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Program9
{
    //***************************************************************
    //
    //  Method:       main
    //
    //  Description:  The main method of the program
    //
    //  Parameters:   String array
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public static void main(String[] args)
    {
        developerInfo();

        Program9 p = new Program9();
        p.run();

    } // End of the main method

    //***************************************************************
    //
    //  Method:       developerInfo
    //
    //  Description:  The developer information method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************

    public static void developerInfo()
    {
        System.out.println("Name:     Afaq Ahmad");
        System.out.println("Course:   ITSE 2321 Object-Oriented Programming");
        System.out.println("Program:  Nine");
        System.out.println("Due Date: December 6th\n");
    } // End of the developerInfo method

    public void run()
    {
        ArrayList<HouseHold> list = readData("Program9.txt");

        // Prepare output file
        try(PrintWriter pw = new PrintWriter(new FileWriter("Program9-Output.txt")))
        {
            // a) Print all households
            printAllHouseholds(pw, list);

            // b) Calculate and print average household income
            double average = computeAverageIncome(list);
            pw.printf("%nAverage Household Income: %.2f%n%n", average);

            // c) Print households that exceed the average
            printAboveAverage(pw, list, average);

            // d) Print households below the poverty level
            printBelowPoverty(pw, list);

            // e) Print percentage of households below poverty level
            double belowPovertyPercent = computeBelowPovertyPercent(list);
            pw.printf("%nPercentage of Households Below Poverty Level: %.2f%%%n", belowPovertyPercent);

            // f) Print percentage of households that would qualify for Medicaid (138% of FPL)
            double medicaidPercent = computeMedicaidPercent(list);
            pw.printf("%nPercentage of Households Qualifying for Medicaid (138%% of FPL): %.2f%%%n", medicaidPercent);

            pw.flush();
        }
        catch(IOException e)
        {
            System.out.println("Error writing output file: " + e.getMessage());
        }
    }

    //***************************************************************
    //
    //  Method:       readData
    //
    //  Description:  Reads household data from the given filename into an ArrayList
    //
    //  Parameters:   filename - name of input file
    //
    //  Returns:      ArrayList<HouseHold>
    //
    //**************************************************************
    public ArrayList<HouseHold> readData(String filename)
    {
        ArrayList<HouseHold> tempList = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(filename)))
        {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    // Split by whitespace
                    String[] tokens = line.split("\\s+");

                    // The first three tokens are ID, Income, and Members
                    int id = Integer.parseInt(tokens[0]);
                    double income = Double.parseDouble(tokens[1]);
                    int members = Integer.parseInt(tokens[2]);

                    // The rest of the tokens combined form the state
                    StringBuilder stateBuilder = new StringBuilder();
                    for (int i = 3; i < tokens.length; i++) {
                        stateBuilder.append(tokens[i]);
                        if (i < tokens.length - 1) {
                            stateBuilder.append(" ");
                        }
                    }
                    String state = stateBuilder.toString();

                    HouseHold hh = new HouseHold(id, income, members, state);
                    tempList.add(hh);
                }
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Input file not found: " + e.getMessage());
        }

        return tempList;
    }

    //***************************************************************
    //
    //  Method:       printAllHouseholds
    //
    //  Description:  Print the record of each household in four-column format
    //
    //  Parameters:   pw - PrintWriter to write to output file
    //                list - ArrayList of HouseHold
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void printAllHouseholds(PrintWriter pw, ArrayList<HouseHold> list)
    {
        pw.println("All Households:");
        pw.printf("%-10s %-15s %-10s %-15s%n", "ID", "Income", "Members", "State");
        pw.println("------------------------------------------------------------");
        for(HouseHold h : list)
        {
            pw.printf("%-10d %-15.2f %-10d %-15s%n", h.getId(), h.getIncome(), h.getMembers(), h.getState());
        }
        pw.println();
    }

    //***************************************************************
    //
    //  Method:       computeAverageIncome
    //
    //  Description:  Compute the average household income
    //
    //  Parameters:   list - ArrayList of HouseHold
    //
    //  Returns:      double - average income
    //
    //**************************************************************
    public double computeAverageIncome(ArrayList<HouseHold> list)
    {
        double total = 0.0;
        for(HouseHold h : list)
        {
            total += h.getIncome();
        }
        return (list.size() > 0) ? (total / list.size()) : 0.0;
    }

    //***************************************************************
    //
    //  Method:       printAboveAverage
    //
    //  Description:  Print households that exceed the average income
    //
    //  Parameters:   pw - PrintWriter
    //                list - ArrayList of HouseHold
    //                average - the average income
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void printAboveAverage(PrintWriter pw, ArrayList<HouseHold> list, double average)
    {
        pw.println("Households Above Average Income:");
        pw.printf("%-10s %-15s %-10s %-15s%n", "ID", "Income", "Members", "State");
        pw.println("------------------------------------------------------------");
        for(HouseHold h : list)
        {
            if(h.getIncome() > average)
            {
                pw.printf("%-10d %-15.2f %-10d %-15s%n", h.getId(), h.getIncome(), h.getMembers(), h.getState());
            }
        }
        pw.println();
    }

    //***************************************************************
    //
    //  Method:       printBelowPoverty
    //
    //  Description:  Print households below poverty level in a five-column format
    //                ID, Income, PovertyLevel, Members, State
    //
    //  Parameters:   pw - PrintWriter
    //                list - ArrayList of HouseHold
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void printBelowPoverty(PrintWriter pw, ArrayList<HouseHold> list)
    {
        pw.println("Households Below Poverty Level:");
        pw.printf("%-10s %-15s %-15s %-10s %-15s%n", "ID", "Income", "PovertyLevel", "Members", "State");
        pw.println("--------------------------------------------------------------------------");
        for(HouseHold h : list)
        {
            double pov = h.getPovertyLevel2024();
            if(h.getIncome() < pov)
            {
                pw.printf("%-10d %-15.2f %-15.2f %-10d %-15s%n", h.getId(), h.getIncome(), pov, h.getMembers(), h.getState());
            }
        }
        pw.println();
    }

    //***************************************************************
    //
    //  Method:       computeBelowPovertyPercent
    //
    //  Description:  Computes the percentage of households below the poverty level
    //
    //  Parameters:   list - ArrayList of HouseHold
    //
    //  Returns:      double - percentage
    //
    //**************************************************************
    public double computeBelowPovertyPercent(ArrayList<HouseHold> list)
    {
        if(list.size() == 0) return 0.0;
        int count = 0;
        for(HouseHold h : list)
        {
            double pov = h.getPovertyLevel2024();
            if(h.getIncome() < pov)
                count++;
        }
        return (count * 100.0 / list.size());
    }

    //***************************************************************
    //
    //  Method:       computeMedicaidPercent
    //
    //  Description:  Computes the percentage of households that qualify for Medicaid,
    //                defined as income < 138% of FPL.
    //
    //  Parameters:   list - ArrayList of HouseHold
    //
    //  Returns:      double - percentage
    //
    //**************************************************************
    public double computeMedicaidPercent(ArrayList<HouseHold> list)
    {
        if(list.size() == 0) return 0.0;
        int count = 0;
        for(HouseHold h : list)
        {
            double pov = h.getPovertyLevel2024();
            double threshold = 1.38 * pov;
            if(h.getIncome() < threshold)
                count++;
        }
        return (count * 100.0 / list.size());
    }
}