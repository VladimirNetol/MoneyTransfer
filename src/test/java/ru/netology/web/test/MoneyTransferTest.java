package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.MoneyTransferPage;

import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
    }


    @Test
    void shouldTransferMoneyBetweenOwnCard() {
        var loginPage = new LoginPage();
        var info = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(info);
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = DashboardPage.getOwnCardBalance(firstCardInfo);
        var secondCardBalance = DashboardPage.getOwnCardBalance(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectATransferCard(secondCardInfo);
        dashboardPage = transferPage.moneyTransfer(String.valueOf(amount), firstCardInfo);
        dashboardPage.pageReload();
        assertAll(
                () -> dashboardPage.checkCardBalance(firstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(secondCardInfo, expectedBalanceSecondCard)
        );
    }

    @Test
    void shouldTransferMoneyOverLimit() {
        var loginPage = new LoginPage();
        var info = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(info);
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = DashboardPage.getOwnCardBalance(firstCardInfo);
        var secondCardBalance = DashboardPage.getOwnCardBalance(secondCardInfo);
        var amount = generateInValidAmount(secondCardBalance);
        var expectedBalanceFirstCard = secondCardBalance - amount;
        var expectedBalanceSecondCard = firstCardBalance + amount;
        var transferPage = dashboardPage.selectATransferCard(firstCardInfo);
        dashboardPage = transferPage.moneyTransfer(String.valueOf(amount), secondCardInfo);
        assertAll(
                () -> transferPage.checkErrorMessage("Ошибка! Сумма перевода превышает остаток на карте списания!"),
                () -> dashboardPage.pageReload(),
                () -> dashboardPage.checkCardBalance(firstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(secondCardInfo, expectedBalanceSecondCard)
        );
    }
}