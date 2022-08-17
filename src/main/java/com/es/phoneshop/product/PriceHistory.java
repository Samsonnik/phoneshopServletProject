package com.es.phoneshop.product;

import com.es.phoneshop.exception.PriceHistoryException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class PriceHistory implements Serializable {

    private Date date;
    private BigDecimal price;
    private final static Date CURRENT_DATE = new GregorianCalendar().getTime();

    private final static Date JANUARY_DATE = new GregorianCalendar(2022, Calendar.JANUARY, 1).getTime();
    private final static Date MARCH_DATE = new GregorianCalendar(2022, Calendar.MARCH, 1).getTime();
    private final static Date JUNE_DATE = new GregorianCalendar(2022, Calendar.JUNE, 1).getTime();



    public PriceHistory(Date date, BigDecimal price) throws PriceHistoryException {
        if ((date == null ) || (price == null)) {
            throw new PriceHistoryException("Date or Price cannot be a null");
        }
        if (date.after(CURRENT_DATE)) {
            throw new PriceHistoryException("Date cannot be after the Current Date");
        }
        if (price.longValue() <= 0) {
            throw new PriceHistoryException("Price cannot be null or less than 0");
        }
        this.date = date;
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static Date CURRENT_DATE() {
        return CURRENT_DATE;
    }

    public static Date JANUARY_DATE() {
        return JANUARY_DATE;
    }

    public static Date MARCH_DATE() {
        return MARCH_DATE;
    }

    public static Date JUNE_DATE() {
        return JUNE_DATE;
    }
}
