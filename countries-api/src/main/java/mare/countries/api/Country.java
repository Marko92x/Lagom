package mare.countries.api;

/**
 * Created by marko on 26.12.17..
 */
public class Country {

    public Country(String name) {
        this.name = name;
    }

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
