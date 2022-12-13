<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" >
<xsl:output method="text" omit-xml-declaration="yes" indent="no"/>
<xsl:template match="/">Index;Obj.Type;Object Id;Obj.Name;Value;Description;Datum;Uhrzeit
<xsl:for-each select="//item">
<xsl:value-of select="concat(Index,';',Obj.Type,';',Object_Id,';',Obj.Name,';',Value,';',Description,';',Datum,';',Uhrzeit,'&#xA;')"/>
</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
