//cited from https://github.com/ReGaSLZR/CardValidator-Android 
package com.affordable.data.models

val DEFAULT_CARD_FORMAT = "(\\d{1,4})"

enum class CardValidator(
    val cardName: String,
    val cardPattern: String,
    val cardFormat: String
) {
    VISA("VISA", "^4[0-9]{12}(?:[0-9]{3})?$", DEFAULT_CARD_FORMAT),
    MASTERCARD("MASTERCARD", "^5[0-5][0-9]{14}$", DEFAULT_CARD_FORMAT),
    AMEX("AMEX", "^3[47][0-9]{13}$", "^(\\d{1,4})(\\d{1,6})?(\\d{1,5})?$"),
    AMERICANEXPRESS(
        "AMERICANEXPRESS", "^[34|37][0-9]{14}$",
        DEFAULT_CARD_FORMAT
    )
}
