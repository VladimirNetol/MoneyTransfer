package ru.netology.web.page;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByText;
import org.openqa.selenium.By;
import ru.netology.web.data.DataHelper;

import java.util.concurrent.TransferQueue;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement topUpBalanceButton = $("[data-test-id='action-transfer']");
    private final SelenideElement transferPageVisibility = $(byText("Пополнение карты"));

    public MoneyTransferPage () {
        transferPageVisibility.shouldBe(Condition.visible);
    }

    public DashboardPage moneyTransfer(String amount, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amount);
        fromField.setValue(cardInfo.getNumberOfCard());
        topUpBalanceButton.click();
        return new DashboardPage();
    }
}
