package command;

import seedu.duke.Transaction;
import seedu.duke.TransactionManager;
import ui.Ui;

import java.util.Scanner;

import static ui.ConsoleFormatter.*;

public class StatusWizardCommand extends Command {
    @Override
    public void execute(TransactionManager transactions, Ui ui) {
        Scanner scanner = new Scanner(System.in);

        printCenteredTitle("Status Wizard");
        printCenteredLine("Status Wizard - Complete or undo a transaction");
        printCenteredLine("Type 'cancel' at any time to abort.");
        printLine();

        Transaction target = null;
        while (target == null) {
            System.out.print("Transaction/Status> Enter Transaction ID: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("cancel")) return;
            try {
                int id = Integer.parseInt(input);
                target = transactions.searchTransaction(id);
                if (target == null) {
                    printCenteredTitle("ERROR: Status Wizard");
                    printLeftAlignedLine("Transaction not found.");
                    printLine();
                }
            } catch (Exception e) {
                printCenteredTitle("ERROR: Status Wizard");
                printLeftAlignedLine("Invalid ID format.");
                printLine();
            }
        }

        System.out.println("Transaction/Status> Choose status:");
        System.out.println("1. Mark as Completed");
        System.out.println("2. Mark as Not Completed");
        System.out.print("Transaction/Status> Enter choice: ");
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("cancel")) return;

        switch (choice) {
            case "1" -> {
                target.complete();
                ui.tickTransaction(target);
            }
            case "2" -> {
                target.notComplete();
                ui.unTickTransaction(target);
            }
            default -> {
                printCenteredTitle("ERROR: Status Wizard");
                printLeftAlignedLine("Invalid choice.");
                printLine();
            }
        }
    }
}
