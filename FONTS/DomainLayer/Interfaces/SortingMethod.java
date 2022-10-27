package DomainLayer.Interfaces;

import java.util.Comparator;

import DomainLayer.Enums.OrderType;

import java.time.Instant;
import DomainLayer.temp.Quartet;

public interface SortingMethod {
    public Comparator<Quartet<String, String, Instant, Instant>> getComparator(OrderType orderType); 
}