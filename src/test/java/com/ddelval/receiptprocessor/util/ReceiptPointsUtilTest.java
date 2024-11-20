package com.ddelval.receiptprocessor.util;

import com.ddelval.receiptprocessor.model.Receipt;
import com.ddelval.receiptprocessor.model.ReceiptItem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptPointsUtilTest {

    @Test
    void testCalcPointsByRetailName_WithOnlyAlphaNumChars_ShouldCountAllChars() {
        String retailName = "Target123";
        int expectedPoints = 9;
        int actualPoints = ReceiptPointsUtil.calcPointsByRetailName(retailName);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcPointsByRetailName_WithSpecialChars_ShouldExcludeSpecialChars() {
        String retailName = "M&M Corner Market";
        int expectedPoints = 14;
        int actualPoints = ReceiptPointsUtil.calcPointsByRetailName(retailName);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcRoundDollarPoints_WithRoundDollarAmount_ShouldAwardFiftyPoints() {
        BigDecimal amount = new BigDecimal("2.00");
        int expectedPoints = 50;
        int actualPoints = ReceiptPointsUtil.calcRoundDollarPoints(amount);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcRoundDollarPoints_WithNonRoundDollarAmount_ShouldNotAwardFiftyPoints() {
        BigDecimal amount = new BigDecimal("1.01");
        int expectedPoints = 0;
        int actualPoints = ReceiptPointsUtil.calcRoundDollarPoints(amount);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcOneFourthDollarPoints_WithMultipleOfQuarter_ShouldAwardTwentyFivePoints() {
        BigDecimal amount = new BigDecimal("12.25");
        int expectedPoints = 25;
        int actualPoints = ReceiptPointsUtil.calcOneFourthDollarPoints(amount);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcOneFourthDollarPoints_NotMultipleOfQuarter_ShouldNotAwardPoints() {
        BigDecimal amount = new BigDecimal("12.30");
        int expectedPoints = 0;
        int actualPoints = ReceiptPointsUtil.calcOneFourthDollarPoints(amount);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcPurchaseDayPoints_WithOddDay_ShouldAwardSixPoints() {
        LocalDate date = LocalDate.of(2022, 3, 1);
        LocalTime time = LocalTime.of(10, 0);
        int expectedPoints = 6;
        int actualPoints = ReceiptPointsUtil.calcPurchaseDayPoints(date, time);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcPurchaseDayPoints_WithEvenDay_ShouldNotAwardPoints() {
        LocalDate date = LocalDate.of(2022, 3, 2);
        LocalTime time = LocalTime.of(10, 0);
        int expectedPoints = 0;
        int actualPoints = ReceiptPointsUtil.calcPurchaseDayPoints(date, time);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcPurchaseDayPoints_BetweenTwoAndFourPM_ShouldAwardTenPoints() {
        LocalDate date = LocalDate.of(2022, 3, 2);
        LocalTime time = LocalTime.of(14, 30);
        int expectedPoints = 10;
        int actualPoints = ReceiptPointsUtil.calcPurchaseDayPoints(date, time);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcPurchaseDayPoints_OutsideTwoAndFourPM_ShouldNotAwardPoints() {
        LocalDate date = LocalDate.of(2022, 3, 2);
        LocalTime time = LocalTime.of(16, 0);
        int expectedPoints = 0;
        int actualPoints = ReceiptPointsUtil.calcPurchaseDayPoints(date, time);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcItemPoints_WithTwoItems_ShouldAwardFivePointsForEachPair() {
        ReceiptItem item1 = new ReceiptItem("Item1", new BigDecimal("1.00"));
        ReceiptItem item2 = new ReceiptItem("Item2", new BigDecimal("2.00"));
        int expectedPoints = 5;
        int actualPoints = ReceiptPointsUtil.calcItemPoints(Arrays.asList(item1, item2));
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcDescriptionPoints_WithTrimmedDescriptionMultipleOfThree_ShouldAwardPriceTimes0_2() {
        String description = "    Mountain Dew 12PKs         ";
        BigDecimal price = new BigDecimal("6.49");
        int expectedPoints = 2; // 6.49 * 0.2 = 1.298, rounded up = 2 points
        int actualPoints = ReceiptPointsUtil.calcDescriptionPoints(description, price);
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    void testCalcDescriptionPoints_WithTrimmedDescriptionNotMultipleOfThree_ShouldAwardZeroPoints() {
        String description = "       Short Description       ";
        BigDecimal price = new BigDecimal("5.00");
        int expectedPoints = 0;
        int actualPoints = ReceiptPointsUtil.calcDescriptionPoints(description, price);
        assertEquals(expectedPoints, actualPoints);
    }

    @ParameterizedTest
    @MethodSource("provideReceiptsForTesting")
    void testCalcReceiptPoints_ShouldCalculateCorrectPoints(Receipt receipt, int expectedPoints) {
        int actualPoints = ReceiptPointsUtil.calcReceiptPoints(receipt);
        assertEquals(expectedPoints, actualPoints);
    }

    private static Stream<Arguments> provideReceiptsForTesting() {
        return Stream.of(
                Arguments.of(
                        new Receipt(
                                UUID.randomUUID(),
                                "M&M Corner Market",
                                LocalDate.of(2022, 3, 20),
                                LocalTime.of(14, 33),
                                new BigDecimal("9.00"),
                                Arrays.asList(
                                        new ReceiptItem("Gatorade", new BigDecimal("2.25")),
                                        new ReceiptItem("Gatorade", new BigDecimal("2.25")),
                                        new ReceiptItem("Gatorade", new BigDecimal("2.25")),
                                        new ReceiptItem("Gatorade", new BigDecimal("2.25"))
                                )
                        ),
                        109
                ),
                Arguments.of(
                        new Receipt(
                                UUID.randomUUID(),
                                "Target",
                                LocalDate.of(2022, 1, 1),
                                LocalTime.of(13, 1),
                                new BigDecimal("35.35"),
                                Arrays.asList(
                                        new ReceiptItem("Mountain Dew 12PK", new BigDecimal("6.49")),
                                        new ReceiptItem("Emils Cheese Pizza", new BigDecimal("12.25")),
                                        new ReceiptItem("Knorr Creamy Chicken", new BigDecimal("1.26")),
                                        new ReceiptItem("Doritos Nacho Cheese", new BigDecimal("3.35")),
                                        new ReceiptItem("Klarbrunn 12-PK 12 FL OZ", new BigDecimal("12.00"))
                                )
                        ),
                        28
                )
        );
    }
}
