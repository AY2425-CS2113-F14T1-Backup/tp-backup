package seedu.duke;

import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import constant.Constant;
import enumStructure.Category;
import enumStructure.Currency;
import enumStructure.Status;
import exceptions.InvalidCommand;
import exceptions.NullException;
import ui.Ui;
import seedu.duke.budget.BudgetList;

public class TransactionManager {
    private ArrayList<Transaction> transactions;
    private Currency defaultCurrency = Currency.SGD;
    private double budgetLimit = -1;
    private boolean isBudgetSet = false;

    private BudgetList budgetList = new BudgetList();

    private Storage storage;
    private int currentMaxId = 0;  // 永久自增ID

    public void setStorage(Storage storage) {
        this.storage = storage;
        this.currentMaxId = storage.loadMaxTransactionId();
    }

    private int getNextAvailableId() {
        currentMaxId += 1;
        if (storage != null) {
            storage.saveMaxTransactionId(currentMaxId);
        }
        return currentMaxId;
    }

    public void setBudgetList(BudgetList budgetList) {
        this.budgetList = budgetList;
    }

    public TransactionManager() {
        transactions = new ArrayList<>();
    }

    public int getNum() {
        return transactions.size();
    }

    public int getSize() {
        int count = 0;
        for (Transaction t : transactions) {
            if (!t.isDeleted()) {
                count++;
            }
        }
        return count;
    }

    public double getTotalTransactionAmount() {
        double sum = 0;
        for (Transaction t : transactions) {
            if (!t.isDeleted()) {
                sum += t.getAmount();
            }
        }
        return sum;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        int id = transaction.getId();
        if (id > currentMaxId) {
            currentMaxId = id;
            if (storage != null) {
                storage.saveMaxTransactionId(currentMaxId);
            }
        }
    }

    public boolean addTransaction(String description, double amount, Category category) {
        int id = getNextAvailableId(); // 自动分配唯一ID
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction(id, description, amount, defaultCurrency, category, date, Status.PENDING);

        if (isBudgetSet) {
            double projected = getTotalTransactionAmount() + amount;
            if (projected > budgetLimit) {
                System.out.println("Cannot add new transaction! Budget limit exceeded!");
                return false;
            }
        }

        transactions.add(transaction);
        return true;
    }


    public boolean addTransaction(int id, String description, double amount, Category category) {
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction(id, description, amount, defaultCurrency, category, date, Status.PENDING);

        if (isBudgetSet && (getTotalTransactionAmount() + amount > budgetLimit)) {
            System.out.println("Cannot add new transaction! Budget limit exceeded!");
            return false;
        }

        transactions.add(transaction);
        if (id > currentMaxId) {
            currentMaxId = id;
            if (storage != null) {
                storage.saveMaxTransactionId(currentMaxId);
            }
        }
        return true;
    }

