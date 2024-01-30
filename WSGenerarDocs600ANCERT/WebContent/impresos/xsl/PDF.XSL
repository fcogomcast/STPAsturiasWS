<?xml version='1.0' encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:msxsl="urn:schemas-microsoft-com:xslt"
                xmlns:pdf1="http://pdfxsl"
                version="1.0">

<msxsl:script language="JScript" implements-prefix="pdf1">
	function pdf_open_file  (){}
	function pdf_begin_page (){}
	function pdf_close      (){}
	function pdf_end_page   (){}
	function pdf_creador    (){}
	function pdf_setfont    (){}
	function pdf_show_text  (){}
	function pdf_show_text_continue(){}
	function pdf_show_boxed (){}
	function pdf_rect       (){}
	function pdf_line       (){}
	function pdf_open_image_file(){};
	function pdf_barras() {};
	function pdf_linea() {};
</msxsl:script>
  

                
<xsl:template match="//Paginas">
	<xsl:value-of select="pdf1:pdf_open_file()"/> 
	<xsl:value-of select="pdf1:pdf_creador('p1','autor','titulo')"/>         
      <xsl:apply-templates/>
	<xsl:value-of select="pdf1:pdf_close()"/> 
</xsl:template>

<xsl:template match="Pagina">    
	<xsl:value-of select="pdf1:pdf_begin_page(.)"/> 	
      <xsl:apply-templates/>	
    <xsl:value-of select="pdf1:pdf_end_page()"/>     
</xsl:template>

<xsl:template match="grupo">
     
</xsl:template>

<xsl:template match="Fila">
     <xsl:apply-templates/>	
</xsl:template>

<xsl:template match="bloque">
     <xsl:apply-templates/>	
</xsl:template>

<xsl:template match="texto">
	<xsl:value-of select="pdf1:pdf_setfont(.)"/> 
	<xsl:value-of select="pdf1:pdf_show_text(.)"/> 
</xsl:template>
<xsl:template match="textobox">
	<xsl:value-of select="pdf1:pdf_setfont(.)"/> 
	<xsl:value-of select="pdf1:pdf_show_boxed(.)"/> 
</xsl:template>
<xsl:template match="cuadro">
	<xsl:value-of select="pdf1:pdf_rect(.)"/> 
</xsl:template>

<xsl:template match="imagen">
	<xsl:value-of select="pdf1:pdf_open_image_file(.)"/> 
</xsl:template>

<xsl:template match="barras">
	<xsl:value-of select="pdf1:pdf_barras(.)"/> 
</xsl:template>

<xsl:template match="linea">
	<xsl:value-of select="pdf1:pdf_linea(.)"/> 
</xsl:template>

<xsl:template match="salto">
	<xsl:value-of select="pdf1:pdf_end_page(.)"/> 	 
	<xsl:apply-templates/>
	<xsl:value-of select="pdf1:pdf_begin_page(.)"/>
</xsl:template>

</xsl:stylesheet>
