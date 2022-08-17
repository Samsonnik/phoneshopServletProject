<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product Detail Page">
   <p><strong>Cart list page</strong></p>
   <p class="homePage">
      <button class="homeButton">
         <a href="${pageContext.servletContext.contextPath}/products">Back to home page</a>
      </button>
   </p>
   <table class="table">
      <thead>
         <tr>
            <th>Image</th>
            <th>Description</th>
            <th class="priceT">Price</th>
            <th class="quantityT">Quantity</th>
         </tr>
      </thead>
      <tbody>
         <c:forEach var="item" items="${items}">
            <tr>
               <td>
                  <img class="product-tile" src="${item.product.imageUrl}">
               </td>
               <td>
                  <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                     ${item.product.description}
                  </a>
               </td>
               <td>
                  <a href="${pageContext.servletContext.contextPath}/products/price_history/${item.product.id}">
                     <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                  </a>
               </td>
               <td class="quantity">
                  ${item.quantity}
               </td>
            </tr>
         </c:forEach>
         <tr>
            <td colspan="4">
               <strong>
                  Products cost - <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="USD"/>
                  <br>Delivery cost - <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="USD"/></br>
                  Total price - <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="USD"/>
               </strong>
            </td>
         </tr>
      </tbody>
   </table>
   <p><strong>Your details</strong><p>
   <table class ="table">
      <tbody>
         <tags:orderTableData name="firstName" label="First name" order="${order}"></tags:orderTableData>
         <tags:orderTableData name="lastName" label="Last name" order="${order}"></tags:orderTableData>
         <tags:orderTableData name="phoneNumber" label="Phone number" order="${order}"></tags:orderTableData>
         <tags:orderTableData name="orderDate" label="Order date" order="${order}"></tags:orderTableData>
         <tags:orderTableData name="address" label="Address" order="${order}"></tags:orderTableData>
         <tags:orderTableData name="paymentMethod" label="Payment method" order="${order}"></tags:orderTableData>
      </tbody>
   </table>
   <p>
      (c) Evgen
   </p>
</tags:master>