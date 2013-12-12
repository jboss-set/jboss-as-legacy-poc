<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:t12="urn:jboss:domain:transactions:1.2"
                xmlns:t13="urn:jboss:domain:transactions:1.3"
                xmlns:t14="urn:jboss:domain:transactions:1.4"
        >

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

    <xsl:template match="//t12:subsystem">
        <xsl:choose>
            <xsl:when test="not(//t12:subsystem/t12:jts)">
                <xsl:copy>
                    <xsl:apply-templates select="node()|@*"/>
                    <t12:jts/>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="node()|@*"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="//t13:subsystem">
        <xsl:choose>
            <xsl:when test="not(//t13:subsystem/t13:jts)">
                <xsl:copy>
                    <xsl:apply-templates select="node()|@*"/>
                    <t13:jts/>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="node()|@*"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="//t14:subsystem">
        <xsl:choose>
            <xsl:when test="not(//t14:subsystem/t14:jts)">
                <xsl:copy>
                    <xsl:apply-templates select="node()|@*"/>
                    <t14:jts/>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="node()|@*"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
