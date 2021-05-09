package br.com.alloy.comanditcliente.ui.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class StringUtil {

    public static String formatCurrencyValue(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String formatado = nf.format(value);
        if(formatado.contains(" ")) {
            return formatado;
        } else {
            String symbol = Objects.requireNonNull(nf.getCurrency()).getSymbol(Locale.getDefault());
            return formatado.replace(symbol, symbol.concat(" "));
        }
    }

    public static String getCurrencyStringWithoutR$(BigDecimal value) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return fmt.format(value).replace(fmt.getCurrency().getSymbol(Locale.getDefault()), "").trim();
    }

}
