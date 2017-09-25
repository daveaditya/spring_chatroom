<%--suppress HtmlUnknownTarget --%>
<%--suppress XmlDefaultAttributeValue --%>
<%--suppress HtmlUnknownTarget --%>
<%--suppress XmlDefaultAttributeValue --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Get Auth</title>
</head>
<body>

<div>
    <form action="${pageContext.request.contextPath}/genAuth" method="post">
        <table>
            <tr>
                <td><label for="name">Name: </label></td>
                <td><input id="name" type="text" name="name"></td>
            </tr>
            <tr>
                <td><label for="emailId">Email Id: </label></td>
                <td><input id="emailId" type="email" name="emailId"></td>
            </tr>
            <tr>
                <td><label for="mobile">Mobile No.: </label></td>
                <td><input id="mobile" type="number" name="mobileNo"></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Get Auth"></td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>