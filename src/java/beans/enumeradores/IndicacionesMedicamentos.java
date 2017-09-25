/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.enumeradores;

/**
 *
 * @author Enderson
 */
public enum IndicacionesMedicamentos {

    AHORA("ahora"),
    CADA_HORAI("1 Hora"),
    CADA_HORAII("2 Hora"),
    CADA_HORAIII("3 Hora"),
    CADA_HORAIV("4 Hora"),
    CADA_HORAV("5 Hora"),
    CADA_HORAVI("6 Hora"),
    CADA_HORAVII("7 Hora"),
    CADA_HORAVIII("8 Hora"),
    CADA_HORAIX("9 Hora"),
    CADA_HORAX("10 Hora"),
    CADA_HORAXI("11 Hora"),
    CADA_HORAXII("12 Hora"),
    CADA_HORAXIII("13 Hora"),
    CADA_HORAXIV("14 Hora"),
    CADA_HORAXV("15 Hora"),
    CADA_HORAXVI("16 Hora"),
    CADA_HORAXVII("17 Hora"),
    CADA_HORAXVIII("18 Hora"),
    CADA_HORAXIX("19 Hora"),
    CADA_HORAXX("20 Hora"),
    CADA_HORAXXI("21 Hora"),
    CADA_HORAXXII("22 Hora"),
    CADA_HORAXXIII("23 Hora"),
    CADA_HORAXXIV("24 Hora");

    private String description;

    private IndicacionesMedicamentos(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
