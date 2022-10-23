package DomainLayer.Factories;

import java.util.Map;
import java.util.HashMap;

import DomainLayer.Interfaces.SortingMethod;
import DomainLayer.Classes.TitleSortingMethod;
import DomainLayer.Classes.AuthorSortingMethod;
import DomainLayer.Classes.LastModifiedDateSortingMethod;
import DomainLayer.Classes.CreationDateSortingMethod;

import DomainLayer.Enums.SortingType;

public class SortingMethodFactory {
    private static SortingMethodFactory instance = null;

    private static Map <SortingType, SortingMethod> methods = new HashMap<SortingType, SortingMethod>();

    public static SortingMethodFactory getInstance(){
        if (instance == null) {
            instance = new SortingMethodFactory();
            methods.put(SortingType.TITLE, new TitleSortingMethod());
            methods.put(SortingType.AUTHOR, new AuthorSortingMethod());
            methods.put(SortingType.LASTMODIFIEDDATE, new LastModifiedDateSortingMethod());
            methods.put(SortingType.CREATIONDATE, new CreationDateSortingMethod());
        }
        return instance;
    }

    public SortingMethod getSortingMethod(SortingType type){
        return methods.get(type);
    }
}