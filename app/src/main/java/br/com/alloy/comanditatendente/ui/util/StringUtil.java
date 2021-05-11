package br.com.alloy.comanditatendente.ui.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class StringUtil {

    public static final Locale  DEFAULT_LOCALE = new Locale("pt", "BR");

    /**
     *  String vazia ("")
     */
    public static final String EMPTY_STRING = "";

    /**
     *  Espaço em branco (" ")
     */
    public static final String WHITE_SPACE = " ";

    /**
     *  Separador do Log para inserções de diferentes termos no log da aplicação
     */
    public static final String LOG_SEPARATOR = " - ";

    /**
     *  Separador de linha genérico
     */
    public static final String NEW_LINE = "\n";

    /**
     *
     * @param value
     * @return
     */
    public static boolean isEmptyString(String value) {
        return value.trim().isEmpty();
    }

    /**
     *
     * @param value
     * @return
     */
    public static boolean isEmptyStringNoTrim(String value) {
        return value.isEmpty();
    }

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
