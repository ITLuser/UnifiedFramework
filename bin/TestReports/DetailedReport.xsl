<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
<html>
<head>
<title>Detailed Test Report</title>
<style>
table, th, td {
    border-collapse: collapse;
}

th, td {
    padding: 10px;
    text-align: left;
}

.bgsuccess
{
	 color: #ffffff;
    background-color: #5cb85c;
    border-color: #4cae4c;
}
.bgdanger
{
	    color: #d9534f;
    //background-color: #d9534f;
    //border-color: #d43f3a;
}
.bgflow
{
	    color: #0099ff;
}
.bginfo
{
	color: #ffffff;
    background-color: #337ab7;
    border-color: #2e6da4;
}
.bodycontainer
{
    width: 90%;
    border: 1px solid #111111;
    margin: 0 auto;
}
.topheader,.bottombody
{
	width:100%;
}
.heading{
	margin-top: 3%;
	padding: 8px;
    margin: 0px;
    border-bottom: 1px solid;
    font-size:25px;
    color:#333;
}
.bottom{
	margin-bottom: 3%;
	padding: 8px;
    margin: 0px;
    text-align: left;
}
</style>
</head>
<body>
	<div align="center" class="bodycontainer">
		<p class="heading"><b>Detailed Test Report</b></p>
		<div class="topheader">
			<table >
				<xsl:for-each select="Test/Details">
					<tr>
						<td width="10%"><b>Name</b></td>
						<td width="30%"><b><xsl:value-of select="TestUser"/></b></td>
						<td width="5%"><b>Date</b></td>
						<td width="10%"><b><xsl:value-of select="testStartTime"/></b></td>
					</tr>
					<tr>
						<td width="10%"><b>Test Script Id</b></td>
						<td width="30%"><b><xsl:value-of select="TestId"/></b></td>
						<td width="10%"><b>Test description</b></td>
						<td width="15%"><b><xsl:value-of select="TestDescription"/></b></td>
					</tr>
					<tr>
						<td width="10%"><b>Duration</b></td>
						<td width="30%"><b><xsl:value-of select="testDuration"/></b></td>
						<td width="10%"><b>MachineName</b></td>
						<td width="15%"><b><xsl:value-of select="testMachineName"/></b></td>
					</tr>
				</xsl:for-each>
			</table>
		</div>
		<div class="bottombody">
			<table  border="1" width="100%" >
				<tr class="bginfo">
					<th width="20%">S.No</th>
					<th width="40%">StepDesc</th>
					<th width="20%">StepStatus</th>
					<th width="20%">Time</th>
				</tr>
			<xsl:for-each select="Test/Steps">
				<xsl:if test="StepStatus = 'Fail'">
					<tr class="bgdanger">
						<td><b><xsl:value-of select="StepNumber"/></b></td>
						<td><b><xsl:value-of select="StepDesc"/></b></td>
						<xsl:variable name="hyperlink"><xsl:value-of select="ScreenshotimagePath" /></xsl:variable>
						<td><a href="{$hyperlink}" style="color:#d9534f;"> <xsl:value-of select="@ScreenshotimagePath" /><b><xsl:value-of select="StepStatus"/></b></a></td>
						<td><b><xsl:value-of select="StepTime"/></b></td>
					</tr>					
				</xsl:if>
				<xsl:if test="StepStatus = 'Pass'">
					<tr>
						<td><b><xsl:value-of select="StepNumber"/></b></td>
						<td><b><xsl:value-of select="StepDesc"/></b></td>
						<td><b><xsl:value-of select="StepStatus"/></b></td>
						<td><b><xsl:value-of select="StepTime"/></b></td>
					</tr>
				</xsl:if>
				<xsl:if test="StepStatus = ' -- '">
					<tr class="bgflow">
						<td><b><xsl:value-of select="StepNumber"/></b></td>
						<td><b><xsl:value-of select="StepDesc"/></b></td>
						<td><b><xsl:value-of select="StepStatus"/></b></td>
						<td><b> -- </b></td>
					</tr>
				</xsl:if>
				<xsl:if test="StepStatus = 'Exception'">
					<tr class="bgdanger">
						<td><b><xsl:value-of select="StepNumber"/></b></td>
						<td><b><xsl:value-of select="StepDesc"/></b></td>
						<xsl:variable name="hyperlink"><xsl:value-of select="ScreenshotimagePath" /></xsl:variable>
						<td><a href="{$hyperlink}" style="color:#d9534f;"> <xsl:value-of select="@ScreenshotimagePath" /><b><xsl:value-of select="StepStatus"/></b></a></td>
						<td><b><xsl:value-of select="StepTime"/></b></td>
					</tr>
				</xsl:if>
				<xsl:if test="StepStatus = 'Interruption'">
					<tr class="bgdanger">
						<td><b><xsl:value-of select="StepNumber"/></b></td>
						<td><b><xsl:value-of select="StepDesc"/></b></td>
						<td><b><xsl:value-of select="StepStatus"/></b></td>
						<td><b><xsl:value-of select="StepTime"/></b></td>
					</tr>
				</xsl:if>		
			</xsl:for-each>
		</table>
		</div>
		<p class="bottom"><a href = "http://idealtechlabs.com/index.html "> <b>IdealTechLabs</b></a></p>
	</div>
</body>
</html>
</xsl:template>
</xsl:stylesheet>