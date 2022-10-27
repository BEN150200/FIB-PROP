package DomainLayer.Classes;

import DomainLayer.Enums.OrderType;
import DomainLayer.Interfaces.SortingMethod;
import java.util.Comparator;
import java.time.Instant;
import DomainLayer.temp.Quartet;

public class LastModifiedDateSortingMethod implements SortingMethod{
    public Comparator<Quartet<String, String, Instant, Instant>> getComparator(OrderType orderType){
        Comparator<Quartet<String, String, Instant, Instant>> comp = (o1, o2) -> o1.c.compareTo(o2.c);
        if (orderType == OrderType.ASC) { return comp;}
        return comp.reversed();
    } 
}