/**
 * 
 */
package es.tributasenasturias.dao;
/**
 * Tipos de llamada al servicio. Pueden ser:
 * Autoliquidaci�n desde Servicio Web (Principado de Asturias)
 * Autoliquidaci�n desde Portal Tributario
 * Autoliquidaci�n S1
 * Liquidaci�n desde Portal Tributario
 * Liquidaci�n S1
 * @author crubencvs
 *
 */
public enum TipoLlamada
{
	SERVICIO_WEB_AUTOLIQUIDACION,
	PORTAL_AUTOLIQUIDACION,
	S1_AUTOLIQUIDACION,
	PORTAL_LIQUIDACION,
	S1_LIQUIDACION
}