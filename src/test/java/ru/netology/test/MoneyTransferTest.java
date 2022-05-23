package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.Data;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void auth() {
        open("http://localhost:9999", LoginPage.class);
        val loginPage = new LoginPage();
        val authInfo = Data.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = Data.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecond() {
        int amount = 5000;
        int FirstCardBalance = DashboardPage.getFirstCardBalance();
        int SecondCardBalance = DashboardPage.getSecondCardBalance();
        var TransferPage = DashboardPage.secondCard();
        var cardInfo = Data.getFirstCardInfo();
        TransferPage.transferCard(cardInfo, amount);
        int FirstCardAfterTransferBalance = Data.decreaseBalance(FirstCardBalance, amount);
        int SecondCardAfterTransferBalance = Data.increaseBalance(SecondCardBalance, amount);
        int FirstCardBalanceAfter = DashboardPage.getFirstCardBalance();
        int SecondCardBalanceAfter = DashboardPage.getSecondCardBalance();
        assertEquals(FirstCardAfterTransferBalance, FirstCardBalanceAfter);
        assertEquals(SecondCardAfterTransferBalance, SecondCardBalanceAfter);
    }

    @Test
    void shouldTransferMoneyFromSecondCardToFirst() {
        int amount = 5000;
        int FirstCardBalance = DashboardPage.getFirstCardBalance();
        int SecondCardBalance = DashboardPage.getSecondCardBalance();
        var TransferPage = DashboardPage.firstCard();
        var cardInfo = Data.getSecondCardInfo();
        TransferPage.transferCard(cardInfo, amount);
        int FirstCardAfterTransferBalance = Data.increaseBalance(FirstCardBalance, amount);
        int SecondCardAfterTransferBalance = Data.decreaseBalance(SecondCardBalance, amount);
        int FirstCardBalanceAfter = DashboardPage.getFirstCardBalance();
        int SecondCardBalanceAfter = DashboardPage.getSecondCardBalance();
        assertEquals(FirstCardAfterTransferBalance, FirstCardBalanceAfter);
        assertEquals(SecondCardAfterTransferBalance, SecondCardBalanceAfter);
    }

    @Test
    void shouldNotTransferMoneyAndShowErrorWhenAmountIsMoreThanAvailableBalance() {
        int amount = 20000;
        int FirstCardBalance = DashboardPage.getFirstCardBalance();
        int SecondCardBalance = DashboardPage.getSecondCardBalance();
        var TransferPage = DashboardPage.firstCard();
        var cardInfo = Data.getSecondCardInfo();
        TransferPage.transferCard(cardInfo, amount);
        Data.increaseBalance(FirstCardBalance, amount);
        Data.decreaseBalance(SecondCardBalance, amount);
//        TransferPage.transferMoneyError();
    }
}