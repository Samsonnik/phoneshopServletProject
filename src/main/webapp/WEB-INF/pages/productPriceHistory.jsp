<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="priceHistory" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Product Detail Page">
   <p>
      ${product.description} price history page
   </p>
   <c:if test="${not empty error}">
      ${error}
   </c:if>
   <table class="table">
   <tr>
      <td>
         <strong>
            Start date
         </strong>
      </td>
      <td>
         <strong>
            Price
         </strong>
      </td>
   </tr>
      <c:forEach var="priceHistoryValue" items="${priceHistory}">
         <tr>
            <td>
               <fmt:formatDate value="${priceHistoryValue.date}" pattern="dd-MM-yyyy" />
            </td>
            <td>
               <fmt:formatNumber value="${priceHistoryValue.price}" type="currency" currencySymbol="&#36;"/>
            </td>
         </tr>
      </c:forEach>
   </table>
   <p>
      (c) Evgen
   </p>
</tags:master>