    public ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> active = new ArrayList<>();
        for (Transaction t : transactions) {
            if (!t.isDeleted()) {
                active.add(t);
            }
        }
        sortTransactions(active);
        return active;
    }

    public void deleteExpense(int id) {
        Transaction t = searchTransaction(id);
        if (t != null) {
            t.delete();
        }
    }

    public void checkBudgetLimit(double limit) {
        double total = getTotalTransactionAmount();
        if (total > limit) {
            System.out.println("Warning: You have exceeded your budget limit!");
            System.out.println("Current amount that exceed the budget are: " + (total - limit));
        } else {
            this.budgetLimit = limit;
            this.isBudgetSet = true;
            System.out.println("Budget limit set to " + limit + " " + defaultCurrency);
        }
    }

    public void clear() {
        transactions.clear();
        budgetList.clear();
    }

    public Transaction searchTransaction(int id) {
        for (Transaction t : transactions) {
            if (t.getId() == id && !t.isDeleted()) {
                return t;
            }
        }
        System.out.println("Transaction is invalid");
        return null;
    }

    public ArrayList<Transaction> searchTransactionList(boolean isIndex, String term) {
        ArrayList<Transaction> result = new ArrayList<>();
        try {
            if (isIndex) {
                Transaction t = searchTransaction(Integer.parseInt(term));
                if (t != null) result.add(t);
            } else {
                for (Transaction t : transactions) {
                    if (!t.isDeleted() && t.getDescription().contains(term)) {
                        result.add(t);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void remindRecurringTransactions() {
        ArrayList<Transaction> upcoming = new ArrayList<>();
        for (Transaction t : transactions) {
            if (!t.isDeleted() && t.getRecurringPeriod() > 0) {
                upcoming.add(t);
            }
        }
        if (!upcoming.isEmpty()) {
            sortRecurringTransactions(upcoming);
            Ui.printRecurringTransactions(upcoming);
        }
    }

    public ArrayList<Transaction> sortRecurringTransactions(ArrayList<Transaction> list) {
        for (Transaction t : list) {
            int period = t.getRecurringPeriod();
            while (t.getDate().isBefore(LocalDate.now())) {
                t.setDate(t.getDate().plusDays(period));
            }
        }
        list.sort(Comparator.comparing(Transaction::getDate));
        return list;
    }

    public void notify(String desc, double amount, String category, String date) {
        LocalDate due = LocalDate.parse(date);
        Category cat = Category.valueOf(category);
        for (Transaction t : transactions) {
            if (t.getDescription().equals(desc) && t.getCategory() == cat) {
                t.setDate(due);
            }
        }
    }

    public void tickTransaction(int id) {
        Transaction t = searchTransaction(id);
        if (t != null) t.complete();
    }

    public void unTickTransaction(int id) {
        Transaction t = searchTransaction(id);
        if (t != null) t.notComplete();
    }

    public void setRecur(int id, int period) {
        Transaction t = searchTransaction(id);
        if (t != null) t.setRecurringPeriod(period);
    }

    public ArrayList<Transaction> sortTransactions(ArrayList<Transaction> list) {
        list.sort(Comparator.comparing(Transaction::getDate, Comparator.nullsLast(Comparator.naturalOrder())));
        return list;
    }

    public ArrayList<Transaction> getTransactionsOnDate(LocalDate date) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction t : getTransactions()) {
            if (t.getDate() != null && t.getDate().equals(date)) {
                result.add(t);
            }
        }
        return result;
    }

    public ArrayList<Transaction> getTransactionsThisMonth() {
        ArrayList<Transaction> result = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (Transaction t : getTransactions()) {
            if (t.getDate() != null &&
                    t.getDate().getMonthValue() == now.getMonthValue() &&
                    t.getDate().getYear() == now.getYear()) {
                result.add(t);
            }
        }
        return result;
    }

    public ArrayList<Transaction> getTransactionsThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction t : getTransactions()) {
            if (t.getDate() != null &&
                    !t.getDate().isBefore(today) &&
                    !t.getDate().isAfter(nextWeek)) {
                result.add(t);
            }
        }
        return result;
    }

    public void getUpcomingTransactions(String period) {
        switch (period.toLowerCase()) {
            case "today" -> System.out.println(getTransactionsOnDate(LocalDate.now()));
            case "week" -> System.out.println(getTransactionsThisWeek());
            case "month" -> System.out.println(getTransactionsThisMonth());
            default -> {
                try {
                    LocalDate date = LocalDate.parse(period);
                    System.out.println(getTransactionsOnDate(date));
                } catch (Exception e) {
                    System.out.println("Invalid period. Use 'today', 'week', 'month', or a date (yyyy-mm-dd)");
                }
            }
        }
    }

    public void editInfo(int id, String value, int type) throws Exception {
        if (checkIdEmpty(id)) return;
        Transaction t = searchTransaction(id);
        if (t == null) return;

        switch (type) {
            case 0 -> t.setDescription(value);
            case 1 -> t.setCategory(Category.valueOf(value));
            case 2 -> {
                int val = Integer.parseInt(value);
                if (val < 0) throw new InvalidCommand("Expense cannot be negative!");
                t.setAmount(val);
            }
            case 3 -> t.setCurrency(Currency.valueOf(value));
        }
    }

    public boolean checkIdEmpty(int id) {
        if (searchTransaction(id) == null) {
            System.out.println(Constant.INVALID_TRANSACTION_ID);
            return true;
        }
        return false;
    }

    public double getTotalAmount() {
        return getRecurringAmount() + getNormalAmount();
    }

    public double getRecurringAmount() {
        double sum = 0;
        for (Transaction t : transactions) {
            if (!t.isDeleted() && t.getRecurringPeriod() > 0) {
                long days = ChronoUnit.DAYS.between(t.getDate(), LocalDate.now());
                sum += t.getAmount() * (days / t.getRecurringPeriod() + 1);
            }
        }
        return sum;
    }

    public double getNormalAmount() {
        double sum = 0;
        for (Transaction t : transactions) {
            if (!t.isDeleted() && t.getRecurringPeriod() <= 0) {
                sum += t.getAmount();
            }
        }
        return sum;
    }

    public BudgetList getBudgetList() {
        return budgetList;
    }
}