<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:m12="urn:jboss:domain:messaging:1.2"
                xmlns:m13="urn:jboss:domain:messaging:1.3"
                xmlns:m14="urn:jboss:domain:messaging:1.4">

    <!--
        An XSLT style sheet which will enable JTS,
        by adding the JTS attribute to the transactions subsystem,
        and turning on transaction propagation in the JacORB subsystem.
    -->
    <!-- traverse the whole tree, so that all elements and attributes are eventually current node -->
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="//m12:jms-connection-factories/m12:connection-factory[@name='RemoteConnectionFactory']">
        <connection-factory name="RemoteConnectionFactory">
            <factory-type>XA_GENERIC</factory-type>                        
                <xsl:copy-of select="./*">
                    <xsl:apply-templates/>
                </xsl:copy-of>
        </connection-factory>
    </xsl:template>

    <xsl:template match="//m13:jms-connection-factories/m13:connection-factory[@name='RemoteConnectionFactory']">
        <connection-factory name="RemoteConnectionFactory">
            <factory-type>XA_GENERIC</factory-type>
            <xsl:copy-of select="./*">
                <xsl:apply-templates/>
            </xsl:copy-of>
        </connection-factory>
    </xsl:template>

    <xsl:template match="//m14:jms-connection-factories/m14:connection-factory[@name='RemoteConnectionFactory']">
        <connection-factory name="RemoteConnectionFactory">
            <factory-type>XA_GENERIC</factory-type>
            <xsl:copy-of select="./*">
                <xsl:apply-templates/>
            </xsl:copy-of>
        </connection-factory>
    </xsl:template>

</xsl:stylesheet>