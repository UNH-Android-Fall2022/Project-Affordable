package com.affordable.data.models

val DEFAULT_CARD_FORMAT = "(\\d{1,4})"

enum class CardValidator(
    val cardName: String,
    val cardPattern: String,
    val cardFormat: String
) {
    VISA("VISA", "^4[0-9]{12}(?:[0-9]{3})?$", DEFAULT_CARD_FORMAT),
    MAESTRO("MAESTRO", "^(5[06-9]|6[37])[0-9]{10,17}$", DEFAULT_CARD_FORMAT),
    MASTERCARD("MASTERCARD", "^5[0-5][0-9]{14}$", DEFAULT_CARD_FORMAT),
    DINERSCLUB(
        "DINERSCLUB",
        "^3(?:0[0-5]|[68][0-9])?[0-9]{11}$",
        "(\\d{1,4})(\\d{1,6})?(\\d{1,4})?"
    ),
    LASER("LASER", "^(6304|6706|6709|6771)[0-9]{12,15}$", DEFAULT_CARD_FORMAT),
    JCB("JCB", "^(?:2131|1800|35[0-9]{3})[0-9]{11}$", DEFAULT_CARD_FORMAT),
    UNIONPAY("UNIONPAY", "^(62[0-9]{14,17})$", DEFAULT_CARD_FORMAT),
    DISCOVER("DISCOVER", "^6(?:011|5[0-9]{2})[0-9]{12}$", DEFAULT_CARD_FORMAT),
    AMEX("AMEX", "^3[47][0-9]{13}$", "^(\\d{1,4})(\\d{1,6})?(\\d{1,5})?$"),
    AMERICANEXPRESS(
        "AMERICANEXPRESS", "^[34|37][0-9]{14}$",
        DEFAULT_CARD_FORMAT
    ),
    SOLO(
        "SOLO", "^(6334|6767)[0-9]{12}|(6334|6767)[0-9]{14}|(6334|6767)[0-9]{15}$",
        DEFAULT_CARD_FORMAT
    ),
    INSTAPAYMENT("", "^63[7-9][0-9]{13}$", DEFAULT_CARD_FORMAT)
}