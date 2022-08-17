<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
   <h2>
      If you have a lot of money, you are in right place! Welcome to the Zadrot Service!
   </h2>
   <form>
      <input name="query" placeholder="Search here..." value = ${query}>
      <button>search</button>
      <button>
         <a href="${pageContext.servletContext.contextPath}/products/recent_products">
            recent products
         </a>
      </button>
      <button>
         <a href="${pageContext.servletContext.contextPath}/products/cart_list">
            cart list
         </a>
      </button>
   </form>
   <table class="table">
      <thead>
         <tr>
            <th>Image</th>
            <th>
               Description
               <tags:sortLink sort="description" order="asc"/>
               <tags:sortLink sort="description" order="desc"/>
            </th>
            <th class="price">
               Price
               <tags:sortLink sort="price" order="asc"/>
               <tags:sortLink sort="price" order="desc"/>
            </th>
            <th>Quantity</th>
            <th>Action</th>
            <th class="notes">Note</th>
         </tr>
      </thead>
      <tbody>
         <c:forEach var="product" items="${products}">
            <tr>
               <td>
                  <img class="product-tile" src="${product.imageUrl}">
               </td>
               <td>
                  <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                     ${product.description}
                  </a>
               </td>
               <td class="price">
                  <a href="${pageContext.servletContext.contextPath}/products/price_history/${product.id}">
                     <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                  </a>
               </td>
               <td class="quantityT">
                  <c:set var="cartQuantity" value="${cartByIdAndQuantity[product.id]}"/>
                  <c:set var="error" value="${errors[product.id]}"/>
                  <c:if test="${not empty cartQuantity}">
                     ${cartQuantity}
                  </c:if>
                  <c:if test="${empty cartQuantity}">
                     0
                  </c:if>
               </td>
               <td>
                  <form method="post">
                     <button class="addButton" value="${product.id}" name="id" type="submit">add to cart</button>
                  </form>
               </td>
               <td>
                  ${error}
               </td>
            </tr>
         </c:forEach>
      </tbody>
   </table>
</tags:master>