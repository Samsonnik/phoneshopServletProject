package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderCheckerAndSetter {

    private static final String CANNOT_BE_NULL = "cannot be a null";
    private static final String PLEASE_CHECK = "Please, check your";

    public static Order checkAndSetValues(HttpServletRequest request, OrderService orderService,
                                          CartService cartService, Map<String, String> errors) {

        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethods");

        if (!checkNotNull(firstName)) {
            errors.put("firstName", "First name " + OrderCheckerAndSetter.CANNOT_BE_NULL);
        } else {
            order.setFirstName(firstName);
        }
        if (!checkNotNull(lastName)) {
            errors.put("lastName", "Last name " + OrderCheckerAndSetter.CANNOT_BE_NULL);
        } else {
            order.setLastName(lastName);
        }
        if (!checkNotNull(address)) {
            errors.put("address", "Address " + OrderCheckerAndSetter.CANNOT_BE_NULL);
        } else if (!checkAddress(address)) {
            errors.put("address", OrderCheckerAndSetter.PLEASE_CHECK + " city");
        } else {
            order.setAddress(address);
        }
        if (!checkPhoneNumber(phoneNumber)) {
            errors.put("phoneNumber", OrderCheckerAndSetter.PLEASE_CHECK  + " phone number");
        } else {
            order.setPhoneNumber(phoneNumber);
        }
        if (!checkNotNull(paymentMethod)) {
            errors.put("paymentMethod", OrderCheckerAndSetter.PLEASE_CHECK + " payment method");
        } else {
            PaymentMethod finalPaymentMethod = checkAndGetPaymentMethod(orderService, paymentMethod);
            order.setPaymentMethod(finalPaymentMethod);
        }
        return order;
    }

    private static boolean checkNotNull(String value) {
        return (value != null) && (!value.isEmpty());
    }

    private static boolean checkAddress(String address) {
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        List<String> addressByWords = Arrays.stream(address.split(", ")).toList();
        addressByWords.forEach(value -> {
            if (addressByWords.indexOf(value) == 0) {
                if (checkCity(value)) {
                    flag.set(true);
                }
            }
        });
        return flag.getAcquire();
    }

    private static boolean checkCity(String city) {
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        Arrays.stream(DeliveryCities.values()).forEach(availableCity -> {
            if (availableCity.toString().equals(city)) {
                flag.set(true);
            }
        });
        return flag.getAcquire();
    }

    private static boolean checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("(\\+375(?:(29)|(33)|(44)))([0-9]{7})");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private static PaymentMethod checkAndGetPaymentMethod(OrderService orderService, String paymentMethod) {
        return orderService.getPaymentMethods().stream()
                .filter(method -> String.valueOf(method).equals(paymentMethod))
                .findFirst()
                .orElseThrow();
    }
}
