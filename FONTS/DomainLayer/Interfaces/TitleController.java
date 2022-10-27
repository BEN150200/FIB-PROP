package DomainLayer.Interfaces;

import java.util.Set;
import DomainLayer.Classes.Title;

public interface TitleController {
    public Boolean existsTitle(String titleName);

    public Title getTitle(String titleName);

    public void addTitle(Title title);

    public void deleteTitle(String titleName);

    public Set<Title> getAllTitles();
}