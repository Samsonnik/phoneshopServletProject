<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="recentProducts" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product Detail Page">
   <p>
      Recent products page
   </p>
   <p class="homePage">
      <button class="homeButton">
         <a href="${pageContext.servletContext.contextPath}/products">Back to home page</a>
      </button>
   </p>
   <form method="post">
      <button value="true" name="remove" type="submit">Remove recent products</button>
   </form>
   <table class="table">
      <tr>
         <thead>
            <th>Image</th>
            <th>Description</th>
            <th>Price</th>
         </thead>
      </tr>
     <tbody>
        <c:forEach var="product" items="${recentProducts}">
           <tr>
              <td>
                 <img class="product-tile" src="${product.imageUrl}">
              </td>
              <td>
                 <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                    ${product.description}
                 </a>
              </td>
              <td>
                 <a href="${pageContext.servletContext.contextPath}/products/price_history/${product.id}">
                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                 </a>
              </td>
           <tr>
        </c:forEach>
     </tbody>
   </table>
  <p>
     (c) Evgen
  </p>
</tags:master>