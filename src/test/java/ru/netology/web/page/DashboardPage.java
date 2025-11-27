package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.WithText;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final SelenideElement header = $("[data-test-id='dashboard']");

    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private static final ElementsCollection cards = $$(".list__item div");
    private static final String balanceStart = "баланс: ";
    private static final String balanceFinish = " р.";

    //private final SelenideElement reloadButton = $("data-test-id='action-reload'");
    private final SelenideElement reloadButton = $(byText("Обновить"));

    public DashboardPage() {
        header.shouldBe(Condition.visible);
    }


    public static int getOwnCardBalance(DataHelper.CardInfo cardInfo) {
        var text = getCard(cardInfo).getText();
        return extractBalance(text);
    }

    public MoneyTransferPage selectATransferCard(DataHelper.CardInfo cardInfo) {
        getCard(cardInfo).$("button").click();
        return new MoneyTransferPage();
    }

    private static SelenideElement getCard(DataHelper.CardInfo cardInfo) {
    return cards.findBy(Condition.attribute("data-test-id", cardInfo.getCardID()));
    }

    public void pageReload() {
        reloadButton.click();
        header.shouldBe(Condition.visible);
    }

    private static int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void checkCardBalance (DataHelper.CardInfo cardInfo, int expectedBalance) {
        getCard(cardInfo).shouldBe(Condition.visible).
                should(Condition.text(balanceStart + expectedBalance +balanceFinish));
    }
}

