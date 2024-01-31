/**
 * 
 */
package es.tributasenasturias.dao;
/**
 * Tipos de llamada al servicio. Pueden ser:
 * Autoliquidación desde Servicio Web (Principado de Asturias)
 * Autoliquidación desde Portal Tributario
 * Autoliquidación S1
 * Liquidación desde Portal Tributario
 * Liquidación S1
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