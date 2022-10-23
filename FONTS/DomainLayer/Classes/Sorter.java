package DomainLayer.Classes;

import java.util.ArrayList;
import java.util.Collections;
import java.time.Instant;
import java.util.Comparator;

import DomainLayer.temp.Quartet;
import DomainLayer.Enums.OrderType;
import DomainLayer.Enums.SortingType;
import DomainLayer.Factories.SortingMethodFactory;
import DomainLayer.Interfaces.SortingMethod;

public class Sorter {

    public Sorter(){}

    public ArrayList<Quartet<String, String, Instant, Instant>> SortElements(ArrayList<Quartet<String, String, Instant, Instant>> list, SortingType sortingType, OrderType orderType){
        SortingMethodFactory factory = SortingMethodFactory.getInstance();
        SortingMethod sortingMethod = factory.getSortingMethod(sortingType); 
        Comparator<Quartet<String, String, Instant, Instant>> c = sortingMethod.getComparator(orderType);
        Collections.sort(list, c);
        return list;
    }
}