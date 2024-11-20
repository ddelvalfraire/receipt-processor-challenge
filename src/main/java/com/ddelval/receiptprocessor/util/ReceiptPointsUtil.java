package com.ddelval.receiptprocessor.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ddelval.receiptprocessor.model.Receipt;
import com.ddelval.receiptprocessor.model.ReceiptItem;

public class ReceiptPointsUtil {

    public static int calcReceiptPoints(Receipt receipt) {
        int points = 0;
        
        points += calcPointsByRetailName(receipt.getRetailer());
        points += calcRoundDollarPoints(receipt.getTotal());
        points += calcOneFourthDollarPoints(receipt.getTotal());
        points += calcPurchaseDayPoints(receipt.getPurchaseDate(), receipt.getPurchaseTime());
        points += calcItemPoints(receipt.getItems());

        return points;
    }

    public static int calcPointsByRetailName(String retailName) {
        int points = 0;
        for (char ch : retailName.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                points++;
            }
        }
        return points;
    }

    public static int calcRoundDollarPoints(BigDecimal amount) {
        if (isRoundNumber(amount)) {
            return 50;
        }
        return 0;
    }

    public static int calcOneFourthDollarPoints(BigDecimal amount) {
        if (isOneFourthDollarMultiple(amount)) {
            return 25;
        }
        return 0;
    }

    public static int calcPurchaseDayPoints(LocalDate date, LocalTime time) {
        int points = 0;

        if (dayIsOdd(date)) {
            points += 6;
        }

        if (isBetween2pmAnd4pm(time)) {
            points += 10;
        }

        return points;

    }

    public static int calcItemPoints(List<ReceiptItem> items) {
        int points = 0;

        for (ReceiptItem item : items) {

            String description = item.getShortDescription();
            BigDecimal price = item.getPrice();

            points += calcDescriptionPoints(description, price);
        }

        // 5 points for every two items
        points += items.size() / 2 * 5;

        return points;
    }

    public static int calcDescriptionPoints(String description, BigDecimal price) {
        String trimmedStr = description.trim();

        if (!isMultipleOfThree(trimmedStr.length())) {
            return 0;
        }

        BigDecimal fifthPrice = price.multiply(BigDecimal.valueOf(0.2));

       
        return fifthPrice.setScale(0, RoundingMode.CEILING).intValue();
    }

    // non-inclusive start and end
    private static boolean isBetween2pmAnd4pm(LocalTime time) {
        return time.isAfter(LocalTime.of(14, 0)) && time.isBefore(LocalTime.of(16, 0));
    }

    private static boolean dayIsOdd(LocalDate date) {
        return date.getDayOfMonth() % 2 != 0;
    }

    private static boolean isMultipleOfThree(int number) {
        return number % 3 == 0;
    }

    private static boolean isOneFourthDollarMultiple(BigDecimal amount) {
        // Multiply by 4 and check if the result is an integer
        BigDecimal multiplied = amount.multiply(BigDecimal.valueOf(4));
        return multiplied.stripTrailingZeros().scale() <= 0;
    }

    private static boolean isRoundNumber(BigDecimal amount) {
        return amount.stripTrailingZeros().scale() <= 0;
    }

}
