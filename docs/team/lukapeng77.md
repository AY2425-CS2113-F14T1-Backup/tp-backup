# Project Portfolio Page

## Overview
This project is a text application that interact using text line. The Main Purpose of this product is for expense management.
It covers data saving, data parsing and data analysis.

## Summary of Contributions
I implemented six main features in the product: deletion, setting budget limits, notification system, priority tagging, spending alerts, and summary generation. 
I also contributed to currency conversion functionalities.

### Code Contributions:
Can view my code contribution from [here](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=Luka&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

I implemented five main functional features of this product, such as deletion, set budget and priorities, notification, alert and summary.

---
**Feature Description:**
added three main functionalities to delete transaction, set budget limit and notifications for transactions:
* Delete Transaction (delete): Lets users remove unwanted or erroneous transaction entries using their index in the displayed list.
* Set Budget Limit (setBudget): Enables users to define a spending cap to avoid overspending. Once the total recorded expenses exceed this limit, a warning is displayed to alert the user.
* Set Notifications for Upcoming Payments (notify): Allows users to schedule reminders for future expenses based on the transaction's description, amount, category, and due date.
* Set Priority (priority): Allows users to assign a priority level (low, medium, or high) to a transaction. By default, all expenses are low priority. This helps users focus on the most urgent or important expenses.
* View Spending Alerts (alert): Displays an overview of upcoming expenses, recurring payments, current remaining budget, and whether the spending has exceeded the set budget limit. This command combines notification, priority, and budget insights in a single view.
* Summary of Expenses (summary): Provides an overview of expenses for a specified time frame, enabling quick and effective financial review.
* Convert Currency (convert): Allows users to convert the amount of a transaction from its original currency to a specified target currency. This is particularly useful for users tracking multi-currency expenses or international purchases.

    
**Design Consideration:**

The `deletion`, `budget limit` and `notification` features are designed to improve financial awareness and discipline.
Users can monitor their spending relative to a predefined threshold and receive timely reminders for future payments.

Priority levels help users organize and focus on transactions based on importance or urgency.  
Spending alerts consolidate relevant financial insights—notifications, recurring transactions, and budget status—into
one unified interface for quick decision-making.

Expense Summaries: The summary feature provides users with quick, insightful overviews of their spending patterns 
within a selected time frame, enhancing their ability to review and adjust financial behavior promptly.

The convert feature adds a layer of flexibility for users dealing with multiple currencies. It simplifies financial tracking by converting values into a uniform currency for easier comparison and understanding. It is designed with immediate feedback through the UI to help users understand how currency changes affect their recorded transactions. 
The conversion is performed using the transaction's stored data and is integrated directly into the TransactionManager, ensuring that currency changes do not interfere with other transaction attributes or analytical features.

These capabilities integrate seamlessly with the transaction management system, enhancing the user experience through automation
and clear visual cues for overspending or pending transactions.

---

### Enhancements implemented:
For the budget feature, logic enhancement ensures users verify current budget conditions before adding transactions. 
Users receive immediate feedback if a transaction exceeds the budget limit, encouraging proactive adjustments.

Refined the budget check logic to verify constraints before allowing new transaction additions.

Enhanced real-time feedback for transactions exceeding budget limits.

Improved resilience of the notify and priority commands through better error handling and clearer CLI prompts.

### Contributions to the UG:
Documented command formats, parameters, and examples for all features I implemented (delete, setbudget, notify, priority, alert, summary, convert).

Structured the command section for better user readability and consistent format.

Helped ensure that V2.0 features were clearly communicated and easy for end-users to understand and use.

### Contributions to the DG:
Authored the V2.0 user stories related to budgeting, alerts, priorities, and summaries.

Added descriptions, design considerations, and sequence diagrams for the features I developed.

Helped maintain the non-functional requirements, ensuring consistency with implementation goals and long-term usability of the product.