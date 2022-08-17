<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.product.Product" scope="request"/>
<tags:master pageTitle="Product Detail Page">
   <p class="homePage">
      <button class="homeButton">
         <a href="${pageContext.servletContext.contextPath}/products">Back to home page</a>
      </button>
   </p>
   <p>
      Product detail page
   </p>
   <p>
      Cart: ${cart}
         <c:if test="${not empty param.success}">
            <p class="success">
               ${param.success}
            </p>
         </c:if>
         <c:if test="${not empty outOfStock}">
            <p class="outOfStock">
               ${outOfStock}
            </p>
         </c:if>
   </p>
   <form method="post">
      <table class="table">
         <tr>
            <td>Image</td>
            <td>
               <img class="product_Image" src="${product.imageUrl}">
            </td>
         </tr>
         <tr>
            <td>Description</td>
            <td class="description">${product.description}</td>
         </tr>
         <tr>
            <td>Price</td>
            <td class="price">
               <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
         </tr>
         <tr>
            <td>Stock available</td>
            <td class="stock">
               ${product.stock}
            </td>
         </tr>
         <tr>
            <td colspan="2">
               <input name="quantity" value="1">
               <button>Add to cart</button>
               <c:if test="${not empty error}">
                  <p class="error">
                     ${error}
                  </p>
               </c:if>
            </td>
         </tr>
      </table>
   </form>
   <p>
      (c) Evgen
   </p>
</tags:master>