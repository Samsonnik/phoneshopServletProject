<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product Detail Page">
  <p>Cart list page</p>
  <p class="homePage">
       <button class="homeButton">
          <a href="${pageContext.servletContext.contextPath}/products">Back to home page</a>
       </button>
  </p>
  <c:if test="${not empty errors}">
     <p class="error">
        There were errors
     </p>
  </c:if>
  <c:if test="${not empty param.delete}">
       <p class="success">
          ${param.delete}
       </p>
    </c:if>
  <c:if test="${not empty param.success}">
       <p class="success">
          ${param.success}
       </p>
    </c:if>
  <table class="table">
     <thead>
        <tr>
           <th>Image</th>
           <th>Description</th>
           <th class="priceT">Price</th>
           <th class="quantityT">Quantity</th>
           <th>Action</th>
        </tr>
     </thead>
     <tbody>
     <c:forEach var="item" items="${cart}">
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
              <form method="post">
                 <c:set var="error" value="${errors[item.product.id]}"/>
                 <input class="inputQuantity" name="quantity" value="${item.quantity}">
                 <input name="id" type="hidden" value="${item.product.id}">
                 <c:if test="${not empty error}">
                    <p class="cartListError">
                       ${errors[item.product.id]}
                    </p>
                 </c:if>
           </td>
           <td>
              <button form="deleteProduct"
                 formAction="${pageContext.servletContext.contextPath}/products/cart_list/delete/${item.product.id}">
                 delete
              </button>
           </td>
        </tr>
     </c:forEach>
     <tr>
        <td colspan="5">
           <strong>
              Total price: <fmt:formatNumber value="${totalPrice}" type="currency" currencySymbol="USD"/>
           </strong>
        </td>
     </tr>
  </tbody>
  </table>
  <p>
     <button>Update</button>
  </form>
  <button>
      <a href="${pageContext.servletContext.contextPath}/checkout">Checkout</a>
  </button>
  </p>
  <form id="deleteProduct" method="post"></form>
  <p>
    (c) Evgen
  </p>
</tags:master>