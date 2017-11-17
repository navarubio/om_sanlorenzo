/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Fechas {
	public Fechas() {

	}

	public String SumarFecha(String desde, int cant, String formato)
			throws Exception {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat(formato);
		java.util.Date fi;
		String hasta = "";
		try {
			fi = df.parse(desde);
			gc.setTime(fi);
			gc.add(Calendar.DATE, cant);
			fi = gc.getTime();
			hasta = df.format(fi);
		} catch (Exception ex) {
			throw new Exception("Error al realizar operacion de fechas");
		}
		return hasta;
	}

	/**
	 * Nuevo que permite saber el formato q se va a aumentar
	 * 
	 * @param desde
	 * @param cant
	 * @param formato
	 * @return
	 * @throws Exception
	 */
	public String SumarFecha(String desde, int cant, String formato, char _tipo)
			throws Exception {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat(formato);
		java.util.Date fi;
		String hasta = "";
		try {
			fi = df.parse(desde);
			gc.setTime(fi);
			switch (_tipo) {
			case 'y':
				gc.add(Calendar.YEAR, cant);
				break;
			case 'm':
				gc.add(Calendar.MONTH, cant);
				break;
			case 'd':
				gc.add(Calendar.DAY_OF_YEAR, cant);
				break;
			default:
				gc.add(Calendar.DATE, cant);
				break;
			}
			fi = gc.getTime();
			hasta = df.format(fi);
		} catch (Exception ex) {
			throw new Exception("Error al realizar operacion de fechas");
		}
		return hasta;
	}

	/**
	 * Para obtener dia final del mes
	 * 
	 * @param _mes
	 * @param _ano
	 * @return
	 */
	public int obtenerDiasMes(int _mes, int _ano) {
		int dias = 0;
		switch (_mes) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			dias = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			dias = 30;
			break;
		case 2:
			if ((_ano % 4) == 0) {
				dias = 29;
			} else {
				dias = 28;
			}
			break;
		}
		return dias;
	}

	public String formateaFecha(String _fecha, String _formato)
			throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(_formato);
		java.util.Date fd = new Date();
		try {
			fd = df.parse(_fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df.format(fd);
	}

	public String fechaActual(String _formato) {
		SimpleDateFormat df = new SimpleDateFormat(_formato);
		return df.format(new Date());

	}

	public String cambiarFormatoFecha(String _fecha, String _fact, String _fnue)
			throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(_fact);
		String result = "";
		try {
			java.util.Date fecha = df.parse(_fecha);
			df = new SimpleDateFormat(_fnue);
			result = df.format(fecha);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public int diferenciaFechas(String _fec1, String _fec2, String _formato) {
		SimpleDateFormat df = new SimpleDateFormat(_formato);
		java.util.Date fec1;
		java.util.Date fec2;
		long result = 0;
		try {
			fec1 = df.parse(_fec1);
			fec2 = df.parse(_fec2);
			result = (fec1.getTime() - fec2.getTime()) / (1000 * 60 * 60 * 24);
		} catch (Exception ex) {
			System.out.print("Error en operacion con fechas " + ex.toString());
		}
		return new Integer("" + result).intValue();
	}

	/**
	 * Convertir Date en String con un formato determinado
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public String fechaToString(java.util.Date fecha, String formato) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(formato);
			return df.format(fecha);
		} catch (Exception ex) {
			System.out.print("Error en operacion con fechas " + ex.toString());
			return null;
		}

	}

	/**
	 * Convertir un String a Date
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public java.util.Date stringToFecha(String fecha, String formato) {
		SimpleDateFormat df = new SimpleDateFormat(formato);
		try {
			return df.parse(fecha);
		} catch (ParseException ex) {
			System.out.print("Error en operacion con fechas " + ex.toString());
			return null;
		}
	}

	public int getMeses(Date _fecIni, Date _fecFin) {
		int elapsed = -1; // Por defecto estaba en 0 y siempre asi no haya
							// pasado un mes contaba 1)
		GregorianCalendar g1 = new GregorianCalendar(); 
		GregorianCalendar g2 = new GregorianCalendar();
		g1.setTime(_fecIni);
		g2.setTime(_fecFin);
		GregorianCalendar gc1, gc2;
		if (g2.after(g1)) {
			gc2 = (GregorianCalendar) g2.clone();
			gc1 = (GregorianCalendar) g1.clone();
		} else {
			gc2 = (GregorianCalendar) g1.clone();
			gc1 = (GregorianCalendar) g2.clone();
		}

		while (gc1.before(gc2)) {
			gc1.add(Calendar.MONTH, 1);
			elapsed++;
		}

		if (gc1.get(Calendar.DATE) == (gc2.get(Calendar.DATE)))
			elapsed++; // si es el mismo dia cuenta para la suma de meses
		return elapsed;
	}

}
