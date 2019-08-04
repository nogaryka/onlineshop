package net.thumbtack.onlineshop.exceptions;

public class ErrorCod {
    public static final String THIS_LOGIN_IS_EXIST = "Такой логин уже занят";

    public static final String UNAUTHORIZED_ATTEMPT_TO_ACCESS =
            "Несанкционированная попытка доступа незарегестрированного пользователя";

    public static final String INCORRECT_PRICE_OR_NAME =
            "Цена или имя указанного продукта не совпадает с указанным именем и ценой в запросе";

    public static final String INCORRECT_PRODUCT_ID = "Товара с таким id не существует в корзине";

    public static final String INCORRECT_PRODUCT_ID_IN_BASKET = "Товара с таким id не существует в корзине";

    public static final String INCORRECT_COUNT = "Неккоректное кол-во товара";

    public static final String INCORRECT_OPERATTION = "Неккоректная операция";

    public static final String LIST_OF_CATEGORIES_AND_PRODUCTS = "Нельзя задать одновременно список категорий и список товаров";

    public static final String THERE_ARE_NO_PURCHASES = "Покупок, удовлевторяющих заданные требования, не сущесвтует";

    public static final String PURCHASE_OR_BASKET = "Надо ввести Purchase или Basket";
}
