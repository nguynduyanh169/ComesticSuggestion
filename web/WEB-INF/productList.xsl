<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : productList.xsl
    Created on : March 23, 2020, 8:54 PM
    Author     : anhnd
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <xsl:for-each select="products/product">
            <div class="col-xl-4 col-md-6 grid-item new">
                <div class="product">
                    <div class="product_image">
                        <img height="552.78px" width="552.78px">
                            <xsl:attribute name="src">
                                <xsl:value-of select="imageLink"/>
                            </xsl:attribute>
                        </img>
                    </div>
                    <div class="product_content">
                        <div class="product_info d-flex flex-row align-items-start justify-content-start">
                            <div>
                                <div>
                                    <div class="product_name">
                                        <a>
                                            <xsl:attribute name="href">DetailServlet?productId=<xsl:value-of select="productId"/></xsl:attribute>
                                            <xsl:value-of select="productName"/>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="ml-auto text-right">
                                <div class="product_price text-right">
                                    <xsl:value-of select='format-number(price, "#")'/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